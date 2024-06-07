package com.application.intercom.manager.service_charge

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.DateModelWithName
import com.application.intercom.data.model.local.IdModel
import com.application.intercom.data.model.local.NameIdModel
import com.application.intercom.data.model.local.NameModel
import com.application.intercom.data.model.local.manager.managerSide.rentEdit.RentEditManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerAddServiceChargePostModel
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerNewEditServiceChargePostModel
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerSecondEditServiceChargeModel
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerPropertyListRes
import com.application.intercom.data.model.remote.manager.managerSide.serviceCharege.ManagerServiceChargeList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityServiceChargeBinding
import com.application.intercom.helper.*
import com.application.intercom.manager.bills.UnPaidBillingManagerActivity
import com.application.intercom.manager.managerFlat.ManagerFlatsActivity
import com.application.intercom.manager.rent.RentManagerActivity
import com.application.intercom.utils.*
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


class ServiceChargeActivity : AppCompatActivity(), NameAdapter.Remove {

    private lateinit var binding: ActivityServiceChargeBinding
    private lateinit var viewModel: ManagerSideViewModel
    private var serviceCharge: String = ""
    private var id_list = ArrayList<IdModel>()
    private var checkid_list = ArrayList<IdModel>()
    private var back_id_list = ArrayList<String>()
    private var name_list = ArrayList<NameModel>()
    private var name_and_date_list = ArrayList<DateModelWithName>()
    private var nameId_list = ArrayList<NameIdModel>()
    private var edit_name_list = ArrayList<String>()
    private var flatList = ArrayList<ManagerAddServiceChargePostModel.Flat>()
    private var from: String = ""
    private var backList = kotlin.collections.ArrayList<ManagerPropertyListRes.Data>()
    private lateinit var edit_list: ManagerServiceChargeList.Data
    private var nameAdpter: NameAdapter? = null
    private var dateAmountAdpter: DateAndAmonutAdapter? = null
    private var selectDate: String = ""
    private var amount: String = ""
    private var editMonths: String = ""
    private var editYears: String = ""
    private var selectDateNew: String = ""
    private var chargeId: String = ""
    private var alreadyId_List = ArrayList<IdModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceChargeBinding.inflate(layoutInflater)
       /* CommonUtil.clearLightStatusBar(this)*/

        setContentView(binding.root)
        CommonUtil.clearLightStatusBar(this)
        CommonUtil.themeSet(this, window)
        from = intent.getStringExtra("from").toString()
        if (from.equals("back_flat_screen")) {
            nameId_list = intent.getSerializableExtra("nameIdList") as ArrayList<NameIdModel>
            println("----back_flat_screennameId_list$nameId_list")
            checkid_list = intent.getSerializableExtra("checkId") as ArrayList<IdModel>
            println("----back_flat_screencheckid_list$checkid_list")
            binding.tvBillDate.setText(intent.getStringExtra("date") ?: "")
            selectDate = intent.getStringExtra("date") ?: ""
            editMonths = intent.getStringExtra("setMonths") ?: ""
            editYears = intent.getStringExtra("setYear") ?: ""
            selectDateNew = intent.getStringExtra("selectDateNew") ?: ""

            for (idd in nameId_list) {
                back_id_list.add(idd._id)
                name_list.add(NameModel(idd.name))
                name_and_date_list.add(DateModelWithName(idd.name, idd._id, "", ""))
                println("name${name_and_date_list}")
            }
            binding.namercy.visibility = View.VISIBLE
            binding.namercy.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            nameAdpter = NameAdapter(this, name_list, this)
            binding.namercy.adapter = nameAdpter
            nameAdpter!!.notifyDataSetChanged()
            id_list = intent.getSerializableExtra("idLIst") as ArrayList<IdModel>
            println("----back_flat_screenidloist$id_list")

            binding.rcyAmount.visibility = View.VISIBLE
            binding.rcyAmount.layoutManager = LinearLayoutManager(this)
            dateAmountAdpter = DateAndAmonutAdapter(this, name_and_date_list, "") {
                try {
                    binding.rcyAmount.getChildAt(0)
                        .findViewById<CheckBox>(R.id.checkedAllDate).isChecked = false
                } catch (exception: java.lang.Exception) {

                }
            }
            binding.rcyAmount.adapter = dateAmountAdpter
            dateAmountAdpter!!.notifyDataSetChanged()


        } else if (from.equals("edit_from")) {
            edit_list = intent.getSerializableExtra("editData") as ManagerServiceChargeList.Data
            println("----edit$edit_list")
            chargeId = edit_list._id ?: ""
            name_list.add(NameModel(edit_list.flatId!!.name ?: ""))
            back_id_list.add(edit_list.flatId!!._id ?: "")
            binding.tvBillDate.setText(getYearMonthOfDate(edit_list.date ?: ""))
            selectDate = setNewFormatDate(edit_list.date).toString()
            binding.tvFlatsService1.setText(edit_list.flatId!!.name)
            binding.rcyAmount.visibility = View.VISIBLE
            name_and_date_list.add(
                DateModelWithName(
                    edit_list.flatId?.name ?: "",
                    edit_list.flatId?._id ?: "",
                    edit_list.amount.toString(),
                    edit_list.dueDate ?: ""
                )
            )
            binding.rcyAmount.layoutManager = LinearLayoutManager(this)
            dateAmountAdpter = DateAndAmonutAdapter(this, name_and_date_list, "edit")

            binding.rcyAmount.adapter = dateAmountAdpter
            dateAmountAdpter!!.notifyDataSetChanged()
        } else {
           id_list = intent.getSerializableExtra("idLIst") as ArrayList<IdModel>
            println("----idloist$id_list")
            binding.checkedAllDate.visibility = View.GONE

        }




