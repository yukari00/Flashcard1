package com.example.todo

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class DatePickerDialogFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

   private var listener : onDateSetListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is onDateSetListener){
            listener = context
        }else{
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface onDateSetListener{
        fun onDateSelected(dateString: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireActivity(),this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val dateString = getDateString(year, month, dayOfMonth)
        listener?.onDateSelected(dateString)
        fragmentManager?.beginTransaction()!!.remove(this).commit()

    }

    private fun getDateString(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        return dateFormat.format(calendar.time)

    }
}