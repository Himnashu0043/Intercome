package com.application.intercom.manager.newFlow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.databinding.ActivityFlatManagerBinding
import com.application.intercom.manager.newFlow.balancesheet.BalanceFlatAdapter

class FlatManagerActivity : AppCompatActivity() {
    lateinit var binding: ActivityFlatManagerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlatManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        listener()
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = "FlatNo.102"
        binding.rcy.visibility = View.VISIBLE
        binding.rcy.layoutManager = LinearLayoutManager(this)
        val adptr = FlatAdapter(this)
        binding.rcy.adapter = adptr
        adptr?.notifyDataSetChanged()
    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}