package com.application.intercom.gatekepper.Fragment.newFragment.ownerTenantSingleEntryHistory

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.remote.singleEntryHistory.OwnerTenantSingleEntryHistoryList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.databinding.FragmentOngoingOwnerTenantSingleEntryHistoryBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.ownerTenantSingleEntryHistory.OngoingOwnerTenantSingleEntryHistoryAdapter
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.utils.*
import java.util.*


class OngoingOwnerTenantSingleEntryHistoryFragment(val type: String) : Fragment(),
    OngoingOwnerTenantSingleEntryHistoryAdapter.ListenerCommon {
    lateinit var binding: FragmentOngoingOwnerTenantSingleEntryHistoryBinding
    private lateinit var viewModel: OwnerSideViewModel
    private var adptr: OngoingOwnerTenantSingleEntryHistoryAdapter? = null
    private var list = ArrayList<OwnerTenantSingleEntryHistoryList.Data>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOngoingOwnerTenantSingleEntryHistoryBinding.inflate(layoutInflater)
        initView()
        listener()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()



    }

    private fun initialize() {
        val repo = OwnerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, OwnerSideFactory(repo))[OwnerSideViewModel::class.java]


    }

    private fun tenantSingleVisitorHistoryList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.singleVisitorHistoryTenant(token, "Active", "")
    }

    private fun ownerSingleVisitorHistoryList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.singleVisitorHistoryOwner(token, "Active", "")
    }

    private fun observer() {
        viewModel.singleVisitorHistoryTenantLiveData.observe(
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
                                list.clear()
                                list.addAll(it.data)
                                binding.rcy.layoutManager = LinearLayoutManager(requireContext())
                                adptr = OngoingOwnerTenantSingleEntryHistoryAdapter(
                                    requireContext(),
                                    list, this
                                )
                                binding.rcy.adapter = adptr
                                adptr!!.notifyDataSetChanged()
                                if (list.isEmpty()) {
                                    binding.lottieEmpty.visibility = View.VISIBLE
                                    binding.rcy.visibility = View.INVISIBLE
                                } else {
                                    binding.lottieEmpty.visibility = View.INVISIBLE
                                    binding.rcy.visibility = View.VISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_404) {
                                //requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcy.visibility = View.INVISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcy.visibility = View.INVISIBLE
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

        viewModel.singleVisitorHistoryOwnerLiveData.observe(
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
                                list.clear()
                                list.addAll(it.data)
                                binding.rcy.layoutManager = LinearLayoutManager(requireContext())
                                adptr = OngoingOwnerTenantSingleEntryHistoryAdapter(
                                    requireContext(),
                                    list, this
                                )
                                binding.rcy.adapter = adptr
                                adptr!!.notifyDataSetChanged()
                                if (list.isEmpty()) {
                                    binding.lottieEmpty.visibility = View.VISIBLE
                                    binding.rcy.visibility = View.INVISIBLE
                                } else {
                                    binding.lottieEmpty.visibility = View.INVISIBLE
                                    binding.rcy.visibility = View.VISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_404) {
                                //requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcy.visibility = View.INVISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcy.visibility = View.INVISIBLE
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
        viewModel.visitorActionTenantLiveData.observe(
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
                                requireContext().longToast(getString(R.string.accept_successfully))
                                tenantSingleVisitorHistoryList()
                            } else if (it.status == AppConstants.STATUS_404) {
                                requireContext().longToast(it.message)
                            } else {

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
        viewModel.visitorActionOwnerLiveData.observe(
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
                                requireContext().longToast(getString(R.string.accept_successfully))
                                ownerSingleVisitorHistoryList()
                            } else if (it.status == AppConstants.STATUS_404) {
                                requireContext().longToast(it.message)
                            } else {

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

        viewModel.rejectvisitorActionOwnerLiveData.observe(
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
                                requireContext().longToast(getString(R.string.reject_successfully))
                                ownerSingleVisitorHistoryList()
                            } else if (it.status == AppConstants.STATUS_404) {
                                requireContext().longToast(it.message)
                            } else {

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
        viewModel.rejectvisitorActionTenantLiveData.observe(
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
                                requireContext().longToast(getString(R.string.reject_successfully))
                                tenantSingleVisitorHistoryList()
                            } else if (it.status == AppConstants.STATUS_404) {
                                requireContext().longToast(it.message)
                            } else {

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

    private fun listener() {
        binding.seach.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                var text = p0.toString().trim()
                if (text.isNotEmpty()) {
                    if (text.length > 0) {
                        var tempFilterList = list.filter {
                            it.visitorName.lowercase(Locale.ROOT)
                                .contains(text.lowercase(Locale.ROOT)) || it.mobileNumber.lowercase(
                                Locale.ROOT
                            ).contains(
                                text.lowercase(
                                    Locale.ROOT
                                )
                            )
                        }


                        binding.rcy.layoutManager = LinearLayoutManager(requireContext())
                        adptr = OngoingOwnerTenantSingleEntryHistoryAdapter(
                            requireContext(),
                            tempFilterList as ArrayList<OwnerTenantSingleEntryHistoryList.Data>,
                            this@OngoingOwnerTenantSingleEntryHistoryFragment
                        )
                        binding.rcy.adapter = adptr
                        adptr!!.notifyDataSetChanged()

                    }
                } else {
                    binding.rcy.layoutManager = LinearLayoutManager(requireContext())
                    adptr = OngoingOwnerTenantSingleEntryHistoryAdapter(
                        requireContext(),
                        list, this@OngoingOwnerTenantSingleEntryHistoryFragment
                    )
                    binding.rcy.adapter = adptr
                    adptr!!.notifyDataSetChanged()
                }
            }

        })

    }

    override fun onAccept(visitorId: String) {
        if (type.equals("tenant")) {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            viewModel.visitorActionTenant(token, visitorId, "Accept")
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            viewModel.visitorActionOwner(token, visitorId, "Accept")
        }

    }

    override fun onReject(visitorId: String) {
        if (type.equals("tenant")) {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            viewModel.rejectvisitorActionTenant(token, visitorId, "Rejected")
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            viewModel.rejectvisitorActionOwner(token, visitorId, "Rejected")
        }
    }

    override fun onClick(msg: OwnerTenantSingleEntryHistoryList.Data, position: Int) {
        detailsPopup(msg)
    }

    private fun detailsPopup(msg: OwnerTenantSingleEntryHistoryList.Data) {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.out_single_entry_his_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val tvOut = dialog.findViewById<TextView>(R.id.tvOut)
        tvOut.visibility = View.GONE
        val tvName = dialog.findViewById<TextView>(R.id.textView166)
        val tvflat = dialog.findViewById<TextView>(R.id.textView167)
        val tvPhone = dialog.findViewById<TextView>(R.id.textView170)
        val tvAddress = dialog.findViewById<TextView>(R.id.textView172)
        val tvFromDate = dialog.findViewById<TextView>(R.id.textView174)
        val tvToDate = dialog.findViewById<TextView>(R.id.tvOutDate)
        val tvInTime = dialog.findViewById<TextView>(R.id.tvInTime)
        val tvOutTime = dialog.findViewById<TextView>(R.id.tvoutTime)
        val tvNote = dialog.findViewById<TextView>(R.id.tvNote)
        val tvDelivery = dialog.findViewById<TextView>(R.id.textView168)
        val tvimg = dialog.findViewById<ImageView>(R.id.imageView91)
        tvName.text = msg.visitorName
        tvflat.text = msg.visitorName
        tvPhone.text = msg.mobileNumber
        tvAddress.text = msg.address
//        if (!msg.fromDate.isNullOrEmpty()) {
//            val fromDate = setNewFormatDate(msg.fromDate)
//            tvFromDate.text = fromDate
//            println("---fromDate${fromDate}")
//        } else {
//            val fromDate = setNewFormatDate(msg.createdAt)
//            tvFromDate.text = fromDate
//            println("---fromDate${fromDate}")
//        }
//        if (!msg.toDate.isNullOrEmpty()) {
//            val toDate = setNewFormatDate(msg.toDate)
//            tvToDate.text = toDate
//            println("---toDate${toDate}")
//        }
        if (msg.visitorStatus.equals("Accept")) {
            val fromDate = setNewFormatDate(msg.createdAt)
            tvFromDate.text = fromDate
            println("---fromDate${fromDate}")
            tvInTime.text = msg.entryTime
        }

//        tvInTime.text = msg.entryTime
//        println("---fromTime${msg.fromTime}")
        tvOutTime.text = msg.toTime
        println("---toTime${msg.toTime}")
        tvNote.text = msg.note
        tvimg.loadImagesWithGlideExt(msg.photo)
        tvDelivery.text = "${msg.visitCategoryName} | ${msg.visitorType} Entry"

        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    override fun onResume() {
        super.onResume()
        if (type.equals("tenant")) {
            tenantSingleVisitorHistoryList()
        } else {
            ownerSingleVisitorHistoryList()
        }
    }
}