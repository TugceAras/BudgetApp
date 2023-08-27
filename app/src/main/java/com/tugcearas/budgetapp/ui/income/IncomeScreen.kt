package com.tugcearas.budgetapp.ui.income

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tugcearas.budgetapp.R
import com.tugcearas.budgetapp.data.BudgetModel
import com.tugcearas.budgetapp.databinding.FragmentIncomeScreenBinding
import com.tugcearas.budgetapp.ui.adapter.BudgetAdapter
import com.tugcearas.budgetapp.util.toastMessage

class IncomeScreen : Fragment() {

    private lateinit var binding: FragmentIncomeScreenBinding
    private val incomeAdapter by lazy { BudgetAdapter(::onBudgetItemClick, ::onDeleteClick) }
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncomeScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.firestore

        binding.rvIncome.adapter = incomeAdapter
        getIncome()
    }

    private fun getIncome(){
        db.collection("income_expense").whereEqualTo("incomeExpense", true)
            .addSnapshotListener { snapshot, error ->
            val tempList = arrayListOf<BudgetModel>()
            var totalBudget: Double = 0.0
            snapshot?.forEach { document ->
                tempList.add(
                    BudgetModel(
                        document.id,
                        document.get("title") as String,
                        (document.get("price") as Number).toDouble(),
                        document.get("description") as String,
                        document.get("incomeExpense") as Boolean?
                    )
                )
                totalBudget += document.get("price") as Double

                with(binding) {
                    tvIncomeTotal.text = "+${totalBudget} $"
                    tvIncomeTotal.setTextColor(Color.GREEN)
                }
            }
            incomeAdapter.submitList(tempList)
        }
    }

    private fun onBudgetItemClick(budget:BudgetModel){
        val action = IncomeScreenDirections.actionIncomeScreenToDetailScreen(budget)
        findNavController().navigate(action)
    }

    private fun deleteBudget(docId:String){
        db.collection("income_expense").document(docId)
            .delete()
            .addOnSuccessListener {
                //
            }
            .addOnFailureListener {
                //
            }
    }

    private fun onDeleteClick(docId:String){
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_dialog_budget_title))
            .setMessage(getString(R.string.budget_delete_message))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)){_,_ ->
                deleteBudget(docId)
                requireContext().toastMessage(getString(R.string.successfully_deleted))
            }
            .setNegativeButton(getString(R.string.no),null)
            .show()
    }
}