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
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.ViewModel.tenantViewModel.TenantSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.tenantFactory.TenantSideFactory
import com.application.intercom.data.model.remote.tenant.tenantSide.tenantBill.TenantUnPaidListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.data.repository.tenantRepo.TenantSideRepo
import com.application.intercom.databinding.FragmentTenantPendingBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.owner.adapter.bill.OwnerUnPaidAdapter
import com.application.intercom.tenant.activity.payment.TenantPaymentActivity
import com.application.intercom.tenant.adapter.billing.HeaderAdapter
import com.application.intercom.utils.*


class TenantPendingFragment : Fragment(), HeaderAdapter.PaidClick {
    lateinit var binding: FragmentTenantPendingBinding
    private var adaptr: HeaderAdapter? = null
    private var changeText = "pending"
    private lateinit var viewModel: TenantSideViewModel
    private var pendingList = ArrayList<TenantUnPaidListRes.Data.Result>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTenantPendingBinding.inflate(layoutInflater)
        initView()
        listener()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()
        pendingList()
    }

    private fun initialize() {
        val repo = TenantSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, TenantSideFactory(repo))[TenantSideViewModel::class.java]

    }

    private fun pendingList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.tenantUnpaidList(token, "Unapproved","")


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
                            pendingList.clear()
                            pendingList.addAll(it.data.result)
                            binding.rcyPending.layoutManager = LinearLayoutManager(requireContext())
                            adaptr = HeaderAdapter(requireContext(), changeText, pendingList, this)
                            binding.rcyPending.adapter = adaptr
                            adaptr!!.notifyDataSetChanged()
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

    }

    private fun listener() {
        binding.payInAdvance.setOnClickListener {
            startActivity(Intent(requireContext(), TenantPaymentActivity::class.java))
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

    }


}