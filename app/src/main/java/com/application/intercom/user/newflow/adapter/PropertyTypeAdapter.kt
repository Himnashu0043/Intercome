package com.application.intercom.user.newflow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.databinding.PropertyItemsBinding
import com.application.intercom.user.newflow.modal.UserPropertyModel

class PropertyTypeAdapter(
    val con: Context,
    val list: ArrayList<UserPropertyModel>,
    val onPress: PropertyUserClick,
    var getConm: String
) :
    RecyclerView.Adapter<PropertyTypeAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: PropertyItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(PropertyItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    var selectdpostion = 0
    var name1: String = ""
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.name.text = list[position].name
        if (getConm.equals(list[position].name)) {
            holder.mView.name.setTextColor(ContextCompat.getColor(con, R.color.orange))
            holder.mView.lay.background =
                ContextCompat.getDrawable(con, R.drawable.oragne_strock_with_white_bg)
        } else {
            holder.mView.name.setTextColor(ContextCompat.getColor(con, R.color.black))
            holder.mView.lay.background =
                ContextCompat.getDrawable(con, R.drawable.black_stroke_bg)
        }


        holder.mView.lay.setOnClickListener {
            getConm = list[position].name
            onPress.onCLickName(list[position].name)
            notifyDataSetChanged()
        }


    }


    override fun getItemCount(): Int {
        return list.size
    }

    interface PropertyUserClick {
        fun onCLickName(name: String)
    }
}