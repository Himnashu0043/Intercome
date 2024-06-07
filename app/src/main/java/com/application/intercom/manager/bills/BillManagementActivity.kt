package com.application.intercom.manager.bills

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.intercom.databinding.ActivityBillManagementBinding

class BillManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBillManagementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutAddBilling.setOnClickListener {
            startActivity(Intent(this,AddBillsActivity::class.java))
        }

        binding.layoutBillingList.setOnClickListener {
            startActivity(Intent(this,BillingListActivity::class.java))
        }

        binding.layoutOverdueUnpaidBills.setOnClickListener {
            startActivity(Intent(this,UnpaidBillsActivity::class.java))
        }

        binding.layoutPendingForApproval.setOnClickListener {
            startActivity(Intent(this,AppCompatActivity::class.java))
        }
    }
}