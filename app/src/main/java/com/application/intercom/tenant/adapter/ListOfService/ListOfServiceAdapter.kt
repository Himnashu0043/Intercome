package com.intercom.tenant.adapter.FragementServices.ListOfService

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.ListOfServiceItemsBinding


class ListOfServiceAdapter(val con: Context) :
    RecyclerView.Adapter<ListOfServiceAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ListOfServiceItemsBinding):RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(ListOfServiceItemsBinding.inflate(LayoutInflater.from(con),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 8
    }
}