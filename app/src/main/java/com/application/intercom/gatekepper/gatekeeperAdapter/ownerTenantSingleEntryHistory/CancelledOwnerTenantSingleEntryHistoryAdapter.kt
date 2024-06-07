package com.application.intercom.gatekepper.gatekeeperAdapter.ownerTenantSingleEntryHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.singleEntryHistory.OwnerTenantSingleEntryHistoryList
import com.application.intercom.databinding.CancelledOwnerTenantSingleEntryHistoryItemBinding
import com.application.intercom.utils.loadImagesWithGlideExt

class CancelledOwnerTenantSingleEntryHistoryAdapter(
    val con: Context,
    val list: ArrayList<OwnerTenantSingleEntryHistoryList.Data>,
    val onPress:CancelOwnerTenant
) :
    RecyclerView.Adapter<CancelledOwnerTenantSingleEntryHistoryAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: CancelledOwnerTenantSingleEntryHistoryItemBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            CancelledOwnerTenantSingleEntryHistoryItemBinding.inflate(
                LayoutInflater.from(
                    con
                ), parent, false
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
        holder.itemView.setOnClickListener {
            onPress.onCLick(list[position],position)
        }
        holder.mView.textView177.text =
            "${list[position].visitCategoryName} | ${list[position].visitorType} Entry"
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface CancelOwnerTenant {
        fun onCLick(msg: OwnerTenantSingleEntryHistoryList.Data, position: Int)
    }
}