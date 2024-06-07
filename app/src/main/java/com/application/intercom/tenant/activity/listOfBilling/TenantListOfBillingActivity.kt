package com.application.intercom.tenant.activity.listOfBilling

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityTenantListOfBillingBinding
import com.application.intercom.tenant.adapter.listOfBilling.TenantListOfBillingAdapter

class TenantListOfBillingActivity : BaseActivity<ActivityTenantListOfBillingBinding>() {

    private var adpter: TenantListOfBillingAdapter? = null
    override fun getLayout(): ActivityTenantListOfBillingBinding {
        return ActivityTenantListOfBillingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()

    }

    private fun initView() {
        binding.listOfBiiling.tvTittle.text = "List of Billing"
        binding.rcyTenantList.layoutManager = LinearLayoutManager(this)
        adpter = TenantListOfBillingAdapter(this)
        binding.rcyTenantList.adapter = adpter
        adpter!!.notifyDataSetChanged()

    }

    private fun listener() {
        binding.listOfBiiling.ivBack.setOnClickListener {
            finish()
        }
    }
}