package com.application.intercom.tenant.activity.payment

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.tenantViewModel.TenantSideViewModel
import com.application.intercom.data.model.factory.tenantFactory.TenantSideFactory
import com.application.intercom.data.model.local.tenant.TenantPayNowPostModel
import com.application.intercom.data.model.remote.manager.managerSide.bill.MangerBillPendingListRes
import com.application.intercom.data.model.remote.owner.bill.OwnerUnPaidBillListRes
import com.application.intercom.data.model.remote.tenant.tenantSide.tenantBill.TenantUnPaidListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.tenantRepo.TenantSideRepo
import com.application.intercom.databinding.ActivityTenantSecondPaymentBinding
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.owner.activity.payment_invoice.VerifyingPaymentActivity
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner

class TenantSecondPaymentActivity : BaseActivity<ActivityTenantSecondPaymentBinding>(), AWSListner {

    private var billingId: String = ""
    private var main_img: String = ""
    private var from: String = ""
    private var owner_unpaidList: OwnerUnPaidBillListRes.Data.Result? = null
    private var tenant_paidList: TenantUnPaidListRes.Data.Result? = null
    private var manager_pendingList: MangerBillPendingListRes.Data.Result? = null
    private var bankKey: String = ""
    private var accountNumber: String = ""
    private lateinit var viewModel: TenantSideViewModel
    override fun getLayout(): ActivityTenantSecondPaymentBinding {
        return ActivityTenantSecondPaymentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonUtil.themeSet(this, window)
        from = intent.getStringExtra("from").toString()
        println("----from$from")
        if (from == "owner") {
            owner_unpaidList =
                intent.getSerializableExtra("ownerBillList") as OwnerUnPaidBillListRes.Data.Result
            println("----owner_unpaidList$owner_unpaidList")
            billingId = owner_unpaidList!!._id
            println("----billl$billingId")
            bankKey = intent.getStringExtra("bankKey").toString()
            binding.tvTotalAmount.text = bankKey
            binding.tvBankName.text = bankKey
            println("----bankKey$bankKey")
        } else if (from == "manager") {
            manager_pendingList =
                intent.getSerializableExtra("managerBillList") as MangerBillPendingListRes.Data.Result?
            println("----manager_pendingList$manager_pendingList")
            billingId = manager_pendingList!!._id?:""
            println("----billl$billingId")
            bankKey = intent.getStringExtra("bankKey").toString()
            binding.tvTotalAmount.text = bankKey
            binding.tvBankName.text = bankKey
            println("----bankKey$bankKey")
        } else {
            tenant_paidList =
                intent.getSerializableExtra("tenantBillList") as TenantUnPaidListRes.Data.Result
            println("----tenant_paidList$tenant_paidList")
            billingId = intent.getStringExtra("billingId").toString()
            bankKey = intent.getStringExtra("bankKey").toString()
            println("----bankKey$bankKey")
            binding.tvTotalAmount.text = bankKey
            binding.tvBankName.text = bankKey
            println("----billl$billingId")

        }


        println("----mainbilll$billingId")
        initView()
        listener()
    }

    private fun initView() {
        initialize()
        observer()
        binding.btnLogin.tv.text = getString(R.string.submit)
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(this, R.color.orange))

