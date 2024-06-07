package com.application.intercom.manager.bills

import android.os.Bundle
import android.view.View
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityViewReceiptManagerBinding
import com.application.intercom.helper.DownloadsImage
import com.application.intercom.utils.loadImagesWithGlideExt
import com.application.intercom.utils.shortToast

class ViewReceiptManagerActivity : BaseActivity<ActivityViewReceiptManagerBinding>() {
    private var getImg: String = ""
    private var getRef: String = ""
    override fun getLayout(): ActivityViewReceiptManagerBinding {
        return ActivityViewReceiptManagerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.toolbar.tvTittle.text = "View Receipt"
        binding.toolbar.ivDownloadIcon.visibility = View.VISIBLE
        getImg = intent.getStringExtra("img") ?: ""
        getRef = intent.getStringExtra("ref") ?: ""
        binding.textView110.text = "Referral Number : $getRef"
        binding.img.loadImagesWithGlideExt(getImg)
        println("=============imhhhh$getImg")
        binding.toolbar.ivDownloadIcon.setOnClickListener {
            if (!getImg.isNullOrEmpty()) {
                shortToast("Start Downloading")
                /*val intent = Intent(this, DownloadsImage::class.java)
                intent.putExtra("url", getImg)
                startService(intent)*/
                DownloadsImage(this).execute(getImg)
            } else {
                shortToast("No Receipt Found")
            }

        }
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }

    }
}