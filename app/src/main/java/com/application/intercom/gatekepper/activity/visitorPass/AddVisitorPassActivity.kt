package com.application.intercom.gatekepper.activity.visitorPass

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import com.application.intercom.R
import com.application.intercom.databinding.ActivityAddVisitorPassBinding


class AddVisitorPassActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddVisitorPassBinding
    var from: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVisitorPassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        initView()
        lstnr()
    }

    private fun initView() {
        binding.visitorPassToolbar.tvTittle.text ="Add Visitor"
        if (from.equals("guest")) {
            guestPopup()
        } else if (from.equals("deli")) {
            deliPopup()
        } else {
            servicePopup()
        }
    }

    private fun lstnr() {

    }

    private fun guestPopup() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.noti_send_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val img = dialog.findViewById<ImageView>(R.id.imageView20)
        img.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, VisitorPassActivity::class.java))
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    private fun deliPopup() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.declined_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val img = dialog.findViewById<ImageView>(R.id.imageView201)
        img.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, VisitorPassActivity::class.java))
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    private fun servicePopup() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.allowed_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val img = dialog.findViewById<ImageView>(R.id.imageView202)
        img.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, VisitorPassActivity::class.java))
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }
}