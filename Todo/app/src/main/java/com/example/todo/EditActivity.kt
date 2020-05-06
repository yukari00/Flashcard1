package com.example.todo

import android.os.Bundle
import android.view.Menu
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity(), EditFragment.OnFragmentInteractionListener,
    DatePickerDialogFragment.onDateSetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(toolbar)

       toolbar.apply {
           setNavigationIcon(R.drawable.ic_arrow_back_24dp)
           setNavigationOnClickListener {
               finish()
           }
       }
       val bundle = intent.extras!!
        val title = bundle.getString(IntentKey.TITLE.name)!!
        val dead_line = bundle.getString(IntentKey.DEAD_LINE.name)!!
        val task_detail = bundle.getString(IntentKey.TASK_DETAIL.name)!!
        val is_completed = bundle.getBoolean(IntentKey.IS_COMPLETED.name)
        val mode = bundle.getSerializable(IntentKey.MODE_IN_EDIT.name) as ModeInEdit

        supportFragmentManager.beginTransaction().add(R.id.container_detail,
            EditFragment.newInstance(title, dead_line, task_detail, is_completed, mode), FragmentTag.EDIT.toString()).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.apply { findItem(R.id.menu_delete).isVisible = false }
        menu.apply { findItem(R.id.menu_edit).isVisible = false }
        menu.apply { findItem(R.id.menu_done).isVisible = false }
        return true
    }

    override fun onDatePickerLaunched() {
        //Todo DatePicker開く処理
        DatePickerDialogFragment().show(supportFragmentManager, FragmentTag.DATE_PICKER.toString())
    }

    override fun onDataEdited() {
        finish()
    }

    override fun onDateSelected(dateString : String) {
        val inputDateText = findViewById<EditText>(R.id.textViewDeadLine)
        inputDateText.setText(dateString)

    }

}
