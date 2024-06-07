package com.application.intercom.user.newflow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.local.newFlow.UserPlanDetailsList
import com.application.intercom.databinding.InfoItemsBinding

class InfoAdpter(val con: Context, val list: ArrayList<UserPlanDetailsList.Data>) :
    RecyclerView.Adapter<InfoAdpter.MyViewHolder>() {
    class MyViewHolder(val mView: InfoItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(InfoItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.textView231.text = list[position].title
        holder.mView.textView233.text = list[position].description
    }

    override fun getItemCount(): Int {
        return list.size
    }
}