package com.application.intercom.user.forgot_password

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.R
import com.application.intercom.databinding.ActivityCongratulationsBinding
import com.application.intercom.user.login.LoginUsingOtpActivity

class CongratulationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCongratulationsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCongratulationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSubmit.tv.text = getString(R.string.back_to_login)
        binding.btnSubmit.tv.setOnClickListener {
            startActivity(Intent(this, LoginUsingOtpActivity::class.java))
        }
//        OtpVerificationDialog.newInstance(getString(R.string.tv_register_member), getString(R.string.app_name))
//            .show(supportFragmentManager, OtpVerificationDialog.TAG)

    }
}