package com.application.intercom.manager.newFlow.balancesheet

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.ExpensesReportsManagerList
import com.application.intercom.databinding.BalanceEaringReportItemBinding
import com.application.intercom.helper.getMonthOfDate
import com.application.intercom.helper.setFormatDate
import com.application.intercom.manager.bills.ViewReceiptManagerActivity
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.SessionConstants

class BalanceExpensesReportAdapter(
    val con: Context,
    var list: ArrayList<ExpensesReportsManagerList.Data.Result>
) :
    RecyclerView.Adapter<BalanceExpensesReportAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: BalanceEaringReportItemBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            BalanceEaringReportItemBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.imageView148.setOnClickListener {
            if (holder.mView.listPop.visibility == View.VISIBLE) {
                holder.mView.listPop.visibility = View.GONE
            } else {
                holder.mView.listPop.visibility = View.VISIBLE
                holder.mView.tvEdit.setOnClickListener {
                    holder.mView.listPop.visibility = View.GONE
                    if (!list[position].referenceNo.isNullOrEmpty()) {
                        con.startActivity(
                            Intent(con, ViewReceiptManagerActivity::class.java).putExtra(
                                "img",
                                list[position].uploadBill?.get(0) ?: ""
                            ).putExtra("ref", list[position].referenceNo ?: "")
                        )
                    } else {
                        Toast.makeText(con, "Bill not Found!", Toast.LENGTH_SHORT).show()
                    }
                }
                holder.mView.tvReport.setOnClickListener {
                    holder.mView.listPop.visibility = View.GONE
                    if (!list[position].uploadReciept.isNullOrEmpty()) {
                        CommonUtil.startDownload(
                            list[position].uploadReciept ?: "",
                            con,
                            "Receipt Download",
                            "Receipt Download"
                        )
                        Toast.makeText(con, "Receipt Download..", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(con, "No Receipt Found yet", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }
        holder.mView.textView255.text = list[position].expenseName
        holder.mView.textView2591.text = "৳${list[position].expenseAmount}"
        holder.mView.textView259.text = list[position].refernceId
        holder.mView.textView26812.text = getMonthOfDate(list[position].date ?: "")
        holder.mView.textView2681225.text = setFormatDate(list[position].date ?: "")
        holder.mView.textView268122.text = setFormatDate(list[position].billDate ?: "")
        holder.mView.textView268.text = prefs.getString(SessionConstants.MANAGERNAME, "")
        holder.mView.textView25951.text = list[position].payType ?: ""
    }
}