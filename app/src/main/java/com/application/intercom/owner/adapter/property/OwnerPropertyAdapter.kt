package com.application.intercom.owner.adapter.property

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.owner.flat.OwnerFlatListRes
import com.application.intercom.data.model.remote.owner.parking.OwnerParkingListRes
import com.application.intercom.databinding.OwnerPropertyItemsBinding
import com.application.intercom.owner.activity.ownerParkingDetails.OwnerParkingDetailsActivity
import com.application.intercom.owner.activity.propertyDetails.OwnerPropertyDetailsActivity
import com.application.intercom.utils.loadImagesWithGlideExt

class OwnerPropertyAdapter(
    val con: Context,
    val list: ArrayList<OwnerFlatListRes.Data>,
    val onPress: ClickProperty
) :
    RecyclerView.Adapter<OwnerPropertyAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: OwnerPropertyItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            OwnerPropertyItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.mView.textView130.visibility = View.VISIBLE
        holder.mView.textView116.text = list[position].buildingInfo[0].buildingName
        holder.mView.textView117.text = list[position].buildingInfo[0].address
        holder.mView.tvFlatName.text = list[position].name
        if (list[position].tenant.count() > 0) {
            holder.mView.textView130.text = "Tenant"
        } else {
            if (list[position].is_home) {
                holder.mView.textView130.text = "My Home"
            } else {
                holder.mView.textView130.text = "Set as My Home"
            }

        }


        println("----size${list.size}")
        holder.mView.imageView63.loadImagesWithGlideExt(
            list[position].buildingInfo[0].photos.get(
                0
            )
        )
        holder.mView.itemsLay.setOnClickListener {
            onPress.onOwnerPropertyDetails(
                position
            )
//                con.startActivity(Intent(con, OwnerPropertyDetailsActivity::class.java))
        }
        holder.mView.textView130.setOnClickListener {
            onPress.onClickMyHome(list[position]._id)
            println("---flatId${list[position]._id}")
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ClickProperty {
        fun onOwnerPropertyDetails(
            position: Int
        )

        fun onClickMyHome(flatId: String)
    }
}