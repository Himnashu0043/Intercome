package com.application.intercom.gatekepper.activity.newFlow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.intercom.R
import com.application.intercom.databinding.ActivityVisitorHistoryTypeBinding
import com.application.intercom.gatekepper.activity.newFlow.regularEntryHistory.RegularEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntry.SingleEntryActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntryHistory.SingleEntryHistoryActivity

class VisitorHistoryTypeActivity : AppCompatActivity() {
    lateinit var binding: ActivityVisitorHistoryTypeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisitorHistoryTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        listener()
    }

    private fun initView() {
        binding.visitorTypaToolbar.tvTittle.text = "Visitor Histroy Type"

    }

    private fun listener() {
        binding.visitorTypaToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.ivSingleEntry.setOnClickListener {
            startActivity(Intent(this, SingleEntryHistoryActivity::class.java))
        }
        binding.ivRegularEntry.setOnClickListener {
            startActivity(Intent(this, RegularEntryHistoryActivity::class.java))
        }

    }
}