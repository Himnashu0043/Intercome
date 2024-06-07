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
import com.application.intercom.R
import com.application.intercom.databinding.FragmentGateKeeperGatePassBinding
import com.application.intercom.gatekepper.activity.gatePass.SecondGatePassActivity
import com.application.intercom.gatekepper.activity.gatepassHistory.GatePassHistoryActivity


class GateKeeperGatePassFragment : Fragment() {
    lateinit var binding: FragmentGateKeeperGatePassBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGateKeeperGatePassBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        binding.createToolbar.tvTittle.text = "Create Gatepass"
        binding.commonBtn.tv.text = "Create Gatepass"

        binding.createToolbar.ivBack.visibility = View.INVISIBLE
        binding.createToolbar.tvText.visibility = View.VISIBLE
        binding.createToolbar.tvText.text = "GatePass History"
        binding.createToolbar.tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
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
                    binding.mainNew.visibility = View.VISIBLE
                } else {
                    binding.mainNew.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun lstnr() {
        binding.createToolbar.tvText.setOnClickListener {
            startActivity(Intent(requireContext(), GatePassHistoryActivity::class.java))
        }
        binding.commonBtn.tv.setOnClickListener {
            startActivity(Intent(requireContext(), SecondGatePassActivity::class.java))
        }
    }

}