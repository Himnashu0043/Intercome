package com.application.intercom.user.newflow.adapter

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.newUser.favList.UserFavParkingListRes
import com.application.intercom.databinding.UserFavItemsBinding
import com.application.intercom.user.property.UserPropertyViewPagerAdapter
import com.application.intercom.utils.CommonUtil
import java.util.*
import kotlin.collections.ArrayList

class UserFavParkingAdapter(
    val con: Context,
    val list: ArrayList<UserFavParkingListRes.Data>,
    val onPress: FavParking
) :
    RecyclerView.Adapter<UserFavParkingAdapter.MyViewHolder>() {
    private lateinit var notificationsAdapterItem: UserPropertyViewPagerAdapter

    class MyViewHolder(val mView: UserFavItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(UserFavItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    var currentPos = 0
    val handler = Handler()
    var test = java.util.ArrayList<String>()
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        test = list[position].parkingInfo.parkingImages
        notificationsAdapterItem =
            UserPropertyViewPagerAdapter(
                con,
                test
            )
        holder.mView.viewPager1.adapter =
            UserPropertyViewPagerAdapter(con, test)
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
        holder.mView.view.visibility = View.GONE
        holder.mView.tvDistance.visibility = View.GONE
        holder.mView.layoutDistance.visibility = View.GONE
        holder.mView.layoutBathroom.visibility = View.GONE
        holder.mView.layoutBedroom.visibility = View.GONE
        holder.mView.layResi.visibility = View.GONE
        holder.mView.tvLocation.visibility = View.GONE
        holder.mView.ivLocation.visibility = View.GONE
        holder.mView.tvPropertyType2.text = item.parkingInfo.parkingStatus
        holder.mView.tvPropertyName.text =
            "${item.parkingInfo.parkingLocation} -${item.parkingInfo.parking_number}"
        if (item.parkingInfo != null) {
            if (item.parkingInfo.price == null) {
                holder.mView.tvPropertyPrice.text = "--"
            } else {
                holder.mView.tvPropertyPrice.text = "৳${item.parkingInfo.price}"
            }
        }
        holder.mView.favImg.setOnClickListener {
            onPress.selectFavParking(list[position].parkingInfo._id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface FavParking {
        fun selectFavParking(parkingId: String)
    }
}