package com.application.intercom.manager.parking

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
import com.application.intercom.data.model.remote.manager.managerParking.ManagerParkingListRes
import com.application.intercom.data.model.remote.manager.managerParking.ManagerParkingToletListRes
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerPropertyListRes
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerToletFlatListRes
import com.application.intercom.databinding.ActivityManagerParkingToLetParkingBinding
import com.application.intercom.user.property.AmenitiesAdapter
import com.application.intercom.user.property.SlidingImagesAdapter
import com.application.intercom.user.subscription.GetIntercomPremiumActivity
import com.application.intercom.utils.CommonUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class ManagerParkingToLetParkingActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityManagerParkingToLetParkingBinding
    private var mAdapter: AmenitiesAdapter? = null
    private lateinit var imagesArray: ArrayList<String>
    private lateinit var handler: Handler
    private var currentPage = 0
    private lateinit var slidingImageDots: Array<ImageView?>
    private var slidingDotsCount = 0
    private var key: String = ""
    private var parking_list: ManagerParkingListRes.Data.Result? = null
    private var toLet_parking_list: ManagerParkingToletListRes.Data.Result? = null
    private var getLati: Double = 0.0
    private var getLongi: Double = 0.0
    private var buildingName: String = ""
    private lateinit var mMap: GoogleMap
    private val slidingCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            for (i in 0 until slidingDotsCount) {
                slidingImageDots[i]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@ManagerParkingToLetParkingActivity,
                        R.drawable.default_dot
                    )
                )
            }

            slidingImageDots[position]?.setImageDrawable(
                ContextCompat.getDrawable(
                    this@ManagerParkingToLetParkingActivity,
                    R.drawable.selected_dot
                )
            )
        }
    }

    private fun setPropertyAdapter(list: ArrayList<String> = ArrayList()) {
//        binding.rvAmenities.layoutManager =  GridLayoutManager(this, 4)
//        mAdapter = AmenitiesAdapter(this,list)
//        binding.rvAmenities.adapter = mAdapter

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerParkingToLetParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        key = intent.getStringExtra("from").toString()
        println("----keypar$key")
        if (key == "parking") {
            parking_list =
                intent.getSerializableExtra("parking_list") as ManagerParkingListRes.Data.Result
            getLati = parking_list!!.buildingInfo.get(0).latitude
            getLongi = parking_list!!.buildingInfo.get(0).longitude
            buildingName = parking_list!!.buildingInfo.get(0).buildingName
            fetchData()
        } else if (key == "tolet_parking") {
            toLet_parking_list =
                intent.getSerializableExtra("tolet_parking_list") as ManagerParkingToletListRes.Data.Result
            getLati = toLet_parking_list!!.buildingInfo.get(0).latitude
            getLongi = toLet_parking_list!!.buildingInfo.get(0).longitude
            buildingName = toLet_parking_list!!.buildingInfo.get(0).buildingName
            fetchData()
        }
        handler = Handler(Looper.myLooper()!!)
        binding.imageView57.setOnClickListener {
            finish()
        }
        binding.tvMembership.setOnClickListener {
            startActivity(Intent(this, GetIntercomPremiumActivity::class.java))
        }
        setPropertyAdapter()
        setUpSlidingViewPager()
    }

    private fun fetchData() {
        val map = SupportMapFragment()
        supportFragmentManager.beginTransaction().replace(binding.mapview.id, map)
            .commitAllowingStateLoss()
        map.getMapAsync(this)
        if (key == "parking") {
            binding.tvPropertyName.text = parking_list!!.parkingLocation
            binding.tvLocation.text = parking_list!!.buildingInfo.get(0).address
            binding.tvDetailsDesc.text =
                parking_list!!.buildingInfo.get(0).description
            binding.tvPropertyPrice.text = "৳${parking_list!!.price}"
        } else if (key == "tolet_parking") {
            binding.tvPropertyName.text = toLet_parking_list!!.parkingLocation
            binding.tvLocation.text = toLet_parking_list!!.buildingInfo.get(0).address
            binding.tvDetailsDesc.text = toLet_parking_list!!.buildingInfo.get(0).description
            binding.tvPropertyPrice.text = "৳${toLet_parking_list!!.price}"
        }
    }

    private fun setUpSlidingViewPager() {
        imagesArray = arrayListOf()
        imagesArray.clear()
        if (key == "parking") {
            imagesArray.addAll(parking_list!!.parkingImages)
        } else if (key == "tolet_parking") {
            imagesArray.addAll(toLet_parking_list!!.parkingImages)
        }


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
            slidingImageDots[i] = ImageView(this@ManagerParkingToLetParkingActivity)
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

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val sydney = LatLng(
            getLati,
            getLongi
        )
        mMap.addMarker(
            MarkerOptions().position(sydney).title(buildingName)
        )
        val zoomLevel = 16.0f
        mMap.minZoomLevel
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))
    }
}