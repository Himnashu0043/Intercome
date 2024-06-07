package com.application.intercom.manager.newFlow.finance.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.application.intercom.R
import com.application.intercom.databinding.FragmentReportsFinanceManagerBinding
import com.application.intercom.manager.newFlow.DueReportManagerActivity
import com.application.intercom.manager.newFlow.EarningReportManagerActivity
import com.application.intercom.manager.newFlow.ExpenseReportManagerActivity
import com.application.intercom.manager.newFlow.balancesheet.BalanceSheetManagerActivity

class ReportsFinanceManagerFragment : Fragment() {
    lateinit var bin: FragmentReportsFinanceManagerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bin = FragmentReportsFinanceManagerBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return bin.root
    }

    private fun initView() {

    }

    private fun lstnr() {
        bin.constraintLayout32.setOnClickListener {
            startActivity(Intent(requireContext(), EarningReportManagerActivity::class.java))
        }
        bin.constraintLayout33.setOnClickListener {
            startActivity(Intent(requireContext(), ExpenseReportManagerActivity::class.java))
        }
        bin.constraintLayout34.setOnClickListener {
            startActivity(Intent(requireContext(), BalanceSheetManagerActivity::class.java))
        }
        bin.constraintLayout35.setOnClickListener {
            startActivity(Intent(requireContext(), DueReportManagerActivity::class.java))
        }
    }

}