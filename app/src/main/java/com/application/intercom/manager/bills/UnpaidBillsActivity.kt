package com.application.intercom.manager.bills

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.databinding.ActivityUnpaidBillsBinding


class UnpaidBillsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUnpaidBillsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnpaidBillsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}