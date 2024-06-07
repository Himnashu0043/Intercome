package com.application.intercom.gatekepper.activity.visitorPass

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.databinding.ActivityVisitorPassBinding


class VisitorPassActivity : AppCompatActivity() {
    lateinit var binding: ActivityVisitorPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisitorPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        lstnr()
    }

    private fun initView() {
        binding.visitorPassToolbar.tvTittle.text = "Visitor Pass"


    }

    private fun lstnr() {
        binding.visitorPassToolbar.ivBack.setOnClickListener {
            finish()
        }
    }


}