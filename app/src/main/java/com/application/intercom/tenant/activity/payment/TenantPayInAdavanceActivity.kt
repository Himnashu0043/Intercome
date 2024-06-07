package com.application.intercom.tenant.activity.payment

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.CommonActivity
import com.application.intercom.R
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.tenantViewModel.TenantSideViewModel
import com.application.intercom.data.model.factory.tenantFactory.TenantSideFactory
import com.application.intercom.data.model.local.tenant.TenantPayInAdvancePostModel
import com.application.intercom.data.model.local.tenant.TenantPayNowPostModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.tenantRepo.TenantSideRepo
import com.application.intercom.databinding.ActivityTenantPayInAdavanceBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.tenant.activity.billing.TenantBillingsActivity
import com.application.intercom.tenant.activity.profile.ProfileActivity
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner

class TenantPayInAdavanceActivity : BaseActivity<ActivityTenantPayInAdavanceBinding>(), AWSListner {

    private var count: Int = 0
    private var amout: Int = 0
    private var main_img: String = ""
    private lateinit var viewModel: TenantSideViewModel
    override fun getLayout(): ActivityTenantPayInAdavanceBinding {
        return ActivityTenantPayInAdavanceBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonUtil.themeSet(this, window)
        amout = intent.getIntExtra("amount", 0)
        println("---amount$amout")
        initView()
        listener()
    }

    private fun initView() {
        initialize()
        observer()
        binding.textView160.text = "৳${amout}"
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(this, R.color.orange))
    }

    private fun initialize() {
        val repo = TenantSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, TenantSideFactory(repo))[TenantSideViewModel::class.java]

    }

    private fun payInAdvance() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        val model =
            TenantPayInAdvancePostModel(count, binding.edRefNumber.text.trim().toString(), main_img)
        viewModel.payInAdvanceTenant(token, model)
    }

    private fun observer() {
        viewModel.payInAdvanceTenantLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(this, TenantBillingsActivity::class.java).putExtra(
                                    "from",
                                    "tenant"
                                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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
            payInAdvance()
        }
        binding.imageView4.setOnClickListener {
//            startActivity(
//                Intent(this, ProfileActivity::class.java).putExtra("from", "tenant")
//                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            )
            finish()
        }
        binding.cardplus.setOnClickListener {

            ++count
            binding.textView162.text = "0${count}"
            binding.textView160.text = "৳${count*amout}"
            binding.cardSub.visibility = View.VISIBLE
        }
        binding.cardSub.setOnClickListener {
            --count
            if (count<2){
                binding.cardSub.visibility = View.INVISIBLE
            }
            binding.textView162.text = "0${count}"
            binding.textView160.text = "৳${count*amout}"
        }

    }

    private fun validationData(): Boolean {
        if (binding.edRefNumber.text.trim().toString().length < 4) {
            Toast.makeText(
                applicationContext, "Please Enter Reference Number!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.ivUpload.drawable == null) {
            Toast.makeText(
                applicationContext, "Please Upload Receipt !!", Toast.LENGTH_SHORT
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == TakeImageWithCrop.CAMERA_REQUEST) {
            val path = data?.getStringExtra("filePath")
            if (!path.isNullOrEmpty()) {
                AWSUtils(
                    this, path, this
                )
            }
        } else if (requestCode == TakeImageWithCrop.GALLERY_REQUEST) {
            val path = data?.getStringExtra("filePath")
            if (!path.isNullOrEmpty()) {
                AWSUtils(
                    this, path, this
                )
            }
        }
    }

    override fun onAWSLoader(isLoader: Boolean) {
        binding.pro.visibility = View.VISIBLE
        binding.ivUpload.visibility = View.INVISIBLE
        binding.imageView24.visibility = View.VISIBLE
    }

    override fun onAWSSuccess(url: String?) {
        if (url != null) {
            main_img = url!!
            binding.ivUpload.visibility = View.VISIBLE
            binding.pro.visibility = View.GONE
            binding.imageView24.visibility = View.GONE
            binding.ivUpload.loadImagesWithGlideExt(main_img)


        }
    }

    override fun onAWSError(error: String?) {
        binding.pro.visibility = View.GONE
        binding.ivUpload.visibility = View.INVISIBLE
        binding.imageView24.visibility = View.VISIBLE
    }

    override fun onAWSProgress(progress: Int?) {
        binding.pro.visibility = View.VISIBLE
        binding.ivUpload.visibility = View.INVISIBLE
        binding.imageView24.visibility = View.VISIBLE
    }
}