package com.application.intercom.user.myactivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.BaseFragment
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.getUserFlat.GetUserFlatViewModel
import com.application.intercom.data.model.factory.getUserFlat.GetUserFlatFactory
import com.application.intercom.data.model.remote.userParkingActivityData.UserParkingActivityListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.getUserFlat.UserFlatRepo
import com.application.intercom.databinding.FragmentMyActivityParkingBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.utils.*

class MyActivityParkingFragment : BaseFragment<FragmentMyActivityParkingBinding>(),
    MyActivityParkingListingAdapter.Click {
    private var mAdapter: MyActivityParkingListingAdapter? = null
    private lateinit var viewModel: GetUserFlatViewModel
    private var activityParkingList = ArrayList<UserParkingActivityListRes.Data>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun lstnr() {

    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyActivityParkingBinding {
        return FragmentMyActivityParkingBinding.inflate(inflater, container, false)

    }

    override fun init() {
        initialize()
        getUserparkingActivityData()
        setParkingAdapter()
    }

    private fun initialize() {
        val repo = UserFlatRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, GetUserFlatFactory(repo))[GetUserFlatViewModel::class.java]
    }

    private fun getUserparkingActivityData() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        viewModel.userParkingActivityData(token)

    }

    override fun observer() {
        viewModel.getuserParkingActivityLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            activityParkingList.clear()
                            activityParkingList.addAll(it.data)
                            mAdapter?.notifiyData(it.data)
                            if (activityParkingList.isNullOrEmpty()){
                                requireContext().longToast("Data not found!!")
                            }
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

    private fun setParkingAdapter(list: ArrayList<UserParkingActivityListRes.Data> = ArrayList()) {
        binding.rvMyActivityParking.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = MyActivityParkingListingAdapter(requireContext(), list, this)
        binding.rvMyActivityParking.adapter = mAdapter

    }

    override fun onParkingActivity(position: Int) {
        startActivity(
            Intent(
                requireContext(),
                MyActivityPropertyParkingDetailsActivity::class.java
            ).putExtra("parkingActivityList", activityParkingList[position])
                .putExtra("from", "parkingActivity")
        )
    }
}