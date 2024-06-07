package com.application.intercom.tenant.activity.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.tenantViewModel.TenantSideViewModel
import com.application.intercom.data.model.factory.tenantFactory.TenantSideFactory
import com.application.intercom.data.model.local.tenant.TenantPayNowPostModel
import com.application.intercom.data.model.remote.manager.managerSide.bill.MangerBillPendingListRes
import com.application.intercom.data.model.remote.owner.bill.OwnerUnPaidBillListRes
import com.application.intercom.data.model.remote.tenant.tenantSide.tenantBill.TenantUnPaidListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.tenantRepo.TenantSideRepo
import com.application.intercom.databinding.ActivityTenantPaymentBinding
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.owner.activity.payment_invoice.VerifyingPaymentActivity
import com.application.intercom.utils.*


class TenantPaymentActivity : BaseActivity<ActivityTenantPaymentBinding>() {

    private var billingId: String = ""
    private var bankKey: String = ""
    private var from: String = ""
    private var owner_unpaidList: OwnerUnPaidBillListRes.Data.Result? = null
    private var tenant_paidList: TenantUnPaidListRes.Data.Result? = null
    private var manager_pendingList: MangerBillPendingListRes.Data.Result? = null
    private lateinit var viewModel: TenantSideViewModel
    override fun getLayout(): ActivityTenantPaymentBinding {
        return ActivityTenantPaymentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from").toString()
        if (from.equals("owner")) {
            billingId = intent.getStringExtra("billingId").toString()
            println("----billl$billingId")
            owner_unpaidList =
                intent.getSerializableExtra("ownerBillList") as OwnerUnPaidBillListRes.Data.Result
            println("----owner_unpaidList$owner_unpaidList")
        } else if (from == "manager") {
            billingId = intent.getStringExtra("billingId").toString()
            println("----billl$billingId")
            manager_pendingList =
                intent.getSerializableExtra("managerBillList") as MangerBillPendingListRes.Data.Result?
            println("----manager_pendingList$manager_pendingList")
            binding.cardView10.visibility = View.GONE
        } else {
            billingId = intent.getStringExtra("billingId").toString()
            println("----billl$billingId")
            tenant_paidList =
                intent.getSerializableExtra("tenantBillList") as TenantUnPaidListRes.Data.Result
            println("----tenant_paidList$tenant_paidList")
        }

        initView()
        lstnr()
    }

    private fun initView() {
        initialize()
        observer()
        binding.paymentToolbar.tvTittle.text = getString(R.string.payment)

    }

    private fun initialize() {
        val repo = TenantSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, TenantSideFactory(repo))[TenantSideViewModel::class.java]

    }

    private fun payNow() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        if (from == "manager") {
            val model = TenantPayNowPostModel(
                billingId, "CASH_PAYMENT", "", bankKey
            )
            viewModel.payNowManagerList(token, model)
        } else if (from == "owner") {
            val model = TenantPayNowPostModel(
                billingId, "CASH_PAYMENT", "", bankKey
            )
            viewModel.payNowOwnerList(token, model)

        } else {
            val model = TenantPayNowPostModel(
                billingId, "CASH_PAYMENT", "", bankKey
            )
            viewModel.payNowTenantList(token, model)
        }

    }

    private fun observer() {
        viewModel.payNowOwnerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(this, VerifyingPaymentActivity::class.java).putExtra(
                                    "from", from
                                ).putExtra("account","CASH PAYMENT")
                            )
                            finish()
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else {
                            this.longToast(it.message)
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
        viewModel.payNowTenantLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(this, VerifyingPaymentActivity::class.java).putExtra(
                                    "from", from
                                ).putExtra("account","CASH PAYMENT")
                            )
                            finish()
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else {
                            this.longToast(it.message)
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
        viewModel.payNowManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            /*startActivity(
                                Intent(this, VerifyingPaymentActivity::class.java).putExtra(
                                    "from", from
                                ).putExtra("account", "CASH PAYMENT")
                            )
                            finish()*/
                            startActivity(
                                Intent(this, ManagerMainActivity::class.java).putExtra("from", from)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else {
                            this.longToast(it.message)
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

    private fun lstnr() {
        binding.paymentToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.cardView10.setOnClickListener {
            bankKey = "MFS Payment"
            if (from =="owner") {
                startActivity(
                    Intent(
                        this,
                        TenantSecondPaymentActivity::class.java
                    ).putExtra("from", from).putExtra("bankKey", bankKey)
                        .putExtra("billingId", billingId)
                        .putExtra("ownerBillList", owner_unpaidList)
                )
            } else if (from == "manager") {
                startActivity(
                    Intent(
                        this,
                        TenantSecondPaymentActivity::class.java
                    ).putExtra("from", from).putExtra("bankKey", bankKey)
                        .putExtra("billingId", billingId)
                        .putExtra("managerBillList", manager_pendingList)
                )
            } else {
                startActivity(
                    Intent(
                        this,
                        TenantSecondPaymentActivity::class.java
                    ).putExtra("from", from).putExtra("bankKey", bankKey)
                        .putExtra("billingId", billingId)
                        .putExtra("tenantBillList", tenant_paidList)
                )
            }

        }
        binding.cardView11.setOnClickListener {
            bankKey = "Bank Payment"
            if (from.equals("owner")) {
                startActivity(
                    Intent(
                        this,
                        TenantSecondPaymentActivity::class.java
                    ).putExtra("from", from).putExtra("bankKey", bankKey)
                        .putExtra("billingId", billingId)
                        .putExtra("ownerBillList", owner_unpaidList)
                )
            } else if (from == "manager") {
                startActivity(
                    Intent(
                        this,
                        TenantSecondPaymentActivity::class.java
                    ).putExtra("from", from).putExtra("bankKey", bankKey)
                        .putExtra("billingId", billingId)
                        .putExtra("managerBillList", manager_pendingList)
                )
            } else {
                startActivity(
                    Intent(
                        this,
                        TenantSecondPaymentActivity::class.java
                    ).putExtra("from", from).putExtra("bankKey", bankKey)
                        .putExtra("billingId", billingId)
                        .putExtra("tenantBillList", tenant_paidList)
                )
            }

        }
        binding.card.setOnClickListener {
            bankKey = "Cash Payment"
            payNow()
            /*startActivity(
                Intent(
                    this,
                    TenantSecondPaymentActivity::class.java
                ).putExtra("from", from).putExtra("bankKey", bankKey)
                    .putExtra("ownerBillList", owner_unpaidList)
            )*/
        }

    }
}