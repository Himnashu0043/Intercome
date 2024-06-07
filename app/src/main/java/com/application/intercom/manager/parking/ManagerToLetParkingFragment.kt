package com.application.intercom.manager.parking

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.BaseFragment
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerHomeViewModel.ManagerHomeViewModel
import com.application.intercom.data.model.factory.managerFactory.managerHome.ManagerHomeFactory
import com.application.intercom.data.model.remote.manager.managerParking.ManagerParkingToletListRes
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerPropertyListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerHomeRepo.ManagerHomeRepo
import com.application.intercom.databinding.FragmentManagerToLetParkingBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.utils.*
import java.util.*
import kotlin.collections.ArrayList

class ManagerToLetParkingFragment : BaseFragment<FragmentManagerToLetParkingBinding>(),
    ManagerToLetParkingAdapter.Click {
    private var mAdapter: ManagerToLetParkingAdapter? = null
    private lateinit var viewModel: ManagerHomeViewModel
    private var toLetParkingList = ArrayList<ManagerParkingToletListRes.Data.Result>()
    private var isSearchHide: Boolean = false
    override fun lstnr() {
        binding.ivSearch.setOnClickListener {
            if (isSearchHide) {
                binding.ivSearch.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.search_bg_icon
                    )
                )
                isSearchHide = false
                binding.edtSearch.visibility = View.GONE
                binding.edtSearch.text.clear()
                binding.tvToLet.visibility = View.VISIBLE


            } else {
                binding.ivSearch.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.cross_bg_icon
                    )
                )
                binding.edtSearch.visibility = View.VISIBLE
                binding.tvToLet.visibility = View.INVISIBLE

                isSearchHide = true
            }

        }
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                var text = p0.toString().trim()
                if (text.isNotEmpty()) {
                    if (text.length > 0) {
                        var tempFilterList = toLetParkingList.filter {
                            it.parkingLocation.lowercase(Locale.ROOT)
                                .contains(text.lowercase(Locale.ROOT))
                        }
                        setAdapter(tempFilterList as ArrayList<ManagerParkingToletListRes.Data.Result>)
                        if (tempFilterList.isEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                "Data Not Found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    setAdapter(toLetParkingList)

                }
            }
        })
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentManagerToLetParkingBinding {
        return FragmentManagerToLetParkingBinding.inflate(inflater, container, false)
    }

    override fun init() {
        initialize()

    }

    private fun initialize() {
        val repo = ManagerHomeRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this, ManagerHomeFactory(repo)
        )[ManagerHomeViewModel::class.java]


    }

    private fun getParkingToLetList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.managerToLetParkingList(token)

    }

    override fun observer() {
        viewModel.managerToLetParkingListLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            toLetParkingList.clear()
                            toLetParkingList.addAll(it.data.result)
                            binding.tvToLet.text =
                                it.data.result.get(0).buildingInfo.get(0).buildingName
                            setAdapter(toLetParkingList)
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.rvManagerToLetParking.visibility = View.INVISIBLE
                        } else {
                            requireActivity().longToast(it.message)
                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireActivity(), it.throwable!!)
                }
                else -> {}
            }
        })
    }

    private fun setAdapter(list: ArrayList<ManagerParkingToletListRes.Data.Result> = ArrayList()) {
        binding.rvManagerToLetParking.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = ManagerToLetParkingAdapter(requireContext(), list, this)
        binding.rvManagerToLetParking.adapter = mAdapter

    }

    override fun onToletClick(position: Int) {
        startActivity(
            Intent(
                context,
                ManagerParkingToLetParkingActivity::class.java
            ).putExtra("tolet_parking_list", toLetParkingList[position])
                .putExtra("from", "tolet_parking")
        )
    }

    override fun onResume() {
        super.onResume()
        getParkingToLetList()
    }
}