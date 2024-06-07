package com.application.intercom.owner.activity.registerComplain

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
import com.application.intercom.data.model.local.owner.actiontoComplain.OwnerActionToComplainPostModel
import com.application.intercom.data.model.remote.owner.registerComplain.OwnerRegisterComplainList
import com.application.intercom.data.model.remote.tenant.tenantSide.TenantComplainListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.data.repository.tenantRepo.TenantSideRepo
import com.application.intercom.databinding.FragmentPendingComplainOwnerTenantBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.tenant.adapter.registerComplain.TenantRegisterComplainAdapter
import com.application.intercom.utils.*

class PendingComplainOwnerTenantFragment(var from: String) :
    BaseFragment<FragmentPendingComplainOwnerTenantBinding>(),
    TenantRegisterComplainAdapter.ComplainClick, CommunityImgAdapter.ClickImg {
    private var adpter: TenantRegisterComplainAdapter? = null
    private lateinit var viewModel: OwnerSideViewModel
    private lateinit var tenant_viewModel: TenantSideViewModel
    private var list = ArrayList<OwnerRegisterComplainList.Data.Result>()
    private var tenantlist = ArrayList<TenantComplainListRes.Data.Result>()

    /* private var from: String = ""*/
    private var comId: String = ""
    private var editData: String = ""
    private var showImg: String? = ""


    override fun lstnr() {
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPendingComplainOwnerTenantBinding {
        return FragmentPendingComplainOwnerTenantBinding.inflate(inflater, container, false)
    }

    override fun init() {
        println("========From$from")
        initView()
    }

    private fun initView() {
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

    private fun actionToComplain() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        val model = OwnerActionToComplainPostModel(comId, "Confirm", "")
        viewModel.actionToComplainOwner(token, model)

    }

    private fun denyactionToComplain() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        val model = OwnerActionToComplainPostModel(comId, "Deny", editData)
        viewModel.actionToComplainOwner(token, model)

    }

    private fun tenantComplainList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        tenant_viewModel.tenantComplainList(token)

    }

    override fun observer() {
        viewModel.listregisterComplainOwnerLiveData.observe(this, Observer {
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
                                if (data.status !="Resolved"){
                                    list.add(data)
                                }
                            }

                            binding.rcyRegister.layoutManager =
                                LinearLayoutManager(requireContext())
                            adpter =
                                TenantRegisterComplainAdapter(
                                    requireContext(),
                                    this,
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
                                if (data.status !="Resolved"){
                                    tenantlist.add(data)
                                }
                            }
                            binding.rcyRegister.layoutManager =
                                LinearLayoutManager(requireContext())
                            adpter =
                                TenantRegisterComplainAdapter(
                                    requireContext(),
                                    this,
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
        viewModel.actionToComplainOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            requireActivity().longToast(it.message)
                          /*  requireActivity().finish()*/
                            if (from == "tenant") {
                                tenantComplainList()
                            } else {

                                registerComplainList()
                            }
                            if (prefs.getString(
                                    SessionConstants.NOTYTYPE,
                                    ""
                                ) == "NEW_COMPLAIN_RESOLVE"
                            ) {
                                prefs.put(SessionConstants.NOTYTYPE, "")
                                if (from == "owner") {
                                    startActivity(
                                        Intent(
                                            requireContext(),
                                            OwnerMainActivity::class.java
                                        )
                                    )
                                } else {
                                    startActivity(
                                        Intent(
                                            requireContext(),
                                            TenantMainActivity::class.java
                                        )
                                    )
                                }
                            }


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

    override fun onClick(id: String, position: Int) {
        comId = id
        println("----id$comId")
        complainPopup()
    }

    override fun onDenyClcik(id: String, position: Int) {
        comId = id
        reasonPopup()
    }

    private fun complainPopup() {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.tenant_complain_resolve_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val resolve = dialog.findViewById<TextView>(R.id.tvComplainresolve)
        val reject = dialog.findViewById<TextView>(R.id.tvReject)
        resolve.setOnClickListener {
            dialog.dismiss()
            actionToComplain()
        }
        reject.setOnClickListener {
            dialog.dismiss()
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    private fun reasonPopup() {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.reason_deny_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val submit = dialog.findViewById<TextView>(R.id.tvsubmit)
        val edit = dialog.findViewById<EditText>(R.id.ed)
        edit.setOnClickListener {
            editData = edit.text.toString()
            println("----data$editData")
        }
        submit.setOnClickListener {
            dialog.dismiss()
            editData = edit.text.toString()
            println("----sndsj$editData")
            denyactionToComplain()
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

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