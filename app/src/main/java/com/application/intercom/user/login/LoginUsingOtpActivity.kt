package com.application.intercom.user.login

import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.local.UserLoginWithPasswordLocalModel
import com.application.intercom.data.model.local.UserSendPhoneOtpLocalModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.LoginRepository
import com.application.intercom.databinding.ActivityLoginUsingOtpBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.user.forgot_password.ForgotPasswordActivity
import com.application.intercom.user.otp.OtpVerificationActivity
import com.application.intercom.utils.*
import com.application.intercom.utils.ExoPlayerUtils.Companion.playVideo
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.messaging.FirebaseMessaging


class LoginUsingOtpActivity : AppCompatActivity(), GPSService.OnLocationUpdate {
    private lateinit var binding: ActivityLoginUsingOtpBinding
    private lateinit var viewModel: LoginViewModel
    private var url: String = ""
    var devToken: String = ""
    private var deviceToken: String? = ""
    val RESULT_PERMISSION_LOCATION = 1
    private val PERMISSION_LOCATION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var hasNotificationPermissionGranted = false

//    private val MIN_BUFFER_DURATION = 2000
//
//    //Max Video you want to buffer during PlayBack
//    private val MAX_BUFFER_DURATION = 5000
//
//    //Min Video you want to buffer before start Playing it
//    private val MIN_PLAYBACK_START_BUFFER = 1500
//
//    //Min video You want to buffer when user resumes video
//    private val MIN_PLAYBACK_RESUME_BUFFER = 2000

    var simpleExoPlayer: ExoPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginUsingOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
         checkLocationPermissions()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            deviceToken = task.result

            Log.d(ContentValues.TAG, "DeviceToken==>>: ${deviceToken} ")

            getSharedPreferences(AppConstants.PREF_NAME, MODE_PRIVATE).edit {
                prefs.put(SessionConstants.DEVICETOKEN, deviceToken!!)
                //putString(AppConstants.DEVICETOKEN, deviceToken)
                apply()
            }
//            prefs.put(SessionConstants.DEVICETOKEN, deviceToken!!)
        })
        CommonUtil.themeSet(this, window)
        devToken =
            prefs.getString(
                SessionConstants.DEVICETOKEN,
                GPSService.mLastLocation?.latitude.toString()
            )
        println("-----dev$devToken")



        /*setLanguage()*/
        init()
        observer()
        viewModel.getTutorial()



        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermissionLauncher.launch(POST_NOTIFICATIONS)
        } else {
            hasNotificationPermissionGranted = true
        }
        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermissionLauncher.launch(POST_NOTIFICATIONS)
        } else {
            hasNotificationPermissionGranted = true
        }
        binding.tvLang.setOnClickListener {
            binding.tvLang.visibility = View.INVISIBLE
            binding.tvENg.visibility = View.VISIBLE
            prefs.put(SessionConstants.LANG, Language.ENGLISH.languageCode)
            println("======test${Language.ENGLISH.languageCode}")
            setLocale(Language.ENGLISH.languageCode)
            finishAffinity()
            startActivity(Intent(this, LoginUsingOtpActivity::class.java))
        }
        binding.tvENg.setOnClickListener {
            binding.tvENg.visibility = View.INVISIBLE
            binding.tvLang.visibility = View.VISIBLE
            prefs.put(SessionConstants.LANG, Language.BANGLA.languageCode)
            println("======test${Language.BANGLA.languageCode}")
            setLocale(Language.BANGLA.languageCode)
            finishAffinity()
            startActivity(Intent(this,LoginUsingOtpActivity::class.java))

        }
        binding.tvterm1.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    TermsOfServiceActivity::class.java
                )/*.putExtra("from", "terms")*/
            )
        }
        binding.tvprivacy.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }
        /* binding.ivPlay.setOnClickListener {
             binding.ivPlay.visibility = View.GONE
             binding.ivThumbnail.visibility = View.INVISIBLE
             binding.videoView.visibility = View.VISIBLE

         }*/

        binding.btnLogin.tv.text = getString(R.string.login)
        binding.tvLoginUsingPassword.setOnClickListener {
            if (binding.tvLoginUsingPassword.visibility == View.VISIBLE) {
                binding.tvLoginUsingOtp.visibility = View.VISIBLE
                binding.tvLoginUsingPassword.visibility = View.INVISIBLE
                binding.singlePassword.visibility = View.VISIBLE
            }
        }
        binding.tvLoginUsingOtp.setOnClickListener {
            if (binding.tvLoginUsingOtp.visibility == View.VISIBLE) {
                binding.tvLoginUsingPassword.visibility = View.VISIBLE
                binding.tvLoginUsingOtp.visibility = View.INVISIBLE
                binding.singlePassword.visibility = View.GONE
            }
        }
        binding.btnLogin.tv.setOnClickListener {
            val mobileNumber = binding.edtPhone.text.toString()
            val countrycode = binding.tvCcp.selectedCountryCode
            // validate here
            println("======$countrycode")
            if (binding.tvLoginUsingOtp.visibility == View.VISIBLE) {
                if (binding.edtPhone.text.isEmpty()) {
                    shortToast(getString(R.string.please_enter_phone_number))
                } else if (binding.edtPassword.text.isEmpty()) {
                    shortToast(getString(R.string.please_enter_password))
                } else {
                    loginUsingPassword()
                }
            } else {
                val userLogin = UserSendPhoneOtpLocalModel(
                    mobileNumber,
                    "+${countrycode}",
                    AppConstants.USER,
                    Enums.login.toString(),
                    devToken,
                    "android"
                )
                viewModel.sendPhoneOtp(userLogin)
            }


        }

