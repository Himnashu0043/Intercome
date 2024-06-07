package com.application.intercom.manager.bills

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.R
import com.application.intercom.databinding.ActivityGenerateInvoiceBinding
import com.application.intercom.manager.dialog.NoticeDateAndTimeDialog
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.tenant.activity.profile.ProfileActivity

class GenerateInvoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenerateInvoiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        lstnr()


    }

    private fun initView() {
        binding.toolbar.tvTittle.text = getString(R.string.invoice)
        binding.btnBilling.tv.text = getString(R.string.add_billings_1)
    }

    private fun lstnr() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.btnBilling.tv.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ManagerMainActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }
//        NoticeDateAndTimeDialog.newInstance(
//            getString(R.string.tv_register_member),
//            getString(R.string.app_name)
//        )
//            .show(supportFragmentManager, NoticeDateAndTimeDialog.TAG)
    }
}