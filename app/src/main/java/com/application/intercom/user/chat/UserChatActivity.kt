package com.application.intercom.user.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityUserChatBinding
import com.application.intercom.gatekepper.Fragment.newFragment.regularEntryHistory.CompletedRegularEntryHisFragment
import com.application.intercom.gatekepper.Fragment.newFragment.regularEntryHistory.OngingRegularEntryHisFragment
import com.google.android.material.tabs.TabLayoutMediator

class UserChatActivity : BaseActivity<ActivityUserChatBinding>() {

    override fun getLayout(): ActivityUserChatBinding {
        return ActivityUserChatBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = "Chats"
        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)
        TabLayoutMediator(binding.tabLay, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Properties"
                }
                1 -> {
                    tab.text = "Parking"
                }

            }
        }.attach()
    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return PropertiesChatFragment()
                }
                1 -> {
                    return ParkingChatFragment()
                }
            }
            return PropertiesChatFragment()
        }
    }
}