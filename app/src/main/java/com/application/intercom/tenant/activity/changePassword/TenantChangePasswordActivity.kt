package com.application.intercom.tenant.activity.changePassword

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.local.UserChangePasswordLocalModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.LoginRepository
import com.application.intercom.databinding.ActivityTenantChangePasswordBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.user.login.LoginFactory
import com.application.intercom.user.login.LoginViewModel
import com.application.intercom.utils.*


class TenantChangePasswordActivity : BaseActivity<ActivityTenantChangePasswordBinding>() {
    private lateinit var viewModel: LoginViewModel
    override fun getLayout(): ActivityTenantChangePasswordBinding {
        return ActivityTenantChangePasswordBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        observer()
        initView()
        lstnr()
        binding.commonBtn.tv.setOnClickListener {
            if (binding.edRecentPwd.text.toString().trim().isEmpty()) {
                shortToast(getString(R.string.please_enter_old_password))
            } else if (binding.edPwd.text.toString().trim().isEmpty()) {
                shortToast(getString(R.string.please_enter_new_password))
            } else if (binding.edCnfrm.text.toString().trim().isEmpty()) {
                shortToast(getString(R.string.please_enter_confirm_password))
            } else if (binding.edPwd.text.toString() != binding.edCnfrm.text.toString()) {
                shortToast(getString(R.string.confrim_passowrd_is_not_matching))
            } else {
                chnagePassword()
            }

        }
    }

    private fun initView() {
        binding.changePasswordtolbar.tvTittle.text = getString(R.string.change_password)
        binding.commonBtn.tv.text = getString(R.string.submit)

    }

    private fun init() {
        val repo = LoginRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, LoginFactory(repo))[LoginViewModel::class.java]
    }

    private fun chnagePassword() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
           /* GPSService.mLastLocation!!.latitude.toString()*/""
        )
        val model = UserChangePasswordLocalModel(
            binding.edCnfrm.text.toString().trim(),
            binding.edRecentPwd.text.toString().trim()
        )
        viewModel.userChangePassword(token, model)
    }

    private fun observer() {

        viewModel.changePasswordLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            shortToast(it.message)
                            finish()

                        }
                        else if (it.status == AppConstants.STATUS_501) {
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

    private fun lstnr() {
        binding.changePasswordtolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.commonBtn.tv.setOnClickListener {
            finish()
        }

        binding.ivOldEyeHide.setOnClickListener {
            CommonUtil.showHidePassword(this, binding.edRecentPwd, binding.ivOldEyeHide)
        }

        binding.ivNewEyeHide.setOnClickListener {
            CommonUtil.showHidePassword(this, binding.edPwd, binding.ivNewEyeHide)
        }

        binding.ivConfirmEyeHide.setOnClickListener {
            CommonUtil.showHidePassword(this, binding.edCnfrm, binding.ivConfirmEyeHide)
        }
    }
}