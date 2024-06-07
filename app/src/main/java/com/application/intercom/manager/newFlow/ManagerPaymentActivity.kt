package com.application.intercom.manager.newFlow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.newFlow.PayUnPaidManagerPostModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityManagerPaymentBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.manager.newFlow.expenses.ManagerExpensesActivity
import com.application.intercom.utils.*

class ManagerPaymentActivity : AppCompatActivity() {
    lateinit var binding: ActivityManagerPaymentBinding
    private var bankKey: String = ""
    private var expensesId: String = ""
    private var amount: Int = 0
    private lateinit var viewModel: ManagerSideViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerPaymentBinding.inflate(layoutInflater)
        expensesId = intent.getStringExtra("expenseId") ?: ""
        amount = intent.getIntExtra("amount", 0)
        setContentView(binding.root)
        initView()
        lstnr()
    }

    private fun initView() {
        initialize()
        observer()
        binding.paymentToolbar.tvTittle.text = getString(R.string.payment_option)


    }

    private fun lstnr() {
        binding.paymentToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.cardView10.setOnClickListener {
            bankKey = "MFS Payment"
            startActivity(
                Intent(
                    this,
                    DetailPaymentManagerActivity::class.java
                ).putExtra("expenseId", expensesId).putExtra("bankKey", bankKey)
                    .putExtra("amount", amount)
            )
        }
        binding.cardView11.setOnClickListener {
            bankKey = "Bank Payment"
            startActivity(
                Intent(
                    this,
                    DetailPaymentManagerActivity::class.java
                ).putExtra("expenseId", expensesId).putExtra("bankKey", bankKey)
                    .putExtra("amount", amount)
            )
        }
        binding.card.setOnClickListener {
            bankKey = "Cash Payment"
            payUnPaidManager()
        }

    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

    private fun payUnPaidManager() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        var model = PayUnPaidManagerPostModel(expensesId, bankKey, "", "")
        viewModel.payUnPaidManager(token, model)

    }

    private fun observer() {
        viewModel.payUnPaidManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(
                                    this,
                                    ManagerExpensesActivity::class.java
                                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                            finish()
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })

    }
}