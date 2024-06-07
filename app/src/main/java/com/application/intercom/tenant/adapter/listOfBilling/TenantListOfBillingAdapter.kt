package com.application.intercom.tenant.adapter.listOfBilling

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.TenantListOfBillingItemsBinding


class TenantListOfBillingAdapter(val con: Context) :
    RecyclerView.Adapter<TenantListOfBillingAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: TenantListOfBillingItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            TenantListOfBillingItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == 2) {
            holder.mView.paynow.setCardBackgroundColor(Color.parseColor("#FD701E"))
            holder.mView.tvPayNow.text = "Pending"
            holder.mView.tvPayNow.setTextColor(Color.parseColor("#FFFFFFFF"))
        } /*else {
            holder.mView.paynow.setCardBackgroundColor(Color.parseColor("#4FBF67"))
            holder.mView.tvPayNow.text = "Paid"
            holder.mView.tvPayNow.setTextColor(Color.parseColor("#FFFFFFFF"))
        }*/
    }

    override fun getItemCount(): Int {
        return 8
    }
}