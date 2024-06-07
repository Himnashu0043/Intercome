package com.application.intercom.manager.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.application.intercom.databinding.DialogNoticeDateAndTimeBinding
import com.application.intercom.helper.setWidthPercent


class NoticeDateAndTimeDialog : DialogFragment() {
    private lateinit var binding: DialogNoticeDateAndTimeBinding

    companion object {

        const val TAG = "SimpleDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(title: String, subTitle: String): NoticeDateAndTimeDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            val fragment = NoticeDateAndTimeDialog()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogNoticeDateAndTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitleDesc()
        binding.btnLogin.tv.text = "Schedule"
    }
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//
//        binding = DialogNoticeDateAndTimeBinding.inflate(LayoutInflater.from(context))
//        return AlertDialog.Builder(requireActivity())
//            .setView(binding.root)
//            .create()
//    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setWidthPercent(90)
    }

    private fun setTitleDesc() {
//        binding.btnLogin.tv.text = "Okay"
//        binding.tvTitle.text = arguments?.getString(KEY_TITLE)
//        binding.tvDesc.text = arguments?.getString(KEY_SUBTITLE)
    }


}