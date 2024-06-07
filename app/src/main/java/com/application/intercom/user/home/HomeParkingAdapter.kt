package com.application.intercom.user.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.UserParkingListRes.UserParkingList
import com.application.intercom.databinding.ItemHomeParkingBinding
import com.application.intercom.utils.loadImagesWithGlideExt

class HomeParkingAdapter(
    private val context: Context,
    val list: ArrayList<UserParkingList.Data>,
    val onPress: ClickParing
) : RecyclerView.Adapter<HomeParkingAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemHomeParkingBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemHomeParkingBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        println("iiii${item.photos}")
        println("-----party${item.parkingInfo}")
        println("-----party111${item}")
        holder.mView.ivProperty.loadImagesWithGlideExt(item.photos.get(0))
        holder.mView.tvPropertyName.text = item.buildingName
        val dis = "${item.district} , ${item.division}"
        holder.mView.tvLocation.text = "${item.district} , ${item.division}"
        val disee: Double = item.distance / 1000
       // val dis2: Double = String.format("%.1f", disee).toDouble()
        holder.mView.tvBasementName.text = item.parkingInfo.parkingLocation
        holder.mView.tvPropertyPrice.text = "৳${item.parkingInfo.price}"
        holder.itemView.setOnClickListener {
            onPress.onCLickParking(
                list[position],
                position,
                item.buildingName,
                dis,
                item.parkingInfo.price,
                item.flatDetail.bedroom.toString(),
                item.flatDetail.sqft.toString(),
                item.flatDetail.bathroom.toString(),
                item.parkingInfo.parkingImages,
               /* dis2.toString()*/ 0.0.toString(),
                item.parkingInfo.parkingDescription ?: "",
                item.flatDetail._id,
                item.flatDetail.buildingId,
                item.parkingInfo._id,
                item.latitude,
                item.longitude
            )
//            context.startActivity(
//                Intent(
//                    context,
//                    CompleteParkingToLetDetailsActivity::class.java
//                ).putExtra("from", key)
//                    .putExtra("parking_send_list", parking_sendList)
//            )

        }
        if (item.isWishList) {
            holder.mView.favImg.setImageResource(R.drawable.fav_active_icon)
        } else {
            holder.mView.favImg.setImageResource(R.drawable.unfav)
        }
        holder.mView.favImg.setOnClickListener {
            updatelist(position)
            onPress.addFavParkingLstnr(item.parkingInfo._id)
        }
    }

    override fun getItemCount(): Int {
        return list.size

    }

    fun notifiyData(list: ArrayList<UserParkingList.Data>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    private fun updatelist(pos: Int) {
        for (i in list.indices) {
            if (i == pos)
                list.get(i).isWishList = true
            else
                list.get(i).isWishList = false
        }
        notifyDataSetChanged()
    }

    interface ClickParing {
        fun onCLickParking(
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

        fun addFavParkingLstnr(parkingId: String)
    }
}