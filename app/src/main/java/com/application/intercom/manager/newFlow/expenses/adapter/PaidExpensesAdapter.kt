package com.application.intercom.manager.newFlow.expenses.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.newflow.UnPaidExpensesManagerRes
import com.application.intercom.databinding.UnpaidExpensesManagerItemBinding
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.loadImagesWithGlideExt

class PaidExpensesAdapter(
    val con: Context,
    val list: ArrayList<UnPaidExpensesManagerRes.Data.Result>
) :
    RecyclerView.Adapter<PaidExpensesAdapter.MyViewHolder>() {
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
        holder.mView.imageView149.visibility = View.VISIBLE
        println("=========$list")
        holder.mView.imageView148.setOnClickListener {
            if (holder.mView.listPopnew.visibility == View.VISIBLE) {
                holder.mView.listPopnew.visibility = View.GONE
            } else {
                holder.mView.listPopnew.visibility = View.VISIBLE
                holder.mView.tvEdit1.setOnClickListener {
                    holder.mView.listPopnew.visibility = View.GONE
                    Toast.makeText(con, "Downloading...", Toast.LENGTH_SHORT).show()
                    CommonUtil.run {
                        startDownload(
                            list[position].uploadBill?.get(0) ?: "",
                            con,
                            "Billing Report",
                            "Billing Report"
                        )
                    }
                }
                holder.mView.tvReport1.setOnClickListener {
                    holder.mView.listPopnew.visibility = View.GONE
                    Toast.makeText(con, "Downloading...", Toast.LENGTH_SHORT).show()
                    CommonUtil.run {
                        startDownload(
                            list[position].uploadBill?.get(0) ?: "",
                            con,
                            "Report",
                            "Report"
                        )
                    }
                }
            }
        }
        holder.mView.tvPayManager.visibility = View.GONE
        holder.mView.textView255.text = list[position].expenseName ?: ""
        holder.mView.textView258.text = list[position].refernceId ?: ""
        holder.mView.textView259.text = setNewFormatDate(list[position].date ?: "")
        holder.mView.textView260.text = list[position].expenseDetail ?: ""
        holder.mView.textView260555.text = "৳ ${list[position].expenseAmount ?: ""}"

        /*  holder.mView.imageView147.loadImagesWithGlideExt(list[position].uploadBill?.get(0)!!)*/

    }

    override fun getItemCount(): Int {
        return list.size
    }
}