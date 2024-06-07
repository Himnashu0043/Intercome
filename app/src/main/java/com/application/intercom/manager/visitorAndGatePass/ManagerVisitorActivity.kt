package com.application.intercom.manager.visitorAndGatePass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.intercom.R
import com.application.intercom.baseActivity.prefs
import com.application.intercom.databinding.ActivityManagerVistorBinding
import com.application.intercom.gatekepper.activity.newFlow.regularEntryHistory.RegularEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntryHistory.SingleEntryHistoryActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.utils.SessionConstants

class ManagerVisitorActivity : AppCompatActivity() {
    lateinit var binding: ActivityManagerVistorBinding
    private var buildungName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerVistorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        lstnr()
    }

    private fun initView() {
        buildungName = prefs.getString(
            SessionConstants.NAMEBUILDING,
            GPSService.mLastLocation?.latitude.toString()
        )
        binding.toolbar.tvTittle.text = getString(R.string.visitors)
        binding.textView245.text = buildungName
        binding.textView2451.text = buildungName

    }

    private fun lstnr() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.cardView7.setOnClickListener {
            startActivity(
                Intent(this, SingleEntryHistoryActivity::class.java).putExtra(
                    "from",
                    "manager"
                )
            )
            finish()
        }
        binding.cardView8.setOnClickListener {
            startActivity(
                Intent(this, RegularEntryHistoryActivity::class.java).putExtra(
                    "from",
                    "manager"
                )
            )
            finish()
        }

    }
}