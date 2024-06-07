package com.application.intercom.owner.activity.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityOwnerChatBinding
import com.application.intercom.owner.fragment.chat.OwnerParkingChatFragment
import com.application.intercom.owner.fragment.chat.OwnerPropertyChatFragment
import com.application.intercom.user.chat.ParkingChatFragment
import com.application.intercom.user.chat.PropertiesChatFragment
import com.google.android.material.tabs.TabLayoutMediator

class OwnerChatActivity : BaseActivity<ActivityOwnerChatBinding>() {

    private var key: String = ""
    override fun getLayout(): ActivityOwnerChatBinding {
        return ActivityOwnerChatBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        key = intent.getStringExtra("key").toString()
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

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return OwnerPropertyChatFragment(key)
                }
                1 -> {
                    return OwnerParkingChatFragment(key)
                }
            }
            return OwnerPropertyChatFragment(key)
        }
    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}