package com.application.intercom.manager.complaint

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.BaseFragment
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.remote.manager.managerSide.complain.ManagerPendingListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.FragmentResolveComplaintBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.utils.*

class ResolveComplaintFragment : BaseFragment<FragmentResolveComplaintBinding>(),
    ResolveComplaintsAdapter.ResolveClick, CommunityImgAdapter.ClickImg {
    private var mAdapter: ResolveComplaintsAdapter? = null
    private lateinit var dialog: Dialog
    private var showImg: String? = ""
    private lateinit var viewModel: ManagerSideViewModel
    private var resolveComplainListList = ArrayList<ManagerPendingListRes.Data.Result>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun lstnr() {}

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentResolveComplaintBinding {
        return FragmentResolveComplaintBinding.inflate(inflater, container, false)

    }


    override fun init() {
        initialize()


    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

    private fun resloveCompainList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation!!.latitude.toString()
        )
        viewModel.pendingComplain(token, "Resolve")
    }

    override fun observer() {
        viewModel.pendingComplainLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            resolveComplainListList.clear()
                            resolveComplainListList.addAll(it.data.result)
                            setAdapter(resolveComplainListList)
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireContext().longToast(it.message)
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

    private fun setAdapter(list: ArrayList<ManagerPendingListRes.Data.Result> = ArrayList()) {
        binding.rvResolve.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = ResolveComplaintsAdapter(requireContext(), list,this,this)
        binding.rvResolve.adapter = mAdapter

    }

    override fun onClick(position: Int) {
        startActivity(
            Intent(
                requireContext(),
                RegisterComplainsOwnerTenantDetailsActivity::class.java
            ).putExtra("pendingList", resolveComplainListList[position]).putExtra("from", "resolve")
        )
    }

    override fun onResume() {
        super.onResume()
        resloveCompainList()
    }

    override fun showImg(img: String) {
        showImg = img
        dialogProile()
    }

    private fun dialogProile() {
        dialog = Dialog(requireContext())
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
}
