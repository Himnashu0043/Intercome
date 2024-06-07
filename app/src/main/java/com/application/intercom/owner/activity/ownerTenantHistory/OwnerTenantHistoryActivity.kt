package com.application.intercom.owner.activity.ownerTenantHistory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityOwnerTenantHistoryBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.intercom.owner.fragment.ownerHome.tenantHistory.OwnerCurrentTenantFragment
import com.intercom.owner.fragment.ownerHome.tenantHistory.OwnerPreviousTenantFragment

class OwnerTenantHistoryActivity : BaseActivity<ActivityOwnerTenantHistoryBinding>() {
    private var flatId: String = ""
    override fun getLayout(): ActivityOwnerTenantHistoryBinding {
        return  ActivityOwnerTenantHistoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flatId = intent.getStringExtra("flatId").toString()
        initView()
        lstnr()
    }

    private fun initView() {
        binding.tenantHistoryToolbar.tvTittle.text = getString(R.string.tenant_history)
        binding.viewpagr.adapter = ScreenSlidePagerAdapter(this)
        TabLayoutMediator(binding.tabLay, binding.viewpagr) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.current_tenant)
                }
                1 -> {
                    tab.text = getString(R.string.previous_tenant)

                }

            }
        }.attach()

    }

    private fun lstnr() {
        binding.tenantHistoryToolbar.ivBack.setOnClickListener {
            finish()
        }

    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return OwnerCurrentTenantFragment(flatId)
                }
                1 -> {
                    return OwnerPreviousTenantFragment(flatId)

                }
            }
            return OwnerCurrentTenantFragment(flatId)
        }
    }
}