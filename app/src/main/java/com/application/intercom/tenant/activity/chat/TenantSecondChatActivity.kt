package com.application.intercom.tenant.activity.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityTenantSecondChatBinding
import com.application.intercom.tenant.adapter.Chat.TenantSecondChatAdapter

class TenantSecondChatActivity : BaseActivity<ActivityTenantSecondChatBinding>() {

    private var adptr: TenantSecondChatAdapter? = null
    override fun getLayout(): ActivityTenantSecondChatBinding {
        return ActivityTenantSecondChatBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        lstnr()
    }

    private fun initView() {
        binding.secondChatToolbar.tvTittle.text = "Chats"
        binding.rcysecond.layoutManager = LinearLayoutManager(this)
        adptr = TenantSecondChatAdapter(this)
        binding.rcysecond.adapter = adptr
        adptr!!.notifyDataSetChanged()

    }

    private fun lstnr() {
        binding.secondChatToolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}