package com.application.intercom.user.parking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.application.intercom.R
import com.application.intercom.databinding.BottomsheetParkingFilterbyBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class ParkingFilterByBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetParkingFilterbyBinding

    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetParkingFilterbyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener {
            dismiss()
        }
        binding.btnSubmit.tv.text = "Apply Filters (3)"
        binding.btnSubmit.tv.setOnClickListener {
            dismiss()
        }
        val location = resources.getStringArray(R.array.location)
        val month = resources.getStringArray(R.array.month)
        val minRent = resources.getStringArray(R.array.min_rent)
        val maxRent = resources.getStringArray(R.array.max_rent)

        // access the spinner
        val locationSpinner = binding.spinnerLocation
        val monthSpinner = binding.spinnerMonth
        val minSpinner = binding.spinnerMin
        val maxSpinner = binding.spinnerMax
        val monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, month)
        val locationAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, location)
        val minAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, minRent)
        val maxAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, maxRent)
        locationSpinner.adapter = locationAdapter
        monthSpinner.adapter = monthAdapter
        minSpinner.adapter = minAdapter
        maxSpinner.adapter = maxAdapter

        minSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
//                Toast.makeText(
//                    this@AddBillsActivity,
//                    "selected_item" + " " +
//                            "" + languages[position], Toast.LENGTH_SHORT
//                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        maxSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
//                Toast.makeText(
//                    this@AddBillsActivity,
//                    "selected_item" + " " +
//                            "" + languages[position], Toast.LENGTH_SHORT
//                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        locationSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
//                Toast.makeText(
//                    this@AddBillsActivity,
//                    "selected_item" + " " +
//                            "" + languages[position], Toast.LENGTH_SHORT
//                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        monthSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
//                Toast.makeText(
//                    this@AddBillsActivity,
//                    "selected_item" + " " +
//                            "" + languages[position], Toast.LENGTH_SHORT
//                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
}