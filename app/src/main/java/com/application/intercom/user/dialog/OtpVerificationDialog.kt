package com.application.intercom.user.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.application.intercom.databinding.DialogOtpVerificationBinding
import com.application.intercom.helper.setWidthPercent


class OtpVerificationDialog : DialogFragment() {
    private lateinit var binding: DialogOtpVerificationBinding

    companion object {

        const val TAG = "SimpleDialog"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(title: String, subTitle: String): OtpVerificationDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            val fragment = OtpVerificationDialog()
            fragment.arguments = args
            return fragment
        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogOtpVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }


//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        binding = DialogOtpVerificationBinding.inflate(LayoutInflater.from(context))
//        return AlertDialog.Builder(requireActivity())
//            .setView(binding.root)
//            .create()
//    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        otpTextChanged()
        setupView(view)
        setupClickListeners(view)

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setWidthPercent(90)
//        dialog?.window?.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
    }

    private fun setupView(view: View) {
//        view.tvTitle.text = arguments?.getString(KEY_TITLE)
//        view.tvSubTitle.text = arguments?.getString(KEY_SUBTITLE)
    }


    private fun setupClickListeners(view: View) {
//        view.btnPositive.setOnClickListener {
//            dismiss()
//        }
//        view.btnNegative.setOnClickListener {
//            dismiss()
//        }

    }
    fun otpTextChanged() {
        binding.edtOne.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString()
                        .isEmpty()
                ) binding.edtTwo.requestFocus() else binding.edtOne.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edtTwo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString()
                        .isEmpty()
                ) binding.edtThree.requestFocus() else binding.edtOne.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edtThree.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString()
                        .isEmpty()
                ) binding.edtFour.requestFocus() else binding.edtTwo.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edtFour.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString()
                        .isEmpty()
                ) binding.edtFive.requestFocus() else binding.edtThree.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.edtFive.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString()
                        .isEmpty()
                ) binding.edtSix.requestFocus() else binding.edtFour.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edtSix.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString()
                        .isEmpty()
                ) binding.edtSix.requestFocus() else binding.edtFive.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }
}