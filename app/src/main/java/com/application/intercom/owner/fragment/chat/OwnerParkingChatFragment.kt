package com.application.intercom.owner.fragment.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.userCreateRoom.UserPropertyChatList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.FragmentOwnerParkingChatBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.owner.adapter.chat.OwnerParkingChatAdapter
import com.application.intercom.owner.adapter.chat.OwnerPropertyChatAdapter
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.utils.*

class OwnerParkingChatFragment(val key:String) : Fragment() {
    lateinit var binding: FragmentOwnerParkingChatBinding
    private var list = ArrayList<UserPropertyChatList.Data3>()
    private lateinit var viewModel: UserHomeViewModel
    private var mAdapter: OwnerParkingChatAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOwnerParkingChatBinding.inflate(layoutInflater)
        initView()
        listener()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()

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

    private fun observer() {
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
                            binding.rvParkingChat.layoutManager =
                                LinearLayoutManager(requireContext())
                            mAdapter = OwnerParkingChatAdapter(requireContext(), list,key)
                            binding.rvParkingChat.adapter = mAdapter
                        } else if (it.response_code == AppConstants.STATUS_404) {
                            requireContext().longToast("Data Not Found!!")
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

    }

    override fun onResume() {
        super.onResume()
        userRoomList()
    }


}