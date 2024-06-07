package com.application.intercom.manager.newFlow.finance.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.FragmentBillingFinanceManagerBinding
import com.application.intercom.manager.bills.ApprovalBillingManagerActivity
import com.application.intercom.manager.bills.PaidBillingManagerActivity
import com.application.intercom.manager.bills.UnPaidBillingManagerActivity
import com.application.intercom.manager.newFlow.expenses.ManagerExpensesActivity
import com.application.intercom.manager.rent.RentManagerActivity
import com.application.intercom.manager.service_charge.ServiceChargeListActivity
import com.application.intercom.utils.*

class BillingFinanceManagerFragment : Fragment() {
    lateinit var binding: FragmentBillingFinanceManagerBinding
    private lateinit var viewModel: ManagerSideViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillingFinanceManagerBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()
        if (prefs.getString(SessionConstants.ASSOCIATION_TYPE, "") == "Owner") {
            binding.rent.visibility = View.VISIBLE
        } else {
            binding.rent.visibility = View.INVISIBLE
        }


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

                            binding.textView263.text = "${it.data?.resultUnPaidCount ?: 0}"
                            binding.textView264.text = "৳ ${it.data?.resultUnPaidSum ?: 0}"

                            binding.textView26311.text = "${it.data?.resultUnApprovedCount ?: 0}"
                            binding.textView26411.text = "৳ ${it.data?.resultUnApprovedSum ?: 0}"

                            binding.textView263111.text = "${it.data?.resultExpenseCount ?: 0}"
                            binding.textView264111.text = "৳ ${it.data?.resultExpenseSum ?: 0}"

                            binding.textView2631112.text = "${it.data?.resultServiceCount ?: 0}"
                            binding.textView2641112.text = "৳ ${it.data?.resultServiceSum ?: 0}"

                            binding.textView26314.text = "${it.data?.resultRentCount ?: 0}"
                            binding.textView26415.text = "৳ ${it.data?.resultRentSum ?: 0}"

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

    private fun lstnr() {
        binding.ExpenseBill.setOnClickListener {
            startActivity(Intent(requireContext(), ManagerExpensesActivity::class.java))
        }
        binding.card.setOnClickListener {
            startActivity(
                Intent(requireContext(), UnPaidBillingManagerActivity::class.java)
            )
        }
        binding.paidBill.setOnClickListener {
            startActivity(
                Intent(requireContext(), PaidBillingManagerActivity::class.java))/*.putExtra("key","paid")
            )*/
        }
        binding.pendingBill.setOnClickListener {
            startActivity(
                Intent(requireContext(), ApprovalBillingManagerActivity::class.java)
            )/*.putExtra("key","pending")
            )*/
        }
        binding.serviceCharge.setOnClickListener {
            startActivity(
                Intent(requireContext(), ServiceChargeListActivity::class.java)
            )/*.putExtra("key","pending")
            )*/
        }
        binding.rent.setOnClickListener {
            startActivity(
                Intent(requireContext(), RentManagerActivity::class.java)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        billCount()
    }

}