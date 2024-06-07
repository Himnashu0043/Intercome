package com.application.intercom.tenant.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.MyPropertyItemsBinding


class MyPropertyAdapter(val con: Context) : RecyclerView.Adapter<MyPropertyAdapter.MyViewHolder>() {
    class MyViewHolder (val mView: MyPropertyItemsBinding):RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(MyPropertyItemsBinding.inflate(LayoutInflater.from(con),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
       return 4
    }
}