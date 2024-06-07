package com.application.intercom.owner.adapter.regular

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.local.owner.commonOwnerTenant.CommonOwnerTenantRegularEntryList
import com.application.intercom.data.model.remote.owner.regularHistory.RegularHistoryList
import com.application.intercom.databinding.CommonRegularEntryItemsBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.ownerTenantRegularEntryHistory.OngoingOwnerTenantRegularEntryHistoryAdapter
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.utils.loadImagesWithGlideExt

class RegularEntryAdapter(
    val con: Context,
    val list: ArrayList<RegularHistoryList.Data.Result>,
    val onPress: CommonCLick
) :
    RecyclerView.Adapter<RegularEntryAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: CommonRegularEntryItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            CommonRegularEntryItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.textView175.text = list[position].visitorName
        holder.mView.textView176.text = list[position].mobileNumber
        holder.mView.textView178.text = list[position].flatInfo.get(0).name
        holder.mView.textView177.text = "${list[position].visitCategoryName} | ${list[position].visitorType} Entry"
        holder.mView.imageView93.loadImagesWithGlideExt(list[position].photo)
        holder.mView.tvEditHis.setOnClickListener {
            onPress.onEdit(position)
        }
        holder.mView.tvDelete.setOnClickListener {
            onPress.onDelete(list[position]._id)
        }
        holder.mView.ivCallIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${list[position].mobileNumber}")
            con.startActivity(intent)
        }
        holder.itemView.setOnClickListener {
            onPress.onClickItem(list[position],position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface CommonCLick {
        //        fun onCLick(visitorId: String)
        fun onEdit(position: Int)
        fun onDelete(visitorId: String)
        fun onClickItem(msg:RegularHistoryList.Data.Result,position: Int)
    }
}