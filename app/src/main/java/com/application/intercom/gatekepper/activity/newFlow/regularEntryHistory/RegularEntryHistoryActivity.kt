package com.application.intercom.gatekepper.activity.newFlow.regularEntryHistory

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.ViewModel.gateKeeperViewModel.GateKeeperHomeViewModel
import com.application.intercom.data.model.factory.gateKeeperFactory.GateKeeperFactory
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.gateKeeperRepo.GateKeeperHomeRepo
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityRegularEntryHistoryBinding
import com.application.intercom.gatekepper.Fragment.newFragment.regularEntryHistory.CompletedRegularEntryHisFragment
import com.application.intercom.gatekepper.Fragment.newFragment.regularEntryHistory.OngingRegularEntryHisFragment
import com.application.intercom.gatekepper.Main.MainGateKepperActivity
import com.application.intercom.gatekepper.activity.newFlow.regularEntry.RegularEntryActivity
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.utils.*
import com.google.android.material.tabs.TabLayoutMediator

class RegularEntryHistoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegularEntryHistoryBinding
    private var flatOfBuildingId: String = ""
    private lateinit var viewModel: GateKeeperHomeViewModel
    private lateinit var manager_viewModel: ManagerSideViewModel
    private var flatOfBuildingList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatName: String = ""
    private var from: String = ""
    private var noty: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegularEntryHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        noty = intent.getStringExtra("noty").toString()
        println("---manager_Regular$from")
        initView()
        listener()
    }

    private fun initView() {
        flatOfBuildingList.add(0, "All")
        initialize()
        observer()
        flatOfBuilding()
        binding.toolbar.tvTittle.text = getString(R.string.regular_entry_history)
        binding.toolbar.tvText.visibility = View.INVISIBLE
        binding.toolbar.tvText.text = getString(R.string.regular_entry)
        binding.toolbar.tvText.textSize = 12F
        flatSpinner()
        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)
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

    private fun initialize() {
        val repo = GateKeeperHomeRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, GateKeeperFactory(repo))[GateKeeperHomeViewModel::class.java]
        val repo1 = ManagerSideRepo(BaseApplication.apiService)
        manager_viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo1))[ManagerSideViewModel::class.java]
    }

    private fun flatOfBuilding() {
        if (from.equals("manager")) {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            manager_viewModel.flatOfBuildingList(token)
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            viewModel.flatOfBuildingList(token)
        }

    }

    private fun observer() {
        viewModel.flatOfBuildingListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            it.data.result.forEach {
                                flatOfBuildingList.add(it.name)
                                flatOfBuildingHashMapID.put(it.name, it._id)

                            }
                            println("---flatlist$flatOfBuildingList")
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })
        ///manager

        manager_viewModel.flatOfBuildingListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            it.data.result.forEach {
                                flatOfBuildingList.add(it.name)
                                flatOfBuildingHashMapID.put(it.name, it._id)

                            }
                            println("---flatlist$flatOfBuildingList")
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })

    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
            if (prefs.getString(SessionConstants.NOTYTYPE, "") == "VISITOR_ADDED") {
                prefs.put(SessionConstants.NOTYTYPE, "")
                if (from == "gatekeeper") {
                    startActivity(
                        Intent(
                            this, MainGateKepperActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                } else {
                    startActivity(
                        Intent(
                            this, ManagerMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                }

            } else if (noty == "noty_list") {
                noty = ""
                finish()
                /*if (prefs.getString(SessionConstants.ROLE, "") == "gatekeeper") {
                    startActivity(
                        Intent(
                            this, MainGateKepperActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                } else {
                    startActivity(
                        Intent(
                            this, ManagerMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                }*/
            } else if (from == "kill_state") {
                if (prefs.getString(SessionConstants.ROLE, "") == "gatekeeper") {
                    startActivity(
                        Intent(
                            this, MainGateKepperActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                } else {
                    startActivity(
                        Intent(
                            this, ManagerMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                }
            } else {
                finish()
            }
        }
        binding.toolbar.tvText.setOnClickListener {
            startActivity(Intent(this, RegularEntryActivity::class.java).putExtra("from",from))
        }

    }

    private fun flatSpinner() {
        //val genderList = resources.getStringArray(R.array.EditProfile)
        binding.flatSpiner.adapter =
            ArrayAdapter(this, R.layout.spinner_dropdown_item, flatOfBuildingList)
        binding.flatSpiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {


                /*if (binding.flatSpiner.selectedItemPosition > 0) {
                    flatName = binding.flatSpiner.selectedItem.toString()
                    flatlistofVisitor()
                }*/ /*else {
                    binding.rcy.visibility = View.GONE
                    binding.constraintLayout.visibility = View.GONE
                }*/
                if (binding.flatSpiner.selectedItemPosition > 0) {
                    flatOfBuildingId =
                        flatOfBuildingHashMapID.get(binding.flatSpiner.selectedItem.toString())
                            .toString()
                    println("---flateId$flatOfBuildingId")
                    binding.viewPager.adapter =
                        ScreenSlidePagerAdapter(this@RegularEntryHistoryActivity)
                } else {
                    flatOfBuildingId = ""
                    binding.viewPager.adapter =
                        ScreenSlidePagerAdapter(this@RegularEntryHistoryActivity)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return OngingRegularEntryHisFragment(flatOfBuildingId,from)
                }
                1 -> {
                    return CompletedRegularEntryHisFragment(flatOfBuildingId,from)
                }
            }
            return OngingRegularEntryHisFragment(flatOfBuildingId,from)
        }
    }
}