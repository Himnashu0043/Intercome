package com.application.intercom.tenant.activity.setting

import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.LoginRepository
import com.application.intercom.databinding.ActivityTenantSettingBinding
import com.application.intercom.gatekepper.Main.MainGateKepperActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.tenant.activity.changePassword.TenantChangePasswordActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.user.login.LoginFactory
import com.application.intercom.user.login.LoginUsingOtpActivity
import com.application.intercom.user.login.LoginViewModel
import com.application.intercom.utils.*
import com.google.firebase.crashlytics.internal.common.CommonUtils
import java.util.*


class TenantSettingActivity : BaseActivity<ActivityTenantSettingBinding>() {

    private lateinit var viewModel: LoginViewModel
    private var role: String = ""
    var devToken: String = ""
    override fun getLayout(): ActivityTenantSettingBinding {
        return ActivityTenantSettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        initView()
        lstnr()
        observer()
        devToken =
            prefs.getString(
                SessionConstants.DEVICETOKEN,
                GPSService.mLastLocation?.latitude.toString()
            )
        println("----SettingUser$devToken")
        role = prefs.getString(SessionConstants.ROLE, "")
        println("______-$role")
        var lang =
            prefs.getString(SessionConstants.LANG, GPSService.mLastLocation?.latitude.toString())
        prefs.put(SessionConstants.LANG, lang)
        println("=====$lang")
        if (lang == "bn") {
            binding.radioMobile.isChecked = true
            binding.radioEmail.isChecked = false
        } else {
            binding.radioEmail.isChecked = true
            binding.radioMobile.isChecked = false
        }
    }

    private fun init() {
        val repo = LoginRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, LoginFactory(repo))[LoginViewModel::class.java]
    }

    private fun observer() {
        viewModel.logoutLiveData.observe(this, Observer {
            when (it) {

                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            prefs.clearData()
                            ExoPlayerUtils.clearCache(this)
                            val intent = Intent(this, LoginUsingOtpActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finishAffinity()


                        } else if (it.status == 503) {
                            prefs.clearData()
                            ExoPlayerUtils.clearCache(this)
                            val intent = Intent(this, LoginUsingOtpActivity::class.java)
                            startActivity(intent)
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

    private fun initView() {
        binding.settingToolbar.tvTittle.text = getString(R.string.settings)

    }

    private fun lstnr() {
        binding.cardView41.setOnClickListener {
            startActivity(Intent(this, TenantChangePasswordActivity::class.java))
        }
        binding.cardView411.setOnClickListener {
            logoutPopup()
        }
        binding.settingToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.radioEmail.setOnClickListener {
            binding.radioEmail.isChecked = true
            binding.radioMobile.isChecked = false
            prefs.put(SessionConstants.LANG, Language.ENGLISH.languageCode)
            setLocale(Language.ENGLISH.languageCode)
            if (role == "owner") {
                finishAffinity()
                startActivity(Intent(this, OwnerMainActivity::class.java))
            } else if (role == "tenant") {
                finishAffinity()
                startActivity(Intent(this, TenantMainActivity::class.java))
            } else if (role == "manager") {
                finishAffinity()
                startActivity(Intent(this, ManagerMainActivity::class.java))
            } else if (role == "gatekeeper") {
                finishAffinity()
                startActivity(Intent(this, MainGateKepperActivity::class.java))
            } else {
                finishAffinity()
                startActivity(Intent(this, MainActivity::class.java))
            }

        }
        binding.radioMobile.setOnClickListener {
            binding.radioMobile.isChecked = true
            binding.radioEmail.isChecked = false
            prefs.put(SessionConstants.LANG, Language.BANGLA.languageCode)
            setLocale(Language.BANGLA.languageCode)
            /*  finishAffinity()
              startActivity(Intent(this,OwnerMainActivity::class.java))*/
            if (role == "owner") {
                finishAffinity()
                startActivity(Intent(this, OwnerMainActivity::class.java))
            } else if (role == "tenant") {
                finishAffinity()
                startActivity(Intent(this, TenantMainActivity::class.java))
            } else if (role == "manager") {
                finishAffinity()
                startActivity(Intent(this, ManagerMainActivity::class.java))
            } else if (role == "gatekeeper") {
                finishAffinity()
                startActivity(Intent(this, MainGateKepperActivity::class.java))
            } else {
                finishAffinity()
                startActivity(Intent(this, MainActivity::class.java))
            }

        }

    }

    private fun logoutApiCall() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.userLogout(token,devToken)
    }

    private fun logoutPopup() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.logout_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val logout = dialog.findViewById<TextView>(R.id.tvLogout)
        val cancel = dialog.findViewById<TextView>(R.id.tvcancel)
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        logout.setOnClickListener {
            //prefs.clearData()
            dialog.dismiss()
            logoutApiCall()


        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

}