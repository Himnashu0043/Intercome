package com.application.intercom.manager.property

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.application.intercom.R
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerPropertyListRes
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerToletFlatListRes
import com.application.intercom.data.model.remote.owner.flat.OwnerFlatListRes
import com.application.intercom.databinding.ActivityManagerPropertyToLetFlatBinding
import com.application.intercom.user.property.SlidingImagesAdapter
import com.application.intercom.utils.CommonUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class ManagerPropertyToLetFlatActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityManagerPropertyToLetFlatBinding
    private var mAdapter: ManagerPropertyAmenitiesAdapter? = null
    private var flat_list: ManagerPropertyListRes.Data? = null
    private var toLet_flat_list: ManagerToletFlatListRes.Data.Result? = null
    private var key: String = ""
    private lateinit var mMap: GoogleMap
    private lateinit var imagesArray: ArrayList<String>
    private lateinit var handler: Handler
    private var currentPage = 0
    private lateinit var slidingImageDots: Array<ImageView?>
    private var slidingDotsCount = 0
    private var getLati: Double = 0.0
    private var getLongi: Double = 0.0
    private var buildingName: String = ""
    private val slidingCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            for (i in 0 until slidingDotsCount) {
                slidingImageDots[i]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.default_dot
                    )
                )
            }

            slidingImageDots[position]?.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.selected_dot
                )
            )
        }
    }

    private fun setPropertyAdapter(list: ArrayList<String> = ArrayList()) {
        binding.rvAmenities.layoutManager = GridLayoutManager(this, 4)
        mAdapter = ManagerPropertyAmenitiesAdapter(this, list)
        binding.rvAmenities.adapter = mAdapter

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerPropertyToLetFlatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        key = intent.getStringExtra("from").toString()
        println("-----key$key")
        handler = Handler(Looper.myLooper()!!)
        binding.imageView56.setOnClickListener {
            finish()
        }
        if (key.equals("flat_list")) {
            flat_list = intent.getSerializableExtra("flatList") as ManagerPropertyListRes.Data
            getLati = flat_list!!.buildingInfo.get(0).latitude
            getLongi = flat_list!!.buildingInfo.get(0).longitude
            buildingName = flat_list!!.buildingInfo.get(0).buildingName
            println("=======$flat_list")
            fetchData()
        } else if (key.equals("to_let_flatList")) {
            toLet_flat_list =
                intent.getSerializableExtra("to_let") as ManagerToletFlatListRes.Data.Result
            getLati = toLet_flat_list!!.buildingInfo.get(0).latitude
            getLongi = toLet_flat_list!!.buildingInfo.get(0).longitude
            buildingName = toLet_flat_list!!.buildingInfo.get(0).buildingName
            fetchData()
        }
        binding.layoutMapview.setOnClickListener {
            val geoUri =
                "http://maps.google.com/maps?q=loc:${getLati},${
                    getLongi
                }(${buildingName})"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
            startActivity(intent)
        }
        setPropertyAdapter()
        setUpSlidingViewPager()
    }

    private fun fetchData() {
        val map = SupportMapFragment()
        supportFragmentManager.beginTransaction().replace(binding.mapview.id, map)
            .commitAllowingStateLoss()
        map.getMapAsync(this)
        if (key == "flat_list") {
            binding.tvPropertyName.text = flat_list!!.name
            binding.tvLocation.text = flat_list!!.buildingInfo.get(0).address
            binding.tvDetailsDesc.text = flat_list!!.buildingInfo.get(0).description
            binding.tvPropertyPrice.text = "৳${flat_list!!.buildingInfo.get(0).amount}"
            binding.tvFit.text = "${flat_list!!.sqft} ft"
            binding.tvBedroom.text = "${flat_list!!.bedroom} bedroom"
            binding.tvBathroom.text = "${flat_list!!.bathroom} bathroom"

        } else if (key == "to_let_flatList") {
            binding.tvPropertyName.text = toLet_flat_list!!.name
            binding.tvLocation.text = toLet_flat_list!!.buildingInfo.get(0).address
            binding.tvDetailsDesc.text = toLet_flat_list!!.buildingInfo.get(0).description
            binding.tvPropertyPrice.text = "৳${toLet_flat_list!!.buildingInfo.get(0).amount}"
            binding.tvFit.text = "${toLet_flat_list!!.sqft} ft"
            binding.tvBedroom.text = "${toLet_flat_list!!.bedroom} bedroom"
            binding.tvBathroom.text = "${toLet_flat_list!!.bathroom} bathroom"
        }


    }

    private fun setUpSlidingViewPager() {
        imagesArray = arrayListOf()
        imagesArray.clear()
        if (key == "flat_list") {
            imagesArray.addAll(flat_list!!.buildingInfo.get(0).photos)
        } else if (key == "to_let_flatList") {
            imagesArray.addAll(toLet_flat_list!!.buildingInfo.get(0).photos)
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
            slidingImageDots[i] = ImageView(applicationContext)
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