package com.application.intercom.manager.bills

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.intercom.databinding.ActivityPaymentHistoryBinding


class PaymentHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}