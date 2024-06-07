package com.application.intercom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.intercom.databinding.ActivityCalenderDemoBinding

class CalenderDemoActivity : AppCompatActivity() {
    lateinit var binding:ActivityCalenderDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalenderDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.caa
    }
}