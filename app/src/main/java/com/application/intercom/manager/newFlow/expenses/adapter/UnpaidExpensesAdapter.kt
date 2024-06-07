package com.application.intercom.manager.newFlow.expenses.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.newflow.UnPaidExpensesManagerRes
import com.application.intercom.databinding.UnpaidExpensesManagerItemBinding
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.manager.newFlow.AddExpensesManagerActivity
import com.application.intercom.manager.newFlow.ManagerPaymentActivity
import com.application.intercom.utils.loadImagesWithGlideExt

class UnpaidExpensesAdapter(
    val con: Context,
    val list: ArrayList<UnPaidExpensesManagerRes.Data.Result>,
    val onPress: Delete
) :
    RecyclerView.Adapter<UnpaidExpensesAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: UnpaidExpensesManagerItemBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            UnpaidExpensesManagerItemBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.tvPayManager.visibility = View.VISIBLE
        println("---id${list[position]._id}")
        holder.mView.textView255.text = list[position].expenseName ?: ""
        holder.mView.textView258.text = list[position].refernceId ?: ""
        holder.mView.textView259.text = setNewFormatDate(list[position].date ?: "")
        holder.mView.textView260.text = list[position].expenseDetail ?: ""
        holder.mView.textView260555.text = "৳ ${list[position].expenseAmount ?: ""}"
        /*holder.mView.imageView147.loadImagesWithGlideExt(list[position].uploadBill?.takeIf { it.isNotEmpty() }
            ?.first() ?: "")*/

        holder.mView.imageView148.setOnClickListener {
            if (holder.mView.listPop.visibility == View.VISIBLE) {
                holder.mView.listPop.visibility = View.GONE
            } else {
                holder.mView.listPop.visibility = View.VISIBLE
            }

        }
        holder.mView.tvInActive.setOnClickListener {
            onPress.unpaidDelete(position, list[position]._id ?: "")
        }
        holder.mView.tvEdit.setOnClickListener {
            con.startActivity(
                Intent(con, AddExpensesManagerActivity::class.java).putExtra(
                    "from",
                    "edit"
                ).putExtra("editList", list[position])
            )
        }
        holder.mView.tvPayManager.setOnClickListener {
            con.startActivity(
                Intent(con, ManagerPaymentActivity::class.java).putExtra(
                    "expenseId",
                    list[position]._id
                ).putExtra("amount", list[position].expenseAmount ?: 0)
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface Delete {
        fun unpaidDelete(position: Int, billId: String?)
    }
}