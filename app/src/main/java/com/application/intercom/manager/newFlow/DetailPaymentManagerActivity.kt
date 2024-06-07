package com.application.intercom.manager.newFlow

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.newFlow.PayUnPaidManagerPostModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityDetailPaymentManagerBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.manager.bills.GenerateInvoiceActivity
import com.application.intercom.manager.newFlow.expenses.ManagerExpensesActivity
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner

class DetailPaymentManagerActivity : AppCompatActivity(), AWSListner {
    lateinit var binding: ActivityDetailPaymentManagerBinding
    private var bankKey: String = ""
    private var expensesId: String = ""
    private var amout: Int = 0
    private lateinit var viewModel: ManagerSideViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPaymentManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bankKey = intent.getStringExtra("bankKey") ?: ""
        expensesId = intent.getStringExtra("expenseId") ?: ""
        amout = intent.getIntExtra("amount", 0)
        println("======$amout")
        CommonUtil.themeSet(this, window)
        initView()
        lstnr()
    }

    private fun initView() {
        initialize()
        observer()
        if (bankKey == "MFS") {
            binding.textView160.text = "৳ $amout"
            binding.tvBankName.text = bankKey
            binding.tvMonthly.text = prefs.getString(SessionConstants.MFSACCOUNT, "")
            binding.tvEveryMonth.text = prefs.getString(SessionConstants.MFSHOLDERNAME, "")
            binding.tvTotalAmount.text = prefs.getString(SessionConstants.MFS, "")
        } else {
            binding.tvBankName.text = bankKey
            binding.textView160.text = "৳ $amout"
            binding.tvMonthly.text = prefs.getString(SessionConstants.ACCOUNT, "")
            binding.tvEveryMonth.text = prefs.getString(SessionConstants.HOLDERNAME, "")
            binding.tvTotalAmount.text = prefs.getString(SessionConstants.PAYTYPE, "")
        }
        binding.btnLogin.tv.text = getString(R.string.submit)
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(this, R.color.orange))
    }

    private fun lstnr() {
        binding.imageView4.setOnClickListener {
            finish()
        }
        binding.imageView22.setOnClickListener {
            if (bankKey == "MFS") {
                copy(prefs.getString(SessionConstants.MFSACCOUNT, ""))
            } else {
                copy(prefs.getString(SessionConstants.ACCOUNT, ""))
            }
        }
        binding.imageView23.setOnClickListener {
            if (bankKey == "MFS") {
                copy(prefs.getString(SessionConstants.MFSHOLDERNAME, ""))
            } else {
                copy(prefs.getString(SessionConstants.HOLDERNAME, ""))
            }
        }
        binding.imageView24.setOnClickListener {
            showImagePickDialog()
        }
        binding.btnLogin.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            }
            payUnPaidManager()
        }
    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

    private fun payUnPaidManager() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        var model = PayUnPaidManagerPostModel(
            expensesId,
            bankKey,
            binding.edRefNumber.text.trim().toString(),
            main_img.toString()
        )
        viewModel.payUnPaidManager(token, model)

    }

    private fun observer() {
        viewModel.payUnPaidManagerLiveData.observe(this, androidx.lifecycle.Observer {
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
                                    this,
                                    ManagerExpensesActivity::class.java
                                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                            finish()
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

    private var main_img: Uri = Uri.EMPTY
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TakeImageWithCrop.CAMERA_REQUEST) {
            val path = data?.getStringExtra("filePath")
            if (!path.isNullOrEmpty()) {
                EmpCustomLoader.showLoader(this)
                main_img = Uri.parse(path)
                AWSUtils(
                    this, path, this
                )
            }
        } else if (requestCode == TakeImageWithCrop.GALLERY_REQUEST) {
            val path = data?.getStringExtra("filePath")
            if (!path.isNullOrEmpty()) {
                EmpCustomLoader.showLoader(this)
                main_img = Uri.parse(path)
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
            binding.ivUpload.visibility = View.VISIBLE
            EmpCustomLoader.hideLoader()
            binding.imageView24.visibility = View.INVISIBLE
            binding.ivUpload.loadImagesWithGlideExt(main_img.toString())


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