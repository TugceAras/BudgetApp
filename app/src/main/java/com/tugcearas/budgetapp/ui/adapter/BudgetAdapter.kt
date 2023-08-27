package com.tugcearas.budgetapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tugcearas.budgetapp.data.BudgetModel
import com.tugcearas.budgetapp.databinding.BudgetItemBinding

class BudgetAdapter(
    private val onBudgetItemClick: (BudgetModel) -> Unit,
    private val onDeleteClick: (String) -> Unit
): ListAdapter<BudgetModel, BudgetAdapter.BudgetViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder =
        BudgetViewHolder(
            BudgetItemBinding.inflate(LayoutInflater.from(parent.context),parent,false),
            onBudgetItemClick,
            onDeleteClick
        )

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) = holder.bind(getItem(position))

    inner class BudgetViewHolder(
        private val binding: BudgetItemBinding,
        private val onBudgetItemClick: (BudgetModel) -> Unit,
        private val onDeleteClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(budgetModel: BudgetModel) = with(binding) {
            tvBudgetTitle.text = budgetModel.title
            tvBudgetDescription.text = budgetModel.description

            val type = budgetModel.incomeExpense
            if (type == true){
                tvBudgetPrice.text = "+${budgetModel.price} $"
                tvBudgetPrice.setTextColor(Color.GREEN)
            }
            else {
                tvBudgetPrice.text = "-${budgetModel.price} $"
                tvBudgetPrice.setTextColor(Color.RED)
            }

            root.setOnClickListener{
                onBudgetItemClick(budgetModel)
            }

            btnDelete.setOnClickListener {
                budgetModel.docId?.let(onDeleteClick)
            }
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<BudgetModel>() {
        override fun areItemsTheSame(oldItem: BudgetModel, newItem: BudgetModel): Boolean {
            return oldItem.docId == newItem.docId
        }

        override fun areContentsTheSame(oldItem: BudgetModel, newItem: BudgetModel): Boolean {
            return oldItem == newItem
        }
    }
}