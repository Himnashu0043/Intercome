package com.application.intercom.user.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.BaseFragment
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.userCreateRoom.UserPropertyChatList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.FragmentParkingChatBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.utils.*


class ParkingChatFragment : BaseFragment<FragmentParkingChatBinding>() {
    private var mAdapter: ParkingChatListingAdapter? = null
    private lateinit var viewModel: UserHomeViewModel
    private var list = ArrayList<UserPropertyChatList.Data3>()

    override fun lstnr() {

    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentParkingChatBinding {
        return FragmentParkingChatBinding.inflate(inflater, container, false)
    }

    override fun init() {
        initialize()


    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]
    }

    private fun userRoomList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.userRoomList(token, "parking")

    }

    override fun observer() {
        viewModel.userRoomListLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.response_code == AppConstants.STATUS_SUCCESS) {
                            list.clear()
                            list.addAll(it.Data)
                            setParkingChatAdapter(list)
                            if (list.isEmpty()) {
                                binding.rvParkingChat.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rvParkingChat.visibility = View.VISIBLE
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                        } else if (it.response_code == AppConstants.STATUS_404) {
                            // requireContext().longToast("Data Not Found!!")
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else {
                            binding.lottieEmpty.visibility = View.VISIBLE
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

    private fun setParkingChatAdapter(list: ArrayList<UserPropertyChatList.Data3> = ArrayList()) {
        binding.rvParkingChat.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = ParkingChatListingAdapter(requireContext(), list)
        binding.rvParkingChat.adapter = mAdapter

    }

    override fun onResume() {
        super.onResume()
        userRoomList()
    }
}