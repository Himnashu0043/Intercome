package com.application.intercom.owner.fragment.ownerHome

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.remote.owner.flat.OwnerFlatListRes
import com.application.intercom.data.model.remote.owner.parking.OwnerParkingListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.databinding.FragmentOwnerParkingBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.activity.ownerParkingDetails.OwnerParkingDetailsActivity
import com.application.intercom.owner.activity.propertyDetails.OwnerPropertyDetailsActivity
import com.application.intercom.owner.adapter.parking.OwnerParkingAdapter
import com.application.intercom.owner.adapter.property.OwnerPropertyAdapter
import com.application.intercom.tenant.activity.profile.ProfileActivity
import com.application.intercom.utils.*

class OwnerParkingFragment : Fragment(), OwnerParkingAdapter.ClickParking {
    lateinit var binding: FragmentOwnerParkingBinding
    private var adapter: OwnerParkingAdapter? = null
    private var parkingList = ArrayList<OwnerParkingListRes.Data>()
    private lateinit var owner_viewModel: OwnerHomeViewModel
    private lateinit var activity: OwnerMainActivity
    var drw: DrawerLayout? = null
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOwnerParkingBinding.inflate(layoutInflater)
//        activity = getActivity() as OwnerMainActivity
//        drw = activity.requireViewById(R.id.ownerDrw)
        initView()
        listener()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()
        getOwnerParkingList()
    }

    private fun initialize() {
        val ownerModel = OwnerHomeRepo(BaseApplication.apiService)
        owner_viewModel = ViewModelProvider(
            this, OwnerHomeFactory(ownerModel)
        )[OwnerHomeViewModel::class.java]
    }

    private fun getOwnerParkingList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        owner_viewModel.ownerParkingList(token)
    }

    private fun observer() {
        owner_viewModel.ownerParkingListLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            parkingList.clear()
                            parkingList.addAll(it.data)
                            binding.rcyOwnerParking.layoutManager =
                                LinearLayoutManager(requireContext())
                            adapter = OwnerParkingAdapter(requireContext(), parkingList, this)
                            binding.rcyOwnerParking.adapter = adapter
                            adapter!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
                        } else {

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

    private fun listener() {
//        binding.toolbar.ivBack.setOnClickListener {
//           /* startActivity(
//                Intent(requireContext(), ProfileActivity::class.java).putExtra(
//                    "from", "owner"
//                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            )*/
//            drw!!.closeDrawer(GravityCompat.START)
//            startActivity(
//                Intent(requireContext(), OwnerMainActivity::class.java).putExtra(
//                    "from",
//                    "from_side_home"
//                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            )
//        }
    }

    override fun onOwnerParkingDetails(position: Int) {
        startActivity(
            Intent(
                requireContext(),
                OwnerParkingDetailsActivity::class.java
            ).putExtra("parkingList", parkingList[position]).putExtra("from", "parking")
        )
    }


}