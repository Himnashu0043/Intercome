package com.application.intercom.user.contact

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.addUserEnquriy.AddUserEnquiryViewModel
import com.application.intercom.data.model.factory.addUserEnquiryFactory.AddUserEnquiryFactory
import com.application.intercom.data.model.local.addUserEnquiryPost.AddUserEnquiryPostModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.userAddEnquiry.UserAddEnquiryRepo
import com.application.intercom.databinding.ActivityContactUsBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.utils.*

class ContactUsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactUsBinding
    private lateinit var viewModel: AddUserEnquiryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        listener()


    }

    private fun initView() {
        initialize()
        observer()
        binding.btnSubmit.tv.text = getString(R.string.contact_us)
        binding.toolbar.tvTittle.text = getString(R.string.contact_us)
    }

    private fun addUserEnquiry() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            /*GPSService.mLastLocation!!.latitude.toString()*/""
        )
        val model = AddUserEnquiryPostModel(
            binding.edtName.text.trim().toString(),
            binding.edtEmail.text.trim().toString(),
            binding.edtMobileNumber.text?.trim().toString(),
            binding.edtDetails.text?.trim().toString()
        )
        viewModel.addUserEnquiry(token, model)

    }

    private fun observer() {
        viewModel.addUserEnquiryLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            finish()
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else {
                            CommonUtil.showSnackBar(this, "Something went wrong!!")
                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })


    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }

//        binding.btnSubmit.tv.setOnClickListener {
//            SuccessDialog.newInstance(
//                "  Submitted", " Our executive will connect with you.", "contact_us", ""
//            )
//                .show(supportFragmentManager, SuccessDialog.TAG)
//
//        }
        binding.btnSubmit.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            }
            addUserEnquiry()
        }
    }

    private fun initialize() {
        val repo = UserAddEnquiryRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this,
            AddUserEnquiryFactory(repo)
        )[AddUserEnquiryViewModel::class.java]


    }

    private fun validationData(): Boolean {
        if (binding.edtName.text.trim().toString().length < 4) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_name), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edtEmail.text.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_email_id), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (!Validator.isValidEmail(
                CommonUtil.getProperText(
                    binding.edtEmail
                )
            )
        ) {
            Toast.makeText(this, getString(R.string.invalid_email_id), Toast.LENGTH_SHORT).show()
            return false

        } else if (TextUtils.isEmpty(binding.edtMobileNumber.text!!.trim().toString())) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_enter_phone_number),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.edtMobileNumber.text!!.trim()
                .toString().length < 10 || binding.edtMobileNumber.text!!.trim()
                .toString().length > 12
        ) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_enter_valid_phone_number),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edtDetails.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_details), Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true

    }
}