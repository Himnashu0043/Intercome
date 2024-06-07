package com.application.intercom.manager.newFlow.balancesheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.DateModel
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityBalanceSheetManagerBinding
import com.application.intercom.databinding.DateFilterBottomsheetBinding
import com.application.intercom.helper.getCurrentMonth
import com.application.intercom.helper.getCurrentYear
import com.application.intercom.helper.setNewFormat
import com.application.intercom.manager.newFlow.balancesheet.fragment.BalanceEarningReportsFragment
import com.application.intercom.manager.newFlow.balancesheet.fragment.BalanceExpensesReportsFragment
import com.application.intercom.utils.CommonUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class BalanceSheetManagerActivity : AppCompatActivity() {
    lateinit var binding: ActivityBalanceSheetManagerBinding
    lateinit var filterBottom: DateFilterBottomsheetBinding
    lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var viewModel: ManagerSideViewModel
    private var startDate: String = ""
    private var months1: String = ""
    private var months: String = ""
    private var year: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBalanceSheetManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        initView()
        lstnr()
    }

    private fun initView() {
        binding.textView20.text = "Balance Sheet ${getCurrentMonth()} (${getCurrentYear()})"
        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Earning"
                }
                1 -> {
                    tab.text = getString(R.string.expenses)
                }
            }
        }.attach()
        initialize()
    }

    private fun initialize() {
        val repo1 = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo1))[ManagerSideViewModel::class.java]
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return BalanceEarningReportsFragment()
                }
                1 -> {
                    return BalanceExpensesReportsFragment()
                }
            }
            return BalanceEarningReportsFragment()
        }
    }

    private fun lstnr() {
        binding.imageView4.setOnClickListener {
            finish()
        }
        binding.layoutFilter.setOnClickListener {
            filter()
        }

    }

    fun filter() {
        filterBottom = DateFilterBottomsheetBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme).apply {
            setContentView(filterBottom.root)
            filterBottom.commonBtn.tv.text = getString(R.string.apply)
            /*filterBottom.tvStartDate.setOnClickListener {
                MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build().apply {
                        show(
                            supportFragmentManager, this@BalanceSheetManagerActivity.toString()
                        )
                        addOnPositiveButtonClickListener {
                            startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
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
                MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build().apply {
                        show(
                            supportFragmentManager, this@BalanceSheetManagerActivity.toString()
                        )
                        addOnPositiveButtonClickListener {
                            endDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
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
                this@BalanceSheetManagerActivity,
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
                        /* year =
                             filterBottom.yearSpiner.selectedItem.toString()
                         println("======$year")*/
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

            val genderList = resources.getStringArray(R.array.Months)
            filterBottom.monthsSpiner.adapter = ArrayAdapter(
                this@BalanceSheetManagerActivity,
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
                            months1 =
                                filterBottom.monthsSpiner.selectedItem.toString()
                            println("---months1${months1}")

                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            filterBottom.commonBtn.tv.setOnClickListener {
                if (binding.viewPager.currentItem == 0) {
                    if (year.isNotEmpty()) {
                        if (months.isEmpty()) {
                            val resultt = getStartAndEndDatesOfYear(year.toInt())
                            if (resultt != null) {
                                val (start, end) = resultt
                                println("======y$start ...$end")
                                viewModel.dateFilterKeyLiveData.postValue(
                                    DateModel(
                                        setNewFormat(
                                            start
                                        ) ?: "", setNewFormat(end) ?: ""
                                    )
                                )
                            }
                        } else {
                            val resultt = getStartAndEndDatesOfMonth(year.toInt(), months.toInt())
                            if (resultt != null) {
                                val (start, end) = resultt
                                println("======ym$start ...$end")
                                viewModel.dateFilterKeyLiveData.postValue(
                                    DateModel(
                                        setNewFormat(
                                            start
                                        ) ?: "", setNewFormat(end) ?: ""
                                    )
                                )
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@BalanceSheetManagerActivity,
                            "PLease Select Year",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    if (year.isNotEmpty()) {
                        if (months.isEmpty()) {
                            val resultt = getStartAndEndDatesOfYear(year.toInt())
                            if (resultt != null) {
                                val (start, end) = resultt
                                println("======y$start ...$end")
                                viewModel.dateExpFilterKeyLiveData.postValue(
                                    DateModel(
                                        setNewFormat(
                                            start
                                        ) ?: "", setNewFormat(end) ?: ""
                                    )
                                )
                            }
                        } else {
                            val resultt = getStartAndEndDatesOfMonth(year.toInt(), months.toInt())
                            if (resultt != null) {
                                val (start, end) = resultt
                                println("======ym$start ...$end")
                                viewModel.dateExpFilterKeyLiveData.postValue(
                                    DateModel(
                                        setNewFormat(
                                            start
                                        ) ?: "", setNewFormat(end) ?: ""
                                    )
                                )
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@BalanceSheetManagerActivity,
                            "PLease Select Year",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                   /* viewModel.dateExpFilterKeyLiveData.postValue(DateModel(startDate, endDate))*/
                }

                bottomSheetDialog.dismiss()
                binding.textView20.text = "Balance Sheet ${months1} (${year})"
            }


        }

        bottomSheetDialog.show()
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