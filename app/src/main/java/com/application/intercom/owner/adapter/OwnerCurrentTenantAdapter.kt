package com.application.intercom.owner.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.owner.ownerTenantHistory.OwnerTenantCurrentHistoryListRes
import com.application.intercom.databinding.OwnerCurrentTenantItemsBinding
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.utils.loadImagesWithGlideExt


class OwnerCurrentTenantAdapter(
    val con: Context,
    val list: ArrayList<OwnerTenantCurrentHistoryListRes.Data>,
    val type: String
) :
    RecyclerView.Adapter<OwnerCurrentTenantAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: OwnerCurrentTenantItemsBinding) :
        RecyclerView.ViewHolder(mView.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            OwnerCurrentTenantItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (type.equals("current")) {
            holder.mView.textView121.text = list[position].fullName
            holder.mView.textView123.text = list[position].flatId.buildingId.address
            holder.mView.textView191.text = list[position].flatId.name
            val date = setNewFormatDate(list[position].dateOfOccupancy)
            holder.mView.textView126.text = date
            holder.mView.imageView65.loadImagesWithGlideExt(
                list[position].flatId.buildingId.photos.get(
                    0
                )
            )
            holder.mView.textView122.text = "৳${list[position].roomRent}"
            holder.mView.imageView66.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${list[position].flatId.owner.phoneNumber}")
                con.startActivity(intent)
            }
        } else if (type.equals("Previous")) {
            holder.mView.textView121.text = list[position].flatId.buildingId.buildingName
            holder.mView.textView123.text = list[position].flatId.buildingId.address
            holder.mView.textView191.text = list[position].flatId.name
            val date = setNewFormatDate(list[position].dateOfOccupancy)
            val removeDate = setNewFormatDate(list[position].removeDate)
            holder.mView.textView126.text = date
            holder.mView.textView127.text = removeDate
            holder.mView.imageView65.loadImagesWithGlideExt(
                list[position].flatId.buildingId.photos.get(
                    0
                )
            )
            holder.mView.textView122.text = "৳${list[position].parkingPrice}"
            holder.mView.imageView66.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${list[position].flatId.owner.phoneNumber}")
                con.startActivity(intent)
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size

    }
}