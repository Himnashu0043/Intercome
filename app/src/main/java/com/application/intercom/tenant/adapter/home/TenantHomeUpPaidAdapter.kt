package com.application.intercom.tenant.adapter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.tenant.tenantSide.tenantBill.TenantUnPaidListRes
import com.application.intercom.databinding.TenantHomeUnpaidItemsBinding
import com.application.intercom.helper.setNewFormatDate


class TenantHomeUpPaidAdapter(
    val con: Context,
    val list: ArrayList<TenantUnPaidListRes.Data.Result>,
    val onPress:PayClick
) :
    RecyclerView.Adapter<TenantHomeUpPaidAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: TenantHomeUnpaidItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            TenantHomeUnpaidItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.textView91.text = list[position].billType
        holder.mView.textView92.text = list[position].flatInfo.get(0).name
        holder.mView.textView142.text = "৳${list[position].amount}"
        val date = setNewFormatDate(list[position].date)
        holder.mView.textView93.text = date
        holder.mView.tvPayNow.setOnClickListener {
            onPress.onPayClick(list[position]._id,position)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface PayClick {
        fun onPayClick(id: String, position: Int)
    }
}