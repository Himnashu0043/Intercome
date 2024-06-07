package com.application.intercom.tenant.activity.tenantHistory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityTenantHistoryBinding
import com.application.intercom.tenant.adapter.tenantHistory.TenantParentAdapter

class TenantHistoryActivity : BaseActivity<ActivityTenantHistoryBinding>() {
    override fun getLayout(): ActivityTenantHistoryBinding {
        return ActivityTenantHistoryBinding.inflate(layoutInflater)
    }

    private var adptr: TenantParentAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()

    }

    private fun initView() {
        binding.tenantToolbar.tvTittle.text = "Tenancy History"
        binding.rcyParentTenant.layoutManager = LinearLayoutManager(this)
        adptr = TenantParentAdapter(this)
        binding.rcyParentTenant.adapter = adptr
        adptr!!.notifyDataSetChanged()

    }

    private fun listener() {

    }
}