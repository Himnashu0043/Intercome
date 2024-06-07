package com.application.intercom.manager.bills

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.databinding.ActivityApprovalBillsBinding


class ApprovalBillsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApprovalBillsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApprovalBillsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}