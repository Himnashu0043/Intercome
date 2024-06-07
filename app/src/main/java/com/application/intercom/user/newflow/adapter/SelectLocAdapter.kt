package com.application.intercom.user.newflow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.SelectLocItemsBinding

class SelectLocAdapter(val con: Context, val cityName: ArrayList<String>) :
    RecyclerView.Adapter<SelectLocAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: SelectLocItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(SelectLocItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.textView225.text = cityName[position]
    }

    override fun getItemCount(): Int {
        return cityName.size
    }
}