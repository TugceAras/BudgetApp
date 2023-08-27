package com.tugcearas.budgetapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BudgetModel(
    val docId:String?,
    val title:String,
    val price:Double,
    val description:String,
    val incomeExpense:Boolean?
):Parcelable
