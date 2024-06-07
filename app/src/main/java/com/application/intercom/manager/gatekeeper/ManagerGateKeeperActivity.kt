package com.application.intercom.manager.gatekeeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.intercom.databinding.ActivityManagerGateKeeperBinding


class ManagerGateKeeperActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManagerGateKeeperBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerGateKeeperBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.toolbar.tvTittle.text = "Manage Visitors & Gatepass"

        binding.layoutCreateGatePass.setOnClickListener {

            startActivity(Intent(this, ManagerCreateGatePassActivity::class.java))

        }
        binding.layoutAddVisitor.setOnClickListener {

        }
        binding.layoutManageGatekeepers.setOnClickListener {
            startActivity(Intent(this, GateKeeperListingActivity::class.java))
        }
        binding.layoutPastVisitors.setOnClickListener {

        }
    }
}