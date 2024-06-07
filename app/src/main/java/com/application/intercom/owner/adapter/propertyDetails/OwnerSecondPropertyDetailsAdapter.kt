package com.application.intercom.owner.adapter.propertyDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.OwnerTenantDetailsItemsBinding

class OwnerSecondPropertyDetailsAdapter(val con: Context) :
    RecyclerView.Adapter<OwnerSecondPropertyDetailsAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: OwnerTenantDetailsItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            OwnerTenantDetailsItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

    }

    override fun getItemCount(): Int {
        return 1
    }
}