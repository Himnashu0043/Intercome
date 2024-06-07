package com.intercom.gatekepper.guest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.R
import com.application.intercom.databinding.ActivityGuestBinding
import com.application.intercom.gatekepper.activity.visitorPass.AddVisitorPassActivity

class GuestActivity : AppCompatActivity() {
    lateinit var binding: ActivityGuestBinding
    var from: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        lstnr()
    }

    private fun initView() {
        binding.commonLay.commonBtn.tv.text = "Add Visitior"
        from = intent.getStringExtra("from").toString()
        val genderList = resources.getStringArray(R.array.EditProfile)
        binding.commonLay.chooseSpiner.adapter =
            ArrayAdapter(this, R.layout.spinner_dropdown_item, genderList)
        /* binding.commonLay.chooseSpiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
             override fun onItemSelected(
                 parent: AdapterView<*>,
                 view: View,
                 position: Int,
                 id: Long,
             ) {
                 if (binding.chooseSpiner.selectedItemPosition > 0) {
                     binding.rcyAddVisitor.visibility = View.VISIBLE
                 } else {
                     binding.rcyAddVisitor.visibility = View.GONE
                 }
             }

             override fun onNothingSelected(parent: AdapterView<*>?) {}
         }*/
        if (from.equals("guest")) {

            binding.guestToolbar.tvTittle.text = "Guest"
            binding.commonLay.tvcompany.visibility = View.INVISIBLE
            binding.commonLay.edCompany.visibility = View.INVISIBLE
            binding.commonLay.tvDelivery.visibility = View.INVISIBLE
            binding.commonLay.edDelivery.visibility = View.INVISIBLE
            binding.commonLay.textView511.visibility = View.VISIBLE
            binding.commonLay.edAddress.visibility = View.VISIBLE
            binding.commonLay.textView5111.visibility = View.VISIBLE
            binding.commonLay.edNumberofVisitor.visibility = View.VISIBLE
        } else if (from.equals("deli")) {

            binding.guestToolbar.tvTittle.text = "Delivery"
            binding.commonLay.tvcompany.visibility = View.VISIBLE
            binding.commonLay.edCompany.visibility = View.VISIBLE
            binding.commonLay.tvDelivery.visibility = View.VISIBLE
            binding.commonLay.edDelivery.visibility = View.VISIBLE
            binding.commonLay.textView511.visibility = View.INVISIBLE
            binding.commonLay.edAddress.visibility = View.INVISIBLE
            binding.commonLay.textView5111.visibility = View.INVISIBLE
            binding.commonLay.edNumberofVisitor.visibility = View.INVISIBLE
        } else {

            binding.guestToolbar.tvTittle.text = "Service"
            binding.commonLay.tvcompany.visibility = View.VISIBLE
            binding.commonLay.edCompany.visibility = View.VISIBLE
            binding.commonLay.tvDelivery.visibility = View.VISIBLE
            binding.commonLay.edDelivery.visibility = View.VISIBLE
            binding.commonLay.textView511.visibility = View.INVISIBLE
            binding.commonLay.edAddress.visibility = View.INVISIBLE
            binding.commonLay.textView5111.visibility = View.INVISIBLE
            binding.commonLay.edNumberofVisitor.visibility = View.INVISIBLE
        }


    }

    private fun lstnr() {
        binding.guestToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.commonLay.commonBtn.tv.setOnClickListener {
            startActivity(Intent(this, AddVisitorPassActivity::class.java).putExtra("from", from))
        }

    }


}