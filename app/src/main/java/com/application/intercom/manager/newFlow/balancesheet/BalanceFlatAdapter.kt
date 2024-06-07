package com.application.intercom.manager.newFlow.balancesheet

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.DueReportManagerList
import com.application.intercom.databinding.FlatItemBinding
import com.application.intercom.helper.setFormatDate

class BalanceFlatAdapter(
    val context: Context,
    val list: ArrayList<DueReportManagerList.Data.Result.Newfieldname>,
    val onPress: DuePayment
) :
    RecyclerView.Adapter<BalanceFlatAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: FlatItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(FlatItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (list[position].categoryData!!.isEmpty()) {
            if (list[position].is_bill_type_new ?: "" != "Service") {
                holder.mView.tvBillName.text = list[position].billType
            } else {
                holder.mView.tvBillName.text = "Service Charge"
            }
        } else {
            holder.mView.tvBillName.text = list[position].categoryData?.get(0)?.name ?: ""
        }
        if (!list[position].dueDate.isNullOrEmpty()) {
            holder.mView.textView187.text =
                "Due Date :${setFormatDate(list[position].dueDate ?: "")}"
        }
        holder.mView.tvPrice.text = "৳ ${list[position].amount}"
        holder.mView.tvPaidUnpaid.setOnClickListener {
            onPress.onDuePaymentNotify(position, list[position]._id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface DuePayment {
        fun onDuePaymentNotify(position: Int, billingId: String?)
    }

}