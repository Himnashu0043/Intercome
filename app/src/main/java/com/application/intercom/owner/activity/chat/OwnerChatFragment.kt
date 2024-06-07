package com.application.intercom.owner.activity.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.databinding.FragmentOwnerChatBinding
import com.application.intercom.owner.fragment.chat.OwnerParkingChatFragment
import com.application.intercom.owner.fragment.chat.OwnerPropertyChatFragment
import com.google.android.material.tabs.TabLayoutMediator

class OwnerChatFragment : Fragment() {
    lateinit var binding: FragmentOwnerChatBinding
    private var key: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOwnerChatBinding.inflate(layoutInflater)
        key = arguments?.getString("key").toString()
        println("------keyFr$key")
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        binding.toolbar.ivBack.visibility = View.INVISIBLE
        binding.toolbar.tvTittle.text = getString(R.string.activity)
        binding.viewPager.adapter = ScreenSlidePagerAdapter(requireActivity())
        TabLayoutMediator(binding.tabLay, binding.viewPager) { tab, position ->
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
                    return OwnerPropertyChatFragment(key)
                }
                1 -> {
                    return OwnerParkingChatFragment(key)
                }
            }
            return OwnerPropertyChatFragment(key)
        }
    }

    private fun lstnr() {

    }


}