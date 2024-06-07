package com.application.intercom.manager.complaint

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import com.application.intercom.databinding.FragmentResolvedComplaintBinding
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.utils.*

class ResolvedComplaintFragment : BaseFragment<FragmentResolvedComplaintBinding>(),
    ResolvedComplaintsAdapter.ReslovedClick, CommunityImgAdapter.ClickImg {
    private var mAdapter: ResolvedComplaintsAdapter? = null
    private lateinit var dialog: Dialog
    private var showImg: String? = ""
    private lateinit var viewModel: ManagerSideViewModel
    private var resolvedComplainListList = ArrayList<ManagerPendingListRes.Data.Result>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun lstnr() {
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentResolvedComplaintBinding {
        return FragmentResolvedComplaintBinding.inflate(inflater, container, false)

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
            SessionConstants.TOKEN, ""
        )
        viewModel.pendingComplain(token, "Resolved")
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
                            resolvedComplainListList.clear()
                            resolvedComplainListList.addAll(it.data.result)
                            setAdapter(resolvedComplainListList)
                            if (resolvedComplainListList.isEmpty()) {
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rvResolved.visibility = View.INVISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.INVISIBLE
                                binding.rvResolved.visibility = View.VISIBLE
                            }
                        } else if (it.status == AppConstants.STATUS_500) {
                            // requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rvResolved.visibility = View.INVISIBLE
                        } else if (it.status == AppConstants.STATUS_404) {
                            //requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rvResolved.visibility = View.INVISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rvResolved.visibility = View.INVISIBLE
                        } else {
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rvResolved.visibility = View.INVISIBLE
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
    }


    private fun setAdapter(list: ArrayList<ManagerPendingListRes.Data.Result> = ArrayList()) {
        binding.rvResolved.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = ResolvedComplaintsAdapter(requireContext(), list, this, this)
        binding.rvResolved.adapter = mAdapter

    }

    override fun onCLick(position: Int) {

        startActivity(
            Intent(
                requireContext(),
                RegisterComplainsOwnerTenantDetailsActivity::class.java
            ).putExtra("pendingList", resolvedComplainListList[position])
                .putExtra("from", "resolved")
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
