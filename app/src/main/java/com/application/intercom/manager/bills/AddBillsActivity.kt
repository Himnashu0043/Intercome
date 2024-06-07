package com.application.intercom.manager.bills

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
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
import com.application.intercom.data.model.local.manager.managerSide.addBill.EditPendingManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.addBill.ManagerAddBillPostModel
import com.application.intercom.data.model.remote.manager.managerSide.bill.MangerBillPendingListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityAddBillsBinding
import com.application.intercom.helper.*
import com.application.intercom.manager.service_charge.NameAdapter
import com.application.intercom.utils.*
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


class AddBillsActivity : AppCompatActivity(),NameAdapter.Remove {
    private lateinit var binding: ActivityAddBillsBinding
    private lateinit var viewModel: ManagerSideViewModel
    private var flatOfBuildingList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingId: String = ""
    private var serviceCategoryList = ArrayList<String>()
    private var serviceCategoryHashMapID: HashMap<String, String> = HashMap()
    private var serviceCategoryId: String = ""
    private var editList: MangerBillPendingListRes.Data.Result? = null
    private var from: String = ""
    private lateinit var selectFlatspinnerAdapter: ArrayAdapter<String>
    private lateinit var selectCategoryspinnerAdapter: ArrayAdapter<String>

    ///changes
    private var id_list = ArrayList<IdModel>()
    private var checkid_list = ArrayList<IdModel>()
    private var back_id_list = ArrayList<String>()
    private var name_list = ArrayList<NameModel>()
    private var name_and_date_list = ArrayList<DateModelWithName>()
    private var nameId_list = ArrayList<NameIdModel>()
    private var edit_name_list = ArrayList<String>()
    private var flatList = ArrayList<ManagerAddBillPostModel.Flat>()
    private var nameAdpter: NameAdapter? = null
    private var dateAmountAdpter: BillingDateAmountAdapter? = null
    private var selectDate: String = ""
    private var chargeId: String = ""
    private var serviceName: String = ""
    private var editMonths: String = ""
    private var editYears: String = ""
    private var selectDateNew: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBillsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        from = intent.getStringExtra("from").toString()
        /*if (from == "edit") {
            editList =
                intent.getSerializableExtra("editList") as MangerBillPendingListRes.Data.Result?
            println("=======list${editList}")
            billId = editList?._id.toString()
            binding.edtDesi.setText(editList?.description ?: "")
            binding.edtAmount.setText(editList?.amount.toString())
            binding.edtDate.setText(setNewFormatDate(editList?.date))
        }*/
        if (from.equals("back_flat_screen")) {
            nameId_list =
                intent.getSerializableExtra("nameIdList") as java.util.ArrayList<NameIdModel>
            println("----back_flat_screennameId_list$nameId_list")
            checkid_list = intent.getSerializableExtra("checkId") as java.util.ArrayList<IdModel>
            println("----back_flat_screencheckid_list$checkid_list")
            if (!intent.getStringExtra("date").isNullOrEmpty()) {
                binding.tvBillingDate.text = intent.getStringExtra("date") ?: ""
                selectDate = intent.getStringExtra("date") ?: ""
            } else {
                binding.tvBillingDate.text = "Select Date"
            }


            editMonths = intent.getStringExtra("setMonths") ?: ""
            editYears = intent.getStringExtra("setYear") ?: ""
            selectDateNew = intent.getStringExtra("selectDateNew") ?: ""

            for (idd in nameId_list) {
                back_id_list.add(idd._id)
                name_list.add(NameModel(idd.name))
                name_and_date_list.add(DateModelWithName(idd.name, idd._id, "", ""))
            }
            binding.namercy.visibility = View.VISIBLE
            binding.namercy.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            nameAdpter = NameAdapter(this, name_list, this)
            binding.namercy.adapter = nameAdpter
            nameAdpter!!.notifyDataSetChanged()
            id_list = intent.getSerializableExtra("idLIst") as ArrayList<IdModel>

            binding.rcyAmount.visibility = View.VISIBLE
            binding.rcyAmount.layoutManager = LinearLayoutManager(this)
            dateAmountAdpter = BillingDateAmountAdapter(this, name_and_date_list, "") {
                try {
                    binding.rcyAmount.getChildAt(0)
                        .findViewById<CheckBox>(R.id.checkedAllDate).isChecked = false
                } catch (exception: java.lang.Exception) {

                }
            }
            binding.rcyAmount.adapter = dateAmountAdpter
            dateAmountAdpter!!.notifyDataSetChanged()


        } else if (from.equals("edit")) {
            editList =
                intent.getSerializableExtra("editList") as MangerBillPendingListRes.Data.Result?
            println("----edit$editList")
            chargeId = editList?._id ?: ""
            name_list.add(NameModel(editList?.flatInfo?.get(0)?.name ?: ""))
            back_id_list.add(editList?.flatInfo?.get(0)?._id ?: "")
            binding.tvBillingDate.setText(getYearMonthOfDate(editList?.date ?: ""))
            selectDate = setNewFormatDate(editList?.date ?: "").toString()
            binding.tvFlatsService1.setText(editList?.flatInfo?.get(0)?.name ?: "")
            binding.rcyAmount.visibility = View.VISIBLE
            name_and_date_list.add(
                DateModelWithName(
                    editList?.flatInfo?.get(0)?.name ?: "",
                    editList?.flatInfo?.get(0)?._id ?: "",
                    editList?.amount.toString(),
                    editList?.dueDate ?: ""
                )
            )
            binding.rcyAmount.layoutManager = LinearLayoutManager(this)
            dateAmountAdpter = BillingDateAmountAdapter(this, name_and_date_list, "edit")
            binding.rcyAmount.adapter = dateAmountAdpter
            dateAmountAdpter!!.notifyDataSetChanged()
        } /*else {
            id_list = intent.getSerializableExtra("idLIst") as java.util.ArrayList<IdModel>
            println("----idloist$id_list")
        }*/
        initView()
        listener()


    }

    private fun initView() {
        serviceCategoryList.add(0, "Select Service Category")
        initialize()
        observer()
        getServicesList()
//        flatOfBuilding()


        binding.btnLogin.tv.text = getString(R.string.submit)
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(this, R.color.orange))

        /*  val selectFlat = resources.getStringArray(R.array.select_flat)
          val selectCategory = resources.getStringArray(R.array.select_category)
  */
        // access the spinner

        /*  val selectFlatspinner = binding.spinnerSelectFlat
             selectCategoryspinnerAdapter =
             ArrayAdapter(this, android.R.layout.simple_spinner_item, serviceCategoryList)
            selectFlatspinner.adapter = selectFlatspinnerAdapter
          selectFlatspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
              override fun onItemSelected(
                  parent: AdapterView<*>, view: View, position: Int, id: Long
              ) {
                  flatOfBuildingId =
                      flatOfBuildingHashMapID.get(selectFlatspinner.selectedItem.toString())
                          .toString()
                  println("---flat$flatOfBuildingId")
              }

              override fun onNothingSelected(parent: AdapterView<*>) {

              }
          }*/
        val selectCategoryspinner = binding.spinnerSelectCategory
        selectCategoryspinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, serviceCategoryList)

        selectCategoryspinner.adapter = selectCategoryspinnerAdapter
        selectCategoryspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {

                serviceCategoryId =
                    serviceCategoryHashMapID.get(selectCategoryspinner.selectedItem.toString())
                        .toString()
                serviceName = selectCategoryspinner.selectedItem.toString()
                println("---service$serviceCategoryId")
                println("---name$serviceName")


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]
    }

    /*  private fun flatOfBuilding() {
          val token = prefs.getString(
              SessionConstants.TOKEN, ""
          )
          viewModel.flatOfBuildingList(token)
      }*/

    private fun getServicesList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.billCategoryListManager(token)
    }


    /*private fun editBillPendingManager() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        amount = binding.edtAmount.text.trim().toString()

        val model = EditPendingManagerPostModel(
            amount.toInt(),
            billId,
            serviceCategoryId,
            binding.edtDate.text.trim().toString(),
            binding.edtDesi.text.trim().toString(),
            flatOfBuildingId
                    amount . toInt (),
            serviceCategoryId,
            binding.edtDate.text.trim().toString(),
            flatOfBuildingId,
            binding.edtDesi.text.trim().toString()
        )
        viewModel.editBillPending(token, model)
    }*/

    private fun observer() {
        /*viewModel.flatOfBuildingListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            it.data.result.forEach {
                                flatOfBuildingList.add(it.name)
                                flatOfBuildingHashMapID.put(it.name, it._id)
                                // getServicesList()
                            }
                            if (from == "edit") {
                                binding.spinnerSelectCategory.setSelection(
                                    selectFlatspinnerAdapter.getPosition(
                                        editList!!.flatInfo.get(0).name
                                    )
                                )
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
        })*/
        viewModel.addbilManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(this, UnPaidBillingManagerActivity::class.java).setFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                                )
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
        viewModel.editBillPendingLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(this, UnPaidBillingManagerActivity::class.java).setFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                                )
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
        viewModel.bilCategoryListManagerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {

                            it.data.forEach {
                                serviceCategoryList.add(it.name)
                                serviceCategoryHashMapID.put(it.name, it._id)
                            }
                            if (from == "back_flat_screen") {
                                serviceCategoryList.forEach {
                                    if (intent.getStringExtra("serviceName").equals(it)) {
                                        binding.spinnerSelectCategory.setSelection(
                                            selectCategoryspinnerAdapter.getPosition(
                                                it
                                            )
                                        )
                                    }
                                }
                            }
                            if (from == "edit") {
                                binding.spinnerSelectCategory.setSelection(
                                    selectCategoryspinnerAdapter.getPosition(
                                        editList!!.serviceCategory?.get(0)?.name
                                    )
                                )
                            }
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

    private fun listener() {
        binding.btnLogin.tv.setOnClickListener {
            flatList.clear()
            for (position in 0 until binding.rcyAmount.childCount) {
                val view = binding.rcyAmount.getChildAt(position)
                val amountType =
                    view.findViewById<EditText>(R.id.edt_amount2).text.trim().toString()
                val dueDateType =
                    view.findViewById<EditText>(R.id.edtDueDate2).text.trim().toString()

                flatList.add(
                    ManagerAddBillPostModel.Flat(
                        amountType.toInt(),
                        editMonths,
                        editYears,
                        serviceCategoryId,
                        selectDateNew,
                        name_and_date_list[position].id,
                        dueDateType
                    )
                )

            }
            if (!validationData()) {
                return@setOnClickListener
            }
            if (from == "edit") {
                for (position in 0 until binding.rcyAmount.childCount) {
                    val view = binding.rcyAmount.getChildAt(position)
                    val amountType =
                        view.findViewById<EditText>(R.id.edt_amount2).text.trim().toString()
                    val dueDateType =
                        view.findViewById<EditText>(R.id.edtDueDate2).text.trim().toString()
                    val token = prefs.getString(
                        SessionConstants.TOKEN, ""
                    )
                    val model = EditPendingManagerPostModel(
                        chargeId,
                        amountType.toInt(),
                        getMonthOfDateNewFormat(editList?.date?:""),
                        getYearofDateNew(editList?.date?:""),
                        serviceCategoryId,
                        setNewFormatDate(editList?.date?:""),
                        name_and_date_list[position].id,dueDateType
                    )
                    viewModel.editBillPending(token, model)
                }
                //editBillPendingManager()
            } else {
                //addBillManager()
                val token = prefs.getString(
                    SessionConstants.TOKEN, ""
                )
                val model = ManagerAddBillPostModel(
                    flatList
                )
                viewModel.addbillManager(token, model)
            }

        }
        binding.imageView4.setOnClickListener {
            finish()
        }
        /* binding.edtDate.setOnClickListener {
             MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build().apply {
                 show(supportFragmentManager, this@AddBillsActivity.toString())
                 addOnPositiveButtonClickListener {
                     binding.edtDate.setText(
                         SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                             Date(it)
                         )
                     )
                 }
             }
         }*/
        binding.tvBillingDate.setOnClickListener {
            if (from == "edit") {
                binding.tvBillingDate.isClickable = false

            } else {
                MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build().apply {
                    show(supportFragmentManager, this@AddBillsActivity.toString())
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
                        selectDateNew = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                            Date(it)
                        )
                        selectDate = getMonthOfDateNew(
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                Date(it)
                            )
                        )
                        binding.tvBillingDate.setText(
                            selectDate
                        )
                    }
                }
            }
        }
        binding.layoutSelectFlatSpinner.setOnClickListener {
            if (from.equals("edit")) {
                binding.layoutSelectFlatSpinner.isClickable = false
            } else if (from.equals("back_flat_screen")) {
                // amount = binding.edtAmount.text?.trim().toString()
                startActivity(
                    Intent(this, FlatBillManagerActivity::class.java)
                        .putExtra("from", "back_service")
                        .putExtra("nameIdList", nameId_list)
                        .putExtra("checkId", checkid_list)
                        .putExtra("idLIst", id_list)
                        .putExtra("date", selectDate)
                        .putExtra("selectDateNew", selectDateNew)
                        .putExtra("setMonths", editMonths)
                        .putExtra("setYear", editYears)
                        .putExtra("serviceName", serviceName)

                )
                finish()
            } else {
                // amount = binding.edtAmount.text?.trim().toString()
                /* startActivity(
                     Intent(this, FlatBillManagerActivity::class.java).putExtra("idLIst", id_list)
                         .putExtra("billing_Type", serviceCharge).putExtra("date", selectDate)
                         .putExtra("amount", amount)
                 )
                 finish()*/
                startActivity(
                    Intent(this, FlatBillManagerActivity::class.java).putExtra(
                        "date",
                        selectDate
                    ).putExtra("serviceName", serviceName).putExtra("selectDateNew", selectDateNew)
                        .putExtra("setMonths", editMonths).putExtra("setYear", editYears)
                )
                finish()
            }

        }
    }

    private fun validationData(): Boolean {
       /* if (binding.spinnerSelectFlat.selectedItemPosition.equals(0)) {
            Toast.makeText(
                applicationContext, getString(R.string.please_select_flat), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.spinnerSelectCategory.selectedItemPosition.equals(0)) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_select_service_category),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edtDesi.text.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_description), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edtAmount.text.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_amount), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edtDate.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_select_date), Toast.LENGTH_SHORT
            ).show()
            return false
        }*/
        if (TextUtils.isEmpty(binding.tvBillingDate.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_select_date), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.spinnerSelectCategory.selectedItemPosition.equals(0)) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_select_service_category),
                Toast.LENGTH_SHORT
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