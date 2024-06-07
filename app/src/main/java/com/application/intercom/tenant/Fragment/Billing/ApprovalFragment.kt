package com.application.intercom.tenant.Fragment.Billing

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.remote.owner.bill.OwnerUnPaidBillListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.databinding.FragmentApprovalBinding
import com.application.intercom.helper.getMonthOfDate
import com.application.intercom.manager.bills.ViewReceiptManagerActivity
import com.application.intercom.owner.adapter.bill.OwnerUnPaidAdapter
import com.application.intercom.utils.*


class ApprovalFragment(val type: String) : Fragment(), OwnerUnPaidAdapter.PaidClick {
    lateinit var binding: FragmentApprovalBinding
    private var adaptr: OwnerUnPaidAdapter? = null
    private var changeText = "approval"
    private lateinit var ownerviewModel: OwnerSideViewModel
    private var approvalList = ArrayList<OwnerUnPaidBillListRes.Data.Result>()
    private var filterKey: String = ""
    private var months_filter: String = ""
    private var flatOfBuildingId: String = ""
    private val viewModelOne: OwnerHomeViewModel by lazy {
        val ownerModel = OwnerHomeRepo(BaseApplication.apiService)
        ViewModelProvider(
            requireActivity(), OwnerHomeFactory(ownerModel)
        )[OwnerHomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApprovalBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()


    }

    private fun initialize() {
        val ownerrepo = OwnerSideRepo(BaseApplication.apiService)
        ownerviewModel =
            ViewModelProvider(this, OwnerSideFactory(ownerrepo))[OwnerSideViewModel::class.java]

    }

    private fun approvalList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        ownerviewModel.unPaidOwnerList(
            token, "Rent", if (flatOfBuildingId == "All") null else flatOfBuildingId
        )


    }

    /* private fun markAsPaid() {
         val token = prefs.getString(
             SessionConstants.TOKEN, GPSService.mLastLocation!!.latitude.toString()
         )
         ownerviewModel.markAsPaidOwner(token, "Rent")


     }*/

