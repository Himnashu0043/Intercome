package com.application.intercom.manager.bills

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.BaseFragment
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.remote.manager.managerSide.bill.MangerBillPendingListRes
import com.application.intercom.data.model.remote.manager.managerSide.complain.ManagerPendingListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.databinding.FragmentManagerPendingBillsBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.getMonthOfDate
import com.application.intercom.manager.home.ManagerUnpaidBillingsAdapter
import com.application.intercom.tenant.activity.payment.TenantPaymentActivity
import com.application.intercom.tenant.adapter.billing.HeaderAdapter
import com.application.intercom.utils.*


class ManagerPendingBillsFragment() :
    BaseFragment<FragmentManagerPendingBillsBinding>(),
    ManagerPendingBillsAdapter.ManagerUserNotify {
    private var mAdapter: ManagerPendingBillsAdapter? = null
    private lateinit var viewModel: ManagerSideViewModel
    private var billPendingList = ArrayList<MangerBillPendingListRes.Data.Result>()
    private var flatOfBuildingId: String = ""
    private var filterKey: String = ""
    private var months_filter: String = ""
    private val viewModelone: ManagerSideViewModel by lazy {
        val repo1 = ManagerSideRepo(BaseApplication.apiService)
        ViewModelProvider(requireActivity(), ManagerSideFactory(repo1))[ManagerSideViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun lstnr() {
        /* binding.btnLogin.tv.setOnClickListener {
             startActivity(
                 Intent(requireContext(), AddBillsActivity::class.java)*//*.putExtra(
                    "from",
                    "pending"
                )*//*
            )
        }*/
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentManagerPendingBillsBinding {
        return FragmentManagerPendingBillsBinding.inflate(inflater, container, false)

    }

    override fun init() {
        setAdapter()
        initialize()

        /* binding.btnLogin.tv.text = "Add Billing"
         binding.btnLogin.tv.backgroundTintList =
             ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
         binding.btnLogin.tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))*/
    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

    private fun billPendingList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.billpending(
            token,
            "Pending",
            if (flatOfBuildingId == "All") null else flatOfBuildingId
        )
    }

    /* private fun billTestPendingList() {
         val token = prefs.getString(
             SessionConstants.TOKEN, ""
         )
         viewModel.billTestpending(
             token,
             "Pending"
         )
     }*/

    override fun observer() {
        viewModel.billpendingLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
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
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })
        /* viewModel.billTestpendingLiveData.observe(this, androidx.lifecycle.Observer {
             when (it) {
                 is EmpResource.Loading -> {
                     EmpCustomLoader.showLoader(requireActivity())
                 }

                 is EmpResource.Success -> {
                     EmpCustomLoader.hideLoader()
                     it.value.let {
                         if (it.status == AppConstants.STATUS_SUCCESS) {
                             binding.rvManagerPending.visibility = View.VISIBLE
                             if (filter_key.isNotEmpty()) {
                                 billPendingList.clear()
                                 if (filter_key == "Rent") {
                                     it.data.result.forEach {
                                         if (it.billType == filter_key) {
                                             billPendingList.add(it)
                                         }
                                     }
                                     setAdapter(billPendingList)
                                 } else {
                                     billPendingList.clear()
                                     it.data.result.forEach {
                                         if (it.billType != filter_key) {
                                             billPendingList.add(it)
                                         }
                                     }
                                     setAdapter(billPendingList)
                                 }
                             } else if (months_filter.isNotEmpty()) {
                                 billPendingList.clear()
                                 it.data.result.forEach {
                                     val month = getMonthOfDate(it.date)
                                     if (month == months_filter) {
                                         billPendingList.add(it)
                                     } else {
                                         billPendingList.clear()
                                         binding.rvManagerPending.visibility = View.INVISIBLE
                                     }
                                 }
                                 setAdapter(billPendingList)
                             }
                             *//* billPendingList.clear()
                             billPendingList.addAll(it.data.result)
                             setAdapter(billPendingList)*//*
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireContext().longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })*/
        viewModel.managerNotifyUserLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            billPendingList()
                            requireContext().longToast(getString(R.string.notify_successfully))
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireContext().longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })
        viewModelone.managerFlatBuildingIDLiveData.observe(viewLifecycleOwner) {
            flatOfBuildingId = it
            filterKey = ""
            months_filter = ""
            billPendingList()


        }
        viewModelone.managerFilterKeyLiveData.observe(viewLifecycleOwner) {
            filterKey = it.filter
            months_filter = it.months
            billPendingList()
        }
    }

    private fun setAdapter(list: ArrayList<MangerBillPendingListRes.Data.Result> = ArrayList()) {
        binding.rvManagerPending.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = ManagerPendingBillsAdapter(requireContext(), list, this)
        binding.rvManagerPending.adapter = mAdapter

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
        viewModel.managerNotifyUser(token, billingId?:"")
    }

    override fun onClickPay(position: Int, billingId: String?) {
        println("===Idd$billingId")
        startActivity(
            Intent(
                requireContext(), TenantPaymentActivity::class.java
            ).putExtra("billingId", billingId?:"")
                .putExtra("from", "manager")
                .putExtra("managerBillList", billPendingList[position])
        )
    }

    override fun onDelete(position: Int, billId: String?) {

    }

}
