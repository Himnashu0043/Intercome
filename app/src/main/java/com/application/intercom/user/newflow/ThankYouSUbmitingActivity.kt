package com.application.intercom.user.newflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.prefs
import com.application.intercom.databinding.ActivityThankYouSubmitingBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.utils.SessionConstants

class ThankYouSUbmitingActivity : AppCompatActivity() {
    lateinit var binding: ActivityThankYouSubmitingBinding
    private var name: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThankYouSubmitingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        name = prefs.getString(SessionConstants.NAME,  GPSService.mLastLocation?.latitude.toString())
        println("----name$name")
        initView()
        listener()

    }

    private fun initView() {
        binding.commonBtn.tv.text = getString(R.string.go_to_my_property)

    }

    private fun listener() {
        binding.commonBtn.tv.setOnClickListener {
            if (name.equals("owner")) {
                startActivity(
                    Intent(this, OwnerMainActivity::class.java).putExtra(
                        "from",
                        "from_myList"
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            } else if (name.equals("tenant")) {
                startActivity(
                    Intent(this, TenantMainActivity::class.java).putExtra(
                        "from",
                        "from_myList"
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            } else {
                startActivity(
                    Intent(this, MainActivity::class.java).putExtra(
                        "from",
                        "from_myList"
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            }

        }

    }
}