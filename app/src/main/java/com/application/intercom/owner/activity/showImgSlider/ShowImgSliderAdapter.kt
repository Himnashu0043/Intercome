package com.application.intercom.owner.activity.showImgSlider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.application.intercom.databinding.UserPropertyViewPagerItemsBinding
import com.application.intercom.tenant.Model.ImageModels
import com.application.intercom.utils.loadImagesWithGlideExt

class ShowImgSliderAdapter(val con: Context, var list: List<String>) : PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding: UserPropertyViewPagerItemsBinding = UserPropertyViewPagerItemsBinding.inflate(
            LayoutInflater.from(
                con
            ), container, false
        )
        if (position in list.indices) {
            val old = list[position].replace(" ", "%20")
            binding.imageView52.loadImagesWithGlideExt(old)
        }
        container.addView(binding.root, 0)
        return binding.root
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}