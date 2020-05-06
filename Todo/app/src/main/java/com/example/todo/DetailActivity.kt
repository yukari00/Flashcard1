package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.toolbar
import kotlinx.android.synthetic.main.activity_edit.*

class DetailActivity : AppCompatActivity(), DetailFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_24dp)
            setNavigationOnClickListener {
                finish()
            }

        val bundle = intent.extras!!
        val title = bundle.getString(IntentKey.TITLE.name)!!
        val deadline = bundle.getString(IntentKey.DEAD_LINE.name)!!
        val taskDetail = bundle.getString(IntentKey.TASK_DETAIL.name)!!
        val isCompleted = bundle.getBoolean(IntentKey.IS_COMPLETED.name)
        val mode = bundle.getSerializable(IntentKey.MODE_IN_EDIT.name) as ModeInEdit

        supportFragmentManager.beginTransaction().add(R.id.container_detail,
            DetailFragment.newInstance(title, deadline, taskDetail, isCompleted, mode), FragmentTag.DETAIL.toString()).commit()


        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.apply { findItem(R.id.menu_delete).isVisible = false }
        menu.apply { findItem(R.id.menu_edit).isVisible = false }
        menu.apply { findItem(R.id.menu_done).isVisible = false }
        return true
    }

    override fun DataDeleted() {
        finish()
    }

    override fun goEdit(title: String, deadline: String, taskDetail: String, isCompleted: Boolean, mode: ModeInEdit?) {
        val intent = Intent(this@DetailActivity, EditActivity::class.java).apply {
            putExtra(IntentKey.TITLE.name, title)
            putExtra(IntentKey.DEAD_LINE.name, deadline)
            putExtra(IntentKey.TASK_DETAIL.name, taskDetail)
            putExtra(IntentKey.IS_COMPLETED.name, isCompleted)
            putExtra(IntentKey.MODE_IN_EDIT.name, mode)
        }

        startActivity(intent)
        finish()
    }

}
