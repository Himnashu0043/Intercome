package com.application.intercom.user.property

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.application.intercom.R
import com.application.intercom.data.model.local.ImageAddModel
import com.bumptech.glide.Glide


class SlidingFragment() : Fragment(R.layout.fragment_sliding) {


    companion object {
        const val ARG_POSITION = "position"
        var listing: ArrayList<String> = ArrayList()

        fun getInstance(position: Int, list: ArrayList<String>): Fragment {
            listing = list
            val fragment = SlidingFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_POSITION, position)
            bundle.putSerializable("FG", listing)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = requireArguments().getInt(ARG_POSITION)
        val l = requireArguments().getStringArrayList("FG")
       // val landingImagesArray = requireContext().resources.getStringArray(R.array.image_urls_array)

        val i = view.findViewById<ImageView>(R.id.sliding_image)
        Glide.with(i)
            .load(l?.get(position))
            .placeholder(R.drawable.back_icon)
            .error(R.drawable.bg_subscription_item)
            .into(i)
    }

}