    private fun observer() {
        ownerviewModel.unPaidOwnerListLiveData.observe(
            requireActivity(),
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(requireActivity())
                    }

                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                binding.rcyApproval.visibility = View.VISIBLE
                                if (filterKey.isNotEmpty()) {
                                    approvalList.clear()
                                    if (months_filter.isNotEmpty()) {
                                        it.data.result.forEach {
                                            if (it.billType == filterKey) {
                                                val months = getMonthOfDate(it.date)
                                                if (months == months_filter) {
                                                    approvalList.add(it)
                                                }
                                            }
                                        }
                                    } else {
                                        it.data.result.forEach {
                                            if (it.billType == filterKey) {
                                                approvalList.add(it)
                                            }
                                        }
                                    }
                                    binding.rcyApproval.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    adaptr = OwnerUnPaidAdapter(
                                        requireContext(), approvalList, changeText, this, ""
                                    )
                                    binding.rcyApproval.adapter = adaptr
                                    adaptr!!.notifyDataSetChanged()
                                } else if (months_filter.isNotEmpty()) {
                                    approvalList.clear()
                                    it.data.result.forEach {
                                        val months = getMonthOfDate(it.date)
                                        if (months == months_filter) {
                                            approvalList.add(it)
                                        }
                                    }
                                    binding.rcyApproval.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    adaptr = OwnerUnPaidAdapter(
                                        requireContext(), approvalList, changeText, this, ""
                                    )
                                    binding.rcyApproval.adapter = adaptr
                                    adaptr!!.notifyDataSetChanged()
                                } else {
                                    approvalList.clear()
                                    approvalList.addAll(it.data.result)
                                    binding.rcyApproval.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    adaptr = OwnerUnPaidAdapter(
                                        requireContext(), approvalList, changeText, this, ""
                                    )
                                    binding.rcyApproval.adapter = adaptr
                                    adaptr!!.notifyDataSetChanged()
                                }

                                if (approvalList.isEmpty()) {
                                    binding.rcyApproval.visibility = View.INVISIBLE
                                    binding.lottieEmpty.visibility = View.VISIBLE
                                } else {
                                    binding.rcyApproval.visibility = View.VISIBLE
                                    binding.lottieEmpty.visibility = View.INVISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_500) {
                                //requireContext().longToast(it.message)
                                binding.rcyApproval.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else if (it.status == AppConstants.STATUS_404) {
                                //requireContext().longToast(it.message)
                                binding.rcyApproval.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else if (it.status == AppConstants.STATUS_FAILURE) {
//                                requireContext().longToast(it.message)
                                binding.rcyApproval.visibility = View.INVISIBLE

                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                //requireContext().longToast(it.message)
                                binding.rcyApproval.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                                // binding.constraintLayout4.visibility = View.INVISIBLE
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
        ownerviewModel.markAsPaidOwnerLiveData.observe(
            requireActivity(),
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(requireActivity())
                    }

                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                approvalList()
                                /* startActivity(
                                     Intent(
                                         requireContext(),
                                         OwnerBillingActivity::class.java
                                     ).putExtra(
                                         "from",
                                         "owner"
                                     ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                 )*/
                            } else if (it.status == AppConstants.STATUS_500) {
                                requireContext().longToast(it.message)
                            } else if (it.status == AppConstants.STATUS_404) {
                                requireContext().longToast(it.message)
                            } else {
                                requireContext().longToast(it.message)
                                // binding.constraintLayout4.visibility = View.INVISIBLE
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
        ownerviewModel.rejectBillOwnerLiveData.observe(
            requireActivity(),
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(requireActivity())
                    }

                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                approvalList()
                            } else if (it.status == AppConstants.STATUS_500) {
                                requireContext().longToast(it.message ?: "")
                            } else if (it.status == AppConstants.STATUS_404) {
                                requireContext().longToast(it.message ?: "")
                            } else {
                                requireContext().longToast(it.message ?: "")
                                // binding.constraintLayout4.visibility = View.INVISIBLE
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
        ////owner
        viewModelOne.ownerFlatBuildingIDLiveData.observe(viewLifecycleOwner) {
            flatOfBuildingId = it
            filterKey = ""
            months_filter = ""
            approvalList()
        }
        viewModelOne.ownerFilterKeyLiveData.observe(viewLifecycleOwner) {
            filterKey = it.filter
            months_filter = it.months
            approvalList()
        }
        /* viewModelOne.ownerBottomMonthsFilterLiveData.observe(viewLifecycleOwner) {
             months_filter = it
             println("---months_filter$months_filter")
             approvalList()
         }*/
        ////owner
    }

    private fun lstnr() {
    }

    override fun onOwnerClick(id: String, position: Int) {
        println("----$id")
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        ownerviewModel.markAsPaidOwner(token, id)
        /* startActivity(
             Intent(
                 requireContext(),
                 TenantPaymentActivity::class.java
             ).putExtra("billingId", id)
         )*/
//        startActivity(
//            Intent(
//                requireContext(),
//                TenantPaymentActivity::class.java
//            ).putExtra("billingId", id).putExtra("from", type)
//                .putExtra("ownerBillList", approvalList[position])
//        )
    }

    override fun onNotifyOwner(id: String, position: Int) {

    }

    override fun onViewReceipt(refno: String, uploadDocument: String) {
        startActivity(
            Intent(requireContext(), ViewReceiptManagerActivity::class.java).putExtra(
                "img",
                uploadDocument
            ).putExtra("ref", refno)
        )
    }

    override fun onReject(billID: String) {
        rejectPopup(billID)
    }

    override fun onOwnertoTenantNotify(billID: String, position: Int) {

    }

    private fun rejectPopup(billID: String) {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.reject_manager_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val tvCancel = dialog.findViewById<TextView>(R.id.tvcancelReject)
        val tvReject = dialog.findViewById<TextView>(R.id.tvReject)
        val tvEdit = dialog.findViewById<EditText>(R.id.edtReject)
        tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        tvReject.setOnClickListener {
            dialog.dismiss()
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            ownerviewModel.rejectBillOwner(token, billID, tvEdit.text.toString())
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
        if (!flatOfBuildingId.isNullOrEmpty()) {
            approvalList()
        }

    }

}