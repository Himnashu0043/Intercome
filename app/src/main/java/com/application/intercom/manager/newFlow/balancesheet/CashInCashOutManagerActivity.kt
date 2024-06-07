package com.application.intercom.manager.newFlow.balancesheet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.ExpensesReportsManagerList
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.IncomeReportManagerList
import com.application.intercom.databinding.ActivityCashInCashOutManagerBinding
import com.application.intercom.utils.CommonUtil
import kotlin.math.exp

class CashInCashOutManagerActivity : AppCompatActivity() {
    lateinit var binding: ActivityCashInCashOutManagerBinding
    private var expenseData: ExpensesReportsManagerList.Data? = null
    private var earningData: IncomeReportManagerList.Data? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCashInCashOutManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        CommonUtil.setLightStatusBar(this)
        initView()
        listener()
    }

    private fun initView() {
        expenseData =
            intent.getSerializableExtra("expensesData") as ExpensesReportsManagerList.Data?
        println("====${expenseData}")
        earningData = intent.getSerializableExtra("earningData") as IncomeReportManagerList.Data?
        println("====${earningData}")
        binding.toolbar.tvTittle.text = "Cash in Hand and Bank Report"
        ///fetch Data
        var monthlyEarningData = earningData
        var monthlyExpenseData = expenseData

        var cashInBank = 0
        var cashInHand = 0
        var earningBank = 0
        var earningHand = 0
        monthlyEarningData?.amountObj?.forEach {
            if (it.payType ?: "" == "Bank Payment") {
                earningBank = it.amount ?: 0
            } else {
                earningHand += it.amount ?: 0
            }
        }
        cashInBank = (monthlyEarningData?.cashInBank ?: 0) - earningBank
        cashInHand = (monthlyEarningData?.cashInHand ?: 0) - earningHand

        var expenseBank = 0
        var expenseHand = 0
        var expensecashInBank = 0
        var expenseCashInHand = 0
        monthlyExpenseData?.amountObj?.forEach {
            if (it.payType ?: "" == "Bank Payment") {
                expenseBank = it.expenseAmount ?: 0
            } else {
                expenseHand += it.expenseAmount ?: 0
            }
        }
        expensecashInBank = (monthlyEarningData?.cashInBank ?: 0) - expenseBank
        expenseCashInHand = (monthlyEarningData?.cashInHand ?: 0) - expenseHand
        binding.tvEarningBank.text = earningBank.toString()
        binding.tvEarningCash.text = earningHand.toString()
        binding.tvEarningTotal.text = (earningBank + earningHand).toString()

        binding.tvExpenseBank.text = expenseBank.toString()
        binding.tvExpenseCash.text = expenseHand.toString()
        binding.tvExpenseTotal.text = (expenseBank + expenseHand).toString()

        binding.tvOpBank.text = cashInBank.toString()
        binding.tvOpCash.text = cashInHand.toString()
        binding.tvOpTotal.text = (cashInBank + cashInHand).toString()

        binding.tvClBank.text = expensecashInBank.toString()
        binding.tvClCash.text = expenseCashInHand.toString()
        binding.tvClTotal.text = (expensecashInBank + expenseCashInHand).toString()


    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}