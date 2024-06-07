package com.application.intercom.tenant.adapter.post

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.TenantMycommunityItemsBinding

class TenantMyPostAdapter(val con: Context) :
    RecyclerView.Adapter<TenantMyPostAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: TenantMycommunityItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            TenantMycommunityItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 6
    }
}