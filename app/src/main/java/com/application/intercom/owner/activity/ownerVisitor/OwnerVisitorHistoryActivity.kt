package com.application.intercom.owner.activity.ownerVisitor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityOwnerVisitorHistoryBinding
import com.application.intercom.gatekepper.activity.newFlow.ownerTenantRegularEntryHistory.OwnerTenantRegularEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.ownerTenantSingleEntryHistory.OwnerTenantSingleEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.regularEntryHistory.RegularEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntryHistory.SingleEntryHistoryActivity

class OwnerVisitorHistoryActivity : BaseActivity<ActivityOwnerVisitorHistoryBinding>() {
    override fun getLayout(): ActivityOwnerVisitorHistoryBinding {
        return ActivityOwnerVisitorHistoryBinding.inflate(layoutInflater)
    }

    private var from: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from").toString()
        println("------OwnerVisitorHistoryActivity$from")
        initView()
        listener()
    }

    private fun initView() {
        binding.visitorTypaToolbar.tvTittle.text = getString(R.string.visitor_history_type)
    }

    private fun listener() {
        binding.visitorTypaToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.ivSingleEntry.setOnClickListener {
            if (from.equals("tenant")) {
                startActivity(
                    Intent(
                        this,
                        OwnerTenantSingleEntryHistoryActivity::class.java
                    ).putExtra("from", "tenant")
                )
            } else {
                startActivity(Intent(this, OwnerTenantSingleEntryHistoryActivity::class.java))
            }

        }
        binding.ivRegularEntry.setOnClickListener {
            if (from.equals("tenant")) {
                startActivity(
                    Intent(
                        this,
                        OwnerTenantRegularEntryHistoryActivity::class.java
                    ).putExtra("from", "tenant")
                )
            } else {
                startActivity(Intent(this, OwnerTenantRegularEntryHistoryActivity::class.java))
            }

        }
    }
}