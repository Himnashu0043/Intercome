package com.application.intercom.gatekepper.gatekeeperAdapter.ownerTenantSingleEntryHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.singleEntryHistory.OwnerTenantSingleEntryHistoryList
import com.application.intercom.databinding.OngoingOwnerTenantSingleEntryHistoryItemBinding
import com.application.intercom.databinding.OngoingRegularEntryHisItemBinding
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.utils.loadImagesWithGlideExt

class OngoingOwnerTenantSingleEntryHistoryAdapter(
    val con: Context,
    val list: ArrayList<OwnerTenantSingleEntryHistoryList.Data>,
    val onPress: ListenerCommon
) :
    RecyclerView.Adapter<OngoingOwnerTenantSingleEntryHistoryAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: OngoingOwnerTenantSingleEntryHistoryItemBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            OngoingOwnerTenantSingleEntryHistoryItemBinding.inflate(
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
        holder.mView.paynow.setOnClickListener {
            onPress.onAccept(list[position]._id)
        }
        holder.mView.rejectnow.setOnClickListener {
            onPress.onReject(list[position]._id)
        }
        holder.mView.imageView93.loadImagesWithGlideExt(list[position].photo)
        holder.mView.textView175.text = list[position].visitorName
        holder.mView.textView176.text = list[position].mobileNumber
        holder.mView.textView178.text = list[position].flatInfo.get(0).name
        holder.mView.textView177.text =
            "${list[position].visitCategoryName} | ${list[position].visitorType} Entry"
        holder.itemView.setOnClickListener {
            onPress.onClick(list[position], position)
        }
        if (list[position].visitorStatus.equals("Accept")) {
            holder.mView.tvin.visibility = View.VISIBLE
            holder.mView.tvinData.visibility = View.VISIBLE
            holder.mView.paynow.visibility = View.INVISIBLE
            holder.mView.rejectnow.visibility = View.INVISIBLE
            holder.mView.tvinData1.visibility = View.VISIBLE

            val date = setNewFormatDate(list[position].createdAt)
            holder.mView.tvinData.text = "${list[position].entryTime}"
            holder.mView.tvinData1.text = "${date}"

        } else {
            holder.mView.tvin.visibility = View.INVISIBLE
            holder.mView.tvinData.visibility = View.INVISIBLE
            holder.mView.paynow.visibility = View.VISIBLE
            holder.mView.rejectnow.visibility = View.VISIBLE
            holder.mView.tvinData1.visibility = View.INVISIBLE
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    interface ListenerCommon {
        fun onAccept(visitorId: String)
        fun onReject(visitorId: String)
        fun onClick(msg: OwnerTenantSingleEntryHistoryList.Data, position: Int)
    }
}