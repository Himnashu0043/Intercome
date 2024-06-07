package com.application.intercom.gatekepper.Fragment.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.gateKeeperViewModel.GateKeeperHomeViewModel
import com.application.intercom.data.model.factory.gateKeeperFactory.GateKeeperFactory
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.gateKeeperRepo.GateKeeperHomeRepo
import com.application.intercom.databinding.FragmentNewGatePassListBinding
import com.application.intercom.gatekepper.Fragment.newFragment.gatePassList.CompletedGatePassListFragment
import com.application.intercom.gatekepper.Fragment.newFragment.gatePassList.OngoingGatePassListFragment
import com.application.intercom.utils.*
import com.google.android.material.tabs.TabLayoutMediator


class NewGatePassListFragment : Fragment() {
    lateinit var binding: FragmentNewGatePassListBinding
    private lateinit var viewModel: GateKeeperHomeViewModel
    private var flatOfBuildingList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingMobileNumberHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewGatePassListBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }


    private fun initView() {
        binding.createToolbar.tvTittle.text = getString(R.string.gate_pass)
        binding.createToolbar.ivBack.visibility = View.INVISIBLE
        flatOfBuildingList.clear()
        flatOfBuildingList.add(0, "All")
        initialize()
        observer()

        binding.chooseSpiner.adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, flatOfBuildingList)
        binding.chooseSpiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                /*flatOfBuildingId =
                    flatOfBuildingHashMapID.get(binding.chooseSpiner.selectedItem.toString())
                        .toString()
                binding.viewPager.adapter = ScreenSlidePagerAdapter(requireActivity())
                println("---flateId$flatOfBuildingId")*/
                if (binding.chooseSpiner.selectedItemPosition > 0) {
                    flatOfBuildingId =
                        flatOfBuildingHashMapID.get(binding.chooseSpiner.selectedItem.toString())
                            .toString()
                    println("---flateId$flatOfBuildingId")
                    binding.viewPager.adapter =
                        ScreenSlidePagerAdapter(requireActivity())
                } else {
                    flatOfBuildingId = ""
                    binding.viewPager.adapter =
                        ScreenSlidePagerAdapter(requireActivity())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.viewPager.adapter = ScreenSlidePagerAdapter(requireActivity())
        TabLayoutMediator(binding.tabLay, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.ongoing)
                }
                1 -> {
                    tab.text = getString(R.string.completed)
                }


            }
        }.attach()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return OngoingGatePassListFragment(flatOfBuildingId)
                }
                1 -> {
                    return CompletedGatePassListFragment(flatOfBuildingId)
                }
            }
            return OngoingGatePassListFragment(flatOfBuildingId)
        }
    }

    private fun initialize() {
        val repo = GateKeeperHomeRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this, GateKeeperFactory(repo)
        )[GateKeeperHomeViewModel::class.java]


    }

    private fun flatOfBuilding() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.flatOfBuildingList(token)
    }

    private fun observer() {
        viewModel.flatOfBuildingListLiveData.observe(
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
                                flatOfBuildingList.clear()
                                flatOfBuildingList.add(0, "All")
                                it.data.result.forEach {
                                    flatOfBuildingList.add(it.name)
                                    flatOfBuildingHashMapID.put(it.name, it._id)
                                    flatOfBuildingBuildingHashMapID.put(it.name, it.buildingId)
                                }

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

    private fun lstnr() {

    }

    override fun onResume() {
        super.onResume()
        flatOfBuilding()
    }


}