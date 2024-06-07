package com.application.intercom.manager.property

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
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerPropertyListRes
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerToletFlatListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerHomeRepo.ManagerHomeRepo
import com.application.intercom.databinding.FragmentManagerToLetFlatBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.utils.*
import java.util.*
import kotlin.collections.ArrayList


class ManagerToLetFlatFragment : BaseFragment<FragmentManagerToLetFlatBinding>(),
    ManagerToLetFlatAdapter.ClickToLet {
    private var mAdapter: ManagerToLetFlatAdapter? = null
    private lateinit var viewModel: ManagerHomeViewModel
    private var toLetflatList = ArrayList<ManagerToletFlatListRes.Data.Result>()
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
                        var tempFilterList = toLetflatList.filter {
                            it.name.lowercase(Locale.ROOT)
                                .contains(text.lowercase(Locale.ROOT))
                        }
                        setAdapter(tempFilterList as ArrayList<ManagerToletFlatListRes.Data.Result>)
                        if (tempFilterList.isEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                "Data Not Found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    setAdapter(toLetflatList)

                }
            }
        })
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentManagerToLetFlatBinding {
        return FragmentManagerToLetFlatBinding.inflate(inflater, container, false)
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

    private fun getToLetFlatList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.managerToLetFlatList(token)

    }

    override fun observer() {
        viewModel.managerToLetFlatListLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            toLetflatList.clear()
                            toLetflatList.addAll(it.data.result)
                            setAdapter(toLetflatList)
                            binding.tvToLet.text =
                                it.data.result.get(0).buildingInfo.get(0).buildingName
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.rvManagerToLetFlat.visibility = View.INVISIBLE
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

    private fun setAdapter(list: ArrayList<ManagerToletFlatListRes.Data.Result> = ArrayList()) {
        binding.rvManagerToLetFlat.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = ManagerToLetFlatAdapter(requireContext(), list, this)
        binding.rvManagerToLetFlat.adapter = mAdapter

    }

    override fun onToLetProperty(position: Int) {
        startActivity(
            Intent(
                requireContext(),
                ManagerPropertyToLetFlatActivity::class.java
            ).putExtra("to_let", toLetflatList[position]).putExtra("from", "to_let_flatList")
        )
    }

    override fun onResume() {
        super.onResume()
        getToLetFlatList()
    }
}