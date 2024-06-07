package com.application.intercom.user.property

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.application.intercom.R
import com.application.intercom.databinding.ActivityUserCompletePropertiesDetailsBinding
import com.application.intercom.user.subscription.GetIntercomPremiumActivity
import com.application.intercom.utils.CommonUtil
import java.util.*

class UserCompletePropertyDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserCompletePropertiesDetailsBinding
    private var mAdapter: AmenitiesAdapter? = null
    private lateinit var imagesArray: ArrayList<String>
    private lateinit var handler: Handler
    private var currentPage = 0
    private lateinit var slidingImageDots: Array<ImageView?>
    private var slidingDotsCount = 0
    private var from: String = ""
    private val slidingCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            for (i in 0 until slidingDotsCount) {
                slidingImageDots[i]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@UserCompletePropertyDetailsActivity,
                        R.drawable.default_dot
                    )
                )
            }

            slidingImageDots[position]?.setImageDrawable(
                ContextCompat.getDrawable(
                    this@UserCompletePropertyDetailsActivity,
                    R.drawable.selected_dot
                )
            )
        }
    }

    private fun setPropertyAdapter(list: ArrayList<String> = ArrayList()) {
//        binding.rvAmenities.layoutManager = GridLayoutManager(this, 4)
//        mAdapter = AmenitiesAdapter(this, list)
//        binding.rvAmenities.adapter = mAdapter

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserCompletePropertiesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        from = intent.getStringExtra("from").toString()
//        if (from.equals("user_parking")) {
//            binding.ivDiamond.visibility = View.VISIBLE
//            binding.paynow.visibility = View.VISIBLE
//        } else {
//            binding.ivDiamond.visibility = View.INVISIBLE
//            binding.paynow.visibility = View.INVISIBLE
//        }
        handler = Handler(Looper.myLooper()!!)
        binding.imageView52.setOnClickListener {
            finish()
        }
        binding.tvMembership.setOnClickListener {
            if (from.equals("user_parking")) {

            } else {
                startActivity(Intent(this, GetIntercomPremiumActivity::class.java))
            }
//            startActivity(Intent(this, GetIntercomPremiumActivity::class.java))
        }
        binding.constraintLayout.setOnClickListener {
            if (from.equals("user_parking")) {
                startActivity(Intent(this, GetIntercomPremiumActivity::class.java))
            } else {
                startActivity(Intent(this, PropertyDetailsActivity::class.java).putExtra("from",from))
            }

        }
        setPropertyAdapter()
        setUpSlidingViewPager()
    }

    private fun setUpSlidingViewPager() {
        imagesArray = arrayListOf()
        imagesArray.clear()
//        for (i in mBannerList) {
//            imagesArray.add(i.image)
//        }

        imagesArray.add("https://cdn.pixabay.com/photo/2020/03/11/01/53/landscape-4920705__340.jpg")
        imagesArray.add("https://cdn.pixabay.com/photo/2020/03/11/01/53/landscape-4920705__340.jpg")
        imagesArray.add("https://cdn.pixabay.com/photo/2020/03/11/01/53/landscape-4920705__340.jpg")

        val landingImagesAdapter =
            SlidingImagesAdapter(this, imagesArray)

        binding.viewpager2.apply {
            adapter = landingImagesAdapter
            registerOnPageChangeCallback(slidingCallback)

        }

        binding.viewpager2.offscreenPageLimit = 1
        binding.viewpager2.clipToPadding = false
        binding.viewpager2.clipChildren = false
//        viewPager2.setCurrentItem(2, false)
//        binding.viewpager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        slidingDotsCount = imagesArray.size
        slidingImageDots = arrayOfNulls(slidingDotsCount)

        for (i in 0 until slidingDotsCount) {
            slidingImageDots[i] = ImageView(this@UserCompletePropertyDetailsActivity)
            slidingImageDots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.selected_dot
                )
            )
            val params =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

            params.setMargins(8, 0, 0, 8)
            binding.sliderDots.addView(slidingImageDots[i], params)
        }

        slidingImageDots[0]?.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.default_dot
            )
        )

        val update = Runnable {
            if (currentPage == imagesArray.size) {
                currentPage = 0
            }
            binding.viewpager2.setCurrentItem(currentPage++, true)

        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 3000, 2000)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.viewpager2.unregisterOnPageChangeCallback(slidingCallback)
    }
}