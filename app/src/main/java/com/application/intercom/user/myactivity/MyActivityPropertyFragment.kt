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
import com.application.intercom.data.model.remote.owner.parking.OwnerParkingListRes
import com.application.intercom.data.model.remote.userFlatData.UserFlatListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.getUserFlat.UserFlatRepo
import com.application.intercom.databinding.FragmentMyActivityPropertyBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.owner.activity.ownerParkingDetails.OwnerParkingDetailsActivity
import com.application.intercom.utils.*


class MyActivityPropertyFragment : BaseFragment<FragmentMyActivityPropertyBinding>(),
    MyActivityPropertyListingAdapter.Click {

    private var mAdapter: MyActivityPropertyListingAdapter? = null
    private lateinit var viewModel: GetUserFlatViewModel
    private var propertyActivityList = ArrayList<UserFlatListRes.Data>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyActivityPropertyBinding {
        return FragmentMyActivityPropertyBinding.inflate(inflater, container, false)

    }

    override fun init() {
        initialize()
        getUserFlatData()
        setPropertyAdapter()
    }

    override fun lstnr() {

    }

    private fun initialize() {
        val repo = UserFlatRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, GetUserFlatFactory(repo))[GetUserFlatViewModel::class.java]
    }

    private fun getUserFlatData() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        viewModel.userFlatData(token)

    }

    override fun observer() {
        viewModel.getuserFlatLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            propertyActivityList.clear()
                            propertyActivityList.addAll(it.data)
                            mAdapter?.notifiyData(it.data)
                            if (propertyActivityList.isNullOrEmpty()){
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

    private fun setPropertyAdapter(list: ArrayList<UserFlatListRes.Data> = ArrayList()) {
        binding.rvMyActivityProperty.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = MyActivityPropertyListingAdapter(requireContext(), list, this)
        binding.rvMyActivityProperty.adapter = mAdapter

    }

    override fun onPropertyActivity(position: Int) {
        startActivity(
            Intent(
                requireContext(),
                MyActivityPropertyParkingDetailsActivity::class.java
            ).putExtra("propertyActivityList", propertyActivityList[position])
                .putExtra("from", "propertyActivity")
        )
    }
}