package com.application.intercom.owner.activity.helpSupport

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityOwnerHelpSupportBinding
import com.application.intercom.tenant.activity.chat.ChatDetailsActivity

class OwnerHelpSupportActivity : BaseActivity<ActivityOwnerHelpSupportBinding>() {
    private var from: String = ""

    override fun getLayout(): ActivityOwnerHelpSupportBinding {
        return ActivityOwnerHelpSupportBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from") ?: ""
        println("-====$from")
        initView()
        listener()

    }


    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.ownerHelp.tvTittle.text = getString(R.string.help_and_support)
        if (from == "tenant") {
            binding.textView128.text = getString(R.string.contact_owner)
            binding.textView1281.text = getString(R.string.contact_manager)
        } else if (from == "gateKeeper") {
            binding.textView128.text = getString(R.string.contact_manager)
            binding.textView1281.text = getString(R.string.contact_project_admin)
        } else if (from == "Manager") {
            binding.textView128.text = getString(R.string.contact_project_admin)
            binding.textView1281.visibility = View.INVISIBLE
            binding.cardView131.visibility = View.INVISIBLE
        } else {
            binding.textView1281.visibility = View.VISIBLE
            binding.cardView131.visibility = View.VISIBLE
        }

    }

    private fun listener() {
        binding.ownerHelp.ivBack.setOnClickListener {
            finish()
        }
//        binding.cardView13.setOnClickListener {
//            startActivity(Intent(this, ChatDetailsActivity::class.java).putExtra("from","manager"))
//        }
        val number = "9650039982"
        binding.imageView142.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=${"+91"}$number"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        binding.imageView143.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.instagram.com/")
            startActivity(openURL)
        }
        binding.imageView144.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.facebook.com/")
            startActivity(openURL)
        }
        binding.imageView137.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.youtube.com/")
            startActivity(openURL)
        }
        binding.cardView13.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${number}")
            startActivity(intent)
        }
        binding.cardView131.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${number}")
            startActivity(intent)
        }
        binding.cardView13111.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${number}")
            startActivity(intent)
        }
        binding.emailCart.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html"
            intent.putExtra(Intent.EXTRA_EMAIL, "himanshu.gupta@mobulous.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
            //intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.")
            startActivity(Intent.createChooser(intent, "Send Email"))
        }
    }

}