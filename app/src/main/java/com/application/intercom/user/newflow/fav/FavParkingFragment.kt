package com.application.intercom.user.newflow.fav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.newUser.favList.UserFavParkingListRes
import com.application.intercom.data.model.remote.newUser.favList.UserFavPropertyListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.FragmentFavParkingBinding
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.user.newflow.adapter.UserFavAdapter
import com.application.intercom.user.newflow.adapter.UserFavParkingAdapter
import com.application.intercom.utils.*

class FavParkingFragment : Fragment(), UserFavParkingAdapter.FavParking {
    lateinit var binding: FragmentFavParkingBinding
    private var list = ArrayList<UserFavParkingListRes.Data>()
    private lateinit var viewModel: UserHomeViewModel
    private var adpter: UserFavParkingAdapter? = null
    private var getparkingId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavParkingBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()
    }

    private fun lstnr() {

    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]


    }

    private fun userFavParkingList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.userfavParking(token, "Parking")

    }

    private fun observer() {
        viewModel.userFavParkingListLiveData.observe(
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
                                if (list.isEmpty()) {
                                    binding.rcyFavPraking.visibility = View.INVISIBLE
                                    binding.lottieEmpty.visibility = View.VISIBLE
                                } else {
                                    binding.rcyFavPraking.visibility = View.VISIBLE
                                    binding.lottieEmpty.visibility = View.INVISIBLE
                                }
                                println("----listSIze${list.size}")
                                binding.rcyFavPraking.layoutManager =
                                    LinearLayoutManager(requireContext())
                                adpter = UserFavParkingAdapter(requireContext(), list, this)
                                binding.rcyFavPraking.adapter = adpter
                                adpter!!.notifyDataSetChanged()

                            }

                        }
                    }

                    is EmpResource.Failure -> {
                        EmpCustomLoader.hideLoader()
                        binding.rcyFavPraking.visibility = View.INVISIBLE
                        binding.lottieEmpty.visibility = View.VISIBLE
                        ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                    }
                    else -> {}
                }
            })
        viewModel.userAddFavListLiveData.observe(requireActivity(), androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            userFavParkingList()
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

    override fun onResume() {
        super.onResume()
        userFavParkingList()
    }

    override fun selectFavParking(parkingId: String) {
        getparkingId = parkingId
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.userAddfavProperty(token, null, getparkingId)
    }

}