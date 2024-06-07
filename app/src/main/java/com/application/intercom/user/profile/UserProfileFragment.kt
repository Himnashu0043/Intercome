package com.application.intercom.user.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.getUserDetailsViewModel.GetUserDetailsViewModel
import com.application.intercom.data.model.factory.getUserDetailsFactory.GetUserDetailsFactory
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ServiceRepository
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.data.repository.getUserDetailsRepo.GetUserDetailsRepo
import com.application.intercom.databinding.FragmentUserProfileBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.tenant.activity.profile.EditProfileActivity
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.user.home.UserTopAdvitAdapter
import com.application.intercom.user.login.LoginUsingOtpActivity
import com.application.intercom.user.service.ServiceFactory
import com.application.intercom.user.service.ServiceViewModel
import com.application.intercom.utils.*
import java.util.*

class UserProfileFragment : Fragment() {
    lateinit var binding: FragmentUserProfileBinding
    private lateinit var getUserDetailsViewModel: GetUserDetailsViewModel
    private var email: String = ""
    private var number: String = ""
    private var name: String = ""
    private var from: String = ""
    private var url: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(layoutInflater)

        initView()
        listener()
        return binding.root
    }

    private fun initView() {
        var key =
            prefs.getString(SessionConstants.ROLE, GPSService.mLastLocation?.latitude.toString())
        println("=======$key")
        if (key == "owner") {
            binding.constraintLayout8.visibility = View.INVISIBLE
            binding.commonBtn.root.visibility = View.INVISIBLE
            binding.mainProfile.visibility = View.VISIBLE
        } else {
            binding.constraintLayout8.visibility = View.VISIBLE
            binding.commonBtn.root.visibility = View.INVISIBLE
            binding.mainProfile.visibility = View.INVISIBLE
        }
        initialize()
        observer()

    }

    private fun listener() {
        binding.tvEdit.setOnClickListener {
            startActivity(
                Intent(requireContext(), EditProfileActivity::class.java).putExtra("from", from)
                    .putExtra("name", name).putExtra("email", email).putExtra("url", url)
                    .putExtra("userFag", true)
            )
        }

    }

    private fun initialize() {
        val getUserDetailsrepo = GetUserDetailsRepo(BaseApplication.apiService)
        getUserDetailsViewModel = ViewModelProvider(
            this, GetUserDetailsFactory(getUserDetailsrepo)
        )[GetUserDetailsViewModel::class.java]

    }

    private fun getUserDetails() {
        val token = prefs.getString(
            SessionConstants.TOKEN, /*GPSService.mLastLocation?.latitude.toString()*/""
        )
        getUserDetailsViewModel.userDetails(token)

    }

    private fun observer() {
        getUserDetailsViewModel.userDetailsLiveData.observe(requireActivity(),
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(requireActivity())
                    }

                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                if (it.data.userDetails.fullName.isNullOrEmpty()) {
                                    binding.textView145.text = ""
                                } else {
                                    println("---name${it.data.userDetails.fullName}")
                                    binding.textView145.text = it.data.userDetails.fullName
                                    name = it.data.userDetails.fullName
                                }
                                if (it.data.userDetails.email.isNullOrEmpty()) {
                                    binding.textView147.text = ""
                                } else {
                                    binding.textView147.text = it.data.userDetails.email
                                    email = it.data.userDetails.email ?: ""
                                }
                                if (it.data.userDetails.phoneNumber.isNullOrEmpty()) {
                                    binding.textView149.text = ""
                                } else {
                                    binding.textView149.text = it.data.userDetails.phoneNumber
                                    number = it.data.userDetails.phoneNumber
                                }
                                if (it.data.userDetails.profilePic.isNullOrEmpty()) {

                                } else {
                                    binding.ivchange.loadImagesWithGlideExt(it.data.userDetails.profilePic)
                                    url = it.data.userDetails.profilePic
                                    binding.textView1452.text = it.data.userDetails.role
                                    binding.textView1472.text = it.data.userDetails.email ?: ""
                                    binding.textView1492.text = it.data.userDetails.phoneNumber
                                    binding.tvAddress.text = it.data.userDetails.address ?: ""
                                    binding.tvNidNumber.text = it.data.userDetails.nid
                                    binding.ivNid.loadImagesWithGlideExt(it.data.userDetails.nidImage)

                                    binding.tvRefName.text = it.data.userDetails.refName
                                    binding.tvRefNidNumber.text = it.data.userDetails.refNid
                                    binding.ivRef.loadImagesWithGlideExt(it.data.userDetails.refNidImage)

                                    binding.tvAccountNumber.text = it.data.userDetails.accnHolder
                                    binding.tvBankName.text = it.data.userDetails.bankName
                                    binding.tvBranchName.text = it.data.userDetails.branchName
                                    binding.tvBankAccHNumber.text = it.data.userDetails.accnNumber

                                    binding.tvMfsHName.text = it.data.userDetails.mfsHolder
                                    binding.tvMfsType.text = it.data.userDetails.mfs
                                    binding.tvMfsAccNumber.text = it.data.userDetails.mfsAccnNumber

                                }
                                println("=====profileList${it.data.userDetails}")
                            } else if (it.status == AppConstants.STATUS_404) {
                                requireActivity().longToast(it.message)
                            } else if (it.status == AppConstants.STATUS_503) {
                                val intent = Intent(
                                    requireContext(), LoginUsingOtpActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            } else {

                            }
                        }
                    }

                    is EmpResource.Failure -> {
                        EmpCustomLoader.hideLoader()
                        ErrorUtil.handlerGeneralError(requireActivity(), it.throwable!!)
                    }
                    else -> {}
                }
            })

    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }
}