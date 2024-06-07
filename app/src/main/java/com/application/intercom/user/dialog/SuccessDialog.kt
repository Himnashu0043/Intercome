package com.application.intercom.user.dialog

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.databinding.DialogSuccessBinding
import com.application.intercom.helper.setWidthPercent
import com.application.intercom.user.profile.CompleteProfileActivity
import com.application.intercom.user.property.PropertyDetailsActivity


class SuccessDialog : DialogFragment() {
    private lateinit var binding: DialogSuccessBinding

    companion object {

        const val TAG = "SimpleDialog"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private const val KEY_IS_FROM = "KEY_IS_FROM"
        private const val ID = "ID"

        fun newInstance(
            title: String,
            subTitle: String,
            isFrom: String,
            id: String
        ): SuccessDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            args.putString(KEY_IS_FROM, isFrom)
            args.putString(ID, id)
            val fragment = SuccessDialog()
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
        return inflater.inflate(R.layout.dialog_success, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isFrom = arguments?.getString("KEY_IS_FROM")
        val id = arguments?.getString("ID")
        println("-----isfrom${isFrom}")
        println("----iiiiii${id}")
        setTitleDesc()
        binding.btnLogin.tv.setOnClickListener {
            /* if (isFrom == "subscription") {
                 startActivity(Intent(requireContext(), CompleteProfileActivity::class.java))

             }*/
            if (isFrom == "user_parking_details") {
                startActivity(
                    Intent(
                        requireContext(),
                        CompleteProfileActivity::class.java
                    ).putExtra("from", "user_parking_details").putExtra("id", id)
                )
            } else if (isFrom == "user_property_details") {
                startActivity(
                    Intent(
                        requireContext(),
                        CompleteProfileActivity::class.java
                    ).putExtra("from", "user_property_details").putExtra("id", id)
                )
            } else if (isFrom == "tenant_parking_details") {
                startActivity(
                    Intent(
                        requireContext(),
                        CompleteProfileActivity::class.java
                    ).putExtra("from", "tenant_parking_details").putExtra("id", id)
                )
            } else if (isFrom == "ownerSide_property") {
                startActivity(
                    Intent(
                        requireContext(),
                        CompleteProfileActivity::class.java
                    ).putExtra("from", "ownerSide_property").putExtra("id", id)
                )
            } else if (isFrom == "complete_tenant_parking_details") {
                startActivity(
                    Intent(
                        requireContext(),
                        PropertyDetailsActivity::class.java
                    ).putExtra("from", "tenant_parking_details").putExtra("id", id)
                )
            } else if (isFrom == "complete_profile_parking") {
                startActivity(
                    Intent(
                        requireContext(),
                        PropertyDetailsActivity::class.java
                    ).putExtra("from", "complete_profile_parking").putExtra("id", id)
                )
            } else if (isFrom == "complete_ownerSide_property") {
                startActivity(
                    Intent(
                        requireContext(),
                        PropertyDetailsActivity::class.java
                    ).putExtra("from", "ownerSide_property").putExtra("id", id)
                )
            } else if (isFrom == "user_side_menu") {
                startActivity(
                    Intent(
                        requireContext(),
                        MainActivity::class.java
                    ).putExtra("from", "from_side_home")
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                requireActivity().finish()
            } else {
                startActivity(
                    Intent(
                        requireContext(),
                        PropertyDetailsActivity::class.java
                    ).putExtra("from", "complete_profile_property").putExtra("id", id)
                )
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogSuccessBinding.inflate(LayoutInflater.from(context))
        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    override fun onStart() {
        super.onStart()
        setWidthPercent(90)
    }

    private fun setTitleDesc() {
        binding.btnLogin.tv.text = "Okay"
        binding.tvTitle.text = arguments?.getString(KEY_TITLE)
        binding.tvDesc.text = arguments?.getString(KEY_SUBTITLE)
    }


}