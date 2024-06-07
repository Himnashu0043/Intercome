package com.application.intercom.gatekepper.gatekeeperAdapter.regularEntryHistoryDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.local.localModel.InTimeModel
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.RegularEntryHistoryDetailsList
import com.application.intercom.databinding.ChildHistoryItemBinding

class ChildHistoryAdapter(val con: Context, val list: ArrayList<InTimeModel>) :
    RecyclerView.Adapter<ChildHistoryAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ChildHistoryItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ChildHistoryItemBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.textView182.text = list[position].inTime
        holder.mView.textView183.text = list[position].exitTime
    }

    override fun getItemCount(): Int {
        return list.size
    }
}