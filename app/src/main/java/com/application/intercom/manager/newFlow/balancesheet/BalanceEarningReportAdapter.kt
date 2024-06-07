package com.application.intercom.manager.newFlow.balancesheet

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.IncomeReportManagerList
import com.application.intercom.databinding.BalanceEaringReportItemBinding
import com.application.intercom.helper.getMonthOfDate
import com.application.intercom.helper.setFormatDate
import com.application.intercom.manager.bills.ReceiptManagerActivity
import com.application.intercom.manager.bills.ViewReceiptManagerActivity
import com.application.intercom.utils.CommonUtil

class BalanceEarningReportAdapter(
    val con: Context,
    var list: ArrayList<IncomeReportManagerList.Data.Result>
) :
    RecyclerView.Adapter<BalanceEarningReportAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: BalanceEaringReportItemBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            BalanceEaringReportItemBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.imageView148.setOnClickListener {
            if (holder.mView.listPop.visibility == View.VISIBLE) {
                holder.mView.listPop.visibility = View.GONE

            } else {
                holder.mView.listPop.visibility = View.VISIBLE
                holder.mView.tvEdit.setOnClickListener {
                    holder.mView.listPop.visibility = View.GONE
                    /* Toast.makeText(con, "Bill Download..", Toast.LENGTH_SHORT).show()
                     CommonUtil.startDownload(
                         list[position].uploadBill?.get(0) ?: "",
                         con,
                         "Bill Download",
                         "Bill Download"
                     )*/
                    if (!list[position].referenceNo.isNullOrEmpty()) {
                        con.startActivity(
                            Intent(con, ViewReceiptManagerActivity::class.java).putExtra(
                                "img",
                                list[position].uploadDocument ?: ""
                            ).putExtra("ref", list[position].referenceNo ?: "")
                        )
                    } else {
                        Toast.makeText(con, "Reference No not Found!", Toast.LENGTH_SHORT).show()
                    }
                }
                holder.mView.tvReport.setOnClickListener {
                    holder.mView.listPop.visibility = View.GONE
                    /*if (!list[position].recieptLink.isNullOrEmpty()) {
                        CommonUtil.startDownload(
                            list[position].recieptLink ?: "",
                            con,
                            "Receipt Download",
                            "Receipt Download"
                        )
                        Toast.makeText(con, "Receipt Download..", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(con, "No Receipt Found yet", Toast.LENGTH_SHORT).show()
                    }*/
                    if (!list[position].recieptLink.isNullOrEmpty()) {
                        con.startActivity(
                            Intent(
                                con,
                                ReceiptManagerActivity::class.java
                            ).putExtra("pdfUrl", list[position].recieptLink ?: "")
                        )
                    } else {
                        Toast.makeText(con, "No Receipt Found yet", Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }
        if (list[position].categoryData.isNullOrEmpty() && list[position].categoryData!!.isEmpty()) {
            if (list[position].billType == "Rent") {
                if (list[position].userType == "owner" && list[position].is_bill_type_new == "Service") {
                    holder.mView.textView255.text = "Service Charge"
                } else {
                    holder.mView.textView255.text = "Rent"
                }
            } else {
                holder.mView.textView255.text = "Service Charge"
            }

        } else {
            holder.mView.textView255.text = list[position].categoryData?.get(0)?.name ?: ""
        }


        holder.mView.textView2591.text = "৳${list[position].amount}"
        if (!list[position].date.isNullOrEmpty()) {
            holder.mView.textView26812.text = getMonthOfDate(list[position].date ?: "")
            holder.mView.textView268122.text = setFormatDate(list[position].date ?: "")
        }
        holder.mView.textView2681225.text = setFormatDate(list[position].paidDate ?: "")
        holder.mView.textView259.text = list[position].voucherNo ?: ""
        holder.mView.textView25951.text = list[position].payType ?: ""
        if (!list[position].ownerInfo.isNullOrEmpty()) {
            holder.mView.textView268.text = list[position].ownerInfo?.first()?.fullName ?: ""
        } else {
            holder.mView.textView268.text = list[position].tenantInfo?.get(0)?.role
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}