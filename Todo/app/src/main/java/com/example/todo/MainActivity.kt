package com.example.todo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.FrameLayout

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), EditFragment.OnFragmentInteractionListener,
    DatePickerDialogFragment.onDateSetListener, MasterFragment.OnListFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //find if it is Smartphone or Tablet

        if(findViewById<FrameLayout>(R.id.container_detail) != null) isTwoPane = true

        supportFragmentManager.beginTransaction().add(R.id.container_master, MasterFragment.newInstance(1),
            FragmentTag.MASTER.toString()).commit()

        fab.setOnClickListener { view ->
            goEditScreen("","","",false,ModeInEdit.NEW_ENTRY)
        }
    }

    override fun onResume() {
        super.onResume()

        upDateTodoList() // スマホの場合
    }

    private fun goEditScreen(title: String, dead_line: String, task_detail: String, is_completed: Boolean, mode: ModeInEdit?) {
        if (isTwoPane){
            //val fragmentManager = supportFragmentManager
            //val fragmentTransaction = fragmentManager.beginTransaction()
            //fragmentTransaction.add(R.id.container_detail, EditFragment.newInstance("1","1"))
            //fragmentTransaction.commit()

            supportFragmentManager.beginTransaction().add(R.id.container_detail,
                EditFragment.newInstance(title, dead_line, task_detail, is_completed, mode), FragmentTag.EDIT.toString()).commit()
            return
        }
        val intent = Intent(this@MainActivity, EditActivity::class.java).apply {
            putExtra(IntentKey.TITLE.name, title)
            putExtra(IntentKey.DEAD_LINE.name, dead_line)
            putExtra(IntentKey.TASK_DETAIL.name, task_detail)
            putExtra(IntentKey.IS_COMPLETED.name, is_completed)
            putExtra(IntentKey.MODE_IN_EDIT.name, mode)

        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.apply { findItem(R.id.menu_delete).isVisible = false }
        menu.apply { findItem(R.id.menu_edit).isVisible = false }
        menu.apply { findItem(R.id.menu_done).isVisible = false }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDatePickerLaunched() {
        DatePickerDialogFragment().show(supportFragmentManager, FragmentTag.DATE_PICKER.toString())
    }

    override fun onDataEdited() {
        //Todo Data表示
        upDateTodoList()
    }

    private fun upDateTodoList() {
        supportFragmentManager.beginTransaction().replace(R.id.container_master, MasterFragment.newInstance(1),
            FragmentTag.MASTER.toString()).commit()
    }

    override fun onDateSelected(dateString : String) {
        val inputDateText = findViewById<EditText>(R.id.textViewDeadLine)
        inputDateText.setText(dateString)

    }

    override fun onListItemClicked(item: TodoModel?) {
        goDetailScreen(item!!.title,item.deadLine,item.taskDetail,item.isCompleted,ModeInEdit.EDIT)
    }

    private fun goDetailScreen(title: String, deadLine: String, taskDetail: String, completed: Boolean, edit: ModeInEdit) {
        if(isTwoPane){
            if (supportFragmentManager.findFragmentByTag(FragmentTag.EDIT.toString() ) == null &&
                supportFragmentManager.findFragmentByTag(FragmentTag.DETAIL.toString()) == null ) {
                supportFragmentManager.beginTransaction().add(
                    R.id.container_detail, DetailFragment.newInstance(title,deadLine,taskDetail,completed, edit),
                    FragmentTag.EDIT.toString()
                ).commit()
            } else {
                supportFragmentManager.beginTransaction().replace(
                    R.id.container_detail, DetailFragment.newInstance(title,deadLine,taskDetail,completed, edit),
                    FragmentTag.EDIT.toString()).commit()
            }

            return
        }
        val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
            putExtra(IntentKey.TITLE.name, title)
            putExtra(IntentKey.DEAD_LINE.name, deadLine)
            putExtra(IntentKey.TASK_DETAIL.name, taskDetail)
            putExtra(IntentKey.IS_COMPLETED.name, completed)
            putExtra(IntentKey.MODE_IN_EDIT.name, edit)


        }
        startActivity(intent)
    }

    override fun DataDeleted() {
        upDateTodoList()
    }

    override fun goEdit(
        title: String,
        deadline: String,
        taskDetail: String,
        isCompleted: Boolean,
        mode: ModeInEdit?
    ) {
        goEditScreen(title, deadline, taskDetail, isCompleted, mode)
    }
}
