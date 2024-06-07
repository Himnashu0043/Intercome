package com.application.intercom.user.profile

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.CompleteProfileViewModel.CompleteProfileViewModel
import com.application.intercom.data.model.factory.CompleteProfileFactory.CompleteProfileFactory
import com.application.intercom.data.model.local.UserUpdateProfileLocalModel
import com.application.intercom.data.repository.CompletProfile.CompleteProfileRepo
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.databinding.ActivityCompleteProfileBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.user.dialog.OtpVerificationDialog
import com.application.intercom.user.dialog.SuccessDialog
import com.application.intercom.utils.*


class CompleteProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompleteProfileBinding
    private lateinit var viewModel: CompleteProfileViewModel
    private var from: String = ""
    private var id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        id = intent.getStringExtra("id").toString()
        Log.d("TAG", "onCreate: ......${id}")
        initView()
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.tvSendOtp.setOnClickListener {
            OtpVerificationDialog.newInstance(
                getString(R.string.tv_register_member),
                getString(R.string.app_name)
            )
                .show(supportFragmentManager, OtpVerificationDialog.TAG)

        }
        binding.btnSubmit.tv.text = "Submit"
        binding.btnSubmit.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            }
//            CommonUtil.showSnackBar(this, "Done")
            setCompleteProfile()


        }
        binding.ivPasswordEyeVisible.setOnClickListener {
        }

        binding.ivPasswordEyeHide.setOnClickListener {
            CommonUtil.showHidePassword(this, binding.edtPassword, binding.ivPasswordEyeHide)
        }
        binding.ivConfirmPasswordEyeVisible.setOnClickListener {
        }

        binding.ivIvConfirmPasswordEyeHide.setOnClickListener {
            CommonUtil.showHidePassword(
                this,
                binding.edtConfirmPassword,
                binding.ivIvConfirmPasswordEyeHide
            )

        }
    }

    private fun initView() {
        initialize()
        observer()

    }

    private fun initialize() {
        val repo = CompleteProfileRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this,
            CompleteProfileFactory(repo)
        )[CompleteProfileViewModel::class.java]


    }

    private fun setCompleteProfile() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
          ""
        )
        val model = UserUpdateProfileLocalModel(
            binding.edtName.text.trim().toString(),
            "",
            binding.edtEmailId.text.trim().toString(),
            binding.edtCity.text?.trim().toString(),
            "28.6258631".toDouble(),
            "77.3747904".toDouble(),
            binding.edtConfirmPassword.text?.trim().toString()
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
                            if (from.equals("user_parking_details")) {
                                SuccessDialog.newInstance(
                                    getString(R.string.tv_register_member),
                                    getString(R.string.app_name),
                                    "complete_profile_parking",
                                    id

                                )
                                    .show(supportFragmentManager, SuccessDialog.TAG)
                            } else if (from.equals("tenant_parking_details")) {
                                SuccessDialog.newInstance(
                                    getString(R.string.tv_register_member),
                                    getString(R.string.app_name),
                                    "complete_profile_parking",
                                    id

                                )
                                    .show(supportFragmentManager, SuccessDialog.TAG)
                            } else if (from.equals("ownerSide_property")) {
                                SuccessDialog.newInstance(
                                    getString(R.string.tv_register_member),
                                    getString(R.string.app_name),
                                    "complete_ownerSide_property",
                                    id

                                )
                                    .show(supportFragmentManager, SuccessDialog.TAG)
                            } else {
                                SuccessDialog.newInstance(
                                    getString(R.string.tv_register_member),
                                    getString(R.string.app_name),
                                    "complete_profile_property",
                                    id
                                )
                                    .show(supportFragmentManager, SuccessDialog.TAG)
                            }

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

    private fun validationData(): Boolean {
        val createpassword: String = binding.edtPassword.text?.trim().toString()
        val cnfrmPassword: String = binding.edtConfirmPassword.text?.trim().toString()
        if (binding.edtName.text.trim().toString().length < 4) {
            Toast.makeText(
                applicationContext, "Please Enter Name!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edtEmailId.text.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Email Id!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (!Validator.isValidEmail(
                CommonUtil.getProperText(
                    binding.edtEmailId
                )
            )
        ) {
            Toast.makeText(this, "Invalid Email Id!!", Toast.LENGTH_SHORT).show()
            return false

        } else if (TextUtils.isEmpty(binding.edtCity.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter City!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edtPassword.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Password!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edtPassword.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Confirm Password!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (!createpassword.equals(cnfrmPassword)) {
            Toast.makeText(this, "Confirm Password does not Match!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true

    }
}