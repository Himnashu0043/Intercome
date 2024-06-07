package com.application.intercom.manager.newFlow.balancesheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.manager.managerSide.newflow.balanceSheet.AddNoteBalanceSheetManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.newflow.balanceSheet.IncomeReportManagerPostModel
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.DueReportManagerList
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.IncomeReportManagerList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityBalanceDueReportBinding
import com.application.intercom.databinding.Step3ImgItemsBinding
import com.application.intercom.helper.getCurrentDate
import com.application.intercom.helper.getCurrentDateNew2
import com.application.intercom.helper.getCurrentMonth
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.manager.newFlow.DueReportManagerAdapter
import com.application.intercom.utils.AppConstants
import com.application.intercom.utils.EmpCustomLoader
import com.application.intercom.utils.ErrorUtil
import com.application.intercom.utils.SessionConstants

class BalanceDueReportActivity : AppCompatActivity() {
    lateinit var binding: ActivityBalanceDueReportBinding
    private lateinit var viewModel: ManagerSideViewModel
    private var adptr: BalanceDueReportAdapter? = null
    var list = ArrayList<DueReportManagerList.Data.Result>()
    var startDate: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBalanceDueReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        listener()
    }

    private fun initView() {
        initialize()
        observer()
        startDate = getCurrentDate()
        binding.textView266.text = getCurrentMonth()
        binding.toolbar.tvTittle.text = getString(R.string.due_report)
        binding.toolbar.tvText.visibility = View.INVISIBLE
        binding.toolbar.tvText.text = getString(R.string.plus_add_note)


    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]
    }

    private fun dueReportManager() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = IncomeReportManagerPostModel(
            "",
            prefs.getString(SessionConstants.NEWBUILDINGID, ""),
            ""
        )
        viewModel.dueReportManager(token, model)
    }


    private fun observer() {
        viewModel.dueReportManagerLiveData.observe(this, Observer {
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
                            adptr = BalanceDueReportAdapter(this, list)
                            binding.rcy.adapter = adptr
                            adptr?.notifyDataSetChanged()
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
        viewModel.addNoteManagerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            Toast.makeText(
                                this,
                                getString(R.string.note_add_successfully),
                                Toast.LENGTH_SHORT
                            )
                                .show()
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

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.toolbar.tvText.setOnClickListener {
            notePopup()
        }

    }

    private fun notePopup() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.note_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        var tvNote = dialog.findViewById<EditText>(R.id.ed1)
        val tvAdd = dialog.findViewById<TextView>(R.id.tv_paid_unpaid)
        tvAdd.setOnClickListener {
            dialog.dismiss()
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            val model = AddNoteBalanceSheetManagerPostModel(
                prefs.getString(SessionConstants.NEWBUILDINGID, ""),
                startDate,
                tvNote.text.trim().toString(),
                startDate
            )
            viewModel.addNoteManager(token, model)
        }


        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    override fun onResume() {
        super.onResume()
        dueReportManager()
    }
}