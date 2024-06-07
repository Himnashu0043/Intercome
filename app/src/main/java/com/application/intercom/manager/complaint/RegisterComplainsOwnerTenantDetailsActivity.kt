package com.application.intercom.manager.complaint

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.manager.managerSide.actionToComplain.ManagerActionToComplainPostModel
import com.application.intercom.data.model.remote.manager.managerSide.complain.ManagerPendingListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityRegisterComplainsOwnerTenantDetailsBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.utils.*

class RegisterComplainsOwnerTenantDetailsActivity : AppCompatActivity(),
    ManagerDetailsComplainAdapter.Show {
    private lateinit var binding: ActivityRegisterComplainsOwnerTenantDetailsBinding
    private lateinit var dialog: Dialog
    private var showImg: String? = ""
    private var list: ManagerPendingListRes.Data.Result? = null
    private var from: String = ""
    private var adpter: ManagerDetailsComplainAdapter? = null
    private var testList = ArrayList<String>()
    private lateinit var viewModel: ManagerSideViewModel
    private var complainId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterComplainsOwnerTenantDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        println("---from$from")
        initView()
        listener()
        initialize()
        observer()


    }

    private fun initView() {
        binding.btnResolve.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.toolbar.tvTittle.text = getString(R.string.complain_details)
        if (from.equals("pending")) {
            list = intent.getSerializableExtra("pendingList") as ManagerPendingListRes.Data.Result
            println("============${list!!._id}")
            println("============${list}")
            binding.tvProfileName.text = list!!.userInfo.get(0).fullName
            binding.tvFlatNumber1.text = list!!.flatInfo.get(0).name
            binding.tvTvComplainId1.text = list!!.compId
            binding.tvOwner.text = list!!.userInfo.get(0).role
            complainId = list!!.compId
            /*binding.tvServiceCategory1.text = list!!.serviceCategory.get(0).category_name*/
            binding.tvComplaintName1.text = list!!.complainName
            binding.tvDescription.text =
                "${getString(R.string.description)} : ${list!!.description}"
            if (!list!!.denyReason.isNullOrEmpty())
                binding.tvDenyResone.text =
                    "${getString(R.string.deny_reason)} : ${list!!.denyReason}"
            else binding.tvDenyResone.visibility = View.GONE
            val date = setFormatDate(list!!.createdAt)
            binding.tvDate1.text = date
//            list!!.photo.forEach {
//                testList.addAll(list!!.photo)
//            }
            testList.addAll(list!!.photo)
            binding.rcy.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            adpter = ManagerDetailsComplainAdapter(this, testList, this)
            binding.rcy.adapter = adpter
            adpter!!.notifyDataSetChanged()
            binding.ivCalling.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + list!!.userInfo.get(0).phoneNumber)
                startActivity(intent)
            }

        } else if (from.equals("resolve")) {
            list = intent.getSerializableExtra("pendingList") as ManagerPendingListRes.Data.Result
            binding.tvProfileName.text = list!!.userInfo.get(0).fullName
            binding.tvFlatNumber1.text = list!!.flatInfo.get(0).name
            binding.tvTvComplainId1.text = list!!.compId
            binding.tvOwner.text = list!!.userInfo.get(0).role
            complainId = list!!.compId
          /*  binding.tvServiceCategory1.text = list!!.serviceCategory.get(0).category_name*/
            binding.tvComplaintName1.text = list!!.complainName
            binding.tvDescription.text =
                "${getString(R.string.description)} : ${list!!.description}"
            val date = setFormatDate(list!!.createdAt)
            binding.tvDate1.text = date
            list!!.photo.forEach {
                testList.add(list!!.photo.get(0))
            }
            binding.rcy.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            adpter = ManagerDetailsComplainAdapter(this, testList, this)
            binding.rcy.adapter = adpter
            adpter!!.notifyDataSetChanged()
            binding.ivCalling.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + list!!.userInfo.get(0).phoneNumber)
                startActivity(intent)
            }
            if (list!!.status.equals("Resolved")) {
                binding.btnResolve.visibility = View.INVISIBLE
            }
        } else if (from.equals("resolved")) {
            list = intent.getSerializableExtra("pendingList") as ManagerPendingListRes.Data.Result
            binding.tvProfileName.text = list!!.userInfo.get(0).fullName
            binding.tvFlatNumber1.text = list!!.flatInfo.get(0).name
            binding.tvTvComplainId1.text = list!!.compId
            binding.tvOwner.text = list!!.userInfo.get(0).role
            complainId = list!!.compId
           /* binding.tvServiceCategory1.text = list!!.serviceCategory.get(0).category_name*/
            binding.tvComplaintName1.text = list!!.complainName
            binding.tvDescription.text =
                "${getString(R.string.description)} : ${list!!.description}"
            val date = setFormatDate(list!!.createdAt)
            binding.tvDate1.text = date
            list!!.photo.forEach {
                testList.add(list!!.photo.get(0))
            }
            binding.rcy.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            adpter = ManagerDetailsComplainAdapter(this, testList, this)
            binding.rcy.adapter = adpter
            adpter!!.notifyDataSetChanged()
            binding.btnResolve.visibility = View.INVISIBLE
            binding.ivCalling.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + list!!.userInfo.get(0).phoneNumber)
                startActivity(intent)
            }
        }
    }

    private fun listener() {
        binding.btnResolve.setOnClickListener {
            if (from.equals("pending")) {
                actionToCompainPopup("Resolve")
                /* ComplaintDialog.newInstance(
                     getString(R.string.tv_register_member),
                     getString(R.string.app_name),
                     list!!._id,
                     "Resolve"

                 )
                     .show(supportFragmentManager, ComplaintDialog.TAG)*/
            } else if (from.equals("resolve")) {
                actionToCompainPopup("Resolve")
                /*ComplaintDialog.newInstance(
                      getString(R.string.tv_register_member),
                      getString(R.string.app_name),
                      list!!._id,
                      "Resolveed"

                  )
                      .show(supportFragmentManager, ComplaintDialog.TAG)*/
            }


        }
        binding.toolbar.ivBack.setOnClickListener {
            /* if (prefs.getString(SessionConstants.NOTYTYPE, "") == "COMPLAIN_DENY") {
                 startActivity(Intent(this, ManagerMainActivity::class.java))
                 prefs.put(SessionConstants.NOTYTYPE, "")
             } else {
                 finish()
             }*/
            finish()
        }
    }

    private fun actionToCompainPopup(type: String) {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.complain_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val tvYes = dialog.findViewById<TextView>(R.id.yes)
        val tvNo = dialog.findViewById<TextView>(R.id.no)
        val tvcomplain = dialog.findViewById<TextView>(R.id.tv_desc1)
        tvcomplain.text =
            "${getString(R.string.complain_id_1)} -${complainId} ${getString(R.string.as_resolved)} !!"

        tvNo.setOnClickListener {
            dialog.dismiss()
        }
        tvYes.setOnClickListener {
            dialog.dismiss()
            actionToComplain(type)
        }

        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]
    }

    private fun actionToComplain(sendType: String) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = ManagerActionToComplainPostModel(list!!._id, sendType)
        viewModel.actionToComplain(token, model)
    }

    private fun observer() {
        viewModel.actionToComplainLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(
                                    this, RegisterComplaintsActivity::class.java
                                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )

                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })
    }

    override fun onClickImgShow(img: String) {
        showImg = img
        dialogProile()
    }

    private fun dialogProile() {
        dialog = Dialog(this)
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