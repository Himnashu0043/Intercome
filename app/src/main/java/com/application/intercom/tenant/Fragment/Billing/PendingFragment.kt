package com.application.intercom.tenant.Fragment.Billing

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.ViewModel.tenantViewModel.TenantSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.factory.tenantFactory.TenantSideFactory
import com.application.intercom.data.model.remote.owner.bill.OwnerUnPaidBillListRes
import com.application.intercom.data.model.remote.tenant.tenantSide.tenantBill.TenantUnPaidListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.data.repository.tenantRepo.TenantSideRepo
import com.application.intercom.databinding.FragmentPendingBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.getMonthOfDate
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.owner.adapter.bill.OwnerUnPaidAdapter
import com.application.intercom.tenant.activity.payment.TenantPaymentActivity
import com.application.intercom.tenant.adapter.billing.HeaderAdapter
import com.application.intercom.utils.*
import java.text.DecimalFormat
import java.text.NumberFormat


class PendingFragment(val type: String) : Fragment(), HeaderAdapter.PaidClick,
    OwnerUnPaidAdapter.PaidClick {
    lateinit var binding: FragmentPendingBinding
    private var adaptr: HeaderAdapter? = null
    private var owneradaptr: OwnerUnPaidAdapter? = null
    private var changeText = "pending"
    private lateinit var viewModel: TenantSideViewModel
    private lateinit var ownerviewModel: OwnerSideViewModel
    private var pendingList = ArrayList<TenantUnPaidListRes.Data.Result>()
    private var owner_pendingList = ArrayList<OwnerUnPaidBillListRes.Data.Result>()
    private var timer_value: String = ""
    private var filterKey: String = ""
    private var months_filter: String = ""
    private var flatOfBuildingId: String = ""
    private val viewModelOne: OwnerHomeViewModel by lazy {
        val ownerModel = OwnerHomeRepo(BaseApplication.apiService)
        ViewModelProvider(
            requireActivity(), OwnerHomeFactory(ownerModel)
        )[OwnerHomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPendingBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()


    }

    private fun initialize() {
        val repo = TenantSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, TenantSideFactory(repo))[TenantSideViewModel::class.java]
        val ownerrepo = OwnerSideRepo(BaseApplication.apiService)
        ownerviewModel =
            ViewModelProvider(this, OwnerSideFactory(ownerrepo))[OwnerSideViewModel::class.java]
    }

    private fun pendingList() {
        if (type.equals("owner")) {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            ownerviewModel.unPaidOwnerList(
                token,
                "Unapproved",
                if (flatOfBuildingId == "All") null else flatOfBuildingId
            )
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            viewModel.tenantUnpaidList(
                token,
                "Unapproved",
                if (flatOfBuildingId == "All") null else flatOfBuildingId
            )
        }

    }

    private fun observer() {
        viewModel.tenantPendingLiveData.observe(requireActivity(), androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            binding.constraintLayout4.visibility = View.GONE
                            if (filterKey.isNotEmpty()) {
                                pendingList.clear()
                                if (months_filter.isNotEmpty()) {
                                    it.data.result.forEach {
                                        if (it.billType == filterKey) {
                                            val months = getMonthOfDate(it.date)
                                            if (months == months_filter) {
                                                pendingList.add(it)
                                            }
                                        }
                                    }
                                } else {
                                    it.data.result.forEach {
                                        if (it.billType == filterKey) {
                                            pendingList.add(it)
                                        }
                                    }
                                }
                                binding.rcyPending.layoutManager =
                                    LinearLayoutManager(requireContext())
                                adaptr =
                                    HeaderAdapter(requireContext(), changeText, pendingList, this)
                                binding.rcyPending.adapter = adaptr
                                adaptr!!.notifyDataSetChanged()
                            } else if (months_filter.isNotEmpty()) {
                                pendingList.clear()
                                it.data.result.forEach {
                                    val months = getMonthOfDate(it.date)
                                    if (months == months_filter) {
                                        pendingList.add(it)
                                    }
                                }
                                binding.rcyPending.layoutManager =
                                    LinearLayoutManager(requireContext())
                                adaptr =
                                    HeaderAdapter(requireContext(), changeText, pendingList, this)
                                binding.rcyPending.adapter = adaptr
                                adaptr!!.notifyDataSetChanged()
                            } else {
                                pendingList.clear()
                                pendingList.addAll(it.data.result)
                                binding.rcyPending.layoutManager =
                                    LinearLayoutManager(requireContext())
                                adaptr =
                                    HeaderAdapter(requireContext(), changeText, pendingList, this)
                                binding.rcyPending.adapter = adaptr
                                adaptr!!.notifyDataSetChanged()
                            }
                            if (pendingList.isEmpty()) {
                                binding.rcyPending.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rcyPending.visibility = View.VISIBLE
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                        } else if (it.status == AppConstants.STATUS_500) {
                            //requireContext().longToast(it.message)
                            binding.rcyPending.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_404) {
                            //requireContext().longToast(it.message)
                            binding.rcyPending.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.constraintLayout4.visibility = View.GONE
                            binding.rcyPending.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                            // requireContext().longToast(it.message)
                        } else {
                            binding.rcyPending.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                            //requireContext().longToast(it.message)
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
        viewModel.notifyUserTenantLiveData.observe(requireActivity(), androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            binding.constraintLayout4.visibility = View.GONE
                            pendingList()
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.constraintLayout4.visibility = View.GONE
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
        ownerviewModel.unPaidOwnerListLiveData.observe(
            requireActivity(),
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(requireActivity())
                    }

                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                binding.constraintLayout4.visibility = View.GONE
                                binding.rcyPending.visibility = View.VISIBLE
                                if (filterKey.isNotEmpty()) {
                                    owner_pendingList.clear()
                                    if (months_filter.isNotEmpty()) {
                                        it.data.result.forEach {
                                            if (it.billType == filterKey) {
                                                val months = getMonthOfDate(it.date)
                                                if (months == months_filter) {
                                                    owner_pendingList.add(it)
                                                }
                                            }
                                        }
                                    } else {
                                        it.data.result.forEach {
                                            if (it.billType == filterKey) {
                                                owner_pendingList.add(it)
                                            }
                                        }
                                    }
                                    binding.rcyPending.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    owneradaptr = OwnerUnPaidAdapter(
                                        requireContext(),
                                        owner_pendingList,
                                        changeText,
                                        this,
                                        timer_value
                                    )
                                    binding.rcyPending.adapter = owneradaptr
                                    owneradaptr!!.notifyDataSetChanged()
                                } else if (months_filter.isNotEmpty()) {
                                    owner_pendingList.clear()
                                    it.data.result.forEach {
                                        val months = getMonthOfDate(it.date)
                                        if (months == months_filter) {
                                            owner_pendingList.add(it)
                                        } else {
                                            owner_pendingList.clear()
                                            binding.rcyPending.visibility = View.INVISIBLE
                                        }
                                    }
                                    binding.rcyPending.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    owneradaptr = OwnerUnPaidAdapter(
                                        requireContext(),
                                        owner_pendingList,
                                        changeText,
                                        this,
                                        timer_value
                                    )
                                    binding.rcyPending.adapter = owneradaptr
                                    owneradaptr!!.notifyDataSetChanged()

                                } else {
                                    owner_pendingList.clear()
                                    owner_pendingList.addAll(it.data.result)
                                    binding.rcyPending.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    owneradaptr = OwnerUnPaidAdapter(
                                        requireContext(),
                                        owner_pendingList,
                                        changeText,
                                        this,
                                        timer_value
                                    )
                                    binding.rcyPending.adapter = owneradaptr
                                    owneradaptr!!.notifyDataSetChanged()
                                }
                                if (owner_pendingList.isEmpty()) {
                                    binding.rcyPending.visibility = View.INVISIBLE
                                } else {
                                    binding.rcyPending.visibility = View.VISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_500) {
                                //requireContext().longToast(it.message)
                                binding.rcyPending.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else if (it.status == AppConstants.STATUS_404) {
                                //requireContext().longToast(it.message)
                                binding.rcyPending.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else if (it.status == AppConstants.STATUS_FAILURE) {
                                binding.rcyPending.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                //requireContext().longToast(it.message)
                                binding.rcyPending.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.constraintLayout4.visibility = View.GONE
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
        ownerviewModel.notifyUserOwnerListLiveData.observe(
            requireActivity(),

            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(requireActivity())
                    }

                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                binding.constraintLayout4.visibility = View.GONE
                                pendingList()
                            } else if (it.status == AppConstants.STATUS_500) {
                                requireContext().longToast(it.message)
                            } else if (it.status == AppConstants.STATUS_404) {
                                requireContext().longToast(it.message)
                            } else {
                                requireContext().longToast(it.message)
                                binding.constraintLayout4.visibility = View.GONE
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
        ownerviewModel.notifyOwnertoTenantLiveData.observe(
            requireActivity(),

            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(requireActivity())
                    }

                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                binding.constraintLayout4.visibility = View.GONE
                                pendingList()
                            } else if (it.status == AppConstants.STATUS_500) {
                                requireContext().longToast(it.message)
                            } else if (it.status == AppConstants.STATUS_404) {
                                requireContext().longToast(it.message)
                            } else {
                                requireContext().longToast(it.message)
                                binding.constraintLayout4.visibility = View.GONE
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
        ////owner
        viewModelOne.ownerFlatBuildingIDLiveData.observe(viewLifecycleOwner) {
            flatOfBuildingId = it
            filterKey = ""
            months_filter = ""
            pendingList()

        }
        viewModelOne.ownerFilterKeyLiveData.observe(viewLifecycleOwner) {
            filterKey = it.filter
            months_filter = it.months
            pendingList()
        }
        /* viewModelOne.ownerBottomMonthsFilterLiveData.observe(viewLifecycleOwner) {
             months_filter = it
             println("---months_filter$months_filter")
             pendingList()
         }*/
        ////owner
    }

    private fun lstnr() {
        binding.payInAdvance.setOnClickListener {
            //startActivity(Intent(requireContext(), TenantPaymentActivity::class.java))
        }

    }

    override fun onClick(id: String, position: Int) {
        startActivity(
            Intent(
                requireContext(),
                TenantPaymentActivity::class.java
            ).putExtra("billingId", id)
        )
    }

    override fun onNotifyTenant(id: String, position: Int) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.notifyUserTenantList(token, id)
    }

    override fun onViewReceiptTenant(refno: String, uploadDocument: String) {

    }

    override fun onOwnerClick(id: String, position: Int) {
        startActivity(
            Intent(
                requireContext(),
                TenantPaymentActivity::class.java
            ).putExtra("billingId", id)
        )
    }


    override fun onNotifyOwner(id: String, position: Int) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        ownerviewModel.notifyUserOwnerList(token, id)
    }

    override fun onViewReceipt(refno: String, uploadDocument: String) {

    }

    override fun onReject(billID: String) {

    }

    override fun onOwnertoTenantNotify(billID: String, position: Int) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        ownerviewModel.notifyOwnertoTenant(token, billID)
    }

    override fun onResume() {
        super.onResume()
        if (!flatOfBuildingId.isNullOrEmpty()) {
            pendingList()
        }

    }

}