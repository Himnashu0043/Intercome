package com.application.intercom.owner.activity.showImgSlider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.application.intercom.R
import com.application.intercom.databinding.ActivityShowImgSliderBinding
import com.application.intercom.tenant.Model.ImageModels
import com.application.intercom.user.property.UserPropertyViewPagerAdapter
import com.application.intercom.utils.CommonUtil
import java.util.*
import kotlin.collections.ArrayList

class ShowImgSliderActivity : AppCompatActivity() {
    lateinit var binding: ActivityShowImgSliderBinding
    private var imgList = listOf<String>()
    private var key: String = ""
    var currentPos = 0
    val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowImgSliderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        key = intent.getStringExtra("key").toString()
        println("=====key$key")
        if (key == "community") {
            imgList = intent.getSerializableExtra("img_url") as ArrayList<String>
            println("====$imgList")
        }
        initView()
        lstnr()
    }

    private fun initView() {
        binding.viewPager1.adapter =
            ShowImgSliderAdapter(this, imgList as ArrayList<String>)
        binding.tabLayout1.setupWithViewPager(binding.viewPager1, true)
        val runnable = Runnable {
            if (currentPos == imgList.size - 1) currentPos = 0
            else currentPos++
            if (binding.viewPager1 != null) {
                binding.viewPager1.setCurrentItem(currentPos, true)
            }
        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(runnable)
            }
        }, CommonUtil.DELAY_MS, CommonUtil.PERIOD_MS)
    }

    private fun lstnr() {
        binding.imageView53.setOnClickListener {
            finish()
        }
    }
}