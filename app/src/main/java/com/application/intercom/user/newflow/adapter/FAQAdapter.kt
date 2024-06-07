package com.application.intercom.user.newflow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.UserFAQList
import com.application.intercom.databinding.FaqItemsBinding

class FAQAdapter(val con: Context, val list: ArrayList<UserFAQList.Data>) :
    RecyclerView.Adapter<FAQAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: FaqItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(FaqItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.ivUp.setOnClickListener {
            holder.mView.Des.visibility = View.VISIBLE
            holder.mView.ivUp.visibility = View.INVISIBLE
            holder.mView.ivdown.visibility = View.VISIBLE
        }
        holder.mView.ivdown.setOnClickListener {
            holder.mView.Des.visibility = View.GONE
            holder.mView.ivUp.visibility = View.VISIBLE
            holder.mView.ivdown.visibility = View.GONE
        }
        holder.mView.textView211.text = list[position].question
        holder.mView.tvdes.text = list[position].answer

    }

    override fun getItemCount(): Int {
        return list.size
    }
}