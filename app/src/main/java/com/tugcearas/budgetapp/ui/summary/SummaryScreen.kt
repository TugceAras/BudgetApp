package com.tugcearas.budgetapp.ui.summary

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tugcearas.budgetapp.R
import com.tugcearas.budgetapp.data.BudgetModel
import com.tugcearas.budgetapp.databinding.FragmentSummaryScreenBinding
import com.tugcearas.budgetapp.ui.adapter.BudgetAdapter
import com.tugcearas.budgetapp.ui.auth.UserDataStore
import com.tugcearas.budgetapp.util.click
import com.tugcearas.budgetapp.util.toastMessage
import kotlinx.coroutines.launch

class SummaryScreen : Fragment() {

    private lateinit var binding: FragmentSummaryScreenBinding
    private lateinit var db: FirebaseFirestore
    private val budgetAdapter by lazy { BudgetAdapter(::onBudgetItemClick, ::onDeleteClick) }
    private lateinit var userDataStore: UserDataStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSummaryScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.firestore
        userDataStore = UserDataStore(requireContext())

        with(binding){

            btnFloating.click {
                findNavController().navigate(R.id.action_summaryScreen_to_detailScreen)
            }

            rvSummary.adapter = budgetAdapter

            // get name-surname
            viewLifecycleOwner.lifecycleScope.launch {
                userDataStore.nameSurnameFlow.collect { nameSurname ->
                    binding.tvNameSurname.text = nameSurname
                }
            }
        }

        listenBudget()
        signOut()
    }

    private fun signOut(){
        binding.btnSignout.click{
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.alert_dialog_signout_title))
                .setMessage(getString(R.string.alert_dialog_signout_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes)){_,_ ->
                    Firebase.auth.signOut()
                    findNavController().navigate(R.id.action_summaryScreen_to_signinScreen)
                    requireContext().toastMessage(getString(R.string.alert_dialog_successfully_signout))
                }
                .setNegativeButton(getString(R.string.no),null)
                .show()
        }
    }

    private fun listenBudget(){
        db.collection("income_expense").addSnapshotListener { snapshot, error ->
            val tempList = arrayListOf<BudgetModel>()
            var totalBudget:Double = 0.0
            snapshot?.forEach{document->
                tempList.add(
                    BudgetModel(
                        document.id,
                        document.get("title") as String,
                        (document.get("price") as Number).toDouble(),
                        document.get("description") as String,
                        document.get("incomeExpense") as Boolean?
                    )
                )
                if(document.get("incomeExpense") as Boolean) {
                    totalBudget += document.get("price") as Double
                } else {
                    totalBudget -= document.get("price") as Double
                }

                with(binding){
                    if (totalBudget > 0){
                        tvTotal.text =  "+${totalBudget} $"
                        tvTotal.setTextColor(Color.GREEN)
                    } else {
                        tvTotal.text = "${totalBudget} $"
                        tvTotal.setTextColor(Color.RED)
                    }
                }
            }
            budgetAdapter.submitList(tempList)
        }
    }

    private fun onBudgetItemClick(budget:BudgetModel){
        val action = SummaryScreenDirections.actionSummaryScreenToDetailScreen(budget)
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