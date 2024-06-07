package com.application.intercom.user.forgot_password


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.data.model.local.UserForgetPasswordLocalModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.LoginRepository
import com.application.intercom.databinding.ActivityResetPasswordBinding
import com.application.intercom.user.login.LoginFactory
import com.application.intercom.user.login.LoginViewModel
import com.application.intercom.utils.*


class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding

    private lateinit var viewModel: LoginViewModel
    private var mobileNumber: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        observer()
        mobileNumber = intent.getStringExtra(AppConstants.USER_MOBILE_NUMBER) ?: ""
        binding.btnSubmit.tv.text = "Submit"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.btnSubmit.tv.setOnClickListener {

            if (binding.edtPassword.text.toString().trim().isEmpty()) {
                shortToast(getString(R.string.please_enter_new_password))
            } else if (binding.edtConfirmPassword.text.toString().trim().isEmpty()) {
                shortToast(getString(R.string.please_enter_confirm_password))
            } else if (binding.edtPassword.text.toString() != binding.edtConfirmPassword.text.toString()) {
                shortToast(getString(R.string.confrim_passowrd_is_not_matching))
            } else {
                resetPassword()
            }
        }
        binding.ivPasswordEyeVisible.setOnClickListener {
//            binding.ivPasswordEyeVisible.visibility = View.GONE
//            binding.ivPasswordEyeHide.visibility = View.VISIBLE
        }

        binding.ivPasswordEyeHide.setOnClickListener {
//            binding.ivPasswordEyeHide.visibility = View.GONE
//            binding.ivPasswordEyeVisible.visibility = View.VISIBLE
            CommonUtil.showHidePassword(this, binding.edtPassword, binding.ivPasswordEyeHide)
        }
        binding.ivConfirmPasswordEyeVisible.setOnClickListener {
//            binding.ivConfirmPasswordEyeVisible.visibility = View.GONE
//            binding.ivConfirmPasswordEyeHide.visibility = View.VISIBLE
        }

        binding.ivPasswordEyeHide.setOnClickListener {
//            binding.ivConfirmPasswordEyeHide.visibility = View.GONE
//            binding.ivConfirmPasswordEyeVisible.visibility = View.VISIBLE

            CommonUtil.showHidePassword(
                this,
                binding.edtConfirmPassword,
                binding.ivConfirmPasswordEyeHide
            )
        }
    }

    private fun init() {
        val repo = LoginRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, LoginFactory(repo))[LoginViewModel::class.java]
    }

    private fun resetPassword() {
        val model = UserForgetPasswordLocalModel(
            mobileNumber,
            binding.edtConfirmPassword.text.toString().trim()
        )
        viewModel.userForgotPassword(model)
    }

    private fun observer() {

        viewModel.resetPasswordLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(Intent(this, CongratulationsActivity::class.java))
                            finishAffinity()


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
}