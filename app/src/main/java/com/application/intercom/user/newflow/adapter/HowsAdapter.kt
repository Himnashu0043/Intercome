package com.application.intercom.user.newflow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.HowsWorkItemsBinding

class HowsAdapter(val con: Context) : RecyclerView.Adapter<HowsAdapter.MyViewHolder>() {
    class MyViewHolder (val mView:HowsWorkItemsBinding):RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        return MyViewHolder(HowsWorkItemsBinding.inflate(LayoutInflater.from(con),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
      return 5
    }
}