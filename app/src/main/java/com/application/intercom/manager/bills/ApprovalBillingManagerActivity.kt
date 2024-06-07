package com.application.intercom.manager.bills

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.remote.manager.managerSide.bill.MangerBillPendingListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityApprovalBillingManagerBinding
import com.application.intercom.databinding.ManagerFilterBillingBottomSheetBinding
import com.application.intercom.helper.getMonthOfDate
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog

class ApprovalBillingManagerActivity : BaseActivity<ActivityApprovalBillingManagerBinding>(),ManagerApprovalBillsAdapter.MarkClick {
    private lateinit var viewModel: ManagerSideViewModel
    private var flatOfBuildingList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingId: String = ""
    lateinit var filterBottom: ManagerFilterBillingBottomSheetBinding
    lateinit var bottomSheetDialog: BottomSheetDialog
    private var filterKey: String = ""
    private var months_filter: String = ""
    private var mAdapter: ManagerApprovalBillsAdapter? = null
    private var approvalList = ArrayList<MangerBillPendingListRes.Data.Result>()
    private var notyType: String = ""
    private var from: String = ""
    override fun getLayout(): ActivityApprovalBillingManagerBinding {
        return ActivityApprovalBillingManagerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()
    }

    private fun initView() {
        binding.layoutFilter.visibility = View.VISIBLE
        notyType = intent.getStringExtra("notyFlow") ?: ""
        from = intent.getStringExtra("from") ?: ""
        println("=====%$notyType")
        println("=====%from$from")
        flatOfBuildingList.add(0, "All")
        initialize()
        observer()
        flatOfBuilding()
        flatSpinner()
        binding.btnLogin.tv.text = getString(R.string.add_billings_1)
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(this, R.color.orange))
    }

    private fun listener() {
        binding.ivBack.setOnClickListener {
            if (notyType == "notyType") {
                notyType = ""
                startActivity(
                    Intent(
                        this,
                        ManagerMainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            } else if (from == "kill_state") {
                startActivity(
                    Intent(
                        this,
                        ManagerMainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            } else if (prefs.getString(SessionConstants.NOTYTYPE, "") == "NEW_BILL_PAID") {
                prefs.put(SessionConstants.NOTYTYPE, "")
                startActivity(
                    Intent(
                        this,
                        ManagerMainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            } else {
                finish()
            }

        }
        binding.btnLogin.tv.setOnClickListener {
            startActivity(
                Intent(this, AddBillsActivity::class.java)
            )
        }
        binding.layoutFilter.setOnClickListener {
            filter()
        }
    }
    private fun initialize() {
        val repo1 = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo1))[ManagerSideViewModel::class.java]
    }

    private fun flatOfBuilding() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.flatOfBuildingList(token)

    }
    private fun billApprovalList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.billpending(
            token, "Unapproved", if (flatOfBuildingId == "All") null else flatOfBuildingId
        )
    }
    private fun observer() {
        viewModel.flatOfBuildingListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            it.data.result.forEach {
                                flatOfBuildingList.add(it.name)
                                flatOfBuildingHashMapID.put(it.name, it._id)
                            }
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
        viewModel.billApproveLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            println("---managerfilterKey$filterKey")
                            println("---managermonths_filter$months_filter")
                            if (filterKey.isNotEmpty()) {
                                approvalList.clear()
                                if (months_filter.isNotEmpty()) {
                                    it.data.result?.forEach {
                                        if (it.billType == filterKey) {
                                            val months = getMonthOfDate(it.date?:"")
                                            if (months == months_filter) {
                                                approvalList.add(it)
                                            }
                                        }
                                    }
                                } else {
                                    it.data.result?.forEach {
                                        if (it.billType == filterKey) {
                                            approvalList.add(it)
                                        }
                                    }
                                }
                                setAdapter(approvalList)
                            } else if (months_filter.isNotEmpty()) {
                                approvalList.clear()
                                it.data.result?.forEach {
                                    val months = getMonthOfDate(it.date?:"")
                                    if (months == months_filter) {
                                        approvalList.add(it)
                                    }
                                }
                                setAdapter(approvalList)
                            } else {
                                approvalList.clear()
                                approvalList.addAll(it.data.result!!)
                                setAdapter(approvalList)
                            }

                            if (approvalList.isEmpty()) {
                                binding.rvManagerApproval.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rvManagerApproval.visibility = View.VISIBLE
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                        } else if (it.status == AppConstants.STATUS_500) {
                            //requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_404) {
                            //requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            //  requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rvManagerApproval.visibility = View.INVISIBLE
                        } else {
                            binding.lottieEmpty.visibility = View.VISIBLE
                            // requireContext().longToast(it.message)
                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })
        viewModel.markAsPaidManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            billApprovalList()
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                           this.longToast(it.message)
                        } else {
                            this.longToast(it.message)
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
        viewModel.rejectBillManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            billApprovalList()
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message ?: "")
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message ?: "")
                        } else {
                            this.longToast(it.message ?: "")
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
    private fun flatSpinner() {
        //val genderList = resources.getStringArray(R.array.EditProfile)
        binding.mainflatSpiner.adapter =
            ArrayAdapter(this, R.layout.spinner_dropdown_item, flatOfBuildingList)
        binding.mainflatSpiner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long,
                ) {

                    /*if (binding.mainflatSpiner.selectedItemPosition > 0) {
                        if (binding.mainflatSpiner.selectedItem.equals("All")) {
                            flatOfBuildingId = "All"
                            println("---All$flatOfBuildingId")
                           manager_viewModel.managerFlatBuildingIDLiveData.postValue(flatOfBuildingId)
                        } else {
                            flatOfBuildingId =
                                flatOfBuildingHashMapID.get(binding.mainflatSpiner.selectedItem.toString())
                                    .toString()
                            println("---flateId$flatOfBuildingId")
                            manager_viewModel.managerFlatBuildingIDLiveData.postValue(flatOfBuildingId)
                        }

                    }*/
                    if (binding.mainflatSpiner.selectedItemPosition > 0) {
                        flatOfBuildingId =
                            flatOfBuildingHashMapID.get(binding.mainflatSpiner.selectedItem.toString())
                                .toString()
                        println("---flateId$flatOfBuildingId")
                        filterKey = ""
                        months_filter = ""
                        billApprovalList()
                    } else {
                        flatOfBuildingId = "All"
                        filterKey = ""
                        months_filter = ""
                        println("---All$flatOfBuildingId")
                        billApprovalList()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    fun filter() {
        filterBottom = ManagerFilterBillingBottomSheetBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme).apply {
                setContentView(filterBottom.root)
                filterBottom.commonBtn.tv.text = getString(R.string.apply)
                filterBottom.checkBox3.setOnClickListener {
                    filterBottom.checkBox3.isChecked = true
                    filterBottom.checkBox31.isChecked = false
                    filterKey = "Rent"
                }
                filterBottom.checkBox31.setOnClickListener {
                    filterBottom.checkBox31.isChecked = true
                    filterBottom.checkBox3.isChecked = false
                    filterKey = "Service"
                }
                filterBottom.commonBtn.tv.setOnClickListener {
                    billApprovalList()
                    bottomSheetDialog.dismiss()
                }
                filterBottom.tvClose.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

                val genderList = resources.getStringArray(R.array.Months)
                filterBottom.flatSpiner.adapter = ArrayAdapter(
                    this@ApprovalBillingManagerActivity,
                    R.layout.spinner_dropdown_item,
                    genderList
                )
                filterBottom.flatSpiner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long,
                        ) {


                            if (filterBottom.flatSpiner.selectedItemPosition > 0) {
                                months_filter =
                                    filterBottom.flatSpiner.selectedItem.toString().substring(0..2)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }


            }
        bottomSheetDialog.show()
    }
    private fun setAdapter(list: ArrayList<MangerBillPendingListRes.Data.Result> = ArrayList()) {
        binding.rvManagerApproval.layoutManager = LinearLayoutManager(this)
        mAdapter = ManagerApprovalBillsAdapter(this, list, this)
        binding.rvManagerApproval.adapter = mAdapter

    }

    override fun onMaskClick(position: Int, id: String?) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.markAsPaidManager(token, id ?: "")
    }

    override fun onViewReceipt(refno: String, uploadDocument: String) {
        startActivity(
            Intent(this, ViewReceiptManagerActivity::class.java).putExtra(
                "img",
                uploadDocument
            ).putExtra("ref", refno)
        )
        //viewReceiptPop(refno, uploadDocument)
    }

    override fun onReject(billID: String) {
        rejectPopup(billID)
    }

    private fun viewReceiptPop(refno: String, uploadDocument: String) {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.view_receipt_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val tvDone = dialog.findViewById<TextView>(R.id.tvRefferral)
        val img = dialog.findViewById<ImageView>(R.id.imageView20)
        tvDone.text = refno
        if (!uploadDocument.isNullOrEmpty()) {
            img.visibility = View.VISIBLE
            img.loadImagesWithGlideExt(uploadDocument)

        } else {
            img.visibility = View.GONE
        }


        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    private fun rejectPopup(billID: String) {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.reject_manager_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val tvCancel = dialog.findViewById<TextView>(R.id.tvcancelReject)
        val tvReject = dialog.findViewById<TextView>(R.id.tvReject)
        val tvEdit = dialog.findViewById<EditText>(R.id.edtReject)
        tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        tvReject.setOnClickListener {
            dialog.dismiss()
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            viewModel.rejectBillManager(token, billID, tvEdit.text.toString())
        }


        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    override fun onResume() {
        super.onResume()
        if (!flatOfBuildingId.isNullOrEmpty()) {
            billApprovalList()
        }

    }
}