package com.application.intercom.user.contact

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityContactIntercomBinding
import com.application.intercom.tenant.activity.chat.ChatDetailsActivity

class ContactIntercomActivity : BaseActivity<ActivityContactIntercomBinding>() {

    override fun getLayout(): ActivityContactIntercomBinding {
        return ActivityContactIntercomBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding.toolbar.tvTittle.text = getString(R.string.help_and_support)
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        /*binding.ivMessage.setOnClickListener {
            startActivity(Intent(this, ChatDetailsActivity::class.java))
        }*/
    }
}