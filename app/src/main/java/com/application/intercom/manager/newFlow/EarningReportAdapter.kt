package com.application.intercom.manager.newFlow

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.IncomeReportManagerList
import com.application.intercom.databinding.EarningReportItemsBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.manager.bills.ReceiptManagerActivity
import com.application.intercom.manager.bills.ViewReceiptManagerActivity
import com.application.intercom.utils.CommonUtil

class EarningReportAdapter(
    val con: Context,
    var list: ArrayList<IncomeReportManagerList.Data.Result>
) :
    RecyclerView.Adapter<EarningReportAdapter.MyViewHolder>() {
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

        /*if (!list[position].categoryData!!.isNullOrEmpty()) {
            holder.mView.textView255.text = list[position].categoryData?.get(0)?.name ?: ""
        } else {
            holder.mView.textView255.text = "Service Charge"
        }*/

        holder.mView.textView2591.text = "৳${list[position].amount}"
        holder.mView.tvpayType.text = list[position].payType ?: ""
        if (!list[position].date.isNullOrEmpty())
            holder.mView.textView259.text = setFormatDate(list[position].date ?: "")
        else holder.mView.textView259.text = ""
        holder.mView.textView268122.text = list[position].buildingInfo?.get(0)?.buildingName ?: ""
        holder.mView.textView2681225.text =
            list[position].flatInfo?.get(0)?.name ?: ""
        holder.mView.textView258.text = list[position].voucherNo ?: ""
        if (list[position]?.ownerInfo?.size ?: 0 > 0) {
            holder.mView.textView268.text = list[position].ownerInfo?.first()?.fullName ?: ""
            holder.mView.textView26812.text = list[position].ownerInfo?.first()?.phoneNumber ?: ""
        } else {
            holder.mView.textView268.text =
                list[position].tenantInfo?.first()?.fullName ?: ""
            holder.mView.textView26812.text = list[position].tenantInfo?.first()?.phoneNumber ?: ""
        }
        holder.mView.imageView148.setOnClickListener {
            if (holder.mView.listPopnew.visibility == View.VISIBLE) {
                holder.mView.listPopnew.visibility = View.GONE
            } else {
                holder.mView.listPopnew.visibility = View.VISIBLE
                holder.mView.tvEdit1.setOnClickListener {
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

                    holder.mView.listPopnew.visibility = View.GONE
                }
                /* holder.mView.tvInActive1.setOnClickListener {
                     Toast.makeText(con, "Coming soon..", Toast.LENGTH_SHORT).show()
                     holder.mView.listPopnew.visibility = View.GONE
                 }*/
                holder.mView.tvReport1.setOnClickListener {
                    holder.mView.listPopnew.visibility = View.GONE
                    if (!list[position].recieptLink.isNullOrEmpty()) {
                        /* CommonUtil.startDownload(
                             list[position].recieptLink ?: "",
                             con,
                             "Receipt Download",
                             "Receipt Download"
                         )
                         Toast.makeText(con, "Receipt Download..", Toast.LENGTH_SHORT).show()*/
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
    }

    override fun getItemCount(): Int {
        return list.size
    }
}