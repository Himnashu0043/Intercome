package com.application.intercom.tenant.activity.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityTenantChatBinding
import com.application.intercom.tenant.adapter.Chat.TenantChatAdapter

class TenantChatActivity : BaseActivity<ActivityTenantChatBinding>() {
    override fun getLayout(): ActivityTenantChatBinding {
        return ActivityTenantChatBinding.inflate(layoutInflater)
    }

    private var chat_adptr: TenantChatAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        lstnr()
    }

    private fun initView() {
        binding.chatToolbar.tvTittle.text = "Chats"
        binding.rcyChat.layoutManager = LinearLayoutManager(this)
        chat_adptr = TenantChatAdapter(this)
        binding.rcyChat.adapter = chat_adptr
        chat_adptr!!.notifyDataSetChanged()


    }

    private fun lstnr() {
        binding.chatToolbar.ivBack.setOnClickListener {
            finish()
        }

    }
}