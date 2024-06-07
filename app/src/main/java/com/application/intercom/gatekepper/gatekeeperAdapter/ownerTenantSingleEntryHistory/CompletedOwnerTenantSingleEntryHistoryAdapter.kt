package com.application.intercom.gatekepper.gatekeeperAdapter.ownerTenantSingleEntryHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.singleEntryHistory.OwnerTenantSingleEntryHistoryList
import com.application.intercom.databinding.CompletedSingleEntryHisItemBinding
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.utils.loadImagesWithGlideExt

class CompletedOwnerTenantSingleEntryHistoryAdapter(
    val con: Context,
    val list: ArrayList<OwnerTenantSingleEntryHistoryList.Data>,
    val onPress:CompletedOwnerTenant
) :
    RecyclerView.Adapter<CompletedOwnerTenantSingleEntryHistoryAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: CompletedSingleEntryHisItemBinding) :
        RecyclerView.ViewHolder(mView.root) {

    }

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
        val date = setNewFormatDate(list[position].createdAt)
        holder.mView.tvComletedInDate.text = date
        holder.mView.tvComletedoutDate.text = date
        holder.mView.textView177.text =
            "${list[position].visitCategoryName} | ${list[position].visitorType} Entry"
        holder.mView.imageView93.loadImagesWithGlideExt(list[position].photo)
        holder.itemView.setOnClickListener {
            onPress.onCLick(list[position],position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface CompletedOwnerTenant{
        fun onCLick(msg:OwnerTenantSingleEntryHistoryList.Data,position: Int)
    }
}