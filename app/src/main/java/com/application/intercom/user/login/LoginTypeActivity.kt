package com.application.intercom.user.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.local.UserLoginWithPasswordLocalModel
import com.application.intercom.data.model.local.UserSendPhoneOtpLocalModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.LoginRepository
import com.application.intercom.databinding.ActivityLoginTypeBinding
import com.application.intercom.gatekepper.Main.MainGateKepperActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.user.forgot_password.ForgotPasswordActivity
import com.application.intercom.user.login.listener.TenantOwnerListener
import com.application.intercom.user.otp.OtpVerificationActivity
import com.application.intercom.utils.*
import com.application.intercom.utils.ExoPlayerUtils.Companion.playVideo
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player


class LoginTypeActivity : AppCompatActivity(), TenantOwnerListener {
    private lateinit var binding: ActivityLoginTypeBinding
    private lateinit var viewModel: LoginViewModel
    private var role: String = AppConstants.MEMBER
    private var url: String = ""
    var devToken: String = ""
    private var type: String = ""
    private val MIN_BUFFER_DURATION = 2000
    private val MAX_BUFFER_DURATION = 5000
    private val MIN_PLAYBACK_START_BUFFER = 1500
    private val MIN_PLAYBACK_RESUME_BUFFER = 2000

    var simpleExoPlayer: ExoPlayer? = null
    var simpleExoPlayerManager: ExoPlayer? = null
    var simpleExoPlayerGateKeeper: ExoPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLanguage()
        devToken =
            prefs.getString(
                SessionConstants.DEVICETOKEN,
                GPSService.mLastLocation?.latitude.toString()
            )
        println("-----devLoginUser$devToken")

        init()
        observer()
        CommonUtil.themeSet(this, window)
        viewModel.getTutorial()
        binding.btnLogin.tv.text = getString(R.string.login)
        binding.tvterm.setOnClickListener {
            startActivity(Intent(this, TermsOfServiceActivity::class.java))
        }
        binding.tvprivacy.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.tvLang.setOnClickListener {
            binding.tvLang.visibility = View.INVISIBLE
            binding.tvENg.visibility = View.VISIBLE
            prefs.put(SessionConstants.LANG, Language.ENGLISH.languageCode)
            println("======test${Language.ENGLISH.languageCode}")
            setLocale(Language.ENGLISH.languageCode)
            finishAffinity()
            startActivity(Intent(this, LoginTypeActivity::class.java))
        }
        binding.tvENg.setOnClickListener {
            binding.tvENg.visibility = View.INVISIBLE
            binding.tvLang.visibility = View.VISIBLE
            prefs.put(SessionConstants.LANG, Language.BANGLA.languageCode)
            println("======test${Language.BANGLA.languageCode}")
            setLocale(Language.BANGLA.languageCode)
            finishAffinity()
            startActivity(Intent(this, LoginTypeActivity::class.java))
        }
        /*  binding.tvLoginUsingPassword.setOnClickListener {
              startActivity(Intent(this, LoginTypeUsingPasswordActivity::class.java))
          }*/
        binding.ivEyeHide.setOnClickListener {
            CommonUtil.showHidePassword(this, binding.edtPassword, binding.ivEyeHide)
//            binding.ivEyeHide.visibility = View.GONE
//            binding.ivEyeVisible.visibility = View.VISIBLE
        }
        binding.tvLoginUsingPassword.setOnClickListener {
            if (binding.tvLoginUsingPassword.visibility == View.VISIBLE) {
                binding.tvLoginUsingOtp.visibility = View.VISIBLE
                binding.password.visibility = View.VISIBLE
                binding.tvLoginUsingPassword.visibility = View.INVISIBLE
            }
        }
        binding.tvLoginUsingOtp.setOnClickListener {
            if (binding.tvLoginUsingOtp.visibility == View.VISIBLE) {
                binding.tvLoginUsingPassword.visibility = View.VISIBLE
                binding.tvLoginUsingOtp.visibility = View.INVISIBLE
                binding.password.visibility = View.GONE
            }
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(
                Intent(this, ForgotPasswordActivity::class.java)
                    .putExtra(AppConstants.FROM, role)
            )

        }
        binding.btnLogin.tv.setOnClickListener {
            if (binding.tvLoginUsingOtp.visibility == View.VISIBLE) {
                if (role == AppConstants.MEMBER) {
                    if (binding.edtPhone.text.toString().trim().isEmpty()) {
                        shortToast(getString(R.string.please_enter_phone_number))
                    } else {
                        loginTypeUsingPassword()
                    }
                } else {
                    if (binding.edtPhone.text.toString().trim().isEmpty()) {
                        shortToast(getString(R.string.please_enter_phone_number))
                    } else {
                        binding.edtPhone.hideKeyboard()

                        loginTypeUsingPassword()
                    }
                }
            } else {
                if (role == AppConstants.MEMBER) {
                    if (binding.edtPhone.text.toString().trim().isEmpty()) {
                        shortToast(getString(R.string.please_enter_phone_number))
                    } else {
                        //choosePopup(this)
                        sendPhoneOtp()

                    }
                } else {
                    if (binding.edtPhone.text.toString().trim().isEmpty()) {
                        shortToast(getString(R.string.please_enter_phone_number))
                    } else {
                        binding.edtPhone.hideKeyboard()
                        sendPhoneOtp()
                    }
                }
            }
        }
            binding.layoutLoginOtp.setOnClickListener {
                startActivity(Intent(this, LoginUsingOtpActivity::class.java))
            }


