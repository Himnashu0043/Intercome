package com.application.intercom.user.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.CommonActivity
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.local.UserLoginWithPasswordLocalModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.LoginRepository
import com.application.intercom.databinding.ActivityLoginUsingPasswordBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.user.forgot_password.ForgotPasswordActivity
import com.application.intercom.utils.*
import com.application.intercom.utils.ExoPlayerUtils.Companion.playVideo
import com.google.android.exoplayer2.*

class LoginUsingPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginUsingPasswordBinding
    private lateinit var viewModel: LoginViewModel
    private var url: String = ""
    var devToken: String = ""
    private val MIN_BUFFER_DURATION = 2000
    private val MAX_BUFFER_DURATION = 5000
    private val MIN_PLAYBACK_START_BUFFER = 1500
    private val MIN_PLAYBACK_RESUME_BUFFER = 2000

    var simpleExoPlayer: ExoPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginUsingPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        devToken =
            prefs.getString(AppConstants.DEVICETOKEN, GPSService.mLastLocation?.latitude.toString())
        println("------devToken$devToken")

        setLanguage()
        init()
        observer()
        viewModel.getTutorial()
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.tvterm.setOnClickListener {
            startActivity(Intent(this, TermsOfServiceActivity::class.java))
        }
        binding.tvprivacy.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }
        binding.tvLang.setOnClickListener {
            binding.tvLang.visibility = View.INVISIBLE
            binding.tvENg.visibility = View.VISIBLE
            prefs.put(SessionConstants.LANG, Language.ENGLISH.languageCode)
            println("======test${Language.ENGLISH.languageCode}")
            setLocale(Language.ENGLISH.languageCode)
            finishAffinity()
            startActivity(Intent(this, LoginUsingPasswordActivity::class.java))
        }
        binding.tvENg.setOnClickListener {
            binding.tvENg.visibility = View.INVISIBLE
            binding.tvLang.visibility = View.VISIBLE
            prefs.put(SessionConstants.LANG, Language.BANGLA.languageCode)
            println("======test${Language.BANGLA.languageCode}")
            setLocale(Language.BANGLA.languageCode)
            finishAffinity()
            startActivity(Intent(this, LoginUsingPasswordActivity::class.java))

        }
        binding.btnLogin.tv.text = getString(R.string.login)
        binding.btnLogin.tv.setOnClickListener {
//            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this, MainActivity::class.java))
            if (binding.edtPhone.text.isEmpty()) {
                shortToast(getString(R.string.please_enter_phone_number))
            } else if (binding.edtPassword.text.isEmpty()) {
                shortToast(getString(R.string.please_enter_password))
            } else {
                loginUsingPassword()
            }
        }
        binding.tvLoginUsingOtp.setOnClickListener {
            startActivity(Intent(this, LoginUsingOtpActivity::class.java))
        }
        binding.layoutLoginAsMember.setOnClickListener {
            startActivity(Intent(this, LoginTypeActivity::class.java))
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(
                Intent(this, ForgotPasswordActivity::class.java)
                    .putExtra(AppConstants.FROM, AppConstants.USER)
            )
        }
        binding.ivEyeVisible.setOnClickListener {
//            binding.ivEyeVisible.visibility = View.GONE
//            binding.ivEyeHide.visibility = View.VISIBLE
        }

        binding.ivEyeHide.setOnClickListener {
//            binding.ivEyeHide.visibility = View.GONE
//            binding.ivEyeVisible.visibility = View.VISIBLE
            CommonUtil.showHidePassword(this, binding.edtPassword, binding.ivEyeHide)
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

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun init() {
        val repo = LoginRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, LoginFactory(repo))[LoginViewModel::class.java]
    }

    private fun observer() {
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
                            println("------role${it.data.role}")
                            prefs.put(
                                SessionConstants.AVAILABLE_CONTACTS,
                                it.data.availableContacts
                            )
                            prefs.put(SessionConstants.TOTALS_CONTACTS, it.data.totalContacts)
                            val intent =
                                Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finishAffinity()


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
                                if (it.userType.equals("User")) {
                                    url = it.file
                                } else if (it.userType == "All") {
                                    url = it.file
                                }
                                println("---urloo$url")
                                // initVideoPlayer()
                                simpleExoPlayer = binding.videoView.playVideo(url) {
                                    if (it == Player.STATE_BUFFERING) {
                                        binding.pro.visibility = View.VISIBLE
                                    } else {
                                        binding.pro.visibility = View.GONE
                                    }
                                }
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
    }

    private fun loginUsingPassword() {
        val model = UserLoginWithPasswordLocalModel(
            binding.edtPhone.text.toString().trim(),
            binding.edtPassword.text.toString().trim(),
            AppConstants.USER,
            "En",
            devToken,
            "android",
            prefs.getString(SessionConstants.KLATITUDE,"0.0").toDouble(),
            prefs.getString(SessionConstants.KLONGITUDE,"0.0").toDouble()
        )

        viewModel.userloginUsingPassword(model)
    }

    override fun onResume() {
        super.onResume()
        if(url.isNotEmpty()){
            simpleExoPlayer = binding.videoView.playVideo(url) {
                if (it == Player.STATE_BUFFERING) {
                    binding.pro.visibility = View.VISIBLE
                } else {
                    binding.pro.visibility = View.GONE
                }
            }
        }
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
    }

    private fun setLanguage() {
//        val langList = resources.getStringArray(R.array.LangOne)
//        binding.langSp.adapter =
//            ArrayAdapter(this, R.layout.spinner_dropdown_item, langList)
//        binding.langSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View,
//                position: Int,
//                id: Long,
//            ) {
//                val itemSelected = parent.getItemAtPosition(position)
//                binding.tvLang.setText(itemSelected.toString())
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
    }

    /*private fun initVideoPlayer() {
        val loadControl: LoadControl

        loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                MIN_BUFFER_DURATION,
                MAX_BUFFER_DURATION,
                MIN_PLAYBACK_START_BUFFER,
                MIN_PLAYBACK_RESUME_BUFFER
            )
            .setPrioritizeTimeOverSizeThresholds(true)
            .createDefaultLoadControl()
        val videoUri = Uri.parse(url)
        simpleExoPlayer = ExoPlayer.Builder(this).setLoadControl(loadControl).build()
        binding.videoView.player = simpleExoPlayer
        val mediaItem: MediaItem =
            MediaItem.fromUri(videoUri)
        simpleExoPlayer?.addMediaItem(mediaItem)
        simpleExoPlayer?.prepare()

        simpleExoPlayer?.playWhenReady = true
        simpleExoPlayer?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_BUFFERING) {
                    binding.pro.visibility = View.VISIBLE
                } else {
                    binding.pro.visibility = View.GONE
                }
            }
        })
        simpleExoPlayer?.repeatMode = Player.REPEAT_MODE_ONE
        simpleExoPlayer?.volume = 0f
        binding.videoView.player = simpleExoPlayer

    }*/
}