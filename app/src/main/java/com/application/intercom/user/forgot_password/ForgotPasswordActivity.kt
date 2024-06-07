package com.application.intercom.user.forgot_password

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.data.model.local.UserSendForgotPhoneOtpLocalModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.LoginRepository
import com.application.intercom.databinding.ActivityForgotPasswordBinding
import com.application.intercom.user.login.LoginFactory
import com.application.intercom.user.login.LoginViewModel
import com.application.intercom.user.otp.OtpVerificationActivity
import com.application.intercom.utils.*


class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding:ActivityForgotPasswordBinding
    private lateinit var viewModel: LoginViewModel
    private var from: String = AppConstants.USER
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra(AppConstants.FROM) ?: ""
        Log.d("AAA", "onCreate: $from")
        init()
        observer()
        binding.btnSubmit.tv.text = getString(R.string.submit)
        binding.btnSubmit.tv.setOnClickListener {
            if (binding.edtPhone.text.toString().trim().isEmpty()) {
                shortToast(getString(R.string.please_enter_phone_number))
            }/*else if (CommonUtil.isValidMobile(binding.edtPhone.text.toString().trim())) {
                shortToast("Please valid phone number")
            }*/
            else {
                forgotPasswordSendOtp()
            }
//            startActivity(Intent(this,ResetPasswordActivity::class.java))
        }
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }

    }

    private fun init() {
        val repo = LoginRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, LoginFactory(repo))[LoginViewModel::class.java]
    }

    private fun forgotPasswordSendOtp() {
        val model = UserSendForgotPhoneOtpLocalModel(
            binding.edtPhone.text.toString().trim(),
            /*"+91"*/
            binding.tvCcp.selectedCountryCode,
            from,
            "forget",
        )
        viewModel.sendForgotPasswordPhoneOtp(model)
    }

    private fun observer() {

        viewModel.forgetPasswordPhoneOtpLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
//                            longToast(it.data.otp)
                            val intent =
                                Intent(this, OtpVerificationActivity::class.java).apply {
                                    putExtra(
                                        AppConstants.USER_MOBILE_NUMBER,
                                        binding.edtPhone.text.toString().trim()
                                    )
                                   // putExtra(AppConstants.COUNTRY_CODE, "91")
                                    putExtra(AppConstants.FROM, AppConstants.FORGOT_PASSWORD)
                                    putExtra(
                                        AppConstants.OTP,
                                        it.data.otp
                                    )
                                    putExtra(
                                        AppConstants.COUNTRY_CODE,
                                        binding.tvCcp.selectedCountryCode
                                    )
                                }
                            startActivity(intent)


                        } else if (it.status == AppConstants.STATUS_404) {
                            shortToast(it.message)

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