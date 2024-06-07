package com.application.intercom.user.community

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityCommunityBinding
import com.application.intercom.user.contact.ContactUsActivity

class CommunityActivity : BaseActivity<ActivityCommunityBinding>() {

    override fun getLayout(): ActivityCommunityBinding {
        return ActivityCommunityBinding.inflate(layoutInflater)
    }

    private var from: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from").toString()
        println("---from$from")
        if (from.equals("billing")) {
            binding.toolbar.tvTittle.text = getString(R.string.my_billings)
            binding.btnSubmit.tv.text = getString(R.string.add_your_complex)
//            binding.tvPasswordDesc.text = "There is no bill to show"
            binding.btnSubmit.tv.setOnClickListener {
                startActivity(Intent(this, ContactUsActivity::class.java))
                finish()
            }
        } else if (from.equals("visitor")) {
            binding.toolbar.tvTittle.text = getString(R.string.visitors)
            binding.btnSubmit.tv.text = getString(R.string.add_new_visitor)
//            binding.tvPasswordDesc.text = "There is no visitor"
            binding.btnSubmit.tv.setOnClickListener {
                startActivity(Intent(this, ContactUsActivity::class.java))
                finish()
            }
        } else if (from.equals("notice")) {
            binding.toolbar.tvTittle.text = getString(R.string.notice_board)
            binding.btnSubmit.tv.text = getString(R.string.add_your_complex)
//            binding.tvPasswordDesc.text = "There is no bill to show"
            binding.btnSubmit.tv.setOnClickListener {
                startActivity(Intent(this, ContactUsActivity::class.java))
                finish()
            }
        } else {
            binding.toolbar.tvTittle.text = getString(R.string.my_community)
            binding.btnSubmit.tv.text = getString(R.string.be_a_member)
            binding.btnSubmit.tv.setOnClickListener {
                startActivity(Intent(this, ContactUsActivity::class.java))
                finish()
            }
        }
        binding.toolbar.ivNotification.visibility = View.VISIBLE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

}