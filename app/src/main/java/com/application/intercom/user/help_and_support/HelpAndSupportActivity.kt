package com.application.intercom.user.help_and_support

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.databinding.ActivityHelpAndSupportBinding

class HelpAndSupportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpAndSupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpAndSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.tvTittle.text = "Help & Support"
        binding.toolbar.ivBack.setOnClickListener {
          finish()
        }
    }
}