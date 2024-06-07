package com.intercom.gatekepper.VisitorDetails

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.databinding.ActivityVisitorDetailsBinding
import com.application.intercom.gatekepper.Fragment.VisitorDetails.CancelledFragment
import com.application.intercom.gatekepper.Fragment.VisitorDetails.CompletedFragment
import com.application.intercom.gatekepper.Fragment.VisitorDetails.UpComingFragment
import com.application.intercom.gatekepper.Main.MainGateKepperActivity
import com.google.android.material.tabs.TabLayoutMediator


class VisitorDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityVisitorDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisitorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        lstnr()
    }

    private fun initView() {
        binding.visitorDetailsToolbar.tvTittle.text = "Visitor Details"
        binding.viewpagr.adapter = ScreenSlidePagerAdapter(this)
        TabLayoutMediator(binding.visitorTabLay, binding.viewpagr) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Ongoing"
                }
                1 -> {
                    tab.text = "Completed"

                }
                2 -> {
                    tab.text = "Cancelled"

                }

            }
        }.attach()

    }

    private fun lstnr() {
        binding.tvAddVisitor.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MainGateKepperActivity::class.java
                ).putExtra("from","from_gate_home"))

        }
        binding.visitorDetailsToolbar.ivBack.setOnClickListener {
            finish()
        }
    }
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return UpComingFragment()
                }
                1 -> {
                    return CompletedFragment()

                }
                2 -> {
                    return CancelledFragment()
                }
            }
            return UpComingFragment()
        }
    }
}