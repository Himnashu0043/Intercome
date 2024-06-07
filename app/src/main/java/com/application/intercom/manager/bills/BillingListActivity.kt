package com.application.intercom.manager.bills

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.intercom.databinding.ActivityBillingListBinding


class BillingListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBillingListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillingListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}