package com.application.intercom.gatekepper.gatekeeperAdapter.regularEntryHistoryDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.local.localModel.OutTimeModel
import com.application.intercom.databinding.ChildHistoryItemBinding

class OutHistoryAdapter(val con: Context, val list: ArrayList<OutTimeModel>) :
    RecyclerView.Adapter<OutHistoryAdapter.MyViewHOlder>() {
    class MyViewHOlder(val mView: ChildHistoryItemBinding) : RecyclerView.ViewHolder(mView.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHOlder {
        return MyViewHOlder(
            ChildHistoryItemBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHOlder, position: Int) {
        holder.mView.textView182.text = list[position].inTime
        holder.mView.textView183.text = list[position].exitTime
        println("-----${list[position].inTime},,,,${list[position].exitTime}")
    }

    override fun getItemCount(): Int {
        return list.size
    }
}