package com.application.intercom.tenant.adapter.FragementServices

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.RecentlyItemsBinding


class ListServiceAdapter(val con: Context) :
    RecyclerView.Adapter<ListServiceAdapter.MyViewHolder>() {
    class MyViewHolder (val mView: RecentlyItemsBinding):RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(RecentlyItemsBinding.inflate(LayoutInflater.from(con),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 20
    }
}