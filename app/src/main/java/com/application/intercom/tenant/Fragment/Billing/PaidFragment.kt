package com.application.intercom.tenant.Fragment.Billing

import android.content.Intent
import android.os.Bundle
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
import com.application.intercom.databinding.FragmentPaidBinding
import com.application.intercom.helper.getMonthOfDate
import com.application.intercom.manager.bills.ViewReceiptManagerActivity
import com.application.intercom.owner.adapter.bill.OwnerUnPaidAdapter
import com.application.intercom.tenant.activity.payment.TenantPayInAdavanceActivity
import com.application.intercom.tenant.activity.payment.TenantPaymentActivity
import com.application.intercom.tenant.adapter.billing.HeaderAdapter
import com.application.intercom.utils.*

class PaidFragment(val type: String) : Fragment(),
    HeaderAdapter.PaidClick, OwnerUnPaidAdapter.PaidClick {
    lateinit var binding: FragmentPaidBinding
    private var adaptr: HeaderAdapter? = null
    private var changeText = "paid"
    private var owneradaptr: OwnerUnPaidAdapter? = null
    private lateinit var viewModel: TenantSideViewModel
    private lateinit var ownerviewModel: OwnerSideViewModel
    private var paidList = ArrayList<TenantUnPaidListRes.Data.Result>()
    private var owner_paidList = ArrayList<OwnerUnPaidBillListRes.Data.Result>()
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaidBinding.inflate(layoutInflater)
        println("---type$type")
        /*println("---flatOfBuildingId$flatOfBuildingId")*/
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

    private fun paidList() {
        if (type.equals("owner")) {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            ownerviewModel.unPaidOwnerList(
                token,
                "Paid",
                if (flatOfBuildingId == "All") null else flatOfBuildingId
            )
        } else if (type.equals("tenant")) {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            viewModel.tenantUnpaidList(token, "Paid", if (flatOfBuildingId == "All") null else flatOfBuildingId)
        }

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


                            /* if (!flatOfBuildingId.isNullOrEmpty()) {

                                 if (flatOfBuildingId == "All") {
                                     paidList.clear()
                                     paidList.addAll(it.data.result)
                                     binding.rcyPaid.layoutManager =
                                         LinearLayoutManager(requireContext())
                                     adaptr =
                                         HeaderAdapter(requireContext(), changeText, paidList, this)
                                     binding.rcyPaid.adapter = adaptr
                                     adaptr!!.notifyDataSetChanged()
                                 } else {
                                     paidList.clear()
                                     it.data.result.forEach {
                                         if (it.flatId.equals(flatOfBuildingId)) {
                                             paidList.add(it)
                                         }
                                     }
                                     binding.rcyPaid.layoutManager =
                                         LinearLayoutManager(requireContext())
                                     adaptr =
                                         HeaderAdapter(requireContext(), changeText, paidList, this)
                                     binding.rcyPaid.adapter = adaptr
                                     adaptr!!.notifyDataSetChanged()
                                 }

                             } else {
                                 paidList.clear()
                                 paidList.addAll(it.data.result)
                                 binding.rcyPaid.layoutManager =
                                     LinearLayoutManager(requireContext())
                                 adaptr = HeaderAdapter(requireContext(), changeText, paidList, this)
                                 binding.rcyPaid.adapter = adaptr
                                 adaptr!!.notifyDataSetChanged()
                             }*/
                            paidList.clear()
                            paidList.addAll(it.data.result)
                            binding.lottieEmpty.visibility = View.INVISIBLE
                            binding.rcyPaid.layoutManager =
                                LinearLayoutManager(requireContext())
                            adaptr = HeaderAdapter(requireContext(), changeText, paidList, this)
                            binding.rcyPaid.adapter = adaptr
                            adaptr!!.notifyDataSetChanged()
                            for (test in it.data.result) {
                                if (test.billType.equals("Rent")) {
                                    count++
                                    id = test._id
                                    amount = test.amount
                                }
                            }
                            println("--id$id")
                            println("--count$count")
                            if (count == 1) {
                                binding.constraintLayout4.visibility = View.VISIBLE

                            } else {
                                binding.constraintLayout4.visibility = View.GONE
                            }
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            // requireContext().longToast(it.message)
                            binding.constraintLayout4.visibility = View.INVISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else {
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
        ownerviewModel.unPaidOwnerListLiveData.observe(requireActivity(),
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(requireActivity())
                    }

                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let { it ->
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                binding.constraintLayout4.visibility = View.GONE
                                if (filterKey.isNotEmpty()) {
                                    owner_paidList.clear()
                                    if (months_filter.isNotEmpty()) {
                                        it.data.result.forEach {
                                            if (it.billType == filterKey) {
                                                val months = getMonthOfDate(it.date)
                                                if (months == months_filter) {
                                                    owner_paidList.add(it)
                                                }
                                            }
                                        }
                                    } else {
                                        it.data.result.forEach {
                                            if (it.billType == filterKey) {
                                                owner_paidList.add(it)
                                            }
                                        }
                                    }
                                    binding.rcyPaid.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    owneradaptr = OwnerUnPaidAdapter(
                                        requireContext(), owner_paidList, changeText, this, ""
                                    )
                                    binding.rcyPaid.adapter = owneradaptr
                                    owneradaptr!!.notifyDataSetChanged()
                                } else if (months_filter.isNotEmpty()) {
                                    owner_paidList.clear()
                                    it.data.result.forEach {
                                        val months = getMonthOfDate(it.date)
                                        if (months == months_filter) {
                                            owner_paidList.add(it)
                                        }
                                    }
                                    binding.rcyPaid.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    owneradaptr = OwnerUnPaidAdapter(
                                        requireContext(), owner_paidList, changeText, this, ""
                                    )
                                    binding.rcyPaid.adapter = owneradaptr
                                    owneradaptr!!.notifyDataSetChanged()
                                } else {
                                    owner_paidList.clear()
                                    owner_paidList.addAll(it.data.result)
                                    binding.rcyPaid.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    owneradaptr = OwnerUnPaidAdapter(
                                        requireContext(), owner_paidList, changeText, this, ""
                                    )
                                    binding.rcyPaid.adapter = owneradaptr
                                    owneradaptr!!.notifyDataSetChanged()
                                }
                                if (owner_paidList.isEmpty()) {
                                    binding.rcyPaid.visibility = View.INVISIBLE
                                    binding.lottieEmpty.visibility = View.VISIBLE
                                } else {
                                    binding.rcyPaid.visibility = View.VISIBLE
                                    binding.lottieEmpty.visibility = View.INVISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_500) {
                                //requireContext().longToast(it.message)
                                binding.rcyPaid.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else if (it.status == AppConstants.STATUS_404) {
                                // requireContext().longToast(it.message)
                                binding.rcyPaid.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else if (it.status == AppConstants.STATUS_FAILURE) {
                                binding.rcyPaid.visibility = View.INVISIBLE

                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rcyPaid.visibility = View.INVISIBLE
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
        ////owner
        viewModelOne.ownerFlatBuildingIDLiveData.observe(viewLifecycleOwner) {
            flatOfBuildingId = it
            filterKey = ""
            months_filter = ""
            paidList()

        }
        viewModelOne.ownerFilterKeyLiveData.observe(viewLifecycleOwner) {
            filterKey = it.filter
            months_filter = it.months

            paidList()
        }
        /* viewModelOne.ownerBottomMonthsFilterLiveData.observe(viewLifecycleOwner) {
             months_filter = it
             println("---months_filter$months_filter")
             paidList()
         }*/
        ////owner
    }

    private fun lstnr() {
        binding.payInAdvance.setOnClickListener {
            /* startActivity(Intent(requireContext(), TenantPaymentActivity::class.java))*/
            startActivity(
                Intent(
                    requireContext(), TenantPayInAdavanceActivity::class.java
                ).putExtra("amount", amount)
            )
        }
    }

    override fun onClick(id: String, position: Int) {
        startActivity(
            Intent(
                requireContext(), TenantPaymentActivity::class.java
            ).putExtra("billingId", id).putExtra("from", type)
                .putExtra("tenantBillList", paidList[position])
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

    override fun onOwnerClick(id: String, position: Int) {

    }

    override fun onNotifyOwner(id: String, position: Int) {


    }

    override fun onViewReceipt(refno: String, uploadDocument: String) {
        startActivity(
            Intent(requireContext(), ViewReceiptManagerActivity::class.java).putExtra(
                "img",
                uploadDocument
            ).putExtra("ref", refno)
        )
    }

    override fun onReject(billID: String) {

    }

    override fun onOwnertoTenantNotify(billID: String, position: Int) {

    }

    override fun onResume() {
        super.onResume()
        if (!flatOfBuildingId.isNullOrEmpty()) {
            paidList()
        }
    }
}