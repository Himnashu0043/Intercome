package com.application.intercom.user.property

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.application.intercom.R
import com.application.intercom.databinding.BottomsheetPropertySortbyBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class PropertySortByBottomSheet(val lis: Click) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetPropertySortbyBinding
    private var sortValue: String = ""

    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetPropertySortbyBinding.inflate(inflater, container, false)
        initView()
        listener()
        return binding.root
    }

    private fun initView() {

    }

    private fun listener() {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    interface Click {
        fun onCLickSortProperty(name: String)
    }

}