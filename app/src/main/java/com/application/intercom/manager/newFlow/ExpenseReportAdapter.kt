package com.application.intercom.manager.newFlow

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.ExpensesReportsManagerList
import com.application.intercom.databinding.EarningReportItemsBinding
import com.application.intercom.helper.getMonthOfDate
import com.application.intercom.helper.setFormatDate
import com.application.intercom.manager.bills.ViewReceiptManagerActivity
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.SessionConstants

class ExpenseReportAdapter(
    val con: Context,
    var list: ArrayList<ExpensesReportsManagerList.Data.Result>
) :
    RecyclerView.Adapter<ExpenseReportAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: EarningReportItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            EarningReportItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.view.visibility = View.GONE
        holder.mView.textView255.text = list[position].expenseName
        holder.mView.textView2591.text = "৳${list[position].expenseAmount}"
        holder.mView.textView259.text = list[position].refernceId
        holder.mView.textView2671.text = "Monthly"
        holder.mView.textView26712.text = "Bill Date"
        holder.mView.textView267124.text = "Payment Date"
        holder.mView.tvpayType.text = list[position].payType ?: ""
        if (!list[position].date.isNullOrEmpty()) {
            holder.mView.textView26812.text = getMonthOfDate(list[position].date ?: "")
            holder.mView.textView2681225.text = setFormatDate(list[position].date ?: "")
        } else {
            holder.mView.textView26812.text = ""
            holder.mView.textView2681225.text = ""
        }

        if (!list[position].billDate.isNullOrEmpty()) {
            holder.mView.textView268122.text = setFormatDate(list[position].billDate ?: "")
        } else {
            holder.mView.textView268122.text =
                ""
        }

        holder.mView.textView268.text = prefs.getString(SessionConstants.MANAGERNAME, "")
        holder.mView.imageView148.setOnClickListener {
            if (holder.mView.listPopnew.visibility == View.VISIBLE) {
                holder.mView.listPopnew.visibility = View.GONE
            } else {
                holder.mView.listPopnew.visibility = View.VISIBLE
                holder.mView.tvEdit1.setOnClickListener {
                    holder.mView.listPopnew.visibility = View.GONE
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
                    /*Toast.makeText(con, "Bill Download..", Toast.LENGTH_SHORT).show()
                    CommonUtil.startDownload(
                        list[position].uploadBill?.get(0) ?: "",
                        con,
                        "Bill Download",
                        "Bill Download"
                    )*/
                }
               /* holder.mView.tvReport1.setOnClickListener {
                    holder.mView.listPopnew.visibility = View.GONE
                    Toast.makeText(con, "Receipt Download..", Toast.LENGTH_SHORT).show()
                    CommonUtil.startDownload(
                        list[position].uploadReciept ?: "",
                        con,
                        "Receipt Download",
                        "Receipt Download"
                    )
                }*/
                holder.mView.tvReport1.setOnClickListener {
                    holder.mView.listPopnew.visibility = View.GONE
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
                /* holder.mView.tvInActive1.setOnClickListener {
                     holder.mView.listPopnew.visibility = View.GONE
                     Toast.makeText(con, "Coming soon..", Toast.LENGTH_SHORT).show()
                 }*/
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}