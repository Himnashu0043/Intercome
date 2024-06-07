package com.application.intercom.tenant.activity.MyCommunity.communityFragement

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.remote.tenant.tenantSide.getAllMember.GetAllMemberListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.databinding.FragmentMemberListBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.owner.activity.viewPostDetails.ViewPostDetailsActivity
import com.application.intercom.tenant.activity.MyCommunity.GetAllMemberListAdapter
import com.application.intercom.tenant.adapter.myCommunity.TenantMyCommunityAdapter
import com.application.intercom.utils.*

class MemberListFragment() : Fragment() {
    lateinit var binding: FragmentMemberListBinding
    private lateinit var owner_viewModel: OwnerHomeViewModel
    private var list = ArrayList<GetAllMemberListRes.Data>()
    private var adptr: GetAllMemberListAdapter? = null
    private var projectId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemberListBinding.inflate(layoutInflater)
        projectId = prefs.getString(
            SessionConstants.PROJECTID, GPSService.mLastLocation?.latitude.toString()
        )
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()
    }


    private fun lstnr() {

    }

    private fun initialize() {
        val ownerModel = OwnerHomeRepo(BaseApplication.apiService)
        owner_viewModel = ViewModelProvider(
            this, OwnerHomeFactory(ownerModel)
        )[OwnerHomeViewModel::class.java]
    }

    private fun getAllMemberList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        owner_viewModel.getAllMember(
            token,
            projectId
        )

    }

    private fun observer() {

        owner_viewModel.userGetAllMemberLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            list.clear()
                            list.addAll(it.data)
                            binding.rcy.layoutManager = LinearLayoutManager(requireContext())
                            adptr = GetAllMemberListAdapter(requireContext(), list)
                            binding.rcy.adapter = adptr
                            adptr?.notifyDataSetChanged()
                            if (list.isEmpty()) {
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcy.visibility = View.INVISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.INVISIBLE
                                binding.rcy.visibility = View.VISIBLE
                            }
                        } else if (it.status == AppConstants.STATUS_404) {
//                            requireActivity().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rcy.visibility = View.INVISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rcy.visibility = View.INVISIBLE

                        } else if (it.status == AppConstants.STATUS_500) {
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rcy.visibility = View.INVISIBLE
                        } else {
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rcy.visibility = View.INVISIBLE
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


    }

    override fun onResume() {
        super.onResume()
        getAllMemberList()
    }
}