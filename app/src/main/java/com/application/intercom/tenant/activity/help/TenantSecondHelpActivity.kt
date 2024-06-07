package com.application.intercom.tenant.activity.help

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityTenantSecondHelpBinding
import com.application.intercom.tenant.activity.chat.ChatDetailsActivity

class TenantSecondHelpActivity : BaseActivity<ActivityTenantSecondHelpBinding>() {
    override fun getLayout(): ActivityTenantSecondHelpBinding {
        return ActivityTenantSecondHelpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()
    }

    private fun initView() {
        binding.secondHelpToolbar.tvTittle.text = "Help & Support"

    }

    private fun listener() {
        binding.secondHelpToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.cardView13.setOnClickListener {
            startActivity(Intent(this, ChatDetailsActivity::class.java).putExtra("from", "manager"))
        }
        binding.cardView131.setOnClickListener {
            startActivity(Intent(this, ChatDetailsActivity::class.java).putExtra("from", "owner"))
        }
    }
}