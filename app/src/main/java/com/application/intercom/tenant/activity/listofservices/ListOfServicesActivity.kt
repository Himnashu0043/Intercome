package com.application.intercom.tenant.activity.listofservices

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityListOfServicesBinding
import com.intercom.tenant.adapter.FragementServices.ListOfService.ListOfServiceAdapter

class ListOfServicesActivity : BaseActivity<ActivityListOfServicesBinding>() {

    private var listOf_Adapter: ListOfServiceAdapter? = null
    private var from: String = ""
    override fun getLayout(): ActivityListOfServicesBinding {
        return ActivityListOfServicesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from").toString()
        initView()
        lstnr()
    }

    private fun initView() {
        if (from.equals("user")) {
            binding.listOfToolbar.tvTittle.text = getString(R.string.electricians)
            binding.listOfToolbar.tvCountText.text = "(150+)"
        } else {
            binding.listOfToolbar.tvTittle.text = "List of service \nprovider (150+)"
//            binding.listOfToolbar.tvCountText.text = "(150+)"
        }

        binding.listOfToolbar.tvCountText.visibility = View.VISIBLE
        binding.rcyListOdService.layoutManager = LinearLayoutManager(this)
        listOf_Adapter = ListOfServiceAdapter(this)
        binding.rcyListOdService.adapter = listOf_Adapter
        listOf_Adapter!!.notifyDataSetChanged()

    }

    private fun lstnr() {

    }
}