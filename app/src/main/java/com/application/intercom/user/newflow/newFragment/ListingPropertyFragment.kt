package com.application.intercom.user.newflow.newFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.prefs
import com.application.intercom.databinding.FilterBottomSheetBinding
import com.application.intercom.databinding.FragmentListingPropertyBinding
import com.application.intercom.databinding.OwnerFlatBottomSheetBinding
import com.application.intercom.owner.adapter.HomeFlatAdpter
import com.application.intercom.user.chat.ParkingChatFragment
import com.application.intercom.user.chat.PropertiesChatFragment
import com.application.intercom.utils.SessionConstants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator

class ListingPropertyFragment : Fragment() {
    lateinit var binding: FragmentListingPropertyBinding
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var filter_by_bottom: FilterBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListingPropertyBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = getString(R.string.listed_properties)
        binding.toolbar.tvFilter.visibility = View.INVISIBLE
        binding.toolbar.ivBack.visibility = View.INVISIBLE
        binding.viewPager.adapter = ScreenSlidePagerAdapter(requireActivity())
        if (prefs.getString(SessionConstants.NOTYTYPE, "") == "BUILDING_APPROVE") {
            prefs.put(SessionConstants.NOTYTYPE, "")
            binding.viewPager.setCurrentItem(0, true)
        } else if (prefs.getString(SessionConstants.NOTYTYPE, "") == "BUILDING_DENY") {
            prefs.put(SessionConstants.NOTYTYPE, "")
            binding.viewPager.setCurrentItem(3, true)
        } else {
            binding.viewPager.setCurrentItem(2, true)
        }

        TabLayoutMediator(binding.tabLay, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.active)
                }
                1 -> {
                    tab.text = getString(R.string.inactive)
                }
                2 -> {
                    tab.text = getString(R.string.pending)
                }
                3 -> {
                    tab.text = getString(R.string.reject)
                }

            }
        }.attach()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 4
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return ActiveFragment()
                }
                1 -> {
                    return InActiveFragment()
                }
                2 -> {
                    return PendingFragment()
                }
                3 -> {
                    return RejectFragment()
                }
            }
            return ActiveFragment()
        }
    }

    private fun lstnr() {
        binding.toolbar.tvFilter.setOnClickListener {
           // filter_BottomSheet()
        }
        /*binding.toolbar.ivBack.setOnClickListener {
            requireActivity().finish()
        }*/
    }
    private fun filter_BottomSheet() {
        filter_by_bottom = FilterBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme).apply {
                setContentView(filter_by_bottom.root)
                filter_by_bottom.radioButton.setOnClickListener {
                    filter_by_bottom.radioButton1.isChecked = false
                }
                filter_by_bottom.radioButton1.setOnClickListener {
                    filter_by_bottom.radioButton.isChecked = false
                }

                filter_by_bottom.tvClose.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

            }
        bottomSheetDialog.show()
    }

}