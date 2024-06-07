package com.application.intercom.gatekepper.gatekeeperAdapter.singleEntryHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.SingleEntryHistoryList
import com.application.intercom.databinding.CompletedSingleEntryHisItemBinding
import com.application.intercom.databinding.OngoingSingleEntryItemBinding
import com.application.intercom.utils.loadImagesWithGlideExt

class CompletedSingleEntryHisAdapter(
    val con: Context,
    val onPress: CompletedClick,
    val list: ArrayList<SingleEntryHistoryList.Data.Result>
) :
    RecyclerView.Adapter<CompletedSingleEntryHisAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: CompletedSingleEntryHisItemBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            CompletedSingleEntryHisItemBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.mView.textView175.text = list[position].visitorName
        holder.mView.textView176.text = list[position].mobileNumber
        holder.mView.textView178.text = list[position].flatInfo.get(0).name
        holder.mView.tvComletedInTime.text = list[position].entryTime
        holder.mView.tvComletedOutTime.text = list[position].exitTime
        holder.mView.textView177.text =
            "${list[position].visitCategoryName} | ${list[position].visitorType} ${con.getString(R.string.entry)}"
        holder.mView.imageView93.loadImagesWithGlideExt(list[position].photo)

        holder.itemView.setOnClickListener {
            onPress.onClick(position, list[position])
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface CompletedClick {
        fun onClick(position: Int, msg: SingleEntryHistoryList.Data.Result)
    }
}