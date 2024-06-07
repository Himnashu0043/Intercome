package com.application.intercom.user.newflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.intercom.R
import com.application.intercom.baseActivity.prefs
import com.application.intercom.databinding.ActivityUserUpgradBinding
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.SessionConstants

class UserUpgradActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserUpgradBinding
    private var availableContacts: Int = 0
    private var totalContacts: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserUpgradBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        initView()
        listener()
    }

    private fun initView() {
        binding.include3.tv.text = getString(R.string.upgrade)
        availableContacts = prefs.getInt(SessionConstants.AVAILABLE_CONTACTS).toInt()
        totalContacts = prefs.getInt(SessionConstants.TOTALS_CONTACTS).toInt()
        println("---availableContacts$availableContacts")
        println("---totalContacts$totalContacts")
        binding.textView201.text = "$availableContacts /"
        binding.textView202.text = "${totalContacts}"
        binding.textView204.text = "${prefs.getInt(SessionConstants.DURATION)} Day left"

    }

    private fun listener() {
        binding.imageView111.setOnClickListener {
            finish()
        }
        binding.include3.tv.setOnClickListener {
            startActivity(
                Intent(this, NewSubUserActivity::class.java).putExtra(
                    "from",
                    "user_side_menu"
                )
            )
        }

    }
}