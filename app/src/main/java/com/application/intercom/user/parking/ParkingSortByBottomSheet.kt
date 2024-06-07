package com.application.intercom.user.parking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.application.intercom.R
import com.application.intercom.databinding.BottomsheetParkingSortbyBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ParkingSortByBottomSheet(val lis:Click) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetParkingSortbyBinding
    private var sortValue: String = ""
    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetParkingSortbyBinding.inflate(inflater, container, false)
        return binding.root   }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener {
            dismiss()
        }
        binding.rentHighToLow.setOnClickListener {
            sortValue = "highToLow"
            lis.onCLickSortProperty(sortValue)
            dismiss()
        }
        binding.rentLowToHigh.setOnClickListener {
            sortValue = "lowToHigh"
            lis.onCLickSortProperty(sortValue)
            dismiss()
        }
    }
    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
    interface Click {
        fun onCLickSortProperty(name: String)
    }
}