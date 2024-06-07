package com.application.intercom.gatekepper.activity.gatePass

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.databinding.ActivityGatePassBinding

class GatePassActivity : AppCompatActivity() {
    lateinit var bindng: ActivityGatePassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindng = ActivityGatePassBinding.inflate(layoutInflater)
        setContentView(bindng.root)
        initView()
        lstnr()
    }

    private fun initView() {
        bindng.gatePassToolbar.tvTittle.text = "Gatepass"

    }

    private fun lstnr() {
        bindng.gatePassToolbar.ivBack.setOnClickListener {
            finish()
        }


    }
}