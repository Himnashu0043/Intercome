package com.application.intercom.gatekepper.gatekeeperAdapter.VisitorDetails

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.UpcomingItemsBinding


class UpComingAdapter(val con: Context, var changeText: String) :
    RecyclerView.Adapter<UpComingAdapter.MyViewHoloder>() {
    class MyViewHoloder(val mView: UpcomingItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHoloder {
        return MyViewHoloder(UpcomingItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHoloder, position: Int) {
        if (changeText.equals("completed")) {
            holder.mView.cardView6.setCardBackgroundColor(Color.parseColor("#4FBF67"))
            holder.mView.tvName.text = "Completed"
            holder.mView.tvName.setTextColor(Color.parseColor("#FFFFFFFF"))
        } else if (changeText.equals("cancelled")) {
            holder.mView.cardView6.setCardBackgroundColor(Color.parseColor("#D80027"))
            holder.mView.tvName.text = "Cancelled"
            holder.mView.tvName.setTextColor(Color.parseColor("#FFFFFFFF"))
        } else {
            if (position == 0) {
                holder.mView.cardView6.setCardBackgroundColor(Color.parseColor("#2299DD"))
                holder.mView.tvName.text = "Approved"
                holder.mView.tvName.setTextColor(Color.parseColor("#FFFFFFFF"))
            } else if (position == 1) {
                holder.mView.cardView6.setCardBackgroundColor(Color.parseColor("#2299DD"))
                holder.mView.tvName.text = "Approved"
                holder.mView.tvName.setTextColor(Color.parseColor("#FFFFFFFF"))
            } else {
                holder.mView.cardView6.setCardBackgroundColor(Color.parseColor("#FD701E"))
                holder.mView.tvName.text = "Pending"
                holder.mView.tvName.setTextColor(Color.parseColor("#FFFFFFFF"))
            }
        }

    }

    override fun getItemCount(): Int {
        return 8
    }
}