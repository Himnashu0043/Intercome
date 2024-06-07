package com.application.intercom.manager.dialog

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.application.intercom.databinding.DialogNoticeDetailsBinding
import com.application.intercom.helper.setWidthPercent
import com.application.intercom.user.property.PropertyDetailsActivity


class NoticeDetailsDialog : DialogFragment() {
    private lateinit var binding: DialogNoticeDetailsBinding

    companion object {

        const val TAG = "SimpleDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(title: String, subTitle: String): NoticeDetailsDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            val fragment = NoticeDetailsDialog()
            fragment.arguments = args
            return fragment
        }

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitleDesc()
        binding.btnLogin.tv.setOnClickListener {
            startActivity(Intent(requireContext(), PropertyDetailsActivity::class.java))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        binding = DialogNoticeDetailsBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setWidthPercent(90)
    }

    private fun setTitleDesc() {
        binding.btnLogin.tv.text = "Okay"
        binding.tvTitle.text = arguments?.getString(KEY_TITLE)
        binding.tvDesc.text = arguments?.getString(KEY_SUBTITLE)
    }


}