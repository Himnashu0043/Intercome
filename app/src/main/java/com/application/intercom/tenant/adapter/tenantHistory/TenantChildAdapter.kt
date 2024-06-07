package com.application.intercom.tenant.adapter.tenantHistory

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.TenantChildItemsBinding
import com.application.intercom.tenant.activity.listOfBilling.TenantListOfBillingActivity

class TenantChildAdapter(val con: Context) :
    RecyclerView.Adapter<TenantChildAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: TenantChildItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            TenantChildItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.textView153.visibility = View.VISIBLE
        holder.mView.childLay.setOnClickListener {
            con.startActivity(Intent(con, TenantListOfBillingActivity::class.java))
        }


    }

    override fun getItemCount(): Int {
        return 2
    }
}