        initView()
        listener()
        CommonUtil.themeSet(this, window)


    }

    private fun initView() {
        initialize()
        // viewServiceCharge()
        observer()
        binding.btnLogin.tv.text = getString(R.string.set)
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(this, R.color.orange))
        /*  val serviceCharges = resources.getStringArray(R.array.serviceCharges)

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
        if (from.equals("back_flat_screen")) {
            serviceCharge = intent.getStringExtra("billing_Type").toString()
            if (serviceCharge.equals("Monthly")) {
                serviceChargesSpinner.setSelection(1)
            } else {
                serviceChargesSpinner.setSelection(2)
            }
        } else if (from.equals("edit_from")) {
            if (edit_list.serviceChargeType.equals("Monthly")) {
                serviceChargesSpinner.setSelection(1)
            } else {
                serviceChargesSpinner.setSelection(2)
            }
        }*/

    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }



    private fun viewServiceCharge() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )

        viewModel.viewServiceChargeManager(token)
    }

    private fun listener() {
        binding.imageView4.setOnClickListener {
            finish()
        }
        binding.btnLogin.tv.setOnClickListener {
            flatList.clear()
            for (position in 0 until binding.rcyAmount.childCount) {
                val view = binding.rcyAmount.getChildAt(position)
                val amountType =
                    view.findViewById<EditText>(R.id.edt_amount2).text.trim().toString()
                val dueDateType =
                    view.findViewById<EditText>(R.id.edtDueDate2).text.trim().toString()
                flatList.add(
                    ManagerAddServiceChargePostModel.Flat(
                        amountType.toInt(),
                        selectDateNew,
                        dueDateType,
                        name_and_date_list[position].id,
                        editMonths,
                        editYears
                    )
                )

            }
            if (name_list.isEmpty()) {
                Toast.makeText(this, getString(R.string.please_select_flat), Toast.LENGTH_SHORT)
                    .show()
            } else if (!validationData()) {
                return@setOnClickListener
            } else if (from.equals("edit_from")) {
                for (position in 0 until binding.rcyAmount.childCount) {
                    val view = binding.rcyAmount.getChildAt(position)
                    val amountType =
                        view.findViewById<EditText>(R.id.edt_amount2).text.trim().toString()
                    val dueDateType =
                        view.findViewById<EditText>(R.id.edtDueDate2).text.trim().toString()
                    val token = prefs.getString(
                        SessionConstants.TOKEN, ""
                    )
                    /*val model = ManagerNewEditServiceChargePostModel(
                        amountType.toInt(),
                        chargeId,
                        setNewFormatDate(edit_list.date?:""),
                        getMonthOfDateNewFormat(edit_list.date?:""),
                        getYearofDateNew(edit_list.date?:""),
                        dueDateType
                    )
                   viewModel.editServiceChargeManager(token, model)*/
                    val model = RentEditManagerPostModel(
                        chargeId,
                        amountType.toInt(),
                        getMonthOfDateNewFormat(edit_list.date ?: ""),
                        getYearofDateNew(edit_list.date ?: ""),
                        setNewFormatDate(edit_list.date ?: ""),
                        name_and_date_list[position].id,
                        dueDateType
                    )
                    viewModel.editRentBill(token, model)
                }

            } else {
                val token = prefs.getString(
                    SessionConstants.TOKEN, ""
                )
                val model = ManagerAddServiceChargePostModel(
                    flatList
                )
                viewModel.addServiceChargeManager(token, model)
            }


        }
        binding.layoutSelectCategorySpinner1.setOnClickListener {
            if (from.equals("edit_from")) {
                binding.layoutSelectCategorySpinner1.isClickable = false
            } else if (from.equals("back_flat_screen")) {
                // amount = binding.edtAmount.text?.trim().toString()
                startActivity(
                    Intent(this, ManagerFlatsActivity::class.java)
                        .putExtra("from", "back_service")
                        .putExtra("nameIdList", nameId_list)
                        .putExtra("checkId", checkid_list)
                        .putExtra("idLIst", id_list)
                        .putExtra("billing_Type", serviceCharge)
                        .putExtra("date", selectDate)
                        .putExtra("amount", amount)
                )
                finish()
            } else {
                if (selectDate.isEmpty()) {
                    Toast.makeText(this, "Please Select Flat!!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener

                } else {
                    //amount = binding.tvBillDate.text?.trim().toString()
                    startActivity(
                        Intent(this, ManagerFlatsActivity::class.java)
                            .putExtra("idLIst", alreadyId_List)
                            .putExtra("billing_Type", serviceCharge).putExtra("date", selectDate)
                            .putExtra("selectDateNew", selectDateNew).putExtra("setMonths",editMonths).putExtra("setYear",editYears)
                    )
                    finish()
                }

            }

        }
        binding.imageView4.setOnClickListener {
            finish()
        }
        /*binding.tvDateService.setOnClickListener {
            MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build().apply {
                show(supportFragmentManager, this@ServiceChargeActivity.toString())
                addOnPositiveButtonClickListener {
                    selectDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                        Date(it)
                    )
                    binding.tvDateService.setText(
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                            Date(it)
                        )
                    )
                }
            }
        }*/
        binding.tvBillDate.setOnClickListener {
            if (from == "edit_from") {
                binding.tvBillDate.isClickable = false
            } else {
                MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build().apply {
                    show(supportFragmentManager, this@ServiceChargeActivity.toString())
                    addOnPositiveButtonClickListener {
                        editMonths = getMonthOfSelectDate(
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                Date(it)
                            )
                        )
                        editYears = getMonthOfSelectYear(
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                Date(it)
                            )
                        )
                        selectDate = getMonthOfDateNew(
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                Date(it)
                            )
                        )
                        selectDateNew = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                            Date(it)
                        )
                        binding.tvBillDate.setText(
                            selectDate
                        )
                        alreadyId_List.clear()

                        for (i in 0 until ((intent.getSerializableExtra("idLIst") as ArrayList<IdModel>).size)) {
                            if (editYears == (intent.getSerializableExtra("idLIst") as ArrayList<IdModel>)[i].year) {
                                if (editMonths == (intent.getSerializableExtra("idLIst") as ArrayList<IdModel>)[i].months) {
                                    alreadyId_List.add((intent.getSerializableExtra("idLIst") as ArrayList<IdModel>)[i])
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private fun observer() {
        viewModel.viewServiceChargeManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
//                            list.add(it.data)
                            if (it.data != null) {
                                startActivity(
                                    Intent(
                                        this,
                                        ServiceChargesDetailsActivity::class.java
                                    )
                                )
                            } else {

                            }

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
        viewModel.addServiceChargeManagerLiveData.observe(this, androidx.lifecycle.Observer {
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
                                    this, ServiceChargeListActivity::class.java
                                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
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
        /*  viewModel.editServiceChargeManagerLiveData.observe(this, androidx.lifecycle.Observer {
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
                                      this, ServiceChargeListActivity::class.java
                                  ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                      .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
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
          })*/
        viewModel.editRentBillLiveData.observe(this, androidx.lifecycle.Observer {
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
                                    this, ServiceChargeListActivity::class.java
                                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
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
        /*if (binding.spinnerSelectFlat.selectedItemPosition.equals(0)) {
            Toast.makeText(
                applicationContext, getString(R.string.please_select_billing_type), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.tvDateService.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_select_date), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edtAmount.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_amount), Toast.LENGTH_SHORT
            ).show()
            return false
        }*/

        if (TextUtils.isEmpty(binding.tvBillDate.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Select Billing Date!!", Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true

    }

    override fun removeItem(positionNew: Int) {
        name_list.removeAt(positionNew)
        name_and_date_list.removeAt(positionNew)
        nameAdpter?.removeItem(name_list, positionNew)
        dateAmountAdpter?.removeItem(name_and_date_list, positionNew)


    }

}