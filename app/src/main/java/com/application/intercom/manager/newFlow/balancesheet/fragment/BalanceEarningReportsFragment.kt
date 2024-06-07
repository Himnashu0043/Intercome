package com.application.intercom.manager.newFlow.balancesheet.fragment

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.manager.managerSide.newflow.balanceSheet.IncomeReportManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.newflow.balanceSheet.PdFGenertePostModel
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.DueReportManagerList
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.ExpensesReportsManagerList
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.IncomeReportManagerList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.FragmentBalanceEarningReportsBinding
import com.application.intercom.databinding.PdfBottomSheetBinding
import com.application.intercom.helper.*
import com.application.intercom.manager.newFlow.balancesheet.*
import com.application.intercom.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog

class BalanceEarningReportsFragment : Fragment() {
    lateinit var binding: FragmentBalanceEarningReportsBinding
    private lateinit var viewModel: ManagerSideViewModel
    private lateinit var activity: BalanceSheetManagerActivity
    private var opening: TextView? = null
    private var closing: TextView? = null
    private var startDateFilter: String = ""
    private var endDateFilter: String = ""
    var list = ArrayList<DueReportManagerList.Data.Result>()
    private var expenseData: ExpensesReportsManagerList.Data? = null
    private var earningData: IncomeReportManagerList.Data? = null
    private var incomeReportData = ArrayList<PdFGenertePostModel.IncomeReportData>()
    private var expenseReportData = ArrayList<PdFGenertePostModel.ExpenseReportData>()
    private var expenseFlatData = ArrayList<PdFGenertePostModel.ExpenseFlatData>()
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var pdfBottom: PdfBottomSheetBinding
    private var earingCategoryList = ArrayList<IncomeReportManagerList.Data.CategoryData>()

    private val viewModelone: ManagerSideViewModel by lazy {
        val repo1 = ManagerSideRepo(BaseApplication.apiService)
        ViewModelProvider(
            requireActivity(),
            ManagerSideFactory(repo1)
        )[ManagerSideViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBalanceEarningReportsBinding.inflate(layoutInflater)
        activity = getActivity() as BalanceSheetManagerActivity
        opening = activity.requireViewById(R.id.textView272)
        closing = activity.requireViewById(R.id.textView274)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()
        binding.btnLogin.tv.text = getString(R.string.download_report)
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
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
            if (!endDateFilter.isNullOrEmpty()) endDateFilter else "",
            prefs.getString(SessionConstants.NEWBUILDINGID, ""),
            if (!startDateFilter.isNullOrEmpty()) startDateFilter else ""
        )
        viewModel.incomeReportManager(token, model)
    }


