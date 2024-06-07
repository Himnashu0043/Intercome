package com.application.intercom.user.otp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.local.UserOtpLoginLocalModel
import com.application.intercom.data.model.local.UserVerifyOtpCommonLocalModel
import com.application.intercom.data.model.remote.OtpLoginResponse
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.LoginRepository
import com.application.intercom.databinding.ActivityOtpVerificationBinding
import com.application.intercom.gatekepper.Main.MainGateKepperActivity
import com.application.intercom.gatekepper.activity.newFlow.home.NewGateKeeperMainActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.manager.notice.NoticeBoardActivity
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.tenant.activity.MyCommunity.TenantMyCommunityActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticeBoardDetailsActivity
import com.application.intercom.user.forgot_password.ResetPasswordActivity
import com.application.intercom.user.login.LoginFactory
import com.application.intercom.user.login.LoginUsingOtpActivity
import com.application.intercom.user.login.LoginViewModel
import com.application.intercom.utils.*
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*


import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.concurrent.TimeUnit

class OtpVerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpVerificationBinding
    private var from: String = ""
    private var mobileNumber: String = ""
    private var countryCode: String = ""
    private var role: String = ""
    private var otpApi: String = ""
    private val TAG = "OtpVerificationActivity"
    private var verificationCode: String = ""
    private lateinit var viewModel: LoginViewModel

    private lateinit var mAuth: FirebaseAuth
    private var mVerificationId = ""
    private var mResendToken = ""
    private var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        init()
        otpTextChanged()
        from = intent.getStringExtra(AppConstants.FROM).toString()
        println("------from${from}")
        mobileNumber = intent.getStringExtra(AppConstants.USER_MOBILE_NUMBER).toString()
        role = intent.getStringExtra(AppConstants.ROLE).toString()
        println("------role${role}")
        countryCode = intent.getStringExtra(AppConstants.COUNTRY_CODE).toString()
        otpApi = intent.getStringExtra(AppConstants.OTP).toString()

        startFireBaseLogin()
        startCountDownTimer()

        val mobileWithcode = "+${countryCode}${mobileNumber}"
        Log.e(TAG, "onCreate: ${mobileWithcode}")

        if (from == AppConstants.USER || from == AppConstants.TENANT || from == AppConstants.OWNER || from == AppConstants.MANAGER || from == AppConstants.GATEKEEPER || from == AppConstants.MEMBER) {
            userObserver()
        } else {
            verifyOtpCommonObserver()

        }

        binding.tvMobileNumber.text = mobileWithcode
        binding.btnSubmit.tv.text = getString(com.application.intercom.R.string.submit)

        binding.tvMobileNumber.setOnClickListener {
            finish()
        }

        binding.tvResendOtp.setOnClickListener {
            startFireBaseLogin()
            val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(mobileWithcode) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(mCallback!!) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
            startCountDownTimer()
            Toast.makeText(this, getString(R.string.resend_otp), Toast.LENGTH_SHORT).show()
        }

        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }

        binding.btnSubmit.tv.setOnClickListener {
            val otp: String = binding.edtOne.text.toString().trim() + binding.edtTwo.text.toString()
                .trim() + binding.edtThree.text.toString()
                .trim() + binding.edtFour.text.toString()
                .trim() + binding.edtFive.text.toString()
                .trim() + binding.edtSix.text.toString().trim()

            if (otp.isEmpty()) {
                shortToast(getString(R.string.please_enter_otp))
            } else {
                verificationCode = otp
                if (from == AppConstants.USER || from == AppConstants.TENANT || from == AppConstants.OWNER || from == AppConstants.MANAGER || from == AppConstants.GATEKEEPER || from == AppConstants.MEMBER) {
                    verifyVerificationCode(otp)
                } else if (from == AppConstants.FORGOT_PASSWORD) {
                    userVerifyOtpCommon()
                }
            }

        }

        // firebase initialize

        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(mobileWithcode) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(mCallback!!) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)


    }

    private fun init() {
        val repo = LoginRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, LoginFactory(repo))[LoginViewModel::class.java]
    }

    private fun startFireBaseLogin() {
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: starts...$credential")
                Log.d(TAG, "onVerificationCompleted: OTP is==" + credential.smsCode)
//                autofillOtp(credential.smsCode)

            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", e)
                Toast.makeText(applicationContext, getString(R.string.number_verification_failed), Toast.LENGTH_SHORT)
                    .show()
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.e(TAG, "onVerificationFailed: " + e.message, e)
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.e(TAG, "onVerificationFailed: " + e.message, e)
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d(TAG, "onCodeSent:===$verificationId")

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                mResendToken = token.toString()
                Toast.makeText(applicationContext, getString(R.string.sending_otp), Toast.LENGTH_SHORT).show()

            }
        }
    }

    fun verifyVerificationCode(otp: String?) {
        if (mVerificationId == "") {
            Toast.makeText(applicationContext, getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show()
        } else {
            val credential = PhoneAuthProvider.getCredential(
                mVerificationId,
                otp!!
            )
            SignInWithPhone(credential)
        }
    }

    /*private fun SignInWithPhone(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "signInWithCredential:success")
                    userOtpLogin()

                } else {
                    Log.e(TAG, "signInWithCredential:failed")
                    Toast.makeText(applicationContext, "Invalid OTP...", Toast.LENGTH_SHORT).show()
                }
            }

    }*/
    private fun SignInWithPhone(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(applicationContext, getString(R.string.success), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "signInWithCredential:success")
                    userOtpLogin()

                } else {
                    Log.e(TAG, "signInWithCredential:failed")
                    Toast.makeText(applicationContext, getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show()
                }
            }

    }


    private fun redirectPage(otpLoginResponse: OtpLoginResponse) {
        if (type == AppConstants.USER) {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(AppConstants.ROLE, otpLoginResponse.data.role)
                putExtra(AppConstants.LOGINOTP, "loginOtpScreen")
            }
            startActivity(intent)
        } else if (type == AppConstants.TENANT) {
            val intent = Intent(this, TenantMainActivity::class.java).apply {
                putExtra(AppConstants.ROLE, otpLoginResponse.data.role)
                putExtra(AppConstants.LOGINOTP, "loginOtpScreen")
            }
            startActivity(intent)
        } else if (type == AppConstants.OWNER) {
            val intent = Intent(this, OwnerMainActivity::class.java).apply {
                putExtra(AppConstants.ROLE, otpLoginResponse.data.role)
                putExtra(AppConstants.LOGINOTP, "loginOtpScreen")
            }
            startActivity(intent)
        } else if (type == AppConstants.MANAGER) {
            val intent = Intent(
                this,
                com.application.intercom.manager.main.ManagerMainActivity::class.java
            ).apply {
                putExtra(AppConstants.ROLE, otpLoginResponse.data.role)
                putExtra(AppConstants.LOGINOTP, "loginOtpScreen")
            }
            startActivity(intent)
        } else if (type == AppConstants.GATEKEEPER) {
            val intent =
                Intent(this, MainGateKepperActivity::class.java).apply {
                    putExtra(AppConstants.ROLE, otpLoginResponse.data.role)
                    putExtra(AppConstants.LOGINOTP, "loginOtpScreen")

                }
            startActivity(intent)
        }

    }


    private fun userObserver() {
        viewModel.otpLoginLiveData.observe(this, Observer {
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
                            prefs.setBoolean(
                                SessionConstants.SUBSCRIPTION,
                                it.data.subscription_active
                            )
                            prefs.put(
                                SessionConstants.AVAILABLE_CONTACTS,
                                it.data.availableContacts
                            )
                            prefs.put(SessionConstants.TOTALS_CONTACTS, it.data.totalContacts)
                            type = it.data.role
                            redirectPage(it)
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

    private fun verifyOtpCommonObserver() {
        viewModel.verifyOtpCommonLiveData.observe(this, Observer {
            when (it) {

                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            val intent =
                                Intent(this, ResetPasswordActivity::class.java).apply {
                                    putExtra(AppConstants.USER_MOBILE_NUMBER, it.data.phoneNumber)

                                }
                            startActivity(intent)


                        }
                        if (it.status == 503) {
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

    private fun userOtpLogin() {
        val model =
            UserOtpLoginLocalModel(mobileNumber, "+${countryCode}", otpApi.toInt(), from, "En",
                prefs.getString(SessionConstants.KLATITUDE,"0.0").toDouble(),prefs.getString(SessionConstants.KLONGITUDE,"0.0").toDouble())
        viewModel.userOtpLogin(model)

    }

    private fun userVerifyOtpCommon() {
        val model = UserVerifyOtpCommonLocalModel(mobileNumber, "", otpApi.toInt())
        viewModel.userVerifyOtpCommon(model)

    }

    fun otpTextChanged() {
        binding.edtOne.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().isEmpty())
                    binding.edtTwo.requestFocus() else binding.edtOne.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edtTwo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString()
                        .isEmpty()
                ) binding.edtThree.requestFocus() else binding.edtOne.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edtThree.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().isEmpty())
                    binding.edtFour.requestFocus() else binding.edtTwo.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edtFour.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().isEmpty())
                    binding.edtFive.requestFocus() else binding.edtThree.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.edtFive.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().isEmpty())
                    binding.edtSix.requestFocus() else binding.edtFour.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edtSix.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().isEmpty())
                    binding.edtSix.requestFocus() else binding.edtFive.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }

    private fun startCountDownTimer() {
        binding.tvResendOtp.visibility = View.GONE
        binding.timer11.visibility = View.VISIBLE
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val f: NumberFormat = DecimalFormat("00")
                val hour = millisUntilFinished / 3600000 % 24
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                binding.timer11.text = getString(
                    com.application.intercom.R.string.min_sec,
                    f.format(min),
                    f.format(sec)
                )
            }

            override fun onFinish() {
                binding.timer11.text =
                    getString(com.application.intercom.R.string.min_sec, "00", "00")
                binding.tvResendOtp.visibility = View.VISIBLE
                binding.timer11.visibility = View.INVISIBLE
            }
        }.start()
    }


}