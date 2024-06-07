package com.application.intercom.user.property

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.application.intercom.R
import com.application.intercom.databinding.BottomsheetPropertyFilterbyBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class PropertyFilterByBottomSheet(val fiterLis:CLickFilter) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetPropertyFilterbyBinding
    private var bhk: Int = 0
    private var typeFlat: String = ""
    private var minValue: String = ""
    private var maxValue: String = ""

    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetPropertyFilterbyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener {
            dismiss()
        }
        binding.btnSubmit.tv.text = "Apply Filters"
        binding.btnSubmit.tv.setOnClickListener {
            fiterLis.onCLickFilter(minValue,maxValue,bhk,typeFlat)
            dismiss()
        }
        binding.tvClear.setOnClickListener {
            dismiss()
        }


        val location = resources.getStringArray(R.array.location)
        val minRent = resources.getStringArray(R.array.min_rent)
        val maxRent = resources.getStringArray(R.array.max_rent)

        // access the spinner
        val locationSpinner = binding.spinnerLocation
        val minSpinner = binding.spinnerMin
        val maxSpinner = binding.spinnerMax
        val locationAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, location)
        val minAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, minRent)
        val maxAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, maxRent)
        locationSpinner.adapter = locationAdapter
        minSpinner.adapter = minAdapter
        maxSpinner.adapter = maxAdapter

        minSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                minValue = parent!!.getItemAtPosition(position) as String
                println("-----mmmmm${minValue}")
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
                maxValue = parent!!.getItemAtPosition(position) as String
                println("-----max${maxValue}")
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
        /* locationSpinner.onItemSelectedListener = object :
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
         }*/

        binding.cbOneBhk.setOnClickListener {
            bhk = 1
            binding.cbOneBhk.isChecked = true
            binding.cbTwoBhk.isChecked = false
            binding.cbThreeBhk.isChecked = false
            binding.cbFourBhk.isChecked = false
        }
        binding.cbTwoBhk.setOnClickListener {
            bhk = 2
            binding.cbOneBhk.isChecked = false
            binding.cbTwoBhk.isChecked = true
            binding.cbThreeBhk.isChecked = false
            binding.cbFourBhk.isChecked = false
        }
        binding.cbThreeBhk.setOnClickListener {
            bhk = 3
            binding.cbOneBhk.isChecked = false
            binding.cbTwoBhk.isChecked = false
            binding.cbThreeBhk.isChecked = true
            binding.cbFourBhk.isChecked = false
        }
        binding.cbFourBhk.setOnClickListener {
            bhk = 4
            binding.cbOneBhk.isChecked = false
            binding.cbTwoBhk.isChecked = false
            binding.cbThreeBhk.isChecked = false
            binding.cbFourBhk.isChecked = true
        }
        binding.cbToLet.setOnClickListener {
            typeFlat = "toLet"
            binding.cbToLet.isChecked = true
            binding.cbSale.isChecked = false
        }
        binding.cbSale.setOnClickListener {
            typeFlat = "sale"
            binding.cbToLet.isChecked = false
            binding.cbSale.isChecked = true
        }

    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    interface CLickFilter {
        fun onCLickFilter(min:String,max:String,flatType:Int,parkingStatus:String)
    }
}