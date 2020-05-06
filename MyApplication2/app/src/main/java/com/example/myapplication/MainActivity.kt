package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

var intBackGroundColor = 0
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        buttonTest.setOnClickListener {
            val intent = Intent(this@MainActivity, TestActivity::class.java)
            when(radioGroup.checkedRadioButtonId){
                R.id.radioButton2 -> intent.putExtra(getString(R.string.intent_key_memory_status), true)
                R.id.radioButton -> intent.putExtra(getString(R.string.intent_key_memory_status), false)
            }
            startActivity(intent)

        }
        buttonEdit.setOnClickListener {
            val intent = Intent(this@MainActivity, WordListActivity::class.java)
            startActivity(intent)
        }
        buttonColor1.setOnClickListener {
            intBackGroundColor = R.color.color1
            constraintLayout.setBackgroundResource(intBackGroundColor)}
        buttonColor2.setOnClickListener {
            intBackGroundColor = R.color.color2
            constraintLayout.setBackgroundResource(intBackGroundColor)
        }
        buttonColor3.setOnClickListener {
            intBackGroundColor = R.color.color3
            constraintLayout.setBackgroundResource(intBackGroundColor)
        }
        buttonColor4.setOnClickListener {
            intBackGroundColor = R.color.color4
            constraintLayout.setBackgroundResource(intBackGroundColor)
        }
        buttonColor5.setOnClickListener {
            intBackGroundColor = R.color.color5
            constraintLayout.setBackgroundResource(intBackGroundColor)
        }
        buttonColor6.setOnClickListener {
            intBackGroundColor = R.color.color6
            constraintLayout.setBackgroundResource(intBackGroundColor)
        }
    }
}
