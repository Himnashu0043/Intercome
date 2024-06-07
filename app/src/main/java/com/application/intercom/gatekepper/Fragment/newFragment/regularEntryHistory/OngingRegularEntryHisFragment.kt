package com.application.intercom.gatekepper.Fragment.newFragment.regularEntryHistory

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.ViewModel.gateKeeperViewModel.GateKeeperHomeViewModel
import com.application.intercom.data.model.factory.gateKeeperFactory.GateKeeperFactory
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.gateKeeper.AddRegularVisitorEntryPostModel
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.RegularVisitorGateKeeperList
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.SingleEntryHistoryList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.gateKeeperRepo.GateKeeperHomeRepo
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.FragmentOngingRegularEntryHisBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.regularEntry.RegularEntryAdapter
import com.application.intercom.gatekepper.gatekeeperAdapter.regularEntryHistory.OngoingRegularEntryHisAdapter
import com.application.intercom.gatekepper.gatekeeperAdapter.singleEntryHistory.OngoingSingleEntryHisAdapter
import com.application.intercom.helper.GPSService
import com.application.intercom.utils.*
import java.util.*
import kotlin.collections.ArrayList

class OngingRegularEntryHisFragment(val flatOfBuildingId: String, val from: String) : Fragment(),
    RegularEntryAdapter.RegularClick {
    lateinit var binding: FragmentOngingRegularEntryHisBinding
    private var adptr: OngoingRegularEntryHisAdapter? = null
    private lateinit var viewModel: GateKeeperHomeViewModel
    private lateinit var manager_viewModel: ManagerSideViewModel
    private var list = ArrayList<RegularVisitorGateKeeperList.Data.Result>()

    private var buildingId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOngingRegularEntryHisBinding.inflate(layoutInflater)

        buildingId = prefs.getString(
            SessionConstants.NEWBUILDINGID,
            ""
        )
        println("---bui$buildingId")
        initView()
        listener()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()
//        flatOfBuilding()


    }

    private fun initialize() {
        val repo = GateKeeperHomeRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, GateKeeperFactory(repo))[GateKeeperHomeViewModel::class.java]

        val repo1 = ManagerSideRepo(BaseApplication.apiService)
        manager_viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo1))[ManagerSideViewModel::class.java]
    }

    private fun regularVisitorHistory() {
        if (from.equals("manager")) {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            manager_viewModel.managerRegularVisitorHistoryList(
                token,
                "Ongoing",
                flatOfBuildingId,
                buildingId
            )
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            viewModel.regularVisitorHistoryList(token, "Ongoing", flatOfBuildingId, buildingId)
        }

    }

    private fun flatOfBuilding() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.flatOfBuildingList(token)
    }

    private fun observer() {
        /* viewModel.flatOfBuildingListLiveData.observe(
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
                                 buildingId = it.data.result[0].buildingId
                                 regularVisitorHistory()
 //                                prefs.put(
 //                                    SessionConstants.NEWBUILDINGID,
 //                                    it.data.result[0].buildingId
 //                                )
 //                                println("------ttte${it.data.result[0].buildingId}")
                                 println("------ttte${buildingId}")
                             } else if (it.status == AppConstants.STATUS_500) {
                                 requireContext().longToast(it.message)
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
             })*/
        viewModel.regularVisitorHistoryListLiveData.observe(
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
                                adptr = OngoingRegularEntryHisAdapter(requireContext(), list, this)
                                binding.rcyRegularOngoing.adapter = adptr
                                adptr!!.notifyDataSetChanged()
                                if (list.isEmpty()) {
                                    binding.lottieEmpty.visibility = View.VISIBLE
                                    binding.rcyRegularOngoing.visibility = View.INVISIBLE
                                } else {
                                    binding.lottieEmpty.visibility = View.INVISIBLE
                                    binding.rcyRegularOngoing.visibility = View.VISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_500) {
                                // requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyRegularOngoing.visibility = View.INVISIBLE
                            } else if (it.status == AppConstants.STATUS_404) {
                                //requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyRegularOngoing.visibility = View.INVISIBLE
                            } else if (it.status == AppConstants.STATUS_FAILURE) {
                                // requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyRegularOngoing.visibility = View.INVISIBLE
                            }
                        }
                    }

                    is EmpResource.Failure -> {
                        EmpCustomLoader.hideLoader()
                        binding.lottieEmpty.visibility = View.VISIBLE
                        binding.rcyRegularOngoing.visibility = View.INVISIBLE
                        ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)

                    }
                    else -> {}
                }
            })

        viewModel.addRegularVisitorEntryLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            requireContext().longToast(getString(R.string.in_successfully))
                            regularVisitorHistory()
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireContext().longToast(it.message)
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
        viewModel.outRegularVisitorEntryLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            requireContext().longToast(getString(R.string.out_successfully))
                            regularVisitorHistory()
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireContext().longToast(it.message)
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
////manager
        manager_viewModel.managerRegularVisitorHistoryListLiveData.observe(
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
                                adptr = OngoingRegularEntryHisAdapter(requireContext(), list, this)
                                binding.rcyRegularOngoing.adapter = adptr
                                adptr!!.notifyDataSetChanged()
                                if (list.isEmpty()) {
                                    binding.lottieEmpty.visibility = View.VISIBLE
                                    binding.rcyRegularOngoing.visibility = View.INVISIBLE
                                } else {
                                    binding.lottieEmpty.visibility = View.INVISIBLE
                                    binding.rcyRegularOngoing.visibility = View.VISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_500) {
                                //requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyRegularOngoing.visibility = View.INVISIBLE
                            } else if (it.status == AppConstants.STATUS_404) {
                                // requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyRegularOngoing.visibility = View.INVISIBLE
                            } else if (it.status == AppConstants.STATUS_FAILURE) {
                                // requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyRegularOngoing.visibility = View.INVISIBLE
                            }
                        }
                    }

                    is EmpResource.Failure -> {
                        EmpCustomLoader.hideLoader()
                        binding.lottieEmpty.visibility = View.VISIBLE
                        binding.rcyRegularOngoing.visibility = View.INVISIBLE
                        ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)

                    }
                    else -> {}
                }
            })
        manager_viewModel.managerAddRegularVisitorEntryLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(requireActivity())
                    }
                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                requireContext().longToast(getString(R.string.in_successfully))
                                regularVisitorHistory()
                            } else if (it.status == AppConstants.STATUS_500) {
                                requireContext().longToast(it.message)
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

        manager_viewModel.managerOutRegularVisitorEntryLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(requireActivity())
                    }
                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                requireContext().longToast(getString(R.string.out_successfully))
                                regularVisitorHistory()
                            } else if (it.status == AppConstants.STATUS_500) {
                                requireContext().longToast(it.message)
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
        /*binding.seach.addTextChangedListener(object : TextWatcher {
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
                        adptr = OngoingRegularEntryHisAdapter(
                            requireContext(),
                            tempFilterList as ArrayList<RegularVisitorGateKeeperList.Data.Result>
                        )
                        binding.rcyRegularOngoing.adapter = adptr
                        adptr!!.notifyDataSetChanged()

                    }
                } else {
                    binding.rcyRegularOngoing.layoutManager =
                        LinearLayoutManager(requireContext())
                    adptr = OngoingRegularEntryHisAdapter(requireContext(), list)
                    binding.rcyRegularOngoing.adapter = adptr
                    adptr!!.notifyDataSetChanged()
                }
            }

        })*/
    }

    override fun onResume() {
        super.onResume()
        if (!flatOfBuildingId.isNullOrEmpty()) {
            regularVisitorHistory()
        } else {
            regularVisitorHistory()
        }
    }

    override fun onClick1(position: Int) {

    }

    override fun onAddEntry(flatId: String, visitorId: String) {
        if (from.equals("manager")) {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            val model = AddRegularVisitorEntryPostModel(flatId, visitorId)
            manager_viewModel.managerAddRegularVisitorEntry(token, model)
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            val model = AddRegularVisitorEntryPostModel(flatId, visitorId)
            viewModel.addRegularVisitorEntry(token, model)
        }

    }

    override fun onOutEntry(visitorId: String) {
        if (from.equals("manager")) {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            manager_viewModel.managerOutRegularVisitorEntry(token, visitorId)
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            viewModel.outRegularVisitorEntry(token, visitorId)
        }

    }

}