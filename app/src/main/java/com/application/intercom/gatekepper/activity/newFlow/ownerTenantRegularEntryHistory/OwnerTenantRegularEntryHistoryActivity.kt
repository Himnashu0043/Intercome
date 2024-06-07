package com.application.intercom.gatekepper.activity.newFlow.ownerTenantRegularEntryHistory

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.prefs
import com.application.intercom.databinding.ActivityOwnerTenantRegularEntryHistoryBinding
import com.application.intercom.gatekepper.Fragment.newFragment.ownerTenantRegularEntryHistroy.CompletedOwnerTenantRegularEntryHistoryFragment
import com.application.intercom.gatekepper.Fragment.newFragment.ownerTenantRegularEntryHistroy.OngingOwnerTenantRegularEntryHistoryFragment
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.utils.SessionConstants
import com.google.android.material.tabs.TabLayoutMediator

class OwnerTenantRegularEntryHistoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityOwnerTenantRegularEntryHistoryBinding
    private var from: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerTenantRegularEntryHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        initView()
        listener()
    }

    private fun initView() {
        println("======test${prefs.getString(SessionConstants.NOTYTYPE, "")}")
        if (from.equals("tenant")) {
            binding.toolbar.tvTittle.text = getString(R.string.regular_entry_history)
        } else {
            binding.toolbar.tvTittle.text = getString(R.string.regular_entry_history)
//            binding.toolbar.tvText.visibility = View.VISIBLE
//            binding.toolbar.tvText.text ="Add"
        }

        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)
        TabLayoutMediator(binding.tabLay, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.ongoing)
                }
                1 -> {
                    tab.text = getString(R.string.completed)
                }

            }
        }.attach()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return OngingOwnerTenantRegularEntryHistoryFragment(from)
                }
                1 -> {
                    return CompletedOwnerTenantRegularEntryHistoryFragment(from)
                }
            }
            return OngingOwnerTenantRegularEntryHistoryFragment(from)
        }
    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            if (prefs.getString(SessionConstants.NOTYTYPE, "") == "REGULAR_CHECKED_IN") {
                if (prefs.getString(SessionConstants.ROLE, "") == "tenant") {
                    prefs.put(SessionConstants.NOTYTYPE, "")
                    startActivity(
                        Intent(
                            this,
                            TenantMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                } else {
                    prefs.put(SessionConstants.NOTYTYPE, "")
                    startActivity(
                        Intent(
                            this,
                            OwnerMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                }
            } else if (prefs.getString(SessionConstants.NOTYTYPE, "") == "REGULAR_CHECKED_OUT") {
                if (prefs.getString(SessionConstants.ROLE, "") == "tenant") {
                    prefs.put(SessionConstants.NOTYTYPE, "")
                    startActivity(
                        Intent(
                            this,
                            TenantMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                } else {
                    prefs.put(SessionConstants.NOTYTYPE, "")
                    startActivity(
                        Intent(
                            this,
                            OwnerMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                }
            } else {
                finish()
            }
        }
    }
}