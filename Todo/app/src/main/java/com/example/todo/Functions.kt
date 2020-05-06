package com.example.todo

import android.content.Context
import android.widget.Toast

//必要なメソッドを補完

fun makeToast(context : Context, message : String){

    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}