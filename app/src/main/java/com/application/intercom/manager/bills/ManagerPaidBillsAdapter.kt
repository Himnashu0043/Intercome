package com.application.intercom.manager.bills

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.manager.managerSide.bill.MangerBillPendingListRes
import com.application.intercom.databinding.ItemBillsToApproveBinding
import com.application.intercom.helper.setFormatDate


class ManagerPaidBillsAdapter(
    private val context: Context,
    val list: ArrayList<MangerBillPendingListRes.Data.Result>,
    val onPress: ViewReceipt
) : RecyclerView.Adapter<ManagerPaidBillsAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemBillsToApproveBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemBillsToApproveBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.tvPaidUnpaid.visibility = View.GONE
        holder.mView.tvPayManager.visibility = View.GONE
        holder.mView.tvTimer.visibility = View.INVISIBLE
        holder.mView.ivBillDownload.visibility = View.VISIBLE
        holder.mView.ivList.visibility = View.VISIBLE

        if (list[position].serviceCategory!!.isEmpty()) {
            if (list[position].is_bill_type_new ?: "" != "Service") {
                holder.mView.tvBillName.text = "${list[position].billType}"
            } else {
                holder.mView.tvBillName.text = context.getString(R.string.service_charge)
            }
        } else {
            holder.mView.tvBillName.text = list[position].serviceCategory?.get(0)?.name ?: ""
        }

        holder.mView.tvFlatNumber.text = list[position].flatInfo?.get(0)?.name ?: ""
        if (!list[position].date.isNullOrEmpty()) {
            val date = setFormatDate(list[position].date)
            holder.mView.tvDate.text = "${context.getString(R.string.due_date)} :$date"
        }

        holder.mView.tvPrice.text = "৳${list[position].amount}"
        println("-----paidList${list[position]}")
        holder.mView.ivBillDownload.setOnClickListener {
            if (list[position].recieptLink.isNullOrEmpty()) {
                Toast.makeText(context, "Receipt not generated yet!!", Toast.LENGTH_SHORT).show()
            } else {
                context.startActivity(
                    Intent(
                        context,
                        ReceiptManagerActivity::class.java
                    ).putExtra("pdfUrl", list[position].recieptLink ?: "")
                )
            }

        }
        holder.mView.ivList.setOnClickListener {
            if (holder.mView.viewReceiptlistPop.visibility == View.VISIBLE) {
                holder.mView.tvPrice.visibility = View.VISIBLE
                holder.mView.viewReceiptlistPop.visibility = View.GONE
            } else {
                holder.mView.tvPrice.visibility = View.INVISIBLE
                holder.mView.viewReceiptlistPop.visibility = View.VISIBLE
                holder.mView.tvRejectManager.visibility = View.GONE
            }

        }
        holder.mView.tvViewRecient.setOnClickListener {
            holder.mView.viewReceiptlistPop.visibility = View.GONE
            onPress.onViewReceipt(
                list[position].referenceNo ?: "",
                list[position].uploadDocument ?: ""
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size

    }

    interface ViewReceipt {
        fun onViewReceipt(refno: String, uploadDocument: String)
    }
}