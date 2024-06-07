package com.application.intercom.owner.fragment.ownerHome

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.databinding.FragmentOwnerPropertyBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.activity.propertyDetails.OwnerPropertyDetailsActivity
import com.application.intercom.owner.adapter.property.OwnerPropertyAdapter
import com.application.intercom.tenant.activity.profile.ProfileActivity
import com.application.intercom.utils.*

class OwnerPropertyFragment : Fragment(), OwnerPropertyAdapter.ClickProperty {
    lateinit var binding: FragmentOwnerPropertyBinding
    private var adapter: OwnerPropertyAdapter? = null
    private lateinit var owner_viewModel: OwnerHomeViewModel
    private var flatList = ArrayList<OwnerFlatListRes.Data>()
    private var key: String = ""
    private lateinit var activity: OwnerMainActivity
    var drw: DrawerLayout? = null

    /*private var key: String = "property"
    private var parkingList = ArrayList<OwnerParkingListRes.Data>()
    private var send_PropertyList = ArrayList<OwnerPropertyModel>()*/
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOwnerPropertyBinding.inflate(layoutInflater)
        initView()
        listener()
        key = arguments?.getString("key").toString()
        Log.d("Himanshu", "onCreateView: $key")
//        activity = getActivity() as OwnerMainActivity
//        drw = activity.requireViewById(R.id.ownerDrw)
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()
        getOwnerFlatList()

    }

    private fun initialize() {
        val ownerModel = OwnerHomeRepo(BaseApplication.apiService)
        owner_viewModel = ViewModelProvider(
            this, OwnerHomeFactory(ownerModel)
        )[OwnerHomeViewModel::class.java]
    }

    private fun getOwnerFlatList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        owner_viewModel.ownerFlatList(token)
    }

    private fun observer() {
        owner_viewModel.ownerFlatListLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            flatList.clear()
                            flatList.addAll(it.data)
                            binding.rcyOwnerProperty.layoutManager =
                                LinearLayoutManager(requireContext())
                            adapter = OwnerPropertyAdapter(requireContext(), flatList, this)
                            binding.rcyOwnerProperty.adapter = adapter
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
        owner_viewModel.setAsHomeOwnerLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getOwnerFlatList()
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
//            /* startActivity(
//                 Intent(requireContext(), ProfileActivity::class.java).putExtra(
//                     "from", "owner"
//                 ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//             )*/
//            drw!!.closeDrawer(GravityCompat.START)
//            startActivity(
//                Intent(requireContext(), OwnerMainActivity::class.java).putExtra(
//                    "from",
//                    "from_side_home"
//                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            )
//        }

    }

    override fun onOwnerPropertyDetails(
        position: Int
    ) {
        startActivity(
            Intent(
                requireContext(), OwnerPropertyDetailsActivity::class.java
            ).putExtra("send", flatList[position]).putExtra("from", "property")
        )
    }

    override fun onClickMyHome(flatId: String) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        owner_viewModel.setAsHomeOwner(token, flatId)
    }



}