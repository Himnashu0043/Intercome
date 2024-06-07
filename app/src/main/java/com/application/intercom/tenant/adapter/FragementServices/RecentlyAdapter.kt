package com.application.intercom.tenant.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.RecentlyItemsBinding
import com.application.intercom.tenant.activity.listofservices.ListOfServicesActivity

class RecentlyAdapter(val context: Context,var key:String) : RecyclerView.Adapter<RecentlyAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: RecentlyItemsBinding) : RecyclerView.ViewHolder(mView.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            RecentlyItemsBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.lay.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    ListOfServicesActivity::class.java
                ).putExtra("from", key)
            )
        }
    }

    override fun getItemCount(): Int {
        return 5
    }
}