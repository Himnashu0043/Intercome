package com.application.intercom.manager.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.manager.managerSide.bill.MangerBillPendingListRes
import com.application.intercom.databinding.ItemBillsToApproveBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.manager.bills.ManagerApprovalBillsAdapter


class ManagerBillsToApproveAdapter(
    private val context: Context,
    val list: ArrayList<MangerBillPendingListRes.Data.Result>,
    val onPress: MarkClick
) : RecyclerView.Adapter<ManagerBillsToApproveAdapter.MyViewHolder>() {
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
        holder.mView.ivList.visibility = View.VISIBLE
        holder.mView.ivBillDownload.visibility = View.INVISIBLE
        holder.mView.tvTimer.visibility = View.INVISIBLE
        holder.mView.tvPayManager.visibility = View.GONE
       // holder.mView.tvBillName.text = list[position].billType
        if (list[position].serviceCategory!!.isEmpty()) {
            if (list[position].is_bill_type_new ?: "" != "Service") {
                holder.mView.tvBillName.text = "${list[position].billType}- ${list[position].billMonth} ${list[position].billYear}"
            } else {
                holder.mView.tvBillName.text = context.getString(R.string.service_charge)
            }
        } else {
            holder.mView.tvBillName.text = list[position].serviceCategory?.get(0)?.name?:""
        }
        holder.mView.tvFlatNumber.text = list[position].flatInfo?.get(0)?.name?:""
        if (!list[position].date.isNullOrEmpty()){
            val date = setFormatDate(list[position].date)
            holder.mView.tvDate.text = "${context.getString(R.string.due_date)} :$date"
        }
        holder.mView.tvPrice.text = "৳${list[position].amount}"
        holder.mView.tvPaidUnpaid.setOnClickListener {
            onPress.onMaskClick(position, list[position]._id?:"")
        }
        holder.mView.ivList.setOnClickListener {
            holder.mView.viewReceiptlistPop.visibility = View.VISIBLE
        }
        holder.mView.tvViewRecient.setOnClickListener {
            holder.mView.viewReceiptlistPop.visibility = View.GONE
            onPress.onViewReceipt(
                list[position].referenceNo ?: "",
                list[position].uploadDocument ?: ""
            )
        }
        holder.mView.tvRejectManager.setOnClickListener {
            holder.mView.viewReceiptlistPop.visibility = View.GONE
            onPress.onReject(
                list[position]._id ?: ""
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface MarkClick {
        fun onMaskClick(position: Int, id: String?)
        fun onViewReceipt(refno: String, uploadDocument: String)
        fun onReject(billID: String)
    }
}