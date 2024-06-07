package com.application.intercom.tenant.Fragment.Billing

import android.annotation.SuppressLint
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
import com.application.intercom.databinding.FragmentUnPaidBinding
import com.application.intercom.helper.getMonthOfDate
import com.application.intercom.owner.adapter.bill.OwnerUnPaidAdapter
import com.application.intercom.tenant.activity.payment.TenantPayInAdavanceActivity
import com.application.intercom.tenant.activity.payment.TenantPaymentActivity
import com.application.intercom.tenant.adapter.billing.HeaderAdapter
import com.application.intercom.utils.*

class UnPaidFragment(val type: String) :
    Fragment(), HeaderAdapter.PaidClick, OwnerUnPaidAdapter.PaidClick {

    lateinit var binding: FragmentUnPaidBinding
    private var adaptr: HeaderAdapter? = null
    private var owneradaptr: OwnerUnPaidAdapter? = null
    private var changeText = "unpaid"
    private lateinit var viewModel: TenantSideViewModel
    private lateinit var ownerviewModel: OwnerSideViewModel
    private var unpaidList = ArrayList<TenantUnPaidListRes.Data.Result>()
    private var owner_unpaidList = ArrayList<OwnerUnPaidBillListRes.Data.Result>()
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
        binding = FragmentUnPaidBinding.inflate(layoutInflater)
        println("----from$type")
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()


    }

    private fun initialize() {
        binding.rcyUnPaid.adapter = owneradaptr
        val repo = TenantSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, TenantSideFactory(repo))[TenantSideViewModel::class.java]
        val ownerrepo = OwnerSideRepo(BaseApplication.apiService)
        ownerviewModel =
            ViewModelProvider(this, OwnerSideFactory(ownerrepo))[OwnerSideViewModel::class.java]

    }

    private fun unPaidList() {
        if (type == "owner") {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            ownerviewModel.unPaidOwnerList(
                token,
                "Unpaid",
                if (flatOfBuildingId == "All") null else flatOfBuildingId
            )
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            viewModel.tenantUnpaidList(
                token,
                "Unpaid",
                if (flatOfBuildingId == "All") null else flatOfBuildingId
            )

        }

    }

    @SuppressLint("NotifyDataSetChanged")
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
                            binding.constraintLayout4.visibility = View.GONE
                            if (filterKey.isNotEmpty()) {
                                unpaidList.clear()
                                if (months_filter.isNotEmpty()) {
                                    it.data.result.forEach {
                                        if (it.billType == filterKey) {
                                            val months = getMonthOfDate(it.date)
                                            if (months == months_filter) {
                                                unpaidList.add(it)
                                            }
                                        }
                                    }
                                } else {
                                    it.data.result.forEach {
                                        if (it.billType == filterKey) {
                                            unpaidList.add(it)
                                        }
                                    }
                                }
                                binding.rcyUnPaid.layoutManager =
                                    LinearLayoutManager(requireContext())
                                adaptr = HeaderAdapter(
                                    requireContext(), changeText, unpaidList, this
                                )
                                binding.rcyUnPaid.adapter = adaptr
                                adaptr!!.notifyDataSetChanged()
                            } else if (months_filter.isNotEmpty()) {
                                unpaidList.clear()
                                it.data.result.forEach {
                                    val months = getMonthOfDate(it.date)
                                    if (months == months_filter) {
                                        unpaidList.add(it)
                                    }
                                }
                                binding.rcyUnPaid.layoutManager =
                                    LinearLayoutManager(requireContext())
                                adaptr = HeaderAdapter(
                                    requireContext(), changeText, unpaidList, this
                                )
                                binding.rcyUnPaid.adapter = adaptr
                                adaptr!!.notifyDataSetChanged()
                            } else {
                                unpaidList.clear()
                                unpaidList.addAll(it.data.result)
                                binding.rcyUnPaid.layoutManager =
                                    LinearLayoutManager(requireContext())
                                adaptr = HeaderAdapter(
                                    requireContext(), changeText, unpaidList, this
                                )
                                binding.rcyUnPaid.adapter = adaptr
                                adaptr!!.notifyDataSetChanged()
                            }
                            if (unpaidList.isEmpty()) {
                                binding.rcyUnPaid.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rcyUnPaid.visibility = View.VISIBLE
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                            /* for (test in it.data.result) {
                                 if (test.billType.equals("Rent")) {
                                     count++
                                     id = test._id
                                     amount = test.amount
                                 }
                             }
                             println("--id$id")
                             println("--count$count")
 //                            if (count == 1) {
 //                                binding.constraintLayout4.visibility = View.VISIBLE
 //                            } else {
 //                                binding.constraintLayout4.visibility = View.INVISIBLE
 //                            }*/
                        } else if (it.status == AppConstants.STATUS_500) {
                            //requireContext().longToast(it.message)
                            binding.rcyUnPaid.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_404) {
                            //requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            //  requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rcyUnPaid.visibility = View.INVISIBLE
                        } else {
                            //requireContext().longToast(it.message)
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
        ownerviewModel.unPaidOwnerListLiveData.observe(
            requireActivity(),
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
                                    owner_unpaidList.clear()
                                    if (months_filter.isNotEmpty()) {
                                        it.data.result.forEach {
                                            if (it.billType == filterKey) {
                                                val months = getMonthOfDate(it.date)
                                                if (months == months_filter) {
                                                    owner_unpaidList.add(it)
                                                }
                                            }
                                        }
                                    } else {
                                        it.data.result.forEach {
                                            if (it.billType == filterKey) {
                                                owner_unpaidList.add(it)
                                            }
                                        }
                                    }

                                    binding.rcyUnPaid.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    owneradaptr = OwnerUnPaidAdapter(
                                        requireContext(), owner_unpaidList, changeText, this, ""
                                    )
                                    binding.rcyUnPaid.adapter = owneradaptr
                                    owneradaptr!!.notifyDataSetChanged()
                                } else if (months_filter.isNotEmpty()) {
                                    owner_unpaidList.clear()
                                    it.data.result.forEach {
                                        val months = getMonthOfDate(it.date)
                                        if (months == months_filter) {
                                            owner_unpaidList.add(it)
                                        }
                                    }
                                    binding.rcyUnPaid.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    owneradaptr = OwnerUnPaidAdapter(
                                        requireContext(), owner_unpaidList, changeText, this, ""
                                    )
                                    binding.rcyUnPaid.adapter = owneradaptr
                                    owneradaptr!!.notifyDataSetChanged()
                                } else {
                                    owner_unpaidList.clear()
                                    owner_unpaidList.addAll(it.data.result)
                                    binding.rcyUnPaid.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    owneradaptr = OwnerUnPaidAdapter(
                                        requireContext(), owner_unpaidList, changeText, this, ""
                                    )
                                    binding.rcyUnPaid.adapter = owneradaptr
                                    println("----owner_unpaidListpppp${owner_unpaidList.size}")
                                    owneradaptr!!.notifyDataSetChanged()
                                }
                                if (owner_unpaidList.isEmpty()) {
                                    binding.rcyUnPaid.visibility = View.INVISIBLE
                                    binding.lottieEmpty.visibility = View.VISIBLE
                                } else {
                                    binding.rcyUnPaid.visibility = View.VISIBLE
                                    binding.lottieEmpty.visibility = View.INVISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_500) {
                                //  requireContext().longToast(it.message)
                                binding.rcyUnPaid.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else if (it.status == AppConstants.STATUS_404) {
                                //requireContext().longToast(it.message)
                                binding.rcyUnPaid.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else if (it.status == AppConstants.STATUS_FAILURE) {
                                // requireContext().longToast(it.message)
                                binding.rcyUnPaid.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                //requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
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
//owner
        viewModelOne.ownerFlatBuildingIDLiveData.observe(viewLifecycleOwner) {
            flatOfBuildingId = it
            filterKey = ""
            months_filter = ""
            unPaidList()


        }
        viewModelOne.ownerFilterKeyLiveData.observe(viewLifecycleOwner) {
            filterKey = it.filter
            months_filter = it.months
            unPaidList()
        }

//owner
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
                .putExtra("tenantBillList", unpaidList[position])
        )
    }

    override fun onNotifyTenant(id: String, position: Int) {

    }

    override fun onViewReceiptTenant(refno: String, uploadDocument: String) {

    }

    override fun onOwnerClick(id: String, position: Int) {
        startActivity(
            Intent(
                requireContext(), TenantPaymentActivity::class.java
            ).putExtra("billingId", id).putExtra("from", type)
                .putExtra("ownerBillList", owner_unpaidList[position])
        )
    }

    override fun onNotifyOwner(id: String, position: Int) {

    }

    override fun onViewReceipt(refno: String, uploadDocument: String) {

    }

    override fun onReject(billID: String) {

    }

    override fun onOwnertoTenantNotify(billID: String, position: Int) {

    }

    override fun onResume() {
        super.onResume()
        if (!flatOfBuildingId.isNullOrEmpty()) {
            unPaidList()
        }

    }
}