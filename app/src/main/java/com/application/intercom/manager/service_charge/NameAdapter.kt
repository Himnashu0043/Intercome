package com.application.intercom.manager.service_charge

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.local.NameModel
import com.application.intercom.databinding.NameItemsBinding

class NameAdapter(val con: Context, var list: ArrayList<NameModel>, var onPress:Remove) :
    RecyclerView.Adapter<NameAdapter.MyViewHolder>() {
    private var adpr: DateAndAmonutAdapter? = null
    class MyViewHolder(val mView: NameItemsBinding) : RecyclerView.ViewHolder(mView.root) {

    }

    fun removeItem(list1: ArrayList<NameModel>,position: Int){
        this.list = list1
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(NameItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.textView225.text = list[position].name
        holder.mView.imageView126.setOnClickListener {
            onPress.removeItem(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface Remove{
        fun removeItem(positionNew: Int)
    }
}