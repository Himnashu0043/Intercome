package com.application.intercom.manager.dialog

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.manager.managerSide.actionToComplain.ManagerActionToComplainPostModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.DialogComplaintBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.setWidthPercent
import com.application.intercom.manager.complaint.RegisterComplaintsActivity
import com.application.intercom.utils.*


class ComplaintDialog : DialogFragment() {
    private lateinit var binding: DialogComplaintBinding
    private lateinit var viewModel: ManagerSideViewModel

    companion object {

        const val TAG = "SimpleDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private const val KEY_ID = "KEY_ID"
        private const val KEY_ACTION_TYPE = "KEY_ACTION_TYPE"
        private var type: String = ""
        private var get_id: String = ""

        fun newInstance(
            title: String,
            subTitle: String,
            id: String,
            actionType: String
        ): ComplaintDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            args.putString(KEY_ID, id)
            args.putString(KEY_ACTION_TYPE, actionType)
            type = args.getString(KEY_ACTION_TYPE).toString()
            get_id = args.getString(KEY_ID).toString()

            val fragment = ComplaintDialog()
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
        return inflater.inflate(R.layout.dialog_complaint, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitleDesc()

        binding.btnYes.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.l_green))
        binding.btnYes.tv.setOnClickListener {
            dismiss()
            actionToComplain()

        }
        binding.btnNo.tv.setOnClickListener { dismiss() }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogComplaintBinding.inflate(LayoutInflater.from(context))
        initialize()
        observer()
        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

    private fun actionToComplain() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation!!.latitude.toString()
        )
        val model = ManagerActionToComplainPostModel(get_id, type)
        viewModel.actionToComplain(token, model)
    }

    private fun observer() {
        viewModel.actionToComplainLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(
                                    requireContext(),
                                    RegisterComplaintsActivity::class.java
                                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )

                        } else if (it.status == AppConstants.STATUS_500) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireContext().longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })
    }

    override fun onStart() {
        super.onStart()
        setWidthPercent(90)
    }

    private fun setTitleDesc() {
        binding.btnYes.tv.text = "Yes"
        binding.btnNo.tv.text = "No"
//        binding.btnLogin.tv.text = "Okay"
//        binding.tvTitle.text = arguments?.getString(KEY_TITLE)
//        binding.tvDesc.text = arguments?.getString(KEY_SUBTITLE)
    }


}