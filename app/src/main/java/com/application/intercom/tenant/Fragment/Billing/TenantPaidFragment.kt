package com.application.intercom.tenant.Fragment.Billing

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.ViewModel.tenantViewModel.TenantSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.factory.tenantFactory.TenantSideFactory
import com.application.intercom.data.model.remote.tenant.tenantSide.tenantBill.TenantUnPaidListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.data.repository.tenantRepo.TenantSideRepo
import com.application.intercom.databinding.FragmentTenantPaidBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.getMonthOfDate
import com.application.intercom.manager.bills.ViewReceiptManagerActivity
import com.application.intercom.owner.adapter.bill.OwnerUnPaidAdapter
import com.application.intercom.tenant.activity.payment.TenantPayInAdavanceActivity
import com.application.intercom.tenant.activity.payment.TenantPaymentActivity
import com.application.intercom.tenant.adapter.billing.HeaderAdapter
import com.application.intercom.utils.*

class TenantPaidFragment : Fragment(), HeaderAdapter.PaidClick {
    lateinit var binding: FragmentTenantPaidBinding
    private var adaptr: HeaderAdapter? = null
    private var changeText = "paid"
    private lateinit var viewModel: TenantSideViewModel
    private var paidList = ArrayList<TenantUnPaidListRes.Data.Result>()
    private var count: Int = 0
    private var id: String = ""
    private var amount: Int = 0

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTenantPaidBinding.inflate(layoutInflater)
        intiView()
        listener()
        return binding.root
    }

    private fun intiView() {
        initialize()
        observer()
        // paidList()
    }

    private fun initialize() {
        val repo = TenantSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, TenantSideFactory(repo))[TenantSideViewModel::class.java]
        /*  val ownerrepo = OwnerSideRepo(BaseApplication.apiService)
          ownerviewModel =
              ViewModelProvider(this, OwnerSideFactory(ownerrepo))[OwnerSideViewModel::class.java]*/
    }

    private fun paidList() {
        /* if (type.equals("owner")) {
             val token = prefs.getString(
                 SessionConstants.TOKEN, GPSService.mLastLocation!!.latitude.toString()
             )
             ownerviewModel.unPaidOwnerList(token, "Service", "Paid")
         } else if (type.equals("tenant")) {*/
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.tenantUnpaidList(
            token,
            "Paid",
            if (flatOfBuildingId == "All") null else flatOfBuildingId
        )


    }

    private fun observer() {
        viewModel.tenantUnpaidLiveData.observe(requireActivity(), androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            binding.constraintLayout4.visibility = View.INVISIBLE
                            binding.rcyPaid.visibility = View.VISIBLE
                            if (filterKey.isNotEmpty()) {
                                paidList.clear()
                                if (months_filter.isNotEmpty()) {
                                    it.data.result.forEach {
                                        if (it.billType == filterKey) {
                                            val months = getMonthOfDate(it.date)
                                            if (months == months_filter) {
                                                paidList.add(it)
                                            }
                                        }
                                    }
                                } else {
                                    it.data.result.forEach {
                                        if (it.billType == filterKey) {
                                            paidList.add(it)
                                        }
                                    }
                                }
                                binding.rcyPaid.layoutManager =
                                    LinearLayoutManager(requireContext())
                                adaptr = HeaderAdapter(requireContext(), changeText, paidList, this)
                                binding.rcyPaid.adapter = adaptr
                                adaptr!!.notifyDataSetChanged()
                            } else if (months_filter.isNotEmpty()) {
                                paidList.clear()
                                it.data.result.forEach {
                                    val months = getMonthOfDate(it.date)
                                    if (months == months_filter) {
                                        paidList.add(it)
                                    }
                                }
                                binding.rcyPaid.layoutManager =
                                    LinearLayoutManager(requireContext())
                                adaptr = HeaderAdapter(requireContext(), changeText, paidList, this)
                                binding.rcyPaid.adapter = adaptr
                                adaptr!!.notifyDataSetChanged()
                            } else {
                                paidList.clear()
                                paidList.addAll(it.data.result)
                                binding.rcyPaid.layoutManager =
                                    LinearLayoutManager(requireContext())
                                adaptr = HeaderAdapter(requireContext(), changeText, paidList, this)
                                binding.rcyPaid.adapter = adaptr
                                adaptr!!.notifyDataSetChanged()
                            }
                            if (paidList.isEmpty()) {
                                binding.rcyPaid.visibility = View.INVISIBLE
                            } else {
                                binding.rcyPaid.visibility = View.VISIBLE
                            }
                            for (test in paidList) {
                                if (test.billType.equals("Rent")) {
                                    count++
                                    id = test._id
                                    amount = test.amount
                                }
                            }
                            println("--id$id")
                            println("--count$count")
                            if (count >= 1) {
                                binding.constraintLayout4.visibility = View.INVISIBLE
                            } else {
                                binding.constraintLayout4.visibility = View.INVISIBLE
                            }

                        } else if (it.status == AppConstants.STATUS_500) {
                            //requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            //requireContext().longToast(it.message)
                            binding.constraintLayout4.visibility = View.INVISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            //requireContext().longToast(it.message)
                            binding.rcyPaid.visibility = View.INVISIBLE
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
        /* ownerviewModel.unPaidOwnerListLiveData.observe(
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
                                 binding.constraintLayout4.visibility = View.INVISIBLE
                                 owner_paidList.clear()
                                 owner_paidList.addAll(it.data.result)
                                 binding.rcyPaid.layoutManager =
                                     LinearLayoutManager(requireContext())
                                 owneradaptr = OwnerUnPaidAdapter(
                                     requireContext(),
                                     owner_paidList,
                                     changeText,
                                     this
                                 )
                                 binding.rcyPaid.adapter = owneradaptr
                                 owneradaptr!!.notifyDataSetChanged()
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
             })*/

        ////owner
        viewModelOne.ownerFlatBuildingIDLiveData.observe(viewLifecycleOwner) {
            flatOfBuildingId = it
            paidList()
            println("---flatOfBuildingIdPaid$flatOfBuildingId")
        }
        viewModelOne.ownerFilterKeyLiveData.observe(viewLifecycleOwner) {
            filterKey = it.filter
            months_filter = it.months
            println("---filterKey$filterKey")
            paidList()
        }
        /* viewModelOne.ownerBottomMonthsFilterLiveData.observe(viewLifecycleOwner) {
             months_filter = it
             println("---months_filter$months_filter")
             paidList()
         }*/
        ////owner
    }

    private fun listener() {
        binding.payInAdvance.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    TenantPayInAdavanceActivity::class.java
                ).putExtra("amount", amount)
            )
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

    }

    override fun onViewReceiptTenant(refno: String, uploadDocument: String) {
        startActivity(
            Intent(requireContext(), ViewReceiptManagerActivity::class.java).putExtra(
                "img",
                uploadDocument
            ).putExtra("ref", refno)
        )
    }

    override fun onResume() {
        super.onResume()
        if (!flatOfBuildingId.isNullOrEmpty()) {
            paidList()
        }

    }
}