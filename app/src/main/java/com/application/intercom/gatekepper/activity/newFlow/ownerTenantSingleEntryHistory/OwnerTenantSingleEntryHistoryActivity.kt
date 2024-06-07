package com.application.intercom.gatekepper.activity.newFlow.ownerTenantSingleEntryHistory

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.prefs
import com.application.intercom.databinding.ActivityOwnerTenantSingleEntryHistoryBinding
import com.application.intercom.gatekepper.Fragment.newFragment.ownerTenantSingleEntryHistory.CancelledOwnerTenantSingleEntryHIstoryFragment
import com.application.intercom.gatekepper.Fragment.newFragment.ownerTenantSingleEntryHistory.CompletedOwnerTenantSingleEntryHistoryFragment
import com.application.intercom.gatekepper.Fragment.newFragment.ownerTenantSingleEntryHistory.OngoingOwnerTenantSingleEntryHistoryFragment
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.utils.SessionConstants
import com.google.android.material.tabs.TabLayoutMediator

class OwnerTenantSingleEntryHistoryActivity :
    BaseActivity<ActivityOwnerTenantSingleEntryHistoryBinding>() {

    override fun getLayout(): ActivityOwnerTenantSingleEntryHistoryBinding {
        return ActivityOwnerTenantSingleEntryHistoryBinding.inflate(layoutInflater)
    }

    private var from: String = ""
    private var noty: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerTenantSingleEntryHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        noty = intent.getStringExtra("noty").toString()
        println("=======$from")
        initView()
        listener()
    }

    private fun initView() {
        if (from.equals("tenant")) {
            binding.toolbar.tvTittle.text = getString(R.string.single_entry_history)
        } else {
            binding.toolbar.tvTittle.text = getString(R.string.single_entry_history)
        }
        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)
        if (noty == "noty_list_checkOut") {
            binding.viewPager.setCurrentItem(1, true)
        } else if (prefs.getString(SessionConstants.NOTYTYPE, "") == "CHECKED_OUT") {
            binding.viewPager.setCurrentItem(1, true)
        } else if (from == "kill_state") {
            binding.viewPager.setCurrentItem(1, true)
        } else {
            binding.viewPager.setCurrentItem(0, true)
        }
        TabLayoutMediator(binding.tabLay, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.ongoing)
                }
                1 -> {
                    tab.text = getString(R.string.completed)
                }
                2 -> {
                    tab.text = getString(R.string.cancelled)
                }

            }
        }.attach()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return OngoingOwnerTenantSingleEntryHistoryFragment(
                        if (from == "kill_state") prefs.getString(
                            SessionConstants.ROLE,
                            ""
                        ) else from
                    )
                }
                1 -> {
                    return CompletedOwnerTenantSingleEntryHistoryFragment(
                        if (from == "kill_state") prefs.getString(
                            SessionConstants.ROLE,
                            ""
                        ) else from
                    )
                }
                2 -> {
                    return CancelledOwnerTenantSingleEntryHIstoryFragment(
                        if (from == "kill_state") prefs.getString(
                            SessionConstants.ROLE,
                            ""
                        ) else from
                    )
                }
            }
            return OngoingOwnerTenantSingleEntryHistoryFragment(
                if (from == "kill_state") prefs.getString(
                    SessionConstants.ROLE,
                    ""
                ) else from
            )
        }
    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
//            finish()
            //deeplinking
            if (prefs.getString(SessionConstants.NOTYTYPE, "") == "CHECKED_IN") {
                prefs.put(SessionConstants.NOTYTYPE, "")
                if (prefs.getString(SessionConstants.ROLE, "") == "tenant") {
                    startActivity(
                        Intent(
                            this,
                            TenantMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                } else {
                    prefs.put(SessionConstants.NOTYTYPE, "")
                    startActivity(
                        Intent(
                            this,
                            OwnerMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                }
                //deeplinking
            } else if (prefs.getString(SessionConstants.NOTYTYPE, "") == "CHECKED_OUT") {
                prefs.put(SessionConstants.NOTYTYPE, "")
                if (from == "tenant") {
                    startActivity(
                        Intent(
                            this,
                            TenantMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                } else {
                    prefs.put(SessionConstants.NOTYTYPE, "")
                    startActivity(
                        Intent(
                            this,
                            OwnerMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                }
            } else if (from == "kill_state") {
                if (prefs.getString(SessionConstants.ROLE, "") == "tenant") {
                    startActivity(
                        Intent(
                            this,
                            TenantMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                } else {
                    startActivity(
                        Intent(
                            this,
                            OwnerMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                }
            } else if (noty == "noty_list_checkIn") {
                if (prefs.getString(SessionConstants.ROLE, "") == "tenant") {
                    noty = ""
                    startActivity(
                        Intent(
                            this,
                            TenantMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                } else {
                    noty = ""
                    startActivity(
                        Intent(
                            this,
                            OwnerMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                }
            } else if (noty == "noty_list_checkOut") {
                if (prefs.getString(SessionConstants.ROLE, "") == "tenant") {
                    noty = ""
                    startActivity(
                        Intent(
                            this,
                            TenantMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                } else {
                    noty = ""
                    startActivity(
                        Intent(
                            this,
                            OwnerMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                }
            } else {
                finish()
            }

        }
    }
}