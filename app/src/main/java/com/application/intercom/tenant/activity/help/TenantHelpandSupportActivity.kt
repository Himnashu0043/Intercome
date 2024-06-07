package com.application.intercom.tenant.activity.help

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityTenantHelpandSupportBinding
import com.application.intercom.tenant.activity.chat.ChatDetailsActivity
import com.application.intercom.tenant.activity.registerComplain.TenantAddRegisterComplainActivity

class TenantHelpandSupportActivity :BaseActivity<ActivityTenantHelpandSupportBinding>() {
    override fun getLayout(): ActivityTenantHelpandSupportBinding {
        return ActivityTenantHelpandSupportBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        lstnr()
    }

    private fun initView() {
        binding.helpToolbar.tvTittle.text = getString(R.string.help_and_support)

    }

    private fun lstnr() {
        binding.helpToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.layOwner.setOnClickListener {
            startActivity(Intent(this, ChatDetailsActivity::class.java).putExtra("from", "owner"))
        }
        binding.layManager.setOnClickListener {
            startActivity(Intent(this, ChatDetailsActivity::class.java).putExtra("from", "manager"))
        }
        binding.layRegi.setOnClickListener {
            startActivity(Intent(this, TenantAddRegisterComplainActivity::class.java))
        }

    }
}