package com.application.intercom.gatekepper.Fragment.Home

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.application.intercom.R
import com.application.intercom.databinding.FragmentGateAddVisitorBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.AddVisitor.AddVisitorAdapter
import com.intercom.gatekepper.VisitorDetails.VisitorDetailsActivity
import com.intercom.gatekepper.guest.GuestActivity

class GateAddVisitorFragment : Fragment(), AddVisitorAdapter.Click {
    lateinit var binding: FragmentGateAddVisitorBinding
    private var adptr: AddVisitorAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGateAddVisitorBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        binding.addVisitorToolbar.tvTittle.text = "Add Visitor"
        binding.addVisitorToolbar.ivBack.visibility = View.INVISIBLE
        binding.addVisitorToolbar.tvText.visibility = View.VISIBLE
        binding.addVisitorToolbar.tvText.text = "Visitor History"
        binding.addVisitorToolbar.tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)

        val list = ArrayList<String>()
        list.add("Guest")
        list.add("Delivery")
        list.add("Service")
        list.add("Cab")
        list.add("Salon")
        list.add("Cleaning")

        val genderList = resources.getStringArray(R.array.EditProfile)
        binding.chooseSpiner.adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, genderList)
        binding.chooseSpiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
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
        binding.rcyAddVisitor.layoutManager = GridLayoutManager(requireContext(), 4)
        adptr = AddVisitorAdapter(requireContext(), list, this)
        binding.rcyAddVisitor.adapter = adptr
        adptr!!.notifyDataSetChanged()
    }

    private fun lstnr() {
        binding.addVisitorToolbar.tvText.setOnClickListener {
            startActivity(Intent(requireContext(), VisitorDetailsActivity::class.java))
        }
    }

    override fun onClick(position: Int) {
        if (position == 0) {
            startActivity(
                Intent(requireContext(), GuestActivity::class.java).putExtra(
                    "from",
                    "guest"
                )
            )

        } else if (position == 1) {
            startActivity(
                Intent(requireContext(), GuestActivity::class.java).putExtra(
                    "from",
                    "deli"
                )
            )

        } else if (position == 2) {
            startActivity(
                Intent(requireContext(), GuestActivity::class.java).putExtra(
                    "from",
                    "service"
                )
            )

        }
    }
}