package com.application.intercom.manager.newFlow.expenses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.databinding.ActivityManagerExpensesBinding
import com.application.intercom.manager.bills.ManagerApprovalBillsFragment
import com.application.intercom.manager.bills.ManagerPaidBillsFragment
import com.application.intercom.manager.bills.ManagerPendingBillsFragment
import com.application.intercom.manager.newFlow.expenses.fragment.PaidExpensesManagerFragment
import com.application.intercom.manager.newFlow.expenses.fragment.UnpaidExpensesManagerFragment
import com.google.android.material.tabs.TabLayoutMediator

class ManagerExpensesActivity : AppCompatActivity() {
    lateinit var binding: ActivityManagerExpensesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerExpensesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        lstnr()
    }

    private fun initView() {
        binding.toolExpense.tvTittle.text = getString(R.string.expenses)
        binding.toolExpense.tvFilter.visibility = View.VISIBLE
        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.unpaid)
                }
                1 -> {
                    tab.text = getString(R.string.paid)
                }

            }
        }.attach()
    }

    private fun lstnr() {
        binding.toolExpense.ivBack.setOnClickListener {
            finish()
        }
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return UnpaidExpensesManagerFragment()
                }
                1 -> {
                    return PaidExpensesManagerFragment()
                }
            }
            return UnpaidExpensesManagerFragment()
        }
    }
}