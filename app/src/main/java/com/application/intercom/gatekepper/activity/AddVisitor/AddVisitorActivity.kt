package com.application.intercom.gatekepper.AddVisitor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.application.intercom.databinding.ActivityAddVisitorBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.AddVisitor.AddVisitorAdapter
import com.intercom.gatekepper.guest.GuestActivity

class AddVisitorActivity : AppCompatActivity(), AddVisitorAdapter.Click {
    lateinit var binding: ActivityAddVisitorBinding
    private var adptr: AddVisitorAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVisitorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        lstnr()

    }

    private fun initView() {
        binding.addVisitorToolbar.tvTittle.text = "Add Visitor"
        val list = ArrayList<String>()
        list.add("Guest")
        list.add("Delivery")
        list.add("Service")
        list.add("Cab")
        list.add("Salon")
        list.add("Cleaning")

        val genderList = resources.getStringArray(com.application.intercom.R.array.EditProfile)
        binding.chooseSpiner.adapter =
            ArrayAdapter(this, com.application.intercom.R.layout.spinner_dropdown_item, genderList)
        binding.chooseSpiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        }
        binding.rcyAddVisitor.layoutManager = GridLayoutManager(this, 4)
        adptr = AddVisitorAdapter(this, list, this)
        binding.rcyAddVisitor.adapter = adptr
        adptr!!.notifyDataSetChanged()

    }

    private fun lstnr() {
        binding.addVisitorToolbar.ivBack.setOnClickListener {
            finish()
        }

    }

    override fun onClick(position: Int) {
        if (position == 0) {
            startActivity(Intent(this, GuestActivity::class.java).putExtra("from", "guest"))

        } else if (position == 1) {
            startActivity(Intent(this, GuestActivity::class.java).putExtra("from", "deli"))

        } else if (position == 2) {
            startActivity(Intent(this, GuestActivity::class.java).putExtra("from", "service"))

        }
    }
}