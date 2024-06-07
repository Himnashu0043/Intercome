package com.application.intercom.tenant.activity.profile

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.CompleteProfileViewModel.CompleteProfileViewModel
import com.application.intercom.data.model.factory.CompleteProfileFactory.CompleteProfileFactory
import com.application.intercom.data.model.local.UserUpdateProfileLocalModel
import com.application.intercom.data.repository.CompletProfile.CompleteProfileRepo
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.databinding.ActivityEditProfileBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.helper.TakeImageWithCrop.CAMERA_REQUEST
import com.application.intercom.helper.TakeImageWithCrop.GALLERY_REQUEST
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner

class EditProfileActivity : AppCompatActivity(), AWSListner {
    lateinit var binding: ActivityEditProfileBinding
    private var imagePath: String = ""
    private lateinit var viewModel: CompleteProfileViewModel
    private var from: String = ""
    private var email: String = ""
    private var number: String = ""
    private var name: String = ""
    private var url: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        url = intent.getStringExtra("url").toString()
        name = intent.getStringExtra("name").toString()
        email = intent.getStringExtra("email").toString()
        number = intent.getStringExtra("number").toString()
        initView()
        lstnr()
        CommonUtil.themeSet(this, window)
        if (name.isNullOrEmpty()) {
            binding.imageView6.visibility = View.VISIBLE
        } else {
            if (intent != null && intent.getBooleanExtra("userFag", true)) {
                binding.edName.setText(name)
                if (email !="null"){
                    binding.edEmail.setText(email)
                }

                binding.ivchange.loadImagesWithGlideExt(url)
                binding.imageView6.visibility = View.INVISIBLE
            }
        }
//        if (intent != null && intent.getBooleanExtra("userFag", true)) {
//            binding.edName.setText(name)
//            binding.edEmail.setText(email)
//            binding.ivchange.loadImagesWithGlideExt(url)
//            binding.imageView6.visibility = View.INVISIBLE
//        } else {
//            binding.imageView6.visibility = View.VISIBLE
//        }
    }

    private fun initView() {
        initialize()
        observer()
        binding.commonBtn.tv.text = getString(R.string.save_change)

    }

    private fun lstnr() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.commonBtn.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            }
            updateProfile()
//            startActivity(
//                Intent(
//                    this,
//                    ProfileActivity::class.java
//                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            )
//            finish()
        }
        binding.ivchange.setOnClickListener {
            showImagePickDialog()
        }

    }

    private fun initialize() {
        val repo = CompleteProfileRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this, CompleteProfileFactory(repo)
        )[CompleteProfileViewModel::class.java]


    }

    private fun updateProfile() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
          /*  GPSService.mLastLocation!!.latitude.toString()*/""
        )
        val model = UserUpdateProfileLocalModel(
            binding.edName.text.trim().toString(),
            imagePath,
            binding.edEmail.text.trim().toString(),
            "",
            "28.6258631".toDouble(),
            "77.3747904".toDouble(),
            ""
        )
        viewModel.completeProfile(token, model)

    }

    private fun observer() {
        viewModel.completeProfileLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
//                            startActivity(
//                                Intent(
//                                    this, ProfileActivity::class.java
//                                ).putExtra("from", from).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            )
                            finish()
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else {
                            CommonUtil.showSnackBar(this, "Something went wrong!!")
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

    private fun showImagePickDialog() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setMessage("Choose image")
        dialog.setPositiveButton(
            "Gallery"
        ) { _, _ ->
            val intent = Intent(this, TakeImageWithCrop::class.java)
            intent.putExtra("from", "gallery")
            startActivityForResult(intent, GALLERY_REQUEST)
        }
        dialog.setNegativeButton(
            "Camera"
        ) { _, _ ->


            val intent = Intent(this, TakeImageWithCrop::class.java)
            intent.putExtra("from", "camera")
            startActivityForResult(intent, CAMERA_REQUEST)


        }
        dialog.setNeutralButton(
            "Cancel"
        ) { dialog, which -> dialog.dismiss() }
        dialog.show()
    }

    private var currentImagePath: Uri = Uri.EMPTY
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val path = data?.getStringExtra("filePath")
        if (resultCode == RESULT_OK) {
            if (!path.isNullOrEmpty()) {
                currentImagePath = Uri.parse(path)
                EmpCustomLoader.showLoader(this)
                AWSUtils(
                    this, path, this
                )
            }
        } else if (requestCode == GALLERY_REQUEST) {
            if (!path.isNullOrEmpty()) {
                currentImagePath = Uri.parse(path)
                EmpCustomLoader.showLoader(this)
                AWSUtils(
                    this, path, this
                )
            }
        }
    }

    private fun validationData(): Boolean {
        if (binding.edName.text.trim().toString().length < 4) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_name), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edEmail.text.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_email_id), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (!Validator.isValidEmail(
                CommonUtil.getProperText(
                    binding.edEmail
                )
            )
        ) {
            Toast.makeText(this, getString(R.string.invalid_email_id), Toast.LENGTH_SHORT).show()
            return false

        }
        return true

    }

    override fun onAWSLoader(isLoader: Boolean) {
        EmpCustomLoader.showLoader(this)
    }

    override fun onAWSSuccess(url: String?) {
        if (url != null) {
            imagePath = url!!
            EmpCustomLoader.hideLoader()
            binding.imageView6.visibility = View.INVISIBLE
            //binding.ivchange.loadImagesWithGlideExt(url)
            binding.ivchange.setImageURI(currentImagePath)
        } else {
            binding.imageView6.visibility = View.VISIBLE
        }
    }

    override fun onAWSError(error: String?) {
        EmpCustomLoader.hideLoader()
        Log.e("error", error ?: "")
    }

    override fun onAWSProgress(progress: Int?) {
        EmpCustomLoader.showLoader(this)
        Log.e("progress", progress!!.toString())
    }
}