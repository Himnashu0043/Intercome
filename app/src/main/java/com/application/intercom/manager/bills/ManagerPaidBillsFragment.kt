package com.application.intercom.manager.bills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.BaseFragment
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.remote.manager.managerSide.bill.MangerBillPendingListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.FragmentManagerPaidBillsBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.getMonthOfDate
import com.application.intercom.utils.*


class ManagerPaidBillsFragment() :
    BaseFragment<FragmentManagerPaidBillsBinding>(),ManagerPaidBillsAdapter.ViewReceipt {
    private var mAdapter: ManagerPaidBillsAdapter? = null
    private lateinit var viewModel: ManagerSideViewModel
    private var paidList = ArrayList<MangerBillPendingListRes.Data.Result>()
    private var filterKey: String = ""
    private var months_filter: String = ""
    private var flatOfBuildingId: String = ""
    private val viewModelOne: ManagerSideViewModel by lazy {
        val repo1 = ManagerSideRepo(BaseApplication.apiService)
        ViewModelProvider(requireActivity(), ManagerSideFactory(repo1))[ManagerSideViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun lstnr() {

    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentManagerPaidBillsBinding {
        return FragmentManagerPaidBillsBinding.inflate(inflater, container, false)

    }

    override fun init() {
        initialize()

        // setAdapter()
    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

    private fun paidManagerList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.billpending(
            token,
            "Paid",
            if (flatOfBuildingId == "All") null else flatOfBuildingId
        )
    }

    override fun observer() {
        viewModel.billPaidLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            if (filterKey.isNotEmpty()) {
                                paidList.clear()
                                if (months_filter.isNotEmpty()) {
                                    it.data.result?.forEach {
                                        if (it.billType == filterKey) {
                                            val months = getMonthOfDate(it.date?:"")
                                            if (months == months_filter) {
                                                paidList.add(it)
                                            }
                                        }
                                    }
                                } else {
                                    it.data.result?.forEach {
                                        if (it.billType == filterKey) {
                                            paidList.add(it)
                                        }
                                    }
                                }
                                setAdapter(paidList)
                            } else if (months_filter.isNotEmpty()) {
                                paidList.clear()
                                it.data.result?.forEach {
                                    val months = getMonthOfDate(it.date?:"")
                                    if (months == months_filter) {
                                        paidList.add(it)
                                    }
                                }
                                setAdapter(paidList)
                            } else {
                                paidList.clear()
                                paidList.addAll(it.data.result!!)
                                setAdapter(paidList)
                            }

                            if (paidList.isEmpty()) {
                                binding.rvManagerPaid.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rvManagerPaid.visibility = View.VISIBLE
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                        } else if (it.status == AppConstants.STATUS_500) {
                            // requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_404) {
                            //requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else {
                            binding.lottieEmpty.visibility = View.VISIBLE
                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    binding.rvManagerPaid.visibility = View.VISIBLE
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })
        viewModelOne.managerFlatBuildingIDLiveData.observe(viewLifecycleOwner) {
            flatOfBuildingId = it
            filterKey = ""
            months_filter = ""
            paidManagerList()


        }
        viewModelOne.managerFilterKeyLiveData.observe(viewLifecycleOwner) {
            filterKey = it.filter
            months_filter = it.months
            paidManagerList()
        }
    }

    private fun setAdapter(list: ArrayList<MangerBillPendingListRes.Data.Result> = ArrayList()) {
        binding.rvManagerPaid.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = ManagerPaidBillsAdapter(requireContext(), list,this)
        binding.rvManagerPaid.adapter = mAdapter

    }

    override fun onResume() {
        super.onResume()
        if (!flatOfBuildingId.isNullOrEmpty()) {
            paidManagerList()
        }

    }

    override fun onViewReceipt(refno: String, uploadDocument: String) {

    }

}
