package com.tugcearas.budgetapp.ui.expense

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tugcearas.budgetapp.R
import com.tugcearas.budgetapp.data.BudgetModel
import com.tugcearas.budgetapp.databinding.FragmentExpenseScreenBinding
import com.tugcearas.budgetapp.ui.adapter.BudgetAdapter
import com.tugcearas.budgetapp.util.toastMessage

class ExpenseScreen : Fragment() {

    private lateinit var binding: FragmentExpenseScreenBinding
    private val expenseAdapter by lazy { BudgetAdapter(::onBudgetItemClick, ::onDeleteClick) }
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpenseScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.firestore

        binding.rvExpense.adapter = expenseAdapter

        getExpense()
    }

    private fun getExpense() {
        db.collection("income_expense").whereEqualTo("incomeExpense", false)
            .addSnapshotListener{ snapshot, e ->
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
                totalBudget -= document.get("price") as Double

                with(binding) {
                    tvExpenseTotal.text = "${totalBudget} $"
                    tvExpenseTotal.setTextColor(Color.RED)
                }
            }
            expenseAdapter.submitList(tempList)
        }
    }

    private fun onBudgetItemClick(budget:BudgetModel){
        val action = ExpenseScreenDirections.actionExpenseScreenToDetailScreen(budget)
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
            .setTitle(R.string.alert_dialog_budget_title)
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