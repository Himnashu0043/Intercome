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
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.remote.manager.managerSide.bill.MangerBillPendingListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.FragmentManagerApprovalBillsBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.getMonthOfDate
import com.application.intercom.tenant.activity.profile.ProfileActivity
import com.application.intercom.utils.*

class ManagerApprovalBillsFragment() : BaseFragment<FragmentManagerApprovalBillsBinding>(),
    ManagerApprovalBillsAdapter.MarkClick {
    private var mAdapter: ManagerApprovalBillsAdapter? = null
    private lateinit var viewModel: ManagerSideViewModel
    private var approvalList = ArrayList<MangerBillPendingListRes.Data.Result>()
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
        /*binding.btnLogin.tv.setOnClickListener {
            startActivity(
                Intent(requireContext(), AddBillsActivity::class.java)*//*.putExtra(
                    "from",
                    "Resolve"
                )*//*
            )
        }*/
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentManagerApprovalBillsBinding {
        return FragmentManagerApprovalBillsBinding.inflate(inflater, container, false)

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

    private fun billApprovalList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.billpending(
            token, "Unapproved", if (flatOfBuildingId == "All") null else flatOfBuildingId
        )
    }

    override fun observer() {
        viewModel.billApproveLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            println("---managerfilterKey$filterKey")
                            println("---managermonths_filter$months_filter")
                            if (filterKey.isNotEmpty()) {
                                approvalList.clear()
                                if (months_filter.isNotEmpty()) {
                                    it.data.result?.forEach {
                                        if (it.billType == filterKey) {
                                            val months = getMonthOfDate(it.date?:"")
                                            if (months == months_filter) {
                                                approvalList.add(it)
                                            }
                                        }
                                    }
                                } else {
                                    it.data.result?.forEach {
                                        if (it.billType == filterKey) {
                                            approvalList.add(it)
                                        }
                                    }
                                }
                                setAdapter(approvalList)
                            } else if (months_filter.isNotEmpty()) {
                                approvalList.clear()
                                it.data.result?.forEach {
                                    val months = getMonthOfDate(it.date?:"")
                                    if (months == months_filter) {
                                        approvalList.add(it)
                                    }
                                }
                                setAdapter(approvalList)
                            } else {
                                approvalList.clear()
                                approvalList.addAll(it.data.result!!)
                                setAdapter(approvalList)
                            }

                            if (approvalList.isEmpty()) {
                                binding.rvManagerApproval.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rvManagerApproval.visibility = View.VISIBLE
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                        } else if (it.status == AppConstants.STATUS_500) {
                            //requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_404) {
                            //requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                          //  requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rvManagerApproval.visibility = View.INVISIBLE
                        } else {
                            binding.lottieEmpty.visibility = View.VISIBLE
                           // requireContext().longToast(it.message)
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
        viewModel.markAsPaidManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            billApprovalList()
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireContext().longToast(it.message)
                        } else {
                            requireContext().longToast(it.message)
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

        viewModelOne.managerFlatBuildingIDLiveData.observe(viewLifecycleOwner) {
            flatOfBuildingId = it
            filterKey = ""
            months_filter = ""
            billApprovalList()


        }
        viewModelOne.managerFilterKeyLiveData.observe(viewLifecycleOwner) {
            filterKey = it.filter
            months_filter = it.months
            billApprovalList()
        }
    }

    private fun setAdapter(list: ArrayList<MangerBillPendingListRes.Data.Result> = ArrayList()) {
        binding.rvManagerApproval.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = ManagerApprovalBillsAdapter(requireContext(), list, this)
        binding.rvManagerApproval.adapter = mAdapter

    }

    override fun onMaskClick(position: Int, id: String?) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.markAsPaidManager(token, id?:"")
    }

    override fun onViewReceipt(refno: String, uploadDocument: String) {

    }

    override fun onReject(billID: String) {

    }

    override fun onResume() {
        super.onResume()
        if (!flatOfBuildingId.isNullOrEmpty()) {
            billApprovalList()
        }

    }

}
