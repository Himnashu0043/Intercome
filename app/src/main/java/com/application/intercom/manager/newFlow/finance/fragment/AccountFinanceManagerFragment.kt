package com.application.intercom.manager.newFlow.finance.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.FragmentAccountFinanceManagerBinding
import com.application.intercom.manager.bills.MyBillingsActivity
import com.application.intercom.manager.bills.PaidBillingManagerActivity
import com.application.intercom.manager.newFlow.expenses.ManagerExpensesActivity
import com.application.intercom.manager.newFlow.finance.adapter.AccountFinanceAdapter
import com.application.intercom.manager.newFlow.finance.adapter.BillingFinanceAdapter
import com.application.intercom.utils.*

class AccountFinanceManagerFragment : Fragment() {
    lateinit var binding: FragmentAccountFinanceManagerBinding
    private lateinit var viewModel: ManagerSideViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountFinanceManagerBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()
       /* binding.rcy.visibility = View.VISIBLE
        binding.rcy.layoutManager = GridLayoutManager(requireContext(), 2)
        val adptr = BillingFinanceAdapter(requireContext())
        binding.rcy.adapter = adptr
        adptr?.notifyDataSetChanged()*/

    }
    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]
    }

    private fun billCount() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.billCountManagerRes(token, prefs.getString(SessionConstants.NEWBUILDINGID, ""))
    }

    private fun observer() {
        viewModel.billCountManagerLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            binding.textView2631.text = "${it.data?.resultPaidCount ?: 0}"
                            binding.textView2641.text = "৳ ${it.data?.resultPaidSum ?: 0}"
                            binding.textView263.text = "${it.data?.resultExpensePaidCount ?: 0}"
                            binding.textView264.text = "৳ ${it.data?.resultExpensePaidSum ?: 0}"
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message ?: "")
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireActivity().longToast(it.message ?: "")
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            requireActivity().longToast(it.message ?: "")
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
    }
    override fun onResume() {
        super.onResume()
        billCount()
    }
    private fun lstnr() {
        binding.paidBill.setOnClickListener {
            startActivity(
                Intent(requireContext(), PaidBillingManagerActivity::class.java)
            )
        }
        binding.card.setOnClickListener {
            startActivity(Intent(requireContext(), ManagerExpensesActivity::class.java))
        }
    }

}