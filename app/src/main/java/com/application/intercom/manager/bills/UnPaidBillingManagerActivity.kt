package com.application.intercom.manager.bills

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.remote.manager.managerSide.bill.MangerBillPendingListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityUnPaidBillingManagerBinding
import com.application.intercom.databinding.ManagerFilterBillingBottomSheetBinding
import com.application.intercom.helper.getMonthOfDate
import com.application.intercom.tenant.activity.payment.TenantPaymentActivity
import com.application.intercom.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog

class UnPaidBillingManagerActivity : BaseActivity<ActivityUnPaidBillingManagerBinding>(),
    ManagerPendingBillsAdapter.ManagerUserNotify {
    private lateinit var manager_viewModel: ManagerSideViewModel
    private var flatOfBuildingList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingId: String = ""
    lateinit var filterBottom: ManagerFilterBillingBottomSheetBinding
    lateinit var bottomSheetDialog: BottomSheetDialog
    private var filterKey: String = ""
    private var months_filter: String = ""
    private var mAdapter: ManagerPendingBillsAdapter? = null
    private var billPendingList = ArrayList<MangerBillPendingListRes.Data.Result>()
    override fun getLayout(): ActivityUnPaidBillingManagerBinding {
        return ActivityUnPaidBillingManagerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()
    }

    private fun listener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnLogin.tv.setOnClickListener {
            startActivity(
                Intent(this, AddBillsActivity::class.java)
            )
        }
        binding.layoutFilter.setOnClickListener {
            filter()
        }
    }

    private fun initView() {
        binding.layoutFilter.visibility = View.VISIBLE
        flatOfBuildingList.add(0, "All")

        initialize()
        observer()
        flatOfBuilding()
        flatSpinner()
        binding.btnLogin.tv.text = getString(R.string.add_billings_1)
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(this, R.color.orange))
    }

    private fun initialize() {
        val repo1 = ManagerSideRepo(BaseApplication.apiService)
        manager_viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo1))[ManagerSideViewModel::class.java]
    }

    private fun flatOfBuilding() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        manager_viewModel.flatOfBuildingList(token)

    }

    private fun billPendingList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        manager_viewModel.billpending(
            token,
            "Pending",
            if (flatOfBuildingId == "All") null else flatOfBuildingId
        )
    }

    private fun observer() {
        manager_viewModel.flatOfBuildingListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            it.data.result.forEach {
                                flatOfBuildingList.add(it.name)
                                flatOfBuildingHashMapID.put(it.name, it._id)
                            }
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
        manager_viewModel.billpendingLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {

                            if (filterKey.isNotEmpty()) {
                                billPendingList.clear()
                                if (months_filter.isNotEmpty()) {
                                    it.data.result?.forEach {
                                        if (it.billType == filterKey) {
                                            val months = getMonthOfDate(it.date?:"")
                                            if (months == months_filter) {
                                                billPendingList.add(it)
                                            }
                                        }
                                    }
                                } else {
                                    it.data.result?.forEach {
                                        if (it.billType == filterKey) {
                                            billPendingList.add(it)
                                        }
                                    }
                                }
                                setAdapter(billPendingList)
                            } else if (months_filter.isNotEmpty()) {
                                billPendingList.clear()
                                it.data.result?.forEach {
                                    val months = getMonthOfDate(it.date?:"")
                                    if (months == months_filter) {
                                        billPendingList.add(it)
                                    }
                                }
                                setAdapter(billPendingList)
                            } else {
                                billPendingList.clear()
                                billPendingList.addAll(it.data.result!!)
                                setAdapter(billPendingList)
                            }

                            if (billPendingList.isEmpty()) {
                                binding.rvManagerPending.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rvManagerPending.visibility = View.VISIBLE
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }

                        } else if (it.status == AppConstants.STATUS_500) {
                            //requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_404) {
                            // requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            // requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rvManagerPending.visibility = View.INVISIBLE
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })
        manager_viewModel.managerNotifyUserLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            billPendingList()
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
        manager_viewModel.deleteUnPaidBillManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            billPendingList()
                            this.longToast("Delete Successfully!!")
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

    private fun setAdapter(list: ArrayList<MangerBillPendingListRes.Data.Result> = ArrayList()) {
        binding.rvManagerPending.layoutManager = LinearLayoutManager(this)
        mAdapter = ManagerPendingBillsAdapter(this, list, this)
        binding.rvManagerPending.adapter = mAdapter

    }

    private fun flatSpinner() {
        //val genderList = resources.getStringArray(R.array.EditProfile)
        binding.mainflatSpiner.adapter =
            ArrayAdapter(this, R.layout.spinner_dropdown_item, flatOfBuildingList)
        binding.mainflatSpiner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long,
                ) {

                    /*if (binding.mainflatSpiner.selectedItemPosition > 0) {
                        if (binding.mainflatSpiner.selectedItem.equals("All")) {
                            flatOfBuildingId = "All"
                            println("---All$flatOfBuildingId")
                           manager_viewModel.managerFlatBuildingIDLiveData.postValue(flatOfBuildingId)
                        } else {
                            flatOfBuildingId =
                                flatOfBuildingHashMapID.get(binding.mainflatSpiner.selectedItem.toString())
                                    .toString()
                            println("---flateId$flatOfBuildingId")
                            manager_viewModel.managerFlatBuildingIDLiveData.postValue(flatOfBuildingId)
                        }

                    }*/
                    if (binding.mainflatSpiner.selectedItemPosition > 0) {
                        flatOfBuildingId =
                            flatOfBuildingHashMapID.get(binding.mainflatSpiner.selectedItem.toString())
                                .toString()
                        println("---flateId$flatOfBuildingId")
                        filterKey = ""
                        months_filter = ""
                        billPendingList()
                    } else {
                        flatOfBuildingId = "All"
                        filterKey = ""
                        months_filter = ""
                        println("---All$flatOfBuildingId")
                        billPendingList()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    fun filter() {
        filterBottom = ManagerFilterBillingBottomSheetBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme).apply {
                setContentView(filterBottom.root)
                filterBottom.commonBtn.tv.text = getString(R.string.apply)
                filterBottom.checkBox3.setOnClickListener {
                    filterBottom.checkBox3.isChecked = true
                    filterBottom.checkBox31.isChecked = false
                    filterKey = "Rent"
                }
                filterBottom.checkBox31.setOnClickListener {
                    filterBottom.checkBox31.isChecked = true
                    filterBottom.checkBox3.isChecked = false
                    filterKey = "Service"
                }
                filterBottom.commonBtn.tv.setOnClickListener {
                    billPendingList()
                    bottomSheetDialog.dismiss()
                }
                filterBottom.tvClose.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

                val genderList = resources.getStringArray(R.array.Months)
                filterBottom.flatSpiner.adapter = ArrayAdapter(
                    this@UnPaidBillingManagerActivity,
                    R.layout.spinner_dropdown_item,
                    genderList
                )
                filterBottom.flatSpiner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long,
                        ) {


                            if (filterBottom.flatSpiner.selectedItemPosition > 0) {
                                months_filter =
                                    filterBottom.flatSpiner.selectedItem.toString().substring(0..2)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }


            }
        bottomSheetDialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (!flatOfBuildingId.isNullOrEmpty()) {
            billPendingList()
        }

    }

    override fun onClickNotify(position: Int, billingId: String?) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        manager_viewModel.managerNotifyUser(token, billingId?:"")
    }

    override fun onClickPay(position: Int, billingId: String?) {
        println("===Idd$billingId")
        startActivity(
            Intent(
                this, TenantPaymentActivity::class.java
            ).putExtra("billingId", billingId?:"")
                .putExtra("from", "manager")
                .putExtra("managerBillList", billPendingList[position])
        )
    }

    override fun onDelete(position: Int, billId: String?) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        manager_viewModel.deleteUnPaidBillManager(token, billId?:"")
    }
}