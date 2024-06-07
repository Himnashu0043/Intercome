package com.application.intercom.user.myactivity

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.userFlatData.UserFlatListRes
import com.application.intercom.data.model.remote.userParkingActivityData.UserParkingActivityListRes
import com.application.intercom.databinding.ItemPropertiesParkingBinding
import com.application.intercom.user.property.UserPropertyViewPagerAdapter
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.loadImagesWithGlideExt
import java.util.*
import kotlin.collections.ArrayList

class MyActivityParkingListingAdapter(
    private val context: Context, val list:ArrayList<UserParkingActivityListRes.Data>, val onPress:Click
) : RecyclerView.Adapter<MyActivityParkingListingAdapter.MyViewHolder>() {
    private lateinit var notificationsAdapterItem: UserPropertyViewPagerAdapter
    class MyViewHolder (val mView: ItemPropertiesParkingBinding): RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder(ItemPropertiesParkingBinding.inflate(LayoutInflater.from(context),parent,false))
    }
    var test = java.util.ArrayList<String>()
    var currentPos = 0
    val handler = Handler()
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
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
        holder.mView.view.visibility = View.GONE
        holder.mView.tvDistance.visibility = View.GONE
       // holder.mView.ivProperty.loadImagesWithGlideExt(item.parkingInfo.parkingImages.get(0))
        holder.mView.tvPropertyName.text = item.buildingInfo.buildingName
        holder.mView.tvLocation.text =
            "${item.buildingInfo.district} , ${item.buildingInfo.division}"
        holder.mView.tvPropertyPrice.text = "৳${item.parkingInfo.price}"
        holder.mView.tvFit.text = "${item.flatDetail.sqft} ft"
        holder.mView.tvBedroom.text = "${item.flatDetail.bedroom} Bedroom"
        holder.mView.tvBathroom.text = "${item.flatDetail.bathroom} Bathroom"
        holder.mView.root.setOnClickListener {
            onPress.onParkingActivity(position)
//            context.startActivity(Intent(context,MyActivityPropertyParkingDetailsActivity::class.java))
       }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun notifiyData(list: ArrayList<UserParkingActivityListRes.Data>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
    interface Click{
        fun onParkingActivity(position: Int)
    }
}