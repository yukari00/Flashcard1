package com.example.todo

import io.realm.RealmObject

open class TodoModel : RealmObject() {

    var title : String = ""

    var deadLine  = ""

    var taskDetail = ""

    var isCompleted = false
}