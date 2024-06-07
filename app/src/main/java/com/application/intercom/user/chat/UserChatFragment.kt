package com.application.intercom.user.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.databinding.FragmentUserChatBinding
import com.application.intercom.utils.CommonUtil
import com.google.android.material.tabs.TabLayoutMediator

class UserChatFragment : Fragment() {
    lateinit var binding: FragmentUserChatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserChatBinding.inflate(layoutInflater)
        initView()
        lstnr()
        CommonUtil.setLightStatusBar(requireActivity())

        return binding.root
    }

    private fun initView() {
        binding.userHomeToolbar.tvTittle.text = getString(R.string.activity)
        binding.userHomeToolbar.ivBack.visibility = View.INVISIBLE
        binding.viewPager.adapter = ScreenSlidePagerAdapter(requireActivity())

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.my_properties)
                }
                1 -> {
                    tab.text = getString(R.string.listed_properties)
                }

            }
        }.attach()

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

    private fun lstnr() {

    }

}