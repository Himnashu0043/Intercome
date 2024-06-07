package com.application.intercom.manager.parking

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerParking.ManagerParkingListRes
import com.application.intercom.databinding.ItemManagerParkingToLetParkingBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.utils.loadImagesWithGlideExt


class ManagerParkingAdapter(
    private val context: Context,
    val list: ArrayList<ManagerParkingListRes.Data.Result>,
    val onPress: Click
) : RecyclerView.Adapter<ManagerParkingAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemManagerParkingToLetParkingBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemManagerParkingToLetParkingBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.itemView.setOnClickListener {
            onPress.onParkingClick(position)
            // context.startActivity(Intent(context, ManagerParkingToLetParkingActivity::class.java))
        }

        holder.mView.ivForwardArrow.setOnClickListener {
            holder.mView.ivForwardArrow.visibility = View.GONE
            holder.mView.ivDownwardArrow.visibility = View.VISIBLE
            holder.mView.layoutExpand.visibility = View.VISIBLE

        }
        holder.mView.ivDownwardArrow.setOnClickListener {
            holder.mView.ivDownwardArrow.visibility = View.GONE
            holder.mView.ivForwardArrow.visibility = View.VISIBLE
            holder.mView.layoutExpand.visibility = View.GONE

        }
        holder.mView.tvProfileName.text = data.parkingLocation
        holder.mView.tvAddress.text =
            data.buildingInfo.get(0).address
        holder.mView.ivProfile.loadImagesWithGlideExt(data.parkingImages.get(0))
        ////owner Data
        if (!data.ownerInfo.isNullOrEmpty()) {
            holder.mView.tvExpandProfileName.text = data.ownerInfo[0].fullName
            holder.mView.tvExpandAddress.text = data.ownerInfo[0].address
            holder.mView.ivExpandProfile.loadImagesWithGlideExt(data.ownerInfo[0].profilePic)
            val valid = setFormatDate(data.buildingInfo.get(0).validFrom)
            holder.mView.tvDate.text = valid
            holder.mView.tvStatus.text = "Till now"
            holder.mView.ivCalling.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${data.ownerInfo[0].phoneNumber}")
                context.startActivity(intent)
            }

        } else {
            holder.mView.tvExpandProfileName.text = ""
            holder.mView.tvExpandAddress.text = ""
            holder.mView.tvDate.text = ""
            holder.mView.tvStatus.text = ""
        }


    }

    override fun getItemCount(): Int {
        return list.size

    }

    interface Click {
        fun onParkingClick(position: Int)
    }
}