package com.application.intercom.manager.newFlow.balancesheet.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.ExpensesReportsManagerList
import com.application.intercom.databinding.CategoryItemBinding

class ExpeneseCategoryAdapter(
    val con: Context,
    val list: ArrayList<ExpensesReportsManagerList.Data.ExpenseCategoryData>
) :
    RecyclerView.Adapter<ExpeneseCategoryAdapter.MyViewHOlder>() {
    class MyViewHOlder(val mView: CategoryItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHOlder {
        return MyViewHOlder(CategoryItemBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHOlder, position: Int) {
        if (!list[position].newfieldname?.get(0)?.categoryData!!.isNullOrEmpty()) {
            holder.mView.textView289.text =
                list[position].newfieldname?.get(0)?.categoryData?.get(0)?.name ?: ""
        } else {
            holder.mView.textView289.text = "Other"
        }
        holder.mView.textView290.text =
            (list[position].newfieldname?.get(0)?.expenseAmount ?: 0).toString()

    }

    override fun getItemCount(): Int {
        return list.size
    }
}