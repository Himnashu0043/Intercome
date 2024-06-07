package com.application.intercom.user.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.databinding.ActivityHomeBinding
import com.application.intercom.user.parking.ParkingFragment
import com.application.intercom.user.property.PropertiesFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Properties"
//                    tab.icon = ContextCompat.getDrawable(this,R.drawable.ic_call)
                }
                1 -> {
                    tab.text = "Parking"
//                    tab.icon = ContextCompat.getDrawable(this,R.drawable.ic_call)
                }

            }
        }.attach()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return PropertiesFragment()
                }
                1 -> {
                    return ParkingFragment()
                }
            }
            return PropertiesFragment()
        }
    }


}