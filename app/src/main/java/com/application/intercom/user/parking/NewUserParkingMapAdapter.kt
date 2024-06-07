package com.application.intercom.user.parking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.UserParkingListRes.UserParkingList
import com.application.intercom.databinding.NewUserPropertyMapItemsBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.utils.loadImagesWithGlideExt

class NewUserParkingMapAdapter(
    val con: Context,
    val list: ArrayList<UserParkingList.Data>,
    val onPress: MapParkingRcy
) :
    RecyclerView.Adapter<NewUserParkingMapAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: NewUserPropertyMapItemsBinding) :
        RecyclerView.ViewHolder(mView.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            NewUserPropertyMapItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.mView.view.visibility = View.INVISIBLE
        holder.mView.textView234.text = list[position].buildingName
        holder.mView.imageView134.loadImagesWithGlideExt(
            list[position].parkingInfo.parkingImages.get(
                0
            )
        )
        holder.mView.tvDate.text =
            "Date of Registration :${setFormatDate(list[position].parkingInfo.parkingDate)}"
        val diseee = "${list[position].district} , ${list[position].division}"
        holder.mView.tvPropertyType.text = list[position].parkingInfo.parkingStatus
        holder.mView.tvLocation.text = diseee
        if (list[position].parkingInfo != null) {
            if (list[position].parkingInfo.price == null) {
                holder.mView.tvPropertyPrice.text = "--"
            } else {
                holder.mView.tvPropertyPrice.text = "৳${list[position].parkingInfo.price}"
            }
        }
        val dis: Double = item.distance / 1000
        val dis2: Double = String.format("%.1f", dis).toDouble()
        holder.mView.tvDistance.text = "${dis2} Km"
        holder.itemView.setOnClickListener {
            onPress.onMapRcyClick(
                list[position],
                position,
                item.buildingName,
                diseee,
                item.parkingInfo.price,
                item.flatDetail.bedroom.toString(),
                item.flatDetail.sqft.toString(),
                item.flatDetail.bathroom.toString(),
                item.parkingInfo.parkingImages,
                dis2.toString(),
                item.parkingInfo.parkingDescription ?: "",
                item.flatDetail._id,
                item.flatDetail.buildingId,
                item.parkingInfo._id,
                item.latitude,
                item.longitude
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface MapParkingRcy {
        fun onMapRcyClick(
            msg: UserParkingList.Data,
            position: Int,
            buildingName: String,
            dis: String,
            price: String,
            bedRoom: String,
            ft: String,
            bathRoom: String,
            parkingImg: java.util.ArrayList<String>,
            disT2: String,
            discrption: String,
            flatDetailsId: String,
            buildingId: String,
            parkingId: String,
            lati: Double,
            longi: Double
        )
    }
}