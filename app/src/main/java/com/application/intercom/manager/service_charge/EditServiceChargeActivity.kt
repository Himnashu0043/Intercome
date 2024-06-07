package com.application.intercom.manager.service_charge

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerAddServiceChargePostModel
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerEditServiceChargePostModel
import com.application.intercom.data.model.remote.manager.managerSide.serviceCharege.ManagerEditServiceChargeListRes
import com.application.intercom.data.model.remote.manager.managerSide.serviceCharege.ManagerViewServiceChargeRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityEditServiceChargeBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.setFormatDate
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.utils.*
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditServiceChargeActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditServiceChargeBinding
    private lateinit var viewModel: ManagerSideViewModel
    private var serviceCharge: String = ""
    private var chargeId: String = ""
    private var from = ArrayList<ManagerViewServiceChargeRes.Data>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditServiceChargeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        from = intent.getSerializableExtra("from") as ArrayList<ManagerViewServiceChargeRes.Data>
        println("---from$from")

        initView()
        listener()
    }

    private fun initView() {

        initialize()
        observer()

        binding.btnLogin.tv.text = "Edit"
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(this, R.color.orange))

        val serviceCharges = resources.getStringArray(R.array.serviceCharges)

        val serviceChargesSpinner = binding.spinnerSelectFlat

        val serviceChargesSpinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, serviceCharges)

        serviceChargesSpinner.adapter = serviceChargesSpinnerAdapter
        serviceChargesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                if (binding.spinnerSelectFlat.selectedItemPosition > 0) {
                    serviceCharge =
                        serviceChargesSpinner.getSelectedItem().toString()
                    println("-----srr$serviceCharge")
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        chargeId = from.get(0)._id!!
        val editDate = setNewFormatDate(from.get(0).date)
        binding.tvDateService.text = editDate
        val amount = from.get(0).amount
        binding.edammount.setText(amount.toString())
        when (from.get(0).serviceChargeType) {
            "Monthly" -> {
                serviceChargesSpinner.setSelection(1)
            }
            "Yearly" -> {
                serviceChargesSpinner.setSelection(2)
            }
        }
    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

//    private fun editServiceCharge() {
//        val token = prefs.getString(
//            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
//        )
//        val data = binding.edammount.text.trim().toString()
//        val model = ManagerEditServiceChargePostModel(
//            data.toInt(), chargeId, binding.tvDateService.text.trim().toString(), serviceCharge
//        )
//        viewModel.editServiceChargeManager(token, model)
//    }

    private fun listener() {
        binding.btnLogin.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            }
//            editServiceCharge()

        }
        binding.imageView4.setOnClickListener {
            finish()
        }
        binding.tvDateService.setOnClickListener {
            MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build().apply {
                show(supportFragmentManager, this@EditServiceChargeActivity.toString())
                addOnPositiveButtonClickListener {
                    binding.tvDateService.setText(
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                            Date(it)
                        )
                    )
                }
            }
        }
    }

    private fun observer() {
        viewModel.editServiceChargeManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(
                                    this, ServiceChargesDetailsActivity::class.java
                                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                            finish()
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else {

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

    private fun validationData(): Boolean {
        if (binding.spinnerSelectFlat.selectedItemPosition.equals(0)) {
            Toast.makeText(
                applicationContext, "Please Select Service Charges!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.tvDateService.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Select Date!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edammount.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Amount!!", Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true

    }
}