//        binding.tvLoginUsingPassword.setOnClickListener {
//            startActivity(Intent(this, LoginUsingPasswordActivity::class.java))
//        }
        binding.layoutLoginAsMember.setOnClickListener {
            startActivity(Intent(this, LoginTypeActivity::class.java))
        }
        binding.ivEyeHide.setOnClickListener {
            CommonUtil.showHidePassword(this, binding.edtPassword, binding.ivEyeHide)
        }

        /*val aManager = getSystemService(AUDIO_SERVICE) as AudioManager
        aManager.setStreamMute(AudioManager.STREAM_MUSIC, false)*/
        binding.ivMute.setOnClickListener {
            if (binding.ivMute.visibility == View.VISIBLE) {
                binding.ivMute.visibility = View.INVISIBLE
                binding.ivunMute.visibility = View.VISIBLE

                simpleExoPlayer?.volume = 1f
                /*val aManager = getSystemService(AUDIO_SERVICE) as AudioManager
                aManager.setStreamMute(AudioManager.STREAM_MUSIC, false)*/
            }
        }
        binding.ivunMute.setOnClickListener {
            if (binding.ivunMute.visibility == View.VISIBLE) {
                binding.ivunMute.visibility = View.INVISIBLE
                binding.ivMute.visibility = View.VISIBLE
                /*val aManager = getSystemService(AUDIO_SERVICE) as AudioManager
                aManager.setStreamMute(AudioManager.STREAM_MUSIC, true)*/
                simpleExoPlayer?.volume = 0f
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(
                Intent(this, ForgotPasswordActivity::class.java)
                    .putExtra(AppConstants.FROM, AppConstants.USER)
            )
        }

    }

    val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                            showNotificationPermissionRationale()
                        } else {
                            showSettingDialog()
                        }
                    }
                }
            } else {
//                Toast.makeText(applicationContext, "notification permission granted", Toast.LENGTH_SHORT)
//                    .show()
            }
        }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun localization(resource: Resources) {
        binding.edtPhone.setHint(resource.getString(R.string.enter_your_mobile_number))
        binding.tvLoginUsingPassword.text = resource.getString(R.string.login_using_password)
        binding.tvLoginAsMember.text = resource.getString(R.string.login_as_a_member)
        binding.textView163.text = resource.getString(R.string.by_login_you_agreed_to_our)
        binding.tvterm1.text = resource.getString(R.string.terms_amp_conditions2)
        binding.tvand.text = resource.getString(R.string.and)
        binding.tvprivacy.text = resource.getString(R.string.privacy_policy)
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    private fun init() {
        val repo = LoginRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, LoginFactory(repo))[LoginViewModel::class.java]
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
                            val intent = Intent(this, OtpVerificationActivity::class.java).apply {
                                putExtra(
                                    AppConstants.USER_MOBILE_NUMBER,
                                    binding.edtPhone.text.toString().trim()
                                )
                                putExtra(AppConstants.FROM, AppConstants.USER)
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
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
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
//                                    url = it.file.replace(" ", "")
                                    url = it.file
                                } else if (it.userType == "All") {
                                    url = it.file
                                }
                                //initVideoPlayer()
                                simpleExoPlayer = binding.videoView.playVideo(url) {
                                    if (it == Player.STATE_BUFFERING) {
                                        binding.progress.visibility = View.VISIBLE
                                    } else {
                                        binding.progress.visibility = View.GONE
                                    }
                                }
                                println("---url$url")

                            }
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
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
    }

    private fun setLanguage() {
//        val genderList = resources.getStringArray(com.application.intercom.R.array.LangOne)
//        binding.langSp.adapter =
//            ArrayAdapter(this, com.application.intercom.R.layout.spinner_dropdown_item, genderList)
//
//        binding.langSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View,
//                position: Int,
//                id: Long,
//            ) {
//                val itemSelected = parent.getItemAtPosition(position)
//                binding.tvLang.text = itemSelected.toString()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
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
    private fun checkLocationPermissions() {
        if (hasAccessFineLocationPermissions(this@LoginUsingOtpActivity)) {
            if (CommonUtil.checkGPS(this@LoginUsingOtpActivity)) {
                Log.d(ContentValues.TAG, getString(R.string.gps_enabled))
                GPSService(this, this)
                // handler()
            } else {
                buildAlertMessageNoGps(getString(com.application.intercom.R.string.enable_gps))
            }
        } else {
            requestLocationPermissions(this@LoginUsingOtpActivity)
        }
    }

    fun hasAccessFineLocationPermissions(context: Context?): Boolean {
        return (ContextCompat.checkSelfPermission(
            context!!,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    fun requestLocationPermissions(activity: Activity?) {
        ActivityCompat.requestPermissions(
            activity!!,
            PERMISSION_LOCATION,
            RESULT_PERMISSION_LOCATION
        )
    }

    private fun buildAlertMessageNoGps(message: String) {
        val builder = AlertDialog.Builder(this@LoginUsingOtpActivity)
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(
                getString(R.string.yes)
            ) { dialog, id ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        val alert = builder.create()
        alert.getWindow()?.setBackgroundDrawable(resources?.let {
            ColorDrawable(
                it.getColor(
                    R.color.white
                )
            )
        })
        alert.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RESULT_PERMISSION_LOCATION) {
            if (hasAccessFineLocationPermissions(this@LoginUsingOtpActivity)) {
                if (CommonUtil.checkGPS(this@LoginUsingOtpActivity)) {
                    GPSService(this@LoginUsingOtpActivity, this)
                    // handler()
                } else {
                    buildAlertMessageNoGps(getString(com.application.intercom.R.string.enable_gps))
                }
            } else {
                requestLocationPermissions(this@LoginUsingOtpActivity)
            }
        }
    }

    override fun onLocationUpdate(latitude: Double, longitude: Double) {

    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer?.stop()
        simpleExoPlayer?.release()
        simpleExoPlayer = null
    }

    override fun onResume() {
        super.onResume()
        if (hasAccessFineLocationPermissions(this@LoginUsingOtpActivity)) {
            if (CommonUtil.checkGPS(this@LoginUsingOtpActivity)) {
                GPSService(this@LoginUsingOtpActivity, this)
                checkLocationPermissions()
            }
        }

        if(url.isNotEmpty()){
            simpleExoPlayer = binding.videoView.playVideo(url) {
                if (it == Player.STATE_BUFFERING) {
                    binding.progress.visibility = View.VISIBLE
                } else {
                    binding.progress.visibility = View.GONE
                }
            }
        }
        var lang =
            prefs.getString(SessionConstants.LANG, "")
        if (lang.isEmpty()) {
            lang = Language.ENGLISH.languageCode
            setLocale(lang)
            println("=====spla1$lang")
        } else {
            setLocale(lang)
        }
        if (lang == "bn") {
            binding.tvLang.visibility = View.VISIBLE
            binding.tvENg.visibility = View.INVISIBLE
            localization(resources)
        } else {
            binding.tvENg.visibility = View.VISIBLE
            binding.tvLang.visibility = View.INVISIBLE
            localization(resources)
        }

    }

}