            binding.tvMember.setOnClickListener {
                role = AppConstants.MEMBER
                println("----MEMBER$role")
                loginTypeSelection(binding.tvMember, binding.tvManager, binding.tvGatekeeper)
                /* simpleExoPlayer?.stop()
                 simpleExoPlayer?.release()
                 binding.videoViewManger.visibility = View.INVISIBLE
                 binding.videoViewGateKeep.visibility = View.INVISIBLE
                 binding.ivThumbnailManager.visibility = View.INVISIBLE
                 binding.ivThumbnailGatekeep.visibility = View.INVISIBLE
                 binding.videoView.visibility = View.VISIBLE
                 binding.ivThumbnail.visibility = View.VISIBLE*/
                viewModel.getTutorial()
            }
            binding.tvManager.setOnClickListener {
                role = AppConstants.MANAGER
                println("----MANAGER$role")
                loginTypeSelection(binding.tvManager, binding.tvMember, binding.tvGatekeeper)
                /*simpleExoPlayerManager?.stop()
                simpleExoPlayerManager?.release()
                binding.videoViewManger.visibility = View.VISIBLE
                binding.videoViewGateKeep.visibility = View.INVISIBLE
                binding.ivThumbnailManager.visibility = View.VISIBLE
                binding.ivThumbnailGatekeep.visibility = View.INVISIBLE
                binding.videoView.visibility = View.INVISIBLE
                binding.ivThumbnail.visibility = View.INVISIBLE*/
                viewModel.getTutorial()
            }
            binding.tvGatekeeper.setOnClickListener {
                role = AppConstants.GATEKEEPER
                println("----MANAGER$role")
                loginTypeSelection(binding.tvGatekeeper, binding.tvMember, binding.tvManager)
                /*simpleExoPlayerGateKeeper?.stop()
                simpleExoPlayerGateKeeper?.release()
                binding.videoViewManger.visibility = View.INVISIBLE
                binding.ivThumbnailManager.visibility = View.INVISIBLE
                binding.ivThumbnailGatekeep.visibility = View.VISIBLE
                binding.videoViewGateKeep.visibility = View.VISIBLE
                binding.videoView.visibility = View.INVISIBLE
                binding.ivThumbnail.visibility = View.INVISIBLE*/

                viewModel.getTutorial()
            }
            binding.ivMute.setOnClickListener {
                if (binding.ivMute.visibility == View.VISIBLE) {
                    binding.ivMute.visibility = View.INVISIBLE
                    binding.ivunMute.visibility = View.VISIBLE
                    simpleExoPlayer?.volume = 1f
                }
            }
            binding.ivunMute.setOnClickListener {
                if (binding.ivunMute.visibility == View.VISIBLE) {
                    binding.ivunMute.visibility = View.INVISIBLE
                    binding.ivMute.visibility = View.VISIBLE
                    simpleExoPlayer?.volume = 0f
                }
            }
    }

    private fun init() {
        val repo = LoginRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, LoginFactory(repo))[LoginViewModel::class.java]
    }

    private fun sendPhoneOtp() {
        val model = UserSendPhoneOtpLocalModel(
            binding.edtPhone.text.toString().trim(),
            "+${binding.tvCcp.selectedCountryCode}",
            role,
            Enums.login.toString(),
            devToken,
            "android"

        )
        viewModel.sendPhoneOtp(model)
    }
        private fun loginTypeUsingPassword() {
            val model = UserLoginWithPasswordLocalModel(
                binding.edtPhone.text.toString().trim(),
                binding.edtPassword.text.toString().trim(),
                role,
                "En",
                devToken,
                "android",
                prefs.getString(SessionConstants.KLATITUDE,"0.0").toDouble(),
                prefs.getString(SessionConstants.KLONGITUDE,"0.0").toDouble()

            )


            viewModel.userloginUsingPassword(model)
        }
    private fun observer() {
        viewModel.loginLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            // longToast(it.data.otp)
                            val intent =
                                Intent(this, OtpVerificationActivity::class.java).apply {
                                    putExtra(
                                        AppConstants.USER_MOBILE_NUMBER,
                                        binding.edtPhone.text.toString().trim()
                                    )
                                    putExtra(AppConstants.FROM, role)
                                    putExtra(
                                        AppConstants.COUNTRY_CODE,
                                        binding.tvCcp.selectedCountryCode
                                    )
                                    putExtra(
                                        AppConstants.OTP,
                                        it.data.otp
                                    )
                                }
                            startActivity(intent)


                        } else if (it.status == AppConstants.STATUS_404) {
                            longToast(it.message)
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
        viewModel.getTutorialLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            it.data.forEach {
                               /* if (role == AppConstants.MEMBER) {
                                    if (it.userType.equals("Member")) {
                                        url = it.file
                                        println("======mem$url")
                                        simpleExoPlayer?.stop()
                                        simpleExoPlayer?.release()
                                        simpleExoPlayer = null
                                        simpleExoPlayer = binding.videoView.playVideo(url) {
                                            if (it == Player.STATE_BUFFERING) {
                                                binding.progress.visibility = View.VISIBLE
                                            } else {
                                                binding.progress.visibility = View.GONE
                                            }
                                        }

                                    }
                                } else if (role == AppConstants.MANAGER) {
                                    if (it.userType.equals("Manager")) {
                                        url = it.file
                                        println("======Manager$url")
                                        simpleExoPlayerManager?.stop()
                                        simpleExoPlayerManager?.release()
                                        simpleExoPlayerManager = null
                                        simpleExoPlayerManager =
                                            binding.videoViewManger.playVideo(url) {
                                                if (it == Player.STATE_BUFFERING) {
                                                    binding.progress.visibility = View.VISIBLE
                                                } else {
                                                    binding.progress.visibility = View.GONE
                                                }
                                            }
                                    }
                                } else if (role == AppConstants.GATEKEEPER) {
                                    if (it.userType == "Gatekeeper") {
                                        url = it.file
                                        println("======Gatekeeper$url")
                                        simpleExoPlayerGateKeeper?.stop()
                                        simpleExoPlayerGateKeeper?.release()
                                        simpleExoPlayerGateKeeper = null
                                        simpleExoPlayerGateKeeper =
                                            binding.videoViewGateKeep.playVideo(url) {
                                                if (it == Player.STATE_BUFFERING) {
                                                    binding.progress.visibility = View.VISIBLE
                                                } else {
                                                    binding.progress.visibility = View.GONE
                                                }
                                            }
                                    }
                                } else {
                                    if (it.userType == "All") {
                                        url = it.file
                                        simpleExoPlayer?.stop()
                                        simpleExoPlayer?.release()
                                        simpleExoPlayer = null
                                        simpleExoPlayer = binding.videoView.playVideo(url) {
                                            if (it == Player.STATE_BUFFERING) {
                                                binding.progress.visibility = View.VISIBLE
                                            } else {
                                                binding.progress.visibility = View.GONE
                                            }
                                        }
                                    }
                                }*/
                                if (it.userType == "All"){
                                    url = it.file
                                }
                                simpleExoPlayer = binding.videoView.playVideo(url) {
                                    if (it == Player.STATE_BUFFERING) {
                                        binding.progress.visibility = View.VISIBLE
                                    } else {
                                        binding.progress.visibility = View.GONE
                                    }
                                }
                              /*  if (simpleExoPlayer?.isPlaying == true){
                                    simpleExoPlayer?.stop()
                                    simpleExoPlayer?.release()
                                    simpleExoPlayer = binding.videoView.playVideo(url) {
                                        if (it == Player.STATE_BUFFERING) {
                                            binding.progress.visibility = View.VISIBLE
                                        } else {
                                            binding.progress.visibility = View.GONE
                                        }
                                    }
                                }else{
                                    simpleExoPlayer = binding.videoView.playVideo(url) {
                                        if (it == Player.STATE_BUFFERING) {
                                            binding.progress.visibility = View.VISIBLE
                                        } else {
                                            binding.progress.visibility = View.GONE
                                        }
                                    }
                                }*/

                                println("---url$url")

                            }
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
        viewModel.loginUsingPasswordLiveData.observe(this, Observer {
            when (it) {

                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            prefs.put(SessionConstants.TOKEN, it.data.jwtToken)
                            prefs.put(SessionConstants.ROLE, it.data.role)
                            type = it.data.role
                            prefs.put(
                                SessionConstants.AVAILABLE_CONTACTS,
                                it.data.availableContacts
                            )
                            prefs.put(SessionConstants.TOTALS_CONTACTS, it.data.totalContacts)
                            if (type == AppConstants.TENANT) {
                                val intent = Intent(this, TenantMainActivity::class.java)
                                startActivity(intent)
                                finishAffinity()
                            } else if (type == AppConstants.OWNER) {
                                val intent = Intent(this, OwnerMainActivity::class.java)
                                startActivity(intent)
                                finishAffinity()
                            } else if (type == AppConstants.MANAGER) {
                                val intent = Intent(this, ManagerMainActivity::class.java)
                                startActivity(intent)
                                finishAffinity()
                            } else if (type == AppConstants.GATEKEEPER) {
                                val intent = Intent(this, MainGateKepperActivity::class.java)
                                startActivity(intent)
                                finishAffinity()
                            }


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

    private fun loginTypeSelection(
        activeTab: TextView,
        inActiveTabOne: TextView,
        inActiveTabTwo: TextView,
    ) {
        activeTab.setTextColor(ContextCompat.getColor(this, R.color.orange))
        activeTab.background = ContextCompat.getDrawable(this, R.drawable.bg_login_type_btn)
        inActiveTabOne.setTextColor(ContextCompat.getColor(this, R.color.black))
        inActiveTabOne.setBackgroundResource(0)
        inActiveTabTwo.setTextColor(ContextCompat.getColor(this, R.color.black))
        inActiveTabTwo.setBackgroundResource(0)

    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun choosePopup(listener: TenantOwnerListener) {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.choose_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val tenant = dialog.findViewById<TextView>(R.id.tvNo)
        val owner = dialog.findViewById<TextView>(R.id.tvYes)
        tenant.setOnClickListener {
            dialog.dismiss()
            listener.tenantOwnerListener(AppConstants.TENANT)
//            startActivity(Intent(this, TenantMainActivity::class.java))
        }
        owner.setOnClickListener {
            dialog.dismiss()

            listener.tenantOwnerListener(AppConstants.OWNER)
//            startActivity(Intent(this, OwnerMainActivity::class.java))
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    override fun tenantOwnerListener(role: String) {
        this.role = role
        sendPhoneOtp()
    }

    private fun setLanguage() {
        /*val genderList = resources.getStringArray(R.array.LangOne)
        binding.langSp.adapter =
            ArrayAdapter(this, R.layout.spinner_dropdown_item, genderList)
        binding.langSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                val itemSelected = parent.getItemAtPosition(position)
                binding.tvLang.text = itemSelected.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }*/
    }
    override fun onResume() {
        super.onResume()
//       if (url.isNotEmpty()) {
//           simpleExoPlayer = binding.videoView.playVideo(url) {
//               if (it == Player.STATE_BUFFERING) {
//                   binding.progress.visibility = View.VISIBLE
//               } else {
//                   binding.progress.visibility = View.GONE
//               }
//           }
//       }
        var lang =
            prefs.getString(SessionConstants.LANG, GPSService.mLastLocation?.latitude.toString())
        if (lang == "bn") {
            binding.tvLang.visibility = View.VISIBLE
            binding.tvENg.visibility = View.INVISIBLE
        } else {
            binding.tvENg.visibility = View.VISIBLE
            binding.tvLang.visibility = View.INVISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer?.stop()
        simpleExoPlayer?.release()
        simpleExoPlayer = null

        simpleExoPlayerManager?.stop()
        simpleExoPlayerManager?.release()
        simpleExoPlayerManager = null

        simpleExoPlayerGateKeeper?.stop()
        simpleExoPlayerGateKeeper?.release()
        simpleExoPlayerGateKeeper = null
    }
}