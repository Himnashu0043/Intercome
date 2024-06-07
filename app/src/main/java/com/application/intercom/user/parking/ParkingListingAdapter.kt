package com.application.intercom.user.parking

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.UserParkingListRes.UserParkingList
import com.application.intercom.databinding.ItemParkingBinding
import com.application.intercom.databinding.ItemPropertiesParkingBinding
import com.application.intercom.user.home.HomeParkingAdapter
import com.application.intercom.user.property.UserPropertyViewPagerAdapter
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.loadImagesWithGlideExt
import java.util.*
import kotlin.collections.ArrayList


class ParkingListingAdapter(
    private val context: Context,
    val list: ArrayList<UserParkingList.Data>,
    val onPress: ClickParing
) : RecyclerView.Adapter<ParkingListingAdapter.MyViewHolder>() {
    private lateinit var notificationsAdapterItem: UserPropertyViewPagerAdapter

    class MyViewHolder(val mView: ItemParkingBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemParkingBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    var currentPos = 0
    val handler = Handler()
    var test = java.util.ArrayList<String>()
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.layoutDistance.visibility = View.GONE
        holder.mView.layoutBedroom.visibility = View.GONE
        holder.mView.layoutBathroom.visibility = View.GONE
        val item = list[position]
        test = list[position].parkingInfo.parkingImages

        notificationsAdapterItem =
            UserPropertyViewPagerAdapter(
                context,
                test
            )
        holder.mView.viewPager1.adapter =
            UserPropertyViewPagerAdapter(context, test)
        holder.mView.tabLayout1.setupWithViewPager(holder.mView.viewPager1, true)
        val runnable = Runnable {
            if (currentPos == test.size - 1) currentPos = 0
            else currentPos++
            if (holder.mView.viewPager1 != null) {
                holder.mView.viewPager1.setCurrentItem(currentPos, true)
            }
        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(runnable)
            }
        }, CommonUtil.DELAY_MS, CommonUtil.PERIOD_MS)
        holder.mView.tvDate.visibility = View.GONE
        holder.mView.textView143.visibility = View.GONE
        val diseee = "${item.district} , ${item.division}"
//        holder.mView.ivProperty.loadImagesWithGlideExt(item.photos.get(0))
        holder.mView.tvPropertyName.text = item.buildingName
        holder.mView.tvPropertyType.text = item.parkingInfo.parkingStatus
        holder.mView.tvLocation.text = "${item.district} , ${item.division}"
        if (item.parkingInfo != null) {
            if (item.parkingInfo.price == null) {
                holder.mView.tvPropertyPrice.text = "--"
            } else {
                holder.mView.tvPropertyPrice.text = "৳${item.parkingInfo.price}"
            }
        }


        val dis: Double = item.distance / 1000
        val dis2: Double = String.format("%.1f", dis).toDouble()
        holder.mView.tvDistance.text = "${dis2} Km"
        holder.itemView.setOnClickListener {
            onPress.onCLickParking(
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
        if (item.isWishList) {
            holder.mView.favImg.setImageResource(R.drawable.fav_active_icon)
        } else {
            holder.mView.favImg.setImageResource(R.drawable.unfav)
        }
        holder.mView.favImg.setOnClickListener {
            updatelist(position)
            onPress.selectFavParking(item.parkingInfo!!._id)
        }
    }

    fun notifiyData(list: ArrayList<UserParkingList.Data>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
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

        fun selectFavParking(parkingId: String)
    }
}