package com.application.intercom.manager.parking

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseFragment
import com.application.intercom.databinding.FragmentParentManagerParkingBinding
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.tenant.activity.profile.ProfileActivity
import com.google.android.material.tabs.TabLayoutMediator


class ParentManagerParkingFragment : BaseFragment<FragmentParentManagerParkingBinding>() {
    private var key: String = ""
    private lateinit var activity: ManagerMainActivity
    var drw: DrawerLayout? = null
    override fun lstnr() {
        binding.userToolbar.ivBack.setOnClickListener {
            if (key.equals("manager_parking")) {
               /* startActivity(
                    Intent(requireContext(), ProfileActivity::class.java).putExtra(
                        "from",
                        "manager"
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )*/
                startActivity(
                    Intent(requireContext(), ManagerMainActivity::class.java).putExtra(
                        "from",
                        "from_side_home"
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentParentManagerParkingBinding {
        key = arguments?.getString("key").toString()
        Log.d("Himanshu", "onCreateView: $key")
        activity = getActivity() as ManagerMainActivity
        drw = activity.requireViewById(R.id.managerDrw)
        return FragmentParentManagerParkingBinding.inflate(inflater, container, false)

    }

    override fun init() {
        binding.viewPager.adapter = ScreenSlidePagerAdapter(requireActivity())

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Parking"
                }
                1 -> {
                    tab.text = "To-Let Parking"
                }

            }
        }.attach()

    }

    override fun observer() {
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return ManagerParkingFragment()
                }
                1 -> {
                    return ManagerToLetParkingFragment()
                }
            }
            return ManagerParkingFragment()
        }
    }


}