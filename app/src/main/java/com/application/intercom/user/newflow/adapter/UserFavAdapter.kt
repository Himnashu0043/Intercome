package com.application.intercom.user.newflow.adapter

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.newUser.favList.UserFavListRes
import com.application.intercom.data.model.remote.newUser.favList.UserFavPropertyListRes
import com.application.intercom.databinding.UserFavItemsBinding
import com.application.intercom.user.property.UserPropertyViewPagerAdapter
import com.application.intercom.utils.CommonUtil
import java.util.*
import kotlin.collections.ArrayList

class UserFavAdapter(
    val con: Context,
    val list: ArrayList<UserFavPropertyListRes.Data>,
    val onPress: FavCLick
) :
    RecyclerView.Adapter<UserFavAdapter.MyViewHolder>() {
    private lateinit var userViewPagaer: UserPropertyViewPagerAdapter
    var currentPos = 0
    val handler = Handler()
    var test = ArrayList<String>()

    inner class MyViewHolder(val mView: UserFavItemsBinding) : RecyclerView.ViewHolder(mView.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(UserFavItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        test = list[position].flatDetail.photo
        userViewPagaer =
            UserPropertyViewPagerAdapter(
                con,
                test
            )
        holder.mView.viewPager1.adapter = userViewPagaer
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

        holder.mView.tvPropertyName.text =
            if (list[position].buildingInfo == null) list[position].flatDetail.title else list[position].buildingInfo.get(
                0
            ).buildingName
        if (list[position].buildingInfo == null) {
            holder.mView.tvLocation.text = "${list[position].flatDetail.address}"
        } else {
            holder.mView.tvLocation.text = "${list[position].buildingInfo.get(0).address}"
        }

        holder.mView.tvPropertyPrice.text = "৳${list[position].flatDetail.price}"
//        val dis: Double = list[position].distance / 1000
//        val dis2: Double = String.format("%.1f", dis).toDouble()
//        holder.mView.tvDistance.text = "${dis2} Km"
        holder.mView.tvDistance.visibility = View.INVISIBLE
        holder.mView.view.visibility = View.INVISIBLE
        if (list[position].flatInfo == null) {
            holder.mView.tvFit.text = "${list[position].flatDetail.sqft} ${con.getString(R.string.ft)}"
            holder.mView.tvBedroom.text = "${list[position].flatDetail.bedroom} ${con.getString(R.string.bhk)}"
            holder.mView.tvBathroom.text = "${list[position].flatDetail.bathroom} ${con.getString(R.string.bath)}"
            holder.mView.tvPropertyType2.text = "${list[position].flatDetail.propertyType}"
            holder.mView.tvPropertyType1.visibility = View.GONE
        } else {
            holder.mView.tvFit.text = "${list[position].flatInfo.sqft} ${con.getString(R.string.ft)}"
            holder.mView.tvBedroom.text = "${list[position].flatInfo.bedroom} ${con.getString(R.string.bhk)}"
            holder.mView.tvBathroom.text = "${list[position].flatInfo.bathroom} ${con.getString(R.string.bath)}"
            holder.mView.tvPropertyType2.text = "${list[position].flatInfo.flatStatus}"
            holder.mView.tvPropertyType1.text =
                "${list[position].buildingInfo.get(0).propertyType}"

        }
        holder.mView.favImg.setOnClickListener {
            if (list[position].flatInfo != null) {
                onPress.selectFav(list[position].flatInfo._id)
            } else {
                onPress.selectFav(list[position].propertyId)
            }

        }

    }


    override fun getItemCount(): Int {
        return list.size
    }

    interface FavCLick {
        fun selectFav(propertyId: String)
    }

}