    private fun expensesReportManager() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = IncomeReportManagerPostModel(
            if (!endDateFilter.isNullOrEmpty()) endDateFilter else "",
            prefs.getString(SessionConstants.NEWBUILDINGID, ""),
            if (!startDateFilter.isNullOrEmpty()) startDateFilter else ""
        )
        viewModel.expensesReportManager(token, model)
    }

    private fun dueReportManager() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = IncomeReportManagerPostModel(
            /*"",
            prefs.getString(SessionConstants.NEWBUILDINGID, ""),
            ""*/
            if (!endDateFilter.isNullOrEmpty()) endDateFilter else "",
            prefs.getString(SessionConstants.NEWBUILDINGID, ""),
            if (!startDateFilter.isNullOrEmpty()) startDateFilter else ""
        )
        viewModel.dueReportManager(token, model)
    }


    private fun observer() {
        viewModel.incomeReportManagerLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            incomeReportData.clear()
                            earingCategoryList.clear()
                            earingCategoryList.addAll(it.data?.categoryData!!)
                            earningData = it.data
                            val monthlyEarningData = it.data
                            var cashInBank = 0
                            var cashInHand = 0
                            var earningBank = 0
                            var earningHand = 0
                            var rentTotal = 0
                            monthlyEarningData?.amountObj?.forEach {
                                if (it.payType ?: "" == "Bank Payment") {
                                    earningBank = it.amount ?: 0
                                } else {
                                    earningHand += it.amount ?: 0
                                }
                            }
                            cashInBank = (monthlyEarningData?.cashInBank ?: 0) - earningBank
                            cashInHand = (monthlyEarningData?.cashInHand ?: 0) - earningHand

                            binding.tvCashInBank.text = cashInBank.toString()
                            binding.tvCashInHand.text = cashInHand.toString()

                            binding.tvTotalBalance.text = (cashInBank + cashInHand).toString()
                            binding.tvRevence.text =
                                (monthlyEarningData?.serviceBallance ?: 0).toString()
                            binding.tvCategory.text =
                                (monthlyEarningData?.categoryBallance ?: 0).toString()
                            binding.tvtotalIncome.text = ((monthlyEarningData?.serviceBallance
                                ?: 0) + (monthlyEarningData?.categoryBallance ?: 0)).toString()
                            binding.tvtotalMain.text =
                                (monthlyEarningData?.openingBallance ?: 0).toString()
                            opening?.text = (binding.tvTotalBalance.text).toString()
                            if (prefs.getString(SessionConstants.ASSOCIATION_TYPE, "") == "owner") {
                                monthlyEarningData?.result?.forEach {
                                    if (it.billType == "tenant" && it.is_bill_type_new == "Rent") {
                                        binding.tvRent.visibility = View.VISIBLE
                                        binding.tvRent1.visibility = View.VISIBLE
                                        rentTotal += it.amount ?: 0
                                        binding.tvRent1.text = rentTotal.toString()

                                    }
                                }
                            } else {
                                binding.tvRent.visibility = View.GONE
                                binding.tvRent1.visibility = View.GONE
                            }


                            var monthlyExpenseData = expenseData

                            var expenseBank = 0
                            var expenseHand = 0
                            monthlyExpenseData?.amountObj?.forEach {
                                if (it.payType ?: "" == "Bank Payment") {
                                    expenseBank = it.expenseAmount ?: 0
                                } else {
                                    expenseHand += it.expenseAmount ?: 0
                                }
                            }
                            closing?.text = ((monthlyEarningData?.openingBallance
                                ?: 0) - expenseBank - expenseHand).toString()

                            for (incomeReport in it.data?.result!!) {
//                                if (!incomeReport.categoryData.isNullOrEmpty()){
//
//                                }
                                incomeReportData.add(
                                    PdFGenertePostModel.IncomeReportData(
                                        (incomeReport.amount ?: 0).toString(),
                                        if (!incomeReport.date.isNullOrEmpty()) {
                                            setNewFormatDate(incomeReport.date ?: "").toString()
                                        } else {
                                            ""
                                        },
                                        if (incomeReport.categoryData?.size ?: 0 > 0)
                                            incomeReport.categoryData?.get(
                                                0
                                            )?.name ?: ""
                                        else if (incomeReport.billType == "Rent")
                                            if (incomeReport.userType == "owner" && incomeReport.is_bill_type_new == "Service")
                                                "Service Charge"
                                            else "Rent"
                                        else "Service Charge",
                                        if (!incomeReport.date.isNullOrEmpty()) {
                                            getMonthOfDate(incomeReport.date ?: "")
                                        } else {
                                            ""
                                        },
                                        if (incomeReport.ownerInfo?.size ?: 0 > 0) incomeReport.ownerInfo?.get(
                                            0
                                        )?.fullName
                                            ?: "" else incomeReport.tenantInfo?.get(0)?.fullName
                                            ?: "",
                                        if (!incomeReport.paidDate.isNullOrEmpty()) {
                                            setNewFormatDate(
                                                incomeReport.paidDate ?: ""
                                            ).toString()
                                        } else {
                                            ""
                                        },
                                        incomeReport.voucherNo ?: "",
                                        incomeReport.payType ?: ""
                                    )
                                )

                            }

                            dueReportManager()
                        } else if (it.status == AppConstants.STATUS_404) {
                            //requireActivity().longToast(it.message ?: "")
                        } else if (it.status == AppConstants.STATUS_500) {
                            // requireActivity().longToast(it.message ?: "")
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            // requireActivity().longToast(it.message ?: "")
                        } else {
                            // requireActivity().longToast(it.message ?: "")
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
        viewModel.expensesReportManagerLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            expenseReportData.clear()
                            expenseData = it.data
                            for (position in 0 until (it.data?.result?.size ?: 0)) {
                                val expenseReport = it.data?.result?.get(position)
                                expenseReportData.add(
                                    PdFGenertePostModel.ExpenseReportData(
                                        (expenseReport?.expenseAmount ?: 0).toString(),
                                        if (!expenseReport?.billDate.isNullOrEmpty()) {
                                            setNewFormatDate(
                                                expenseReport?.billDate ?: ""
                                            ).toString()
                                        } else {
                                            ""
                                        },
                                        expenseReport?.expenseName ?: "",
                                        if (!expenseReport?.billDate.isNullOrEmpty())
                                            getMonthOfDate(expenseReport?.billDate ?: "")
                                        else "",
                                        if (!expenseReport?.date.isNullOrEmpty())
                                            setNewFormatDate(
                                                expenseReport?.date ?: ""
                                            ).toString() else "",
                                        expenseReport?.refernceId ?: "",
                                        expenseReport?.payType ?: ""
                                    )
                                )
                            }
                           incomeReportManager()
                        } else if (it.status == AppConstants.STATUS_404) {
                            //requireActivity().longToast(it.message ?: "")
                        } else if (it.status == AppConstants.STATUS_500) {
                            //requireActivity().longToast(it.message ?: "")
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            // requireActivity().longToast(it.message ?: "")
                        } else {
                            //requireActivity().longToast(it.message ?: "")
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
        viewModel.dueReportManagerLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            list.clear()
                            list.addAll(it.data?.result!!)
                            expenseFlatData.clear()
                            list.forEach {
                                it.newfieldname?.forEach {
                                    expenseFlatData.add(
                                        PdFGenertePostModel.ExpenseFlatData(
                                            (it.amount ?: 0).toString(),
                                            setNewFormatDate(it.createdAt ?: ""),
                                            getMonthOfDate(it.createdAt ?: ""),
                                            if (!it.ownerInfo.isNullOrEmpty()) it.ownerInfo?.get(0)?.fullName
                                                ?: "" else it.tenantInfo?.get(0)?.fullName ?: "",
                                            if (!it.date.isNullOrEmpty()) setNewFormatDate(
                                                it.date ?: ""
                                            ) else "",
                                            it.status,
                                            it.voucherNo ?: "",
                                            if (!it.flatInfo.isNullOrEmpty()) it.flatInfo?.get(0)?.name
                                                ?: "" else "",
                                            if (!it.categoryData!!.isNullOrEmpty()) it.categoryData?.get(
                                                0
                                            )?.name else if(it.billType == "Rent") "Rent" else "Service Charge"
                                        )
                                    )
                                }

                            }
                        } else if (it.status == AppConstants.STATUS_404) {
                            // requireActivity().longToast(it.message ?: "")
                        } else if (it.status == AppConstants.STATUS_500) {
                            /// requireActivity().longToast(it.message ?: "")
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            //requireActivity().longToast(it.message ?: "")
                        } else {
                            // requireActivity().longToast(it.message ?: "")
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
        viewModel.pdfManagerLiveData.observe(requireActivity(), Observer {
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
                                    MonthlyReportPDFManagerActivity::class.java
                                ).putExtra("pdfUrl", it.data ?: "")
                            )
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message ?: "")
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireActivity().longToast(it.message ?: "")
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            //requireActivity().longToast(it.message ?: "")
                        } else {
                            requireActivity().longToast(it.message ?: "")
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
        viewModelone.dateFilterKeyLiveData.observe(viewLifecycleOwner) {
            startDateFilter = it.startDate
            endDateFilter = it.endDate
            println("hfdjstartDateFilter$startDateFilter")
            println("endDateFilter$endDateFilter")
            expensesReportManager()

        }
    }

    private fun lstnr() {
        binding.constraintLayout32.setOnClickListener {
            startActivity(Intent(requireContext(), BalanceEarningReportActivity::class.java))
        }
        binding.constraintLayout33.setOnClickListener {
            startActivity(Intent(requireContext(), BalanceExpneseReportActivity::class.java))
        }
        binding.constraintLayout35.setOnClickListener {
            startActivity(Intent(requireContext(), BalanceDueReportActivity::class.java))
        }
        binding.constraintLayout4.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    CashInCashOutManagerActivity::class.java
                ).putExtra("expensesData", expenseData).putExtra("earningData", earningData)
            )
        }
        binding.btnLogin.tv.setOnClickListener {
            pdfBottomSheet()
        }
        binding.textView2823.setOnClickListener {
            categoryPopup()
        }

    }

    private fun pdfBottomSheet() {
        pdfBottom =
            PdfBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme).apply {
                setContentView(pdfBottom.root)
                pdfBottom.commonBtn.tv.text = "Add"
                pdfBottom.toolbar.tvTittle.text = "Cash In Hand And Bank Report"
                pdfBottom.commonBtn.tv.setOnClickListener {
                    //pdfManager()
                    dismiss()
                    val token = prefs.getString(
                        SessionConstants.TOKEN, ""
                    )
                    var monthlyEarningData = earningData
                    var monthlyExpenseData = expenseData

                    var cashInBank = 0
                    var cashInHand = 0
                    var earningBank = 0
                    var earningHand = 0
                    monthlyEarningData?.amountObj?.forEach {
                        if (it.payType ?: "" == "Bank Payment") {
                            earningBank = it.amount ?: 0
                        } else {
                            earningHand += it.amount ?: 0
                        }
                    }
                    cashInBank = (monthlyEarningData?.cashInBank ?: 0) - earningBank
                    cashInHand = (monthlyEarningData?.cashInHand ?: 0) - earningHand

                    var expenseBank = 0
                    var expenseHand = 0
                    monthlyExpenseData?.amountObj?.forEach {
                        if (it.payType ?: "" == "Bank Payment") {
                            expenseBank = it.expenseAmount ?: 0
                        } else {
                            expenseHand += it.expenseAmount ?: 0
                        }
                    }

                    val model = PdFGenertePostModel(
                        buildingId = prefs.getString(SessionConstants.NEWBUILDINGID, ""),
                        expenseCashInBank = ((monthlyEarningData?.cashInBank
                            ?: 0) - expenseBank).toString(),
                        expenseCashInHand = ((monthlyEarningData?.cashInHand
                            ?: 0) - expenseHand).toString(),
                        expenseFlatData,
                        pdfBottom.edExpenseNote.text.trim().toString(),
                        expenseReportData,
                        incomeCashInBank = cashInBank.toString(),
                        incomeCashInHand = cashInHand.toString(),
                        incomeCategoryAmount = (monthlyEarningData?.categoryBallance
                            ?: 0).toString(),
                        pdfBottom.edEarningNote.text.trim().toString(),
                        incomeOpeningBallance = (cashInBank + cashInHand).toString(),
                        incomeReportData,
                        incomeServiceAmount = (monthlyEarningData?.serviceBallance ?: 0).toString(),
                        totalCashBankBallance = ((monthlyEarningData?.openingBallance
                            ?: 0) - expenseBank - expenseHand).toString(),
                        totalEarningInBank = earningBank.toString(),
                        totalEarningInHand = earningHand.toString(),
                        totalEarningIncome = (earningBank + earningHand).toString(),
                        totalExpenseBallance = (monthlyEarningData?.openingBallance
                            ?: 0).toString(),
                        totalExpenseCategory = (expenseHand + expenseBank).toString(),
                        totalExpenseInBank = expenseBank.toString(),
                        totalExpenseInHand = expenseHand.toString(),
                        totalExpense = (expenseHand + expenseBank).toString(),
                        totalIncome = ((monthlyEarningData?.serviceBallance
                            ?: 0) + (monthlyEarningData?.categoryBallance
                            ?: 0)).toString(),
                        totalIncomeBallance = (monthlyEarningData?.openingBallance ?: 0).toString(),
                        dueNote = pdfBottom.edDueNote.text.trim().toString(),
                        incomeExpenseNote = pdfBottom.edBalanceNote.text.trim().toString(),
                        monthYear = /*"${getCurrentMonth()} (${getCurrentYear()})"*/if(!startDateFilter.isNullOrEmpty()){
                            "${getMonthOfSelectDatenew(startDateFilter)} (${getMonthOfSelectYear1(startDateFilter)})"
                        }else{
                            "${getCurrentMonth()} (${getCurrentYear()})"
                        }
                    )
                    viewModel.pdfManagerRes(token, model)
                }
                pdfBottom.toolbar.ivBack.setOnClickListener {
                    dismiss()
                }


            }
        bottomSheetDialog.show()
    }

    private fun categoryPopup() {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.erning_category_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val rv = dialog.findViewById<RecyclerView>(R.id.rvcategory)
        rv.layoutManager = LinearLayoutManager(requireContext())
        val adptr = CategoryAdapter(requireContext(), earingCategoryList)
        rv.adapter = adptr
        adptr!!.notifyDataSetChanged()


        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    override fun onResume() {
        super.onResume()
        expensesReportManager()
    }

}