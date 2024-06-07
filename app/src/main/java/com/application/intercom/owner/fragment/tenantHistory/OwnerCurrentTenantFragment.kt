package com.intercom.owner.fragment.ownerHome.tenantHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.remote.owner.ownerTenantHistory.OwnerTenantCurrentHistoryListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.databinding.FragmentOwnerCurrentTenantBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.owner.adapter.OwnerCurrentTenantAdapter
import com.application.intercom.owner.adapter.ownerHome.OwnerPostingAdapter
import com.application.intercom.tenant.adapter.home.TenantHomeOfferAdapter
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.utils.*

class OwnerCurrentTenantFragment(val flatId: String) : Fragment() {
    lateinit var binding: FragmentOwnerCurrentTenantBinding
    private var adptr: OwnerCurrentTenantAdapter? = null
    private lateinit var owner_viewModel: OwnerHomeViewModel
    private var currentList = java.util.ArrayList<OwnerTenantCurrentHistoryListRes.Data>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOwnerCurrentTenantBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()


    }

    private fun initialize() {
        val ownerModel = OwnerHomeRepo(BaseApplication.apiService)
        owner_viewModel = ViewModelProvider(
            this, OwnerHomeFactory(ownerModel)
        )[OwnerHomeViewModel::class.java]
    }

    private fun getOwnerCurrentHistoryList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        owner_viewModel.getOwnerCurrentHistory(token, "Current", flatId)
    }

    private fun observer() {

        owner_viewModel.ownerCurrentHistoryLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            currentList.clear()
                            currentList.addAll(it.data)
                            binding.rcyCurrentTenant.layoutManager =
                                LinearLayoutManager(requireContext())
                            adptr =
                                OwnerCurrentTenantAdapter(requireContext(), currentList, "current")
                            binding.rcyCurrentTenant.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireActivity(), it.throwable!!)
                }
                else -> {}
            }
        })


    }

    private fun lstnr() {

    }

    override fun onResume() {
        super.onResume()
        getOwnerCurrentHistoryList()
    }

}