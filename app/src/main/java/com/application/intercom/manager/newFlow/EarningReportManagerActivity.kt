package com.application.intercom.manager.newFlow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.manager.managerSide.newflow.balanceSheet.IncomeReportManagerPostModel
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.IncomeReportManagerList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityEarningReportManagerBinding
import com.application.intercom.databinding.DateFilterBottomsheetBinding
import com.application.intercom.databinding.DateFilterFinanceBottomsheetBinding
import com.application.intercom.helper.getCurrentMonth
import com.application.intercom.helper.getMonthOfSelectDate
import com.application.intercom.helper.getMonthOfSelectDatenew
import com.application.intercom.helper.setNewFormat
import com.application.intercom.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class EarningReportManagerActivity : AppCompatActivity() {
    lateinit var binding: ActivityEarningReportManagerBinding
    private lateinit var viewModel: ManagerSideViewModel
    var list = ArrayList<IncomeReportManagerList.Data.Result>()
    private var adptr: EarningReportAdapter? = null
    lateinit var filterBottom: DateFilterBottomsheetBinding
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var datefilterBottom: DateFilterFinanceBottomsheetBinding
    lateinit var bottomSheetDialog1: BottomSheetDialog
    private var startDate: String = ""
    private var endDate: String = ""
    private var months: String = ""
    private var year: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEarningReportManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.setLightStatusBar(this)

        initView()
        lstnr()
    }

    private fun initView() {
        initialize()
        observer()
        binding.textView266.text = getCurrentMonth()
        binding.toolbar.tvTittle.text = getString(R.string.earning_report)
        binding.rcy.visibility = View.VISIBLE

    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]
    }

    private fun incomeReportManager() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = IncomeReportManagerPostModel(
            "",
            prefs.getString(SessionConstants.NEWBUILDINGID, ""),
            ""
        )
        viewModel.incomeReportManager(token, model)
    }

    private fun observer() {
        viewModel.incomeReportManagerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            list.clear()
                            list.addAll(it.data?.result!!)
                            binding.rcy.layoutManager = LinearLayoutManager(this)
                            adptr = EarningReportAdapter(this, list)
                            binding.rcy.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                            if (list.isEmpty()) {
                                binding.rcy.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rcy.visibility = View.VISIBLE
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }

                        } else if (it.status == AppConstants.STATUS_404) {
                            binding.rcy.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_500) {

                            binding.rcy.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.rcy.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else {
                            binding.rcy.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })

    }

    private fun lstnr() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.toolbar.layoutFilter.setOnClickListener {
            filter()
        }
        binding.toolbar.layoutDaily.setOnClickListener {
            dateFilter()

        }
    }

    fun filter() {
        filterBottom = DateFilterBottomsheetBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme).apply {
                setContentView(filterBottom.root)
                filterBottom.commonBtn.tv.text = getString(R.string.apply)
                /* filterBottom.tvStartDate.setOnClickListener {
                     MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build()
                         .apply {
                             show(
                                 supportFragmentManager,
                                 this@EarningReportManagerActivity.toString()
                             )
                             addOnPositiveButtonClickListener {
                                 startDate =
                                     SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                         Date(it)
                                     )
                                 filterBottom.tvStartDate.setText(
                                     SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                         Date(it)
                                     )
                                 )
                             }
                         }
                 }
                 filterBottom.tvEndDate.setOnClickListener {
                     MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build()
                         .apply {
                             show(
                                 supportFragmentManager,
                                 this@EarningReportManagerActivity.toString()
                             )
                             addOnPositiveButtonClickListener {
                                 endDate =
                                     SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                         Date(it)
                                     )
                                 filterBottom.tvEndDate.setText(
                                     SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                         Date(it)
                                     )
                                 )
                             }
                         }
                 }*/
                var yearArr = ArrayList<String>()
                for (i in 2000..2050) {
                    yearArr.add(i.toString())
                }
                filterBottom.yearSpiner.adapter = ArrayAdapter(
                    this@EarningReportManagerActivity,
                    R.layout.spinner_dropdown_item,
                    yearArr
                )
                filterBottom.yearSpiner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long,
                        ) {


                            if (filterBottom.yearSpiner.selectedItemPosition > 0) {
                                year =
                                    filterBottom.yearSpiner.selectedItem.toString()
                                println("======$year")
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }

                val genderList = resources.getStringArray(R.array.Months)
                filterBottom.monthsSpiner.adapter = ArrayAdapter(
                    this@EarningReportManagerActivity,
                    R.layout.spinner_dropdown_item,
                    genderList
                )
                filterBottom.monthsSpiner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long,
                        ) {


                            if (filterBottom.monthsSpiner.selectedItemPosition > 0) {
                                months =
                                    filterBottom.monthsSpiner.selectedItemPosition.toString()
                                println("---months${months}")

                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }

                /*filterBottom.commonBtn.tv.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    val token = prefs.getString(
                        SessionConstants.TOKEN, ""
                    )
                    val model = IncomeReportManagerPostModel(
                        endDate,
                        prefs.getString(SessionConstants.NEWBUILDINGID, ""),
                        startDate
                    )
                    viewModel.incomeReportManager(token, model)
                    binding.textView266.text = "$startDate to $endDate"
                }*/
                filterBottom.commonBtn.tv.setOnClickListener {
                    if (year.isNotEmpty()) {
                        if (months.isEmpty()) {
                            val resultt = getStartAndEndDatesOfYear(year.toInt())
                            if (resultt != null) {
                                val (start, end) = resultt
                                println("======y$start ...$end")
                                val token = prefs.getString(
                                    SessionConstants.TOKEN, ""
                                )
                                val model = IncomeReportManagerPostModel(
                                    setNewFormat(end) ?: "",
                                    prefs.getString(SessionConstants.NEWBUILDINGID, ""),
                                    setNewFormat(
                                        start
                                    ) ?: ""
                                )
                                viewModel.incomeReportManager(token, model)
                                dismiss()
                                binding.textView266.text =
                                    getMonthOfSelectDatenew(setNewFormat(end) ?: "")
                            }
                        } else {
                            val resultt =
                                getStartAndEndDatesOfMonth(year.toInt(), months.toInt())
                            if (resultt != null) {
                                val (start, end) = resultt
                                println("======ym$start ...$end")
                                val token = prefs.getString(
                                    SessionConstants.TOKEN, ""
                                )
                                val model = IncomeReportManagerPostModel(
                                    setNewFormat(end) ?: "",
                                    prefs.getString(SessionConstants.NEWBUILDINGID, ""),
                                    setNewFormat(
                                        start
                                    ) ?: ""
                                )
                                viewModel.incomeReportManager(token, model)
                                dismiss()
                                binding.textView266.text =
                                    getMonthOfSelectDatenew(setNewFormat(end) ?: "")
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@EarningReportManagerActivity,
                            "PLease Select Year",
                            Toast.LENGTH_SHORT
                        ).show()
                        dismiss()
                    }
                }
                filterBottom.tvClose.setOnClickListener {
                    dismiss()
                }

            }

        bottomSheetDialog.show()
    }

    fun dateFilter() {
        datefilterBottom = DateFilterFinanceBottomsheetBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog1 =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme).apply {
                setContentView(datefilterBottom.root)
                datefilterBottom.commonBtn.tv.text = getString(R.string.apply)
                datefilterBottom.tvStartDate.setOnClickListener {
                    MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build()
                        .apply {
                            show(
                                supportFragmentManager,
                                this@EarningReportManagerActivity.toString()
                            )
                            addOnPositiveButtonClickListener {
                                startDate =
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                        Date(it)
                                    )
                                datefilterBottom.tvStartDate.setText(
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                        Date(it)
                                    )
                                )
                            }
                        }
                }
                datefilterBottom.tvEndDate.setOnClickListener {
                    MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build()
                        .apply {
                            show(
                                supportFragmentManager,
                                this@EarningReportManagerActivity.toString()
                            )
                            addOnPositiveButtonClickListener {
                                endDate =
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                        Date(it)
                                    )
                                datefilterBottom.tvEndDate.setText(
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                        Date(it)
                                    )
                                )
                            }
                        }

                }
                datefilterBottom.commonBtn.tv.setOnClickListener {
                    val token = prefs.getString(
                        SessionConstants.TOKEN, ""
                    )
                    val model = IncomeReportManagerPostModel(
                        startDate,
                        prefs.getString(SessionConstants.NEWBUILDINGID, ""),
                        endDate
                    )
                    viewModel.incomeReportManager(token, model)
                    binding.textView266.text = getMonthOfSelectDate(startDate)
                    dismiss()
                }

                datefilterBottom.tvClose.setOnClickListener {
                    dismiss()
                }

            }

        bottomSheetDialog1.show()
    }

    override fun onResume() {
        super.onResume()
        incomeReportManager()
    }

    fun getStartAndEndDatesOfMonth(year: Int, month: Int): Pair<Date, Date>? {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1) // Note: month in Calendar is 0-indexed

        val startOfMonth = calendar.time

        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val endOfMonth = calendar.time

        return Pair(startOfMonth, endOfMonth)
    }

    fun getStartAndEndDatesOfYear(year: Int): Pair<Date, Date>? {
        val calendar = Calendar.getInstance()
        calendar.set(year, 0, 1) // Note: month in Calendar is 0-indexed

        val startOfYear = calendar.time

        calendar.add(Calendar.YEAR, 1)
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val endOfYear = calendar.time

        return Pair(startOfYear, endOfYear)
    }
}