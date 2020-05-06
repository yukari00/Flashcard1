package com.example.todo

//必要な定数を補完

var isTwoPane = false

enum class IntentKey{
    TITLE, DEAD_LINE, TASK_DETAIL, IS_COMPLETED, MODE_IN_EDIT
}

enum class ModeInEdit{
    NEW_ENTRY, EDIT
}

enum class FragmentTag{
    MASTER, EDIT, DETAIL, DATE_PICKER
}