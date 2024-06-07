package com.application.intercom.manager.newFlow.balancesheet

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.DueReportManagerList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityBalanceFlatBinding
import com.application.intercom.utils.*

class BalanceFlatActivity : AppCompatActivity(), BalanceFlatAdapter.DuePayment {
    var list = ArrayList<DueReportManagerList.Data.Result.Newfieldname>()
    private var from: String? = null
    lateinit var binding: ActivityBalanceFlatBinding
    var amount: Int = 0
    var flatId: String? = ""
    private lateinit var manager_viewModel: ManagerSideViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBalanceFlatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from") ?: ""
        if (from == "listData") {
            list =
                intent.getSerializableExtra("list") as ArrayList<DueReportManagerList.Data.Result.Newfieldname>
            println("===========$list")
            flatId = list[0].flatInfo?.get(0)?._id
            for (amt in list) {
                amount += (amt.amount ?: 0)
            }
            if (!list[0].ownerInfo!!.isNullOrEmpty()) {
                binding.textView278.text = list[0].ownerInfo?.first()?.fullName ?: ""
            } else {
                binding.textView278.text = list[0].tenantInfo?.first()?.fullName ?: ""
            }

        }
        initView()
        listener()
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = list[0].flatInfo?.get(0)?.name
        binding.rcy.visibility = View.VISIBLE
        binding.rcy.layoutManager = LinearLayoutManager(this)
        val adptr = BalanceFlatAdapter(this, list, this)
        binding.rcy.adapter = adptr
        binding.tvPropertyPrice.text = "৳ $amount"
        initialize()
        observer()
    }

    private fun initialize() {
        val repo1 = ManagerSideRepo(BaseApplication.apiService)
        manager_viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo1))[ManagerSideViewModel::class.java]
    }

    private fun observer() {
        manager_viewModel.managerNotifyUserLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            // billPendingList()
                            this.longToast(getString(R.string.notify_successfully))
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
        manager_viewModel.managerNotifyAllLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            // billPendingList()
                            this.longToast(getString(R.string.notify_successfully))
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

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.laycontactowner.setOnClickListener {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            manager_viewModel.managerNotifyAll(token, flatId ?: "")
        }
    }

    override fun onDuePaymentNotify(position: Int, billingId: String?) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        manager_viewModel.managerNotifyUser(token, billingId ?: "")
    }


}