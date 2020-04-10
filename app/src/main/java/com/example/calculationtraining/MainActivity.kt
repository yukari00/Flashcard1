package com.example.calculationtraining

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Spinner セット
        //val arrayAdapter = ArrayAdapter<Int>(this, android.R.layout.simple_spinner_item)
        //arrayAdapter.add(10)
        //arrayAdapter.add(20)
        //arrayAdapter.add(30)

        val arrayAdapter = ArrayAdapter.createFromResource(this, R.array.numbers_of_spinner, android.R.layout.simple_spinner_item)
        //スピナーとadapterをつなぐ
        spinner.adapter = arrayAdapter

    button.setOnClickListener {
        //ボタンを押したらtestActivityページに行き、スピナーで選んだものを残り問題数に表示
        val numberOfQuestions =spinner.selectedItem.toString().toInt()
        val intent = Intent(this@MainActivity, TestActivity::class.java)
        intent.putExtra("numberOfQuestions", numberOfQuestions)
        startActivity(intent)
    }
    }
}
