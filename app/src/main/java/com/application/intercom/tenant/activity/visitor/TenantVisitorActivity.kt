package com.application.intercom.tenant.activity.visitor

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityTenantVisitorBinding
import com.application.intercom.tenant.adapter.visitorAdapter.ChildVisitorAdapter
import com.application.intercom.tenant.adapter.visitorAdapter.ParentVisitorAdapter

class TenantVisitorActivity : BaseActivity<ActivityTenantVisitorBinding>(), ChildVisitorAdapter.Click {

    override fun getLayout(): ActivityTenantVisitorBinding {
        return ActivityTenantVisitorBinding.inflate(layoutInflater)
    }

    private var parentAdapter: ParentVisitorAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        lstnr()
    }

    private fun initView() {
        binding.visitorToolbar.tvTittle.text = getString(R.string.visitors)
        binding.rcyParant.layoutManager = LinearLayoutManager(this)
        parentAdapter = ParentVisitorAdapter(this, this)
        binding.rcyParant.adapter = parentAdapter
        parentAdapter!!.notifyDataSetChanged()

    }

    private fun lstnr() {
        binding.visitorToolbar.ivBack.setOnClickListener {
            finish()
        }

    }

    private fun callingPopup() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.tenant_visitor_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val yes = dialog.findViewById<TextView>(R.id.textView85)
        val no = dialog.findViewById<TextView>(R.id.textView851)
        val call = dialog.findViewById<TextView>(R.id.textView81)
        val callimg = dialog.findViewById<ImageView>(R.id.imageView80)

        yes.setOnClickListener {
            call.visibility = View.VISIBLE
            callimg.visibility = View.VISIBLE
            yes.visibility = View.INVISIBLE
            no.visibility = View.INVISIBLE
        }
        no.setOnClickListener {
            dialog.dismiss()
        }
        call.setOnClickListener {
            dialog.dismiss()
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    override fun onClick(position: Int) {
        callingPopup()
    }
}