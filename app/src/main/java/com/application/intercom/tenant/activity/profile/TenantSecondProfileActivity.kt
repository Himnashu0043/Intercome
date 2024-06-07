package com.application.intercom.tenant.activity.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.databinding.ActivityTenantSecondProfileBinding
import com.application.intercom.tenant.activity.tenantHistory.TenantHistoryActivity
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.loadImagesWithGlideExt

class TenantSecondProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityTenantSecondProfileBinding
    private var email: String = ""
    private var number: String = ""
    private var name: String = ""
    private var from: String = ""
    private var url: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTenantSecondProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        url = intent.getStringExtra("url").toString()
        name = intent.getStringExtra("name").toString()
        email = intent.getStringExtra("email").toString()
        number = intent.getStringExtra("number").toString()

        initView()
        listener()
        CommonUtil.themeSet(this, window)
    }


    private fun initView() {
        if (from.equals("user")) {
            binding.commonBtn.tv.text = "User History"
        } else if (from.equals("owner")) {
            binding.commonBtn.tv.text = "Owner History"
        } else {
            binding.commonBtn.tv.text = "Tenancy History"
        }

        if (name.isNotEmpty()) {
            binding.textView145.text = name
        }
        if (email !="null") {
            binding.textView147.text = email
        }
        if (number.isNotEmpty()) {
            binding.textView149.text = number
        } else {

        }
        if (url.isNotEmpty()) {
            binding.ivchange.loadImagesWithGlideExt(url)
        } else {

        }
//        if (name.isNotEmpty() && email.isNotEmpty() && number.isNotEmpty() && url.isNotEmpty()) {
//            binding.textView145.text = name
//            binding.textView147.text = email
//            binding.textView149.text = number
//            binding.ivchange.loadImagesWithGlideExt(url)
////            binding.textView145.text = "Guest user"
////            binding.textView147.text = "guest@gmail.com"
////            binding.textView149.text = "+91999999999"
//        } else {
////            binding.textView145.text = name
////            if (email.isNullOrEmpty()) {
////
////            } else {
////                binding.textView147.text = email
////            }
////
////            binding.textView149.text = number
////            binding.ivchange.loadImagesWithGlideExt(url)
//        }


    }

    private fun listener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        if (from.equals("user")) {
            binding.commonBtn.tv.setOnClickListener {
                startActivity(
                    Intent(this, EditProfileActivity::class.java).putExtra("from", from)
                        .putExtra("name", name).putExtra("email", email).putExtra("url", url)
                        .putExtra("userFag", true)
                )
            }
        } else {
            binding.commonBtn.tv.setOnClickListener {
                startActivity(Intent(this, TenantHistoryActivity::class.java))
            }
        }
//        binding.commonBtn.tv.setOnClickListener {
//            startActivity(Intent(this, TenantHistoryActivity::class.java))
//        }
        binding.tvEdit.setOnClickListener {
            startActivity(
                Intent(this, EditProfileActivity::class.java).putExtra("from", from)
                    .putExtra("name", name).putExtra("email", email).putExtra("url", url)
                    .putExtra("userFag", true)
            )
        }

    }

}