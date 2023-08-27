package com.tugcearas.budgetapp.ui.budgetdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tugcearas.budgetapp.R
import com.tugcearas.budgetapp.data.BudgetModel
import com.tugcearas.budgetapp.databinding.FragmentDetailScreenBinding
import com.tugcearas.budgetapp.util.click
import com.tugcearas.budgetapp.util.gone
import com.tugcearas.budgetapp.util.toastMessage
import com.tugcearas.budgetapp.util.visible

class DetailScreen : BottomSheetDialogFragment(R.layout.fragment_detail_screen) {

    private lateinit var binding: FragmentDetailScreenBinding
    private lateinit var db: FirebaseFirestore
    private val args:DetailScreenArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.firestore

        with(binding){
            args.budgetmodel?.let {
                tvDetailBudgetTitle.setText(it.title)
                tvDetailBudgetPrice.setText(it.price.toString())
                tvDetailBudgetDescription.setText(it.description)
                btnEdit.visible()
                btnAdd.gone()
            }
        }

        clickAddButton()
        clickEditButton()
    }

    private fun addBudget(title:String,price:Double,description:String, isIncomeExpense:Boolean){
        val budget = BudgetModel(
            docId = null,
            title = title,
            price = price,
            description = description,
            incomeExpense = isIncomeExpense
        )
        db.collection("income_expense").document(title).set(budget).addOnSuccessListener {
            val action = DetailScreenDirections.actionDetailScreenToSummaryScreen()
            findNavController().navigate(action)
        }.addOnFailureListener {
            //
        }
    }

    private fun clickAddButton(){
        with(binding){
            btnAdd.click {
                val title = tvDetailBudgetTitle.text.toString()
                val price = tvDetailBudgetPrice.text.toString()
                val description = tvDetailBudgetDescription.text.toString()
                val isIncomeExpense = btnRadioIncome.isChecked

                if (title.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty()) {
                    addBudget(title, price.toDouble(),description, isIncomeExpense)
                } else {
                    requireContext().toastMessage(getString(R.string.add_failed))
                }
            }
        }
    }

    private fun editBudget(docId:String,title:String,price:Double,description:String, isIncomeExpense:Boolean){

        db.collection("income_expense").document(docId)
            .update(
                mapOf(
                    "title" to title,
                    "price" to price,
                    "description" to description,
                    "incomeExpense" to isIncomeExpense
                )
            )
            .addOnSuccessListener {
                val action = DetailScreenDirections.actionDetailScreenToSummaryScreen()
                findNavController().navigate(action)
            }
            .addOnFailureListener {
                //
            }
    }

    private fun clickEditButton(){
        with(binding){
            btnEdit.click {
                val title = tvDetailBudgetTitle.text.toString()
                val description = tvDetailBudgetDescription.text.toString()
                val price = tvDetailBudgetPrice.text.toString()
                val isIncomeExpense = btnRadioIncome.isChecked

                if (title.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty()) {
                    editBudget((args.budgetmodel?.docId ?: "") as String, title, price.toDouble(), description, isIncomeExpense)
                } else {
                    //
                }
            }
        }
    }
}