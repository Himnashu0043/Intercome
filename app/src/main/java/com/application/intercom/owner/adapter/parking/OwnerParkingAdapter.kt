package com.application.intercom.owner.adapter.parking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.owner.parking.OwnerParkingListRes
import com.application.intercom.databinding.OwnerPropertyItemsBinding
import com.application.intercom.owner.adapter.property.OwnerPropertyAdapter
import com.application.intercom.utils.loadImagesWithGlideExt

class OwnerParkingAdapter(
    val con: Context,
    var parkingList: ArrayList<OwnerParkingListRes.Data>,
    val onPress: ClickParking
) : RecyclerView.Adapter<OwnerParkingAdapter.MyViewHolder>() {
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
        holder.mView.textView130.visibility = View.INVISIBLE
        holder.mView.itemsLay.setOnClickListener {
            onPress.onOwnerParkingDetails(
                position
            )
            //  con.startActivity(Intent(con, OwnerParkingDetailsActivity::class.java))
        }
        holder.mView.tvFlat.visibility = View.VISIBLE
        holder.mView.textView116.text = parkingList[position].buildingInfo[0].buildingName
        holder.mView.textView117.text = parkingList[position].buildingInfo[0].address
        holder.mView.tvFlatName.text = parkingList[position].parkingNumber
        holder.mView.tvFlat.text = "Parking no:"
        holder.mView.textView130.text = "Owner"
        println("----size${parkingList.size}")
        holder.mView.imageView63.loadImagesWithGlideExt(
            parkingList[position].buildingInfo[0].photos.get(
                0
            )
        )
    }

    override fun getItemCount(): Int {
        return parkingList.size
    }

    interface ClickParking {
        fun onOwnerParkingDetails(
            position: Int
        )
    }
}