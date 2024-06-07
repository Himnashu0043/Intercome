package com.application.intercom.tenant.adapter.visitorAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.ChildTenantVisitorItemsBinding


class ChildVisitorAdapter(val con: Context, val onPress: Click) :
    RecyclerView.Adapter<ChildVisitorAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ChildTenantVisitorItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ChildTenantVisitorItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.vistorChildLay.setOnClickListener {
            onPress.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    interface Click {
        fun onClick(position: Int)
    }
}