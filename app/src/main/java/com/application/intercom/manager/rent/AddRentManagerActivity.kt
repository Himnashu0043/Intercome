package com.application.intercom.manager.rent

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
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
import com.application.intercom.data.model.remote.manager.managerSide.rent.AddRentManagerPostModel
import com.application.intercom.data.model.remote.manager.managerSide.rent.RentManagerListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityAddRentManagerBinding
import com.application.intercom.helper.*
import com.application.intercom.manager.service_charge.DateAndAmonutAdapter
import com.application.intercom.manager.service_charge.NameAdapter
import com.application.intercom.utils.*
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class AddRentManagerActivity : BaseActivity<ActivityAddRentManagerBinding>(), NameAdapter.Remove {
    private var from: String = ""
    private var editMonths: String = ""
    private var editYears: String = ""
    private var selectDateNew: String = ""
    private var chargeId: String = ""
    private var selectDate: String = ""
    private var id_list = ArrayList<IdModel>()
    private var nameId_list = ArrayList<NameIdModel>()
    private var checkid_list = ArrayList<IdModel>()
    private var back_id_list = ArrayList<String>()
    private var name_list = ArrayList<NameModel>()
    private lateinit var viewModel: ManagerSideViewModel
    private var name_and_date_list = ArrayList<DateModelWithName>()
    private var nameAdpter: NameAdapter? = null
    private var dateAmountAdpter: DateAndAmonutAdapter? = null
    private lateinit var edit_list: RentManagerListRes.Data.Result
    private var amount: String = ""
    private var serviceCharge: String = ""
    private var flatList = ArrayList<AddRentManagerPostModel.Flat>()
    private var alreadyId_List = ArrayList<IdModel>()
    override fun getLayout(): ActivityAddRentManagerBinding {
        return ActivityAddRentManagerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonUtil.themeSet(this, window)
        from = intent.getStringExtra("from").toString()
        if (from.equals("back_flat_screen")) {
            nameId_list = intent.getSerializableExtra("nameIdList") as ArrayList<NameIdModel>
            println("----back_flat_screennameId_list$nameId_list")
            checkid_list = intent.getSerializableExtra("checkId") as ArrayList<IdModel>
            println("----back_flat_screencheckid_list$checkid_list")
            binding.tvRentDate.setText(intent.getStringExtra("date") ?: "")
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
            binding.nameRentrcy.visibility = View.VISIBLE
            binding.nameRentrcy.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            nameAdpter = NameAdapter(this, name_list, this)
            binding.nameRentrcy.adapter = nameAdpter
            nameAdpter!!.notifyDataSetChanged()
            id_list = intent.getSerializableExtra("idLIst") as ArrayList<IdModel>
            println("----back_flat_screenidloist$id_list")

            binding.rcyRentAmount.visibility = View.VISIBLE
            binding.rcyRentAmount.layoutManager = LinearLayoutManager(this)
            dateAmountAdpter = DateAndAmonutAdapter(this, name_and_date_list, "") {
                try {
                    binding.rcyRentAmount.getChildAt(0)
                        .findViewById<CheckBox>(R.id.checkedAllDate).isChecked = false
                } catch (exception: Exception) {

                }
            }
            binding.rcyRentAmount.adapter = dateAmountAdpter
            dateAmountAdpter!!.notifyDataSetChanged()


        } else if (from.equals("edit_from")) {
            edit_list = intent.getSerializableExtra("editData") as RentManagerListRes.Data.Result
            println("----edit$edit_list")
            chargeId = edit_list._id ?: ""
            name_list.add(NameModel(edit_list.flatInfo?.get(0)?.name ?: ""))
            back_id_list.add(edit_list.flatInfo?.get(0)?._id ?: "")
            binding.tvRentDate.setText(getYearMonthOfDate(edit_list.date ?: ""))
            selectDate = setNewFormatDate(edit_list.date).toString()
            binding.tvRentService1.setText(edit_list.flatInfo?.get(0)?.name)
            binding.rcyRentAmount.visibility = View.VISIBLE
            name_and_date_list.add(
                DateModelWithName(
                    edit_list.flatInfo?.get(0)?.name ?: "",
                    edit_list.flatInfo?.get(0)?._id ?: "",
                    edit_list.amount.toString(),
                    edit_list.dueDate ?: ""
                )
            )
            println("====editData$name_and_date_list")
            binding.rcyRentAmount.layoutManager = LinearLayoutManager(this)
            dateAmountAdpter = DateAndAmonutAdapter(this, name_and_date_list, "edit")

            binding.rcyRentAmount.adapter = dateAmountAdpter
            dateAmountAdpter!!.notifyDataSetChanged()
        } else {
            id_list = intent.getSerializableExtra("idLIst") as ArrayList<IdModel>
            println("----idloist$id_list")
//            binding.checkedAllDate.visibility = View.GONE

        }

        initView()
        listener()
    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

    private fun observer() {
        viewModel.addRentManagerLiveData.observe(this, androidx.lifecycle.Observer {
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
                                    this, RentManagerActivity::class.java
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
                                    this, RentManagerActivity::class.java
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

    private fun initView() {
        initialize()
        observer()
        binding.btnLogin.tv.text = "Generate Bill"
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(this, R.color.orange))
    }

    private fun listener() {
      /*  binding.layoutSelectCategorySpinner1.setOnClickListener {
            startActivity(Intent(this, RentFlatManagerActivity::class.java))
        }*/
        binding.tvRentDate.setOnClickListener {
            if (from == "edit_from") {
                binding.tvRentDate.isClickable = false
            } else {
                MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build().apply {
                    show(supportFragmentManager, this@AddRentManagerActivity.toString())
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
                        binding.tvRentDate.setText(
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
        binding.btnLogin.tv.setOnClickListener {
            flatList.clear()
            for (position in 0 until binding.rcyRentAmount.childCount) {
                val view = binding.rcyRentAmount.getChildAt(position)
                val amountType =
                    view.findViewById<EditText>(R.id.edt_amount2).text.trim().toString()
                val dueDateType =
                    view.findViewById<EditText>(R.id.edtDueDate2).text.trim().toString()
                flatList.add(
                    AddRentManagerPostModel.Flat(
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
                for (position in 0 until binding.rcyRentAmount.childCount) {
                    val view = binding.rcyRentAmount.getChildAt(position)
                    val amountType =
                        view.findViewById<EditText>(R.id.edt_amount2).text.trim().toString()
                    val dueDateType =
                        view.findViewById<EditText>(R.id.edtDueDate2).text.trim().toString()
                    val token = prefs.getString(
                        SessionConstants.TOKEN, ""
                    )
                    val model = RentEditManagerPostModel(
                       /* amountType.toInt(),
                        chargeId,
                        setNewFormatDate(edit_list.date ?: ""),
                        getMonthOfDateNewFormat(edit_list.date ?: ""),
                        getYearofDateNew(edit_list.date ?: ""),
                        dueDateType*/
                        /*chargeId,
                        amountType.toInt(),
                        getMonthOfDateNewFormat(edit_list.date?:""),
                        getYearofDateNew(edit_list.date?:""),
                        *//*serviceCategoryId*//*"",
                        setNewFormatDate(edit_list.date?:""),
                        name_and_date_list[position].id,
                        dueDateType*/
                        chargeId,
                        amountType.toInt(),
                        getMonthOfDateNewFormat(edit_list.date?:""),
                        getYearofDateNew(edit_list.date?:""),
                        setNewFormatDate(edit_list.date?:""),
                        name_and_date_list[position].id,
                        dueDateType
                    )
                    viewModel.editRentBill(token, model)
                }
                // editServiceCharge()

            } else {
                val token = prefs.getString(
                    SessionConstants.TOKEN, ""
                )
                val model = AddRentManagerPostModel(
                    flatList
                )
                viewModel.addRentManager(token, model)
            }


        }
        binding.layoutSelectCategorySpinner1.setOnClickListener {
            if (from.equals("edit_from")) {
                binding.layoutSelectCategorySpinner1.isClickable = false
            } else if (from.equals("back_flat_screen")) {
                // amount = binding.edtAmount.text?.trim().toString()
                startActivity(
                    Intent(this, RentFlatManagerActivity::class.java)
                        .putExtra("from", "back_service")
                        .putExtra("nameIdList", nameId_list)
                        .putExtra("checkId", checkid_list)
                        .putExtra("idLIst", id_list)
                        .putExtra("billing_Type", serviceCharge)
                        .putExtra("date", selectDate)
                        .putExtra("amount", amount)
                )
                //finish()
            } else {
                if (selectDate.isEmpty()) {
                    Toast.makeText(this, "Please Select Flat!!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener

                } else {
                    //amount = binding.tvBillDate.text?.trim().toString()
                    startActivity(
                        Intent(this, RentFlatManagerActivity::class.java)
                            .putExtra("idLIst", alreadyId_List)
                            .putExtra("billing_Type", serviceCharge).putExtra("date", selectDate)
                            .putExtra("selectDateNew", selectDateNew)
                            .putExtra("setMonths", editMonths).putExtra("setYear", editYears)
                    )
                   // finish()
                }

            }

        }
        binding.imageView4.setOnClickListener {
            finish()
        }
    }

    override fun removeItem(positionNew: Int) {
        name_list.removeAt(positionNew)
        name_and_date_list.removeAt(positionNew)
        nameAdpter?.removeItem(name_list, positionNew)
        dateAmountAdpter?.removeItem(name_and_date_list, positionNew)
    }

    private fun validationData(): Boolean {
        if (TextUtils.isEmpty(binding.tvRentDate.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Select Rent Date!!", Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true

    }
}