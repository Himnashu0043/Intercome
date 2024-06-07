package com.application.intercom.user.myactivity

import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseFragment
import com.application.intercom.databinding.FragmentParentManagerPropertyBinding
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.tenant.activity.profile.ProfileActivity
import com.google.android.material.tabs.TabLayoutMediator


class UserMyActivityFragment : BaseFragment<FragmentParentManagerPropertyBinding>() {
    private var key: String = ""
    private lateinit var activity: MainActivity
    private lateinit var owneractivity: OwnerMainActivity
    private lateinit var tenantactivity: TenantMainActivity
    var drw: DrawerLayout? = null
    override fun lstnr() {
        binding.userToolbar.ivBack.setOnClickListener {
            if (key.equals("tenant_myActivity")) {
                /*  startActivity(
                      Intent(requireContext(), ProfileActivity::class.java).putExtra(
                          "from",
                          "tenant"
                      ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                  )*/
                drw!!.closeDrawer(GravityCompat.START)
                startActivity(
                    Intent(requireContext(), TenantMainActivity::class.java).putExtra(
                        "from",
                        "from_side_home"
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            } else if (key.equals("ownerActivity")) {
                /* startActivity(
                     Intent(requireContext(), ProfileActivity::class.java).putExtra(
                         "from",
                         "owner"
                     ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                 )*/
                drw!!.closeDrawer(GravityCompat.START)
                startActivity(
                    Intent(requireContext(), OwnerMainActivity::class.java).putExtra(
                        "from",
                        "from_side_home"
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            } else if (key.equals("user_activity")) {
                /* startActivity(
                     Intent(requireContext(), ProfileActivity::class.java).putExtra(
                         "from",
                         "user"
                     ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                 )*/
                drw!!.closeDrawer(GravityCompat.START)
                startActivity(
                    Intent(requireContext(), MainActivity::class.java).putExtra(
                        "from",
                        "from_side_home"
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentParentManagerPropertyBinding {
        key = arguments?.getString("key").toString()
        Log.d("Himanshu", "onCreateView: $key")
        if (key.equals("user_activity")) {
            activity = getActivity() as MainActivity
            drw = activity.requireViewById(R.id.content)
        } else if (key.equals("ownerActivity")) {
            owneractivity = getActivity() as OwnerMainActivity
            drw = owneractivity.requireViewById(R.id.ownerDrw)
        } else {
            tenantactivity = getActivity() as TenantMainActivity
            drw = tenantactivity.requireViewById(R.id.tenantDrw)
        }

        return FragmentParentManagerPropertyBinding.inflate(inflater, container, false)

    }

    override fun init() {
        binding.userToolbar.tvTittle.text = getString(R.string.activity)
        binding.userToolbar.ivBack.visibility = View.VISIBLE
        binding.viewPager.adapter = ScreenSlidePagerAdapter(requireActivity())

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.properties)
                }
                1 -> {
                    tab.text = getString(R.string.parking)
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
                    return MyActivityPropertyFragment()
                }
                1 -> {
                    return MyActivityParkingFragment()
                }
            }
            return MyActivityPropertyFragment()
        }
    }


}