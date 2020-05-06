package com.example.todo

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.example.todo.MasterFragment.OnListFragmentInteractionListener
import com.example.todo.dummy.DummyContent.DummyItem
import io.realm.RealmResults

import kotlinx.android.synthetic.main.fragment_master.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyMasterRecyclerViewAdapter(
    private val mValues: RealmResults<TodoModel>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyMasterRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as TodoModel
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListItemClicked(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_master, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.textViewTitle.text = mValues[position]!!.title
        holder.textViewDeadline.text = MyApplication.appContext.getString(R.string.deadline) +
                " : " + mValues[position]!!.deadLine

        val today = Date()
        val data = SimpleDateFormat("yyyy/MM/dd").parse(mValues[position]!!.deadLine)!!
        if(today >= data){
            holder.imageStatus.setImageResource(R.drawable.ic_warning_black_24dp)
        }else{
            holder.imageStatus.setImageResource(R.drawable.ic_work_24dp)
        }
         // Todo 期限が過ぎてたらアテンションマーク

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var textViewTitle: TextView = mView.textViewTitle
        var textViewDeadline: TextView = mView.textViewDeadline
        var imageStatus = mView.imageStatus



    }
}
