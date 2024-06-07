package com.application.intercom.gatekepper.Fragment.newFragment.ownerTenantRegularEntryHistroy

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.remote.owner.regularHistory.RegularHistoryList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.databinding.FragmentComletedOwnerTenantRegularEntryHistoryBinding
import com.application.intercom.gatekepper.activity.newFlow.ownerTenantRegularEntryHistoryDetails.OwnerTenantRegularEntryHistoryDetailsActivity
import com.application.intercom.gatekepper.gatekeeperAdapter.ownerTenantRegularEntryHistory.CompletedOwnerTenantRegularEntryHistoryAdapter
import com.application.intercom.gatekepper.gatekeeperAdapter.ownerTenantRegularEntryHistory.OngoingOwnerTenantRegularEntryHistoryAdapter
import com.application.intercom.helper.GPSService
import com.application.intercom.utils.*
import java.util.*
import kotlin.collections.ArrayList

class CompletedOwnerTenantRegularEntryHistoryFragment(val type: String) : Fragment(),
    CompletedOwnerTenantRegularEntryHistoryAdapter.SendData {
    lateinit var binding: FragmentComletedOwnerTenantRegularEntryHistoryBinding
    private lateinit var viewModel: OwnerSideViewModel
    private var list = ArrayList<RegularHistoryList.Data.Result>()
    private var adptr: CompletedOwnerTenantRegularEntryHistoryAdapter? = null
    private var flatId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComletedOwnerTenantRegularEntryHistoryBinding.inflate(layoutInflater)
        initView()
        listener()
        flatId = prefs.getString(SessionConstants.FLATID, GPSService.mLastLocation?.latitude.toString())
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()
        if (type.equals("tenant")) {
            regularVisitorHistoryListTenant()
        } else {
            regularVisitorHistoryList()
        }


    }

    private fun initialize() {
        val repo = OwnerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, OwnerSideFactory(repo))[OwnerSideViewModel::class.java]


    }

    private fun regularVisitorHistoryList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.regularVisitorHistoryOwner(token, "Completed",null)
    }

    private fun regularVisitorHistoryListTenant() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.regularVisitorHistoryTenant(token, "Completed",null)
    }

    private fun observer() {
        viewModel.regularVisitorHistoryOwnerLiveData.observe(
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
                                list.addAll(it.data.result)
                                binding.rcyRegularOngoing.layoutManager =
                                    LinearLayoutManager(requireContext())
                                adptr = CompletedOwnerTenantRegularEntryHistoryAdapter(
                                    requireContext(),
                                    list,
                                    this
                                )
                                binding.rcyRegularOngoing.adapter = adptr
                                adptr!!.notifyDataSetChanged()
                                if (list.isEmpty()) {
                                    binding.lottieEmpty.visibility = View.VISIBLE
                                    binding.rcyRegularOngoing.visibility = View.INVISIBLE
                                } else {
                                    binding.lottieEmpty.visibility = View.INVISIBLE
                                    binding.rcyRegularOngoing.visibility = View.VISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_404) {
                                //requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyRegularOngoing.visibility = View.INVISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyRegularOngoing.visibility = View.INVISIBLE
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
        viewModel.regularVisitorHistoryTenantLiveData.observe(
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
                                list.addAll(it.data.result)
                                binding.rcyRegularOngoing.layoutManager =
                                    LinearLayoutManager(requireContext())
                                adptr = CompletedOwnerTenantRegularEntryHistoryAdapter(
                                    requireContext(),
                                    list,
                                    this
                                )
                                binding.rcyRegularOngoing.adapter = adptr
                                adptr!!.notifyDataSetChanged()
                                if (list.isEmpty()) {
                                    binding.lottieEmpty.visibility = View.VISIBLE
                                    binding.rcyRegularOngoing.visibility = View.INVISIBLE
                                } else {
                                    binding.lottieEmpty.visibility = View.INVISIBLE
                                    binding.rcyRegularOngoing.visibility = View.VISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_404) {
                                //requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyRegularOngoing.visibility = View.INVISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyRegularOngoing.visibility = View.INVISIBLE
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


                        binding.rcyRegularOngoing.layoutManager =
                            LinearLayoutManager(requireContext())
                        adptr =
                            CompletedOwnerTenantRegularEntryHistoryAdapter(
                                requireContext(),
                                tempFilterList as ArrayList<RegularHistoryList.Data.Result>,
                                this@CompletedOwnerTenantRegularEntryHistoryFragment
                            )
                        binding.rcyRegularOngoing.adapter = adptr
                        adptr!!.notifyDataSetChanged()

                    }
                } else {
                    binding.rcyRegularOngoing.layoutManager = LinearLayoutManager(requireContext())
                    adptr = CompletedOwnerTenantRegularEntryHistoryAdapter(
                        requireContext(),
                        list,
                        this@CompletedOwnerTenantRegularEntryHistoryFragment
                    )
                    binding.rcyRegularOngoing.adapter = adptr
                    adptr!!.notifyDataSetChanged()
                }
            }

        })
    }

    override fun onCLick(visitorId: String) {
        startActivity(
            Intent(
                requireContext(), OwnerTenantRegularEntryHistoryDetailsActivity::class.java
            ).putExtra("visitorId", visitorId).putExtra("from", type)
        )
    }


}