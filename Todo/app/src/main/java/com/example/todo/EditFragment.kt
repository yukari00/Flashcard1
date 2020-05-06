package com.example.todo

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_edit.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private val ARG_title = IntentKey.TITLE.name
private val ARG_deadline = IntentKey.DEAD_LINE.name
private val ARG_task_detail = IntentKey.TASK_DETAIL.name
private val ARG_is_completed = IntentKey.IS_COMPLETED.name
private val ARG_mode = IntentKey.MODE_IN_EDIT.name
lateinit var realm : Realm
/**
 * A simple [Fragment] subclass.
 * Use the [EditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var title: String? = ""
    private var deadLine: String? = ""
    private var taskDetail: String? = ""
    private var isCompleted : Boolean = false
    private var mode : ModeInEdit? = null

    private var mListener : OnFragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentInteractionListener){
            mListener = context
        }else{
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_title)
            deadLine = it.getString(ARG_deadline)
            taskDetail = it.getString(ARG_task_detail)
            isCompleted = it.getBoolean(ARG_is_completed)
            mode = it.getSerializable(ARG_mode) as ModeInEdit
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUI(mode)
        imageButtonDateSet.setOnClickListener {
            mListener!!.onDatePickerLaunched()
        }
    }

    private fun updateUI(mode: ModeInEdit?) {
        when(mode){
            ModeInEdit.NEW_ENTRY ->{
                checkBox.visibility = View.INVISIBLE
            }
            ModeInEdit.EDIT ->{
                textViewTitle.setText(title)
                textViewDeadLine.setText(deadLine)
                textViewTaskDetail.setText(taskDetail)
                checkBox.isChecked = isCompleted
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.apply {
            menu.apply { findItem(R.id.menu_delete).isVisible = false }
            menu.apply { findItem(R.id.menu_edit).isVisible = false }
            menu.apply { findItem(R.id.menu_done).isVisible = true }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Todo DBへの登録処理
        if(item.itemId == R.id.menu_done) recordTodo(mode)
        return super.onOptionsItemSelected(item)
    }

    private fun recordTodo(mode: ModeInEdit?) {

        val isRequiredItemFilled = isRequiredItemFilled()
        if(!isRequiredItemFilled) return

        when(mode){
            ModeInEdit.NEW_ENTRY -> newAdd()
            ModeInEdit.EDIT -> edit()
        }
        mListener?.onDataEdited()
        fragmentManager?.beginTransaction()!!.remove(this).commit()
    }

    private fun isRequiredItemFilled(): Boolean {
        if(textViewTitle.text.toString() == ""){
            inputTitle.error = getString(R.string.error)
            return false
        }
        //if (textViewDeadLine.text.toString()== ""){
        //   inputDate.error = getString(R.string.error)
        //    return false
        //}
        if(!inputDateCheck(textViewDeadLine.text.toString())){
            inputDate.error = getString(R.string.error)
            return false
        }
        return true
    }

    private fun inputDateCheck(deadLine: String): Boolean {
        if (deadLine == "") return false
        try {
            val format = SimpleDateFormat("yyyy/MM/dd")
            format.isLenient = false
            format.parse(deadLine)
        } catch (e : ParseException){
            return false
        }
        return true
    }

    private fun newAdd() {
        realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val newTodo = realm.createObject(TodoModel::class.java)
        newTodo.title = textViewTitle.text.toString()
        newTodo.deadLine = textViewDeadLine.text.toString()
        newTodo.taskDetail = textViewTaskDetail.text.toString()
        newTodo.isCompleted = checkBox.isChecked
        realm.commitTransaction()

        realm.close()
    }

    private fun edit() {
        realm = Realm.getDefaultInstance()
        val todo = realm.where(TodoModel::class.java).equalTo(TodoModel::title.name, title)
            .equalTo(TodoModel::deadLine.name, deadLine).equalTo(TodoModel::taskDetail.name, taskDetail).findFirst()!!
        realm.beginTransaction()
        todo.title = textViewTitle.text.toString()
        todo.deadLine = textViewDeadLine.text.toString()
        todo.taskDetail = textViewTaskDetail.text.toString()
        todo.isCompleted = checkBox.isChecked
        realm.commitTransaction()

        realm.close()
    }

    interface OnFragmentInteractionListener{
        fun onDatePickerLaunched()
        fun onDataEdited()

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(title: String, deadline: String, taskDetail: String, isCompleted: Boolean, mode: ModeInEdit?) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_title, title)
                    putString(ARG_deadline, deadline)
                    putString(ARG_task_detail, taskDetail)
                    putBoolean(ARG_is_completed, isCompleted)
                    putSerializable(ARG_mode, mode)
                }
            }
    }
}
