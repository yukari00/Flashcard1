package com.example.todo

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_detail.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private val ARG_title = IntentKey.TITLE.name
private val ARG_deadline = IntentKey.DEAD_LINE.name
private val ARG_task_detail = IntentKey.TASK_DETAIL.name
private val ARG_is_completed = IntentKey.IS_COMPLETED.name
private val ARG_mode = IntentKey.MODE_IN_EDIT.name

private var mListener : DetailFragment.OnFragmentInteractionListener? = null


/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var title: String = ""
    private var deadline: String = ""
    private var taskDetail: String = ""
    private var isCompleted: Boolean = false
    private var mode: ModeInEdit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_title)!!
            deadline = it.getString(ARG_deadline)!!
            taskDetail = it.getString(ARG_task_detail)!!
            isCompleted = it.getBoolean(ARG_is_completed)
            mode = it.getSerializable(ARG_mode) as ModeInEdit
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.apply {
            menu.apply { findItem(R.id.menu_delete).isVisible = true }
            menu.apply { findItem(R.id.menu_edit).isVisible = true }
            menu.apply { findItem(R.id.menu_done).isVisible = false }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete ->{
                deleteItemTodo(title, deadline, taskDetail)

            }
            R.id.menu_edit ->{
                mListener?.goEdit(title, deadline, taskDetail, isCompleted, mode)

            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun deleteItemTodo(title: String?, deadline: String?, taskDetail: String?) {
        val realm = Realm.getDefaultInstance()
        val selectedTodo = realm.where(TodoModel::class.java).equalTo(TodoModel::title.name, title)
            .equalTo(TodoModel::deadLine.name, deadline).equalTo(TodoModel::taskDetail.name, taskDetail).findFirst()!!

            val dialog = AlertDialog.Builder(requireActivity()).apply {
                setTitle(getString(R.string.delete))
                setMessage(getString(R.string.delete_message))
                setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    realm.beginTransaction()
                    selectedTodo.deleteFromRealm()
                    realm.commitTransaction()

                    realm.close()
                    mListener?.DataDeleted()
                    fragmentManager?.beginTransaction()!!.remove(this@DetailFragment).commit()
                }
                setNegativeButton(getString(R.string.cancel)) { dialog, which -> }
                show()
            }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        title_detail.text = title
        deadline_detail.text = deadline
        todo_detail.text = taskDetail
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentInteractionListener){
            mListener = context
        }else{
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener{
        fun DataDeleted()
        fun goEdit(title: String, deadline: String, taskDetail: String, isCompleted: Boolean, mode: ModeInEdit?)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(title: String, deadline: String, taskDesk: String, completed: Boolean, mode: ModeInEdit) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_title, title)
                    putString(ARG_deadline, deadline)
                    putString(ARG_task_detail, taskDesk)
                    putBoolean(ARG_is_completed, completed)
                    putSerializable(ARG_mode, mode)
                }
            }
    }
}
