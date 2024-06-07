package com.application.intercom.user.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.UserAdvertismentResponse
import com.application.intercom.databinding.ItemHomeOfferBannerBinding
import com.application.intercom.user.property.PropertyDetailsActivity
import com.application.intercom.user.property.UserPropertyViewPagerAdapter
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.loadImagesWithGlideExt
import java.util.*
import kotlin.collections.ArrayList

class HomeTopOfferBannerAdapter(
    private val context: Context, val list: ArrayList<UserAdvertismentResponse.Data>
) : RecyclerView.Adapter<HomeTopOfferBannerAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemHomeOfferBannerBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemHomeOfferBannerBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.mView.ivProperty.loadImagesWithGlideExt(item.image)
        holder.itemView.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(item.url)
            context.startActivity(openURL)
        }
//        notificationsAdapterItem =
//            UserTopAdvitAdapter(
//                context,
//                list
//            )
//        holder.mView.viewPager1.adapter =
//            UserTopAdvitAdapter(context, list)
//        holder.mView.tabLayout1.setupWithViewPager(holder.mView.viewPager1, true)
//        val runnable = Runnable {
//            if (currentPos == list.size - 1) currentPos = 0
//            else currentPos++
//            if (holder.mView.viewPager1 != null) {
//                holder.mView.viewPager1.setCurrentItem(currentPos, true)
//            }
//        }
//
//        Timer().schedule(object : TimerTask() {
//            override fun run() {
//                handler.post(runnable)
//            }
//        }, CommonUtil.DELAY_MS, CommonUtil.PERIOD_MS)

    }

    override fun getItemCount(): Int {
        return list.size

    }

    fun notifiyData(list: ArrayList<UserAdvertismentResponse.Data>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}