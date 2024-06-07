package com.application.intercom.owner.activity.registerComplain

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.BaseFragment
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.ViewModel.tenantViewModel.TenantSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.tenantFactory.TenantSideFactory
import com.application.intercom.data.model.remote.owner.registerComplain.OwnerRegisterComplainList
import com.application.intercom.data.model.remote.tenant.tenantSide.TenantComplainListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.data.repository.tenantRepo.TenantSideRepo
import com.application.intercom.databinding.FragmentResolvedComplainOwnerTenantBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.utils.*

class ResolvedComplainOwnerTenantFragment(var from: String) :
    BaseFragment<FragmentResolvedComplainOwnerTenantBinding>(),
    CommunityImgAdapter.ClickImg {
    private var adpter: ResolvedCompainOwnerTenantAdapter? = null
    private lateinit var viewModel: OwnerSideViewModel
    private lateinit var tenant_viewModel: TenantSideViewModel
    private var list = ArrayList<OwnerRegisterComplainList.Data.Result>()
    private var tenantlist = ArrayList<TenantComplainListRes.Data.Result>()
    private var editData: String = ""
    private var showImg: String? = ""

    override fun lstnr() {

    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentResolvedComplainOwnerTenantBinding {
        return FragmentResolvedComplainOwnerTenantBinding.inflate(inflater, container, false)
    }

    override fun init() {
        println("========From$from")
        initialize()
        observer()
    }

    private fun initialize() {
        val repo = OwnerSideRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this,
            OwnerSideFactory(repo)
        )[OwnerSideViewModel::class.java]

        val tenantrepo = TenantSideRepo(BaseApplication.apiService)
        tenant_viewModel = ViewModelProvider(
            this,
            TenantSideFactory(tenantrepo)
        )[TenantSideViewModel::class.java]


    }

    private fun registerComplainList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        viewModel.listregisterComplainOwner(token)

    }


    private fun tenantComplainList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        tenant_viewModel.tenantComplainList(token)

    }

    override fun observer() {
        viewModel.listregisterComplainOwnerLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            list.clear()
                            for (data in it.data.result){
                                if (data.status =="Resolved"){
                                    list.add(data)
                                }
                            }
                            /*list.addAll(it.data.result)*/
                            binding.rcyRegister.layoutManager =
                                LinearLayoutManager(requireContext())
                            adpter =
                                ResolvedCompainOwnerTenantAdapter(
                                    requireContext(),
                                    list,
                                    tenantlist,
                                    from,
                                    this
                                )
                            binding.rcyRegister.adapter = adpter
                            adpter!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
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
        tenant_viewModel.tenantComplainListLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            tenantlist.clear()
                            for (data in it.data.result){
                                if (data.status =="Resolved"){
                                    tenantlist.add(data)
                                }
                            }
                           /* tenantlist.addAll(it.data.result)*/
                            binding.rcyRegister.layoutManager =
                                LinearLayoutManager(requireContext())
                            adpter =
                                ResolvedCompainOwnerTenantAdapter(
                                    requireContext(),
                                    list,
                                    tenantlist,
                                    from,
                                    this
                                )
                            binding.rcyRegister.adapter = adpter
                            adpter!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
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
    }

    override fun showImg(img: String) {
        showImg = img
        dialogshowImg()
    }

    private fun dialogshowImg() {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_profile_owner)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val imgGallery = dialog.findViewById<ImageView>(R.id.ivProImg)
        imgGallery.loadImagesWithGlideExt(showImg.toString())


        dialog.show()

    }

    override fun onResume() {
        super.onResume()
        if (from == "tenant") {
            tenantComplainList()
        } else {
            registerComplainList()
        }
    }

}