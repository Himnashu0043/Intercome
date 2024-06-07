package com.application.intercom.gatekepper.gatekeeperAdapter.ownerTenantRegularEntryHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.owner.regularHistory.RegularHistoryList
import com.application.intercom.databinding.CompletedRegularEntryHisItemBinding
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.utils.loadImagesWithGlideExt

class CompletedOwnerTenantRegularEntryHistoryAdapter(
    val con: Context,
    val list: ArrayList<RegularHistoryList.Data.Result>,
    val onPress: SendData
) :
    RecyclerView.Adapter<CompletedOwnerTenantRegularEntryHistoryAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: CompletedRegularEntryHisItemBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            CompletedRegularEntryHisItemBinding.inflate(
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
        holder.mView.imageView93.loadImagesWithGlideExt(list[position].photo)
        val date = setNewFormatDate(list[position].fromDate)
        val todate = setNewFormatDate(list[position].toDate)
        holder.mView.tvComletedInTime.text = "${list[position].fromTime} , ${date}"
        holder.mView.tvComletedOutTime.text = "${list[position].toTime} , ${todate}"
        holder.itemView.setOnClickListener {
            onPress.onCLick(list[position]._id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface SendData {
        fun onCLick(visitorId: String)
    }
}