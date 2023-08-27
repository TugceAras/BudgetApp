package com.tugcearas.budgetapp.util

import android.content.Context
import android.view.View
import android.widget.Toast

fun Context.toastMessage(message:String) =
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()


fun View.click(func:() -> Unit){
    this.setOnClickListener{
        func()
    }
}

fun View.gone(){
    visibility = View.GONE
}

fun View.visible(){
    visibility = View.VISIBLE
}

