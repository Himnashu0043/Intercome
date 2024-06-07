package com.application.intercom.user.property

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.data.model.local.ImageAddModel
import java.util.ArrayList

class SlidingImagesAdapter(activity: AppCompatActivity, private val itemsCount: ArrayList<String>) :
    FragmentStateAdapter(activity) {
    override fun getItemCount() = itemsCount.size
    override fun createFragment(position: Int) = SlidingFragment.getInstance(position,itemsCount)

}