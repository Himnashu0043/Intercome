package com.application.intercom.user.dialog

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.application.intercom.R
import com.application.intercom.helper.setWidthPercent


class RegisterMemberDialog : DialogFragment() {

    companion object {

        const val TAG = "SimpleDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(title: String, subTitle: String): RegisterMemberDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            val fragment = RegisterMemberDialog()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_register_member, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageView>(R.id.iv_cross).setOnClickListener {
            dismiss()
        }
        setupView(view)
        setupClickListeners(view)

        val spannableString = SpannableString(context?.getString(R.string.tv_register_member_note))

        // Creating the spans to style the string

        // Creating the spans to style the string
        val foregroundColorSpanBlack = ForegroundColorSpan(Color.BLACK)
        val boldSpan = StyleSpan(Typeface.BOLD)
        // Setting the spans on spannable string

        // Setting the spans on spannable string
        spannableString.setSpan(foregroundColorSpanBlack, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(boldSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.findViewById<TextView>(R.id.tv_note).text = spannableString
    }

    override fun onStart() {
        super.onStart()
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

}