        ///fetch Data
        if (from.equals("owner")) {
            if (!owner_unpaidList!!.managerInfo.isNullOrEmpty()) {
                binding.tvMonthly.text = owner_unpaidList!!.managerInfo.get(0).accnNumber
                accountNumber = binding.edRefNumber.text.trim().toString()
                binding.tvEveryMonth.text = owner_unpaidList!!.managerInfo.get(0).accnHolder
                //binding.tvTotalAmount.text = owner_unpaidList!!.managerInfo.get(0).payMenthod
                binding.textView160.text = "৳${owner_unpaidList!!.amount}"
                binding.imageView22.setOnClickListener {
                    copy(owner_unpaidList!!.managerInfo.get(0).accnNumber)
                }
                binding.imageView23.setOnClickListener {
                    copy(owner_unpaidList!!.managerInfo.get(0).accnHolder)
                }
            } else {
                binding.tvMonthly.text = owner_unpaidList!!.ownerInfo.get(0).mfsAccnNumber
                accountNumber = binding.edRefNumber.text.trim().toString()
                binding.tvEveryMonth.text = owner_unpaidList!!.ownerInfo.get(0).mfsHolder
                //binding.tvTotalAmount.text = owner_unpaidList!!.ownerInfo.get(0).payMenthod
                binding.textView160.text = "৳${owner_unpaidList!!.amount}"
                binding.imageView22.setOnClickListener {
                    copy(owner_unpaidList!!.managerInfo.get(0).mfsAccnNumber)
                }
                binding.imageView23.setOnClickListener {
                    copy(owner_unpaidList!!.managerInfo.get(0).mfsHolder)
                }
            }
        } else if (from == "manager") {
            if (bankKey == "MFS Payment") {
                binding.tvMonthly.text = prefs.getString(SessionConstants.MFSACCOUNT, "")
                accountNumber = binding.edRefNumber.text.trim().toString()
                binding.tvEveryMonth.text = prefs.getString(SessionConstants.MFSHOLDERNAME, "")
               // binding.tvTotalAmount.text = prefs.getString(SessionConstants.MFS, "")
                binding.textView160.text = "৳${manager_pendingList!!.amount}"
                binding.imageView22.setOnClickListener {
                    copy(prefs.getString(SessionConstants.MFSACCOUNT, ""))
                }
                binding.imageView23.setOnClickListener {
                    copy(prefs.getString(SessionConstants.MFSHOLDERNAME, ""))
                }
            } else {
                binding.tvMonthly.text = prefs.getString(SessionConstants.ACCOUNT, "")
                accountNumber = binding.edRefNumber.text.trim().toString()
                binding.tvEveryMonth.text = prefs.getString(SessionConstants.HOLDERNAME, "")
               // binding.tvTotalAmount.text = prefs.getString(SessionConstants.PAYTYPE, "")
                binding.textView160.text = "৳${manager_pendingList!!.amount}"
                binding.imageView22.setOnClickListener {
                    copy(prefs.getString(SessionConstants.ACCOUNT, ""))
                }
                binding.imageView23.setOnClickListener {
                    copy(prefs.getString(SessionConstants.HOLDERNAME, ""))
                }
            }
            /* if (!manager_pendingList!!.managerInfo.isNullOrEmpty()) {
                 binding.tvMonthly.text = manager_pendingList!!.managerInfo.get(0).accnNumber
                 accountNumber = binding.edRefNumber.text.trim().toString()
                 binding.tvEveryMonth.text = manager_pendingList!!.managerInfo.get(0).accnHolder
                 binding.tvTotalAmount.text = manager_pendingList!!.ownerInfo.get(0).payMenthod
                 binding.textView160.text = "৳${manager_pendingList!!.amount}"
                 binding.imageView22.setOnClickListener {
                     copy(manager_pendingList!!.managerInfo.get(0).accnNumber)
                 }
                 binding.imageView23.setOnClickListener {
                     copy(manager_pendingList!!.managerInfo.get(0).accnHolder)
                 }
             } else {
                 binding.tvMonthly.text = manager_pendingList!!.ownerInfo.get(0).mfsAccnNumber
                 accountNumber = binding.edRefNumber.text.trim().toString()
                 binding.tvEveryMonth.text = manager_pendingList!!.ownerInfo.get(0).mfsHolder
                 binding.tvTotalAmount.text = manager_pendingList!!.ownerInfo.get(0).payMenthod
                 binding.textView160.text = "৳${manager_pendingList!!.amount}"
                 binding.imageView22.setOnClickListener {
                     copy(manager_pendingList!!.managerInfo.get(0).mfsAccnNumber)
                 }
                 binding.imageView23.setOnClickListener {
                     copy(manager_pendingList!!.managerInfo.get(0).mfsHolder)
                 }
             }*/
        } else {
            if (!tenant_paidList!!.managerInfo.isNullOrEmpty()) {
                binding.tvMonthly.text = tenant_paidList!!.managerInfo.get(0).accnNumber
                accountNumber = binding.edRefNumber.text.trim().toString()
                binding.tvEveryMonth.text = tenant_paidList!!.managerInfo.get(0).accnHolder
               // binding.tvTotalAmount.text = tenant_paidList!!.managerInfo.get(0).payMenthod
                binding.textView160.text = "৳${tenant_paidList!!.amount}"
                binding.imageView22.setOnClickListener {
                    copy(tenant_paidList!!.managerInfo.get(0).accnNumber)
                }
                binding.imageView23.setOnClickListener {
                    copy(tenant_paidList!!.managerInfo.get(0).accnHolder)
                }
            } else {
                binding.tvMonthly.text = tenant_paidList!!.ownerInfo.get(0).mfsAccnNumber
                accountNumber = binding.edRefNumber.text.trim().toString()
                binding.tvEveryMonth.text = tenant_paidList!!.ownerInfo.get(0).mfsHolder
               // binding.tvTotalAmount.text = tenant_paidList!!.ownerInfo.get(0).payMenthod
                binding.textView160.text = "৳${tenant_paidList!!.amount}"
                binding.imageView22.setOnClickListener {
                    copy(tenant_paidList!!.ownerInfo.get(0).mfsAccnNumber)
                }
                binding.imageView23.setOnClickListener {
                    copy(tenant_paidList!!.managerInfo.get(0).mfsHolder)
                }
            }
        }


    }

    private fun initialize() {
        val repo = TenantSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, TenantSideFactory(repo))[TenantSideViewModel::class.java]

    }

    private fun payNow() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        if (from == "manager") {
            val model = TenantPayNowPostModel(
                billingId, binding.edRefNumber.text.trim().toString(), main_img, bankKey
            )
            viewModel.payNowManagerList(token, model)
        } else if (from == "owner") {
            val model = TenantPayNowPostModel(
                billingId, binding.edRefNumber.text.trim().toString(), main_img, bankKey
            )
            viewModel.payNowOwnerList(token, model)

        } else {
            val model = TenantPayNowPostModel(
                billingId, binding.edRefNumber.text.trim().toString(), main_img, bankKey
            )
            viewModel.payNowTenantList(token, model)
        }
    }

    private fun observer() {
        viewModel.payNowTenantLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            accountNumber = binding.edRefNumber.text.trim().toString()
                            startActivity(
                                Intent(this, VerifyingPaymentActivity::class.java).putExtra(
                                    "from", from
                                ).putExtra("account", accountNumber)
                            )
                            finish()
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
        viewModel.payNowManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            accountNumber = binding.edRefNumber.text.trim().toString()
                            /*startActivity(
                                Intent(this, VerifyingPaymentActivity::class.java).putExtra(
                                    "from", from
                                ).putExtra("account", accountNumber)
                            )
                            finish()*/
                            startActivity(
                                Intent(this, ManagerMainActivity::class.java).putExtra("from", from)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()
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
        viewModel.payNowOwnerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            accountNumber = binding.edRefNumber.text.trim().toString()
                            startActivity(
                                Intent(this, VerifyingPaymentActivity::class.java).putExtra(
                                    "from", from
                                ).putExtra(
                                    "account", accountNumber
                                )
                            )
                            finish()
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
    }

    private fun listener() {
        binding.imageView24.setOnClickListener {
            showImagePickDialog()
        }
        binding.btnLogin.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            }
            payNow()
        }
        binding.imageView4.setOnClickListener {
            finish()
        }

    }

    private fun validationData(): Boolean {
        if (TextUtils.isEmpty(binding.edRefNumber.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_ref_number), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.ivUpload.drawable == null) {
            Toast.makeText(
                applicationContext, getString(R.string.please_upload_receipt), Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true

    }

    private fun showImagePickDialog() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setMessage("Choose image")
        dialog.setPositiveButton(
            "Gallery"
        ) { _, _ ->
            val intent = Intent(this, TakeImageWithCrop::class.java)
            intent.putExtra("from", "gallery")
            startActivityForResult(intent, TakeImageWithCrop.GALLERY_REQUEST)
        }
        dialog.setNegativeButton(
            "Camera"
        ) { _, _ ->


            val intent = Intent(this, TakeImageWithCrop::class.java)
            intent.putExtra("from", "camera")
            startActivityForResult(intent, TakeImageWithCrop.CAMERA_REQUEST)


        }
        dialog.setNeutralButton(
            "Cancel"
        ) { dialog, which -> dialog.dismiss() }
        dialog.show()
    }
    private var currentImg: Uri = Uri.EMPTY
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TakeImageWithCrop.CAMERA_REQUEST) {
            val path = data?.getStringExtra("filePath")
            if (!path.isNullOrEmpty()) {
                EmpCustomLoader.showLoader(this)
                currentImg = Uri.parse(path)
                EmpCustomLoader.showLoader(this)
                AWSUtils(
                    this, path, this
                )
            }
        } else if (requestCode == TakeImageWithCrop.GALLERY_REQUEST) {
            val path = data?.getStringExtra("filePath")
            if (!path.isNullOrEmpty()) {
                EmpCustomLoader.showLoader(this)
                currentImg = Uri.parse(path)
                AWSUtils(
                    this, path, this
                )
            }
        }
    }

    override fun onAWSLoader(isLoader: Boolean) {
        EmpCustomLoader.showLoader(this)
        binding.ivUpload.visibility = View.INVISIBLE
        binding.imageView24.visibility = View.VISIBLE
    }

    override fun onAWSSuccess(url: String?) {
        if (url != null) {
            main_img = url!!
            println("--------$currentImg")
            binding.ivUpload.visibility = View.VISIBLE
            EmpCustomLoader.hideLoader()
            binding.imageView24.visibility = View.INVISIBLE
            binding.ivUpload.loadImagesWithGlideExt(currentImg.toString())


        }
    }

    override fun onAWSError(error: String?) {
        EmpCustomLoader.hideLoader()
        binding.ivUpload.visibility = View.INVISIBLE
        binding.imageView24.visibility = View.VISIBLE
    }

    override fun onAWSProgress(progress: Int?) {
        EmpCustomLoader.showLoader(this)
        binding.ivUpload.visibility = View.INVISIBLE
        binding.imageView24.visibility = View.VISIBLE
    }

    private fun copy(text: String) {
        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("Copy!!", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Copy!!", Toast.LENGTH_SHORT).show()
    }
}