package com.application.intercom.manager.newFlow.expenses.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.remote.manager.managerSide.newflow.UnPaidExpensesManagerRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.FragmentPaidExpensesManagerBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.manager.newFlow.AddExpensesManagerActivity
import com.application.intercom.manager.newFlow.expenses.adapter.PaidExpensesAdapter
import com.application.intercom.manager.newFlow.expenses.adapter.UnpaidExpensesAdapter
import com.application.intercom.utils.AppConstants
import com.application.intercom.utils.EmpCustomLoader
import com.application.intercom.utils.ErrorUtil
import com.application.intercom.utils.SessionConstants

class PaidExpensesManagerFragment : Fragment() {
    lateinit var binding: FragmentPaidExpensesManagerBinding
    private lateinit var viewModel: ManagerSideViewModel
    private var list = ArrayList<UnPaidExpensesManagerRes.Data.Result>()
    private var buildingId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaidExpensesManagerBinding.inflate(layoutInflater)
        buildingId = prefs.getString(SessionConstants.NEWBUILDINGID, "")
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()
        binding.btnLogin.tv.text = getString(R.string.add_expenses)
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))

    }

    private fun lstnr() {
        binding.btnLogin.tv.setOnClickListener {
            startActivity(Intent(requireContext(), AddExpensesManagerActivity::class.java))
        }
    }
    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

    private fun paidExpensesList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.unpaidExpensesList(token, "PAID", buildingId)

    }

    private fun observer() {
        viewModel.unpaidExpensesLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            list.clear()
                            list.addAll(it.data.result)
                            if (list.isEmpty()) {
                                binding.rcy.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rcy.visibility = View.VISIBLE
                                binding.lottieEmpty.visibility = View.INVISIBLE
                                binding.rcy.layoutManager = LinearLayoutManager(requireContext())
                                val adptr = PaidExpensesAdapter(requireContext(),list)
                                binding.rcy.adapter = adptr
                                adptr?.notifyDataSetChanged()
                            }

                        } else if (it.status == AppConstants.STATUS_500) {
                            binding.rcy.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_404) {
                            binding.rcy.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.rcy.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })

    }

    override fun onResume() {
        super.onResume()
        paidExpensesList()
    }

}