package com.application.intercom.manager.visitorAndGatePass

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
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityManagerGatePassBinding
import com.application.intercom.manager.visitorAndGatePass.fragment.ManagerCompletedGatePassListFragment
import com.application.intercom.manager.visitorAndGatePass.fragment.ManagerOngoingGatePassListFragment
import com.application.intercom.utils.*
import com.google.android.material.tabs.TabLayoutMediator

class ManagerGatePassActivity : AppCompatActivity() {
    lateinit var bindng: ActivityManagerGatePassBinding
    private lateinit var manager_viewModel: ManagerSideViewModel
    private var flatOfBuildingList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingId: String = ""
    private var noty: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindng = ActivityManagerGatePassBinding.inflate(layoutInflater)
        noty = intent.getStringExtra("noty") ?: ""
        println("=====$noty")
        setContentView(bindng.root)
        initView()
        lstnr()
    }

    private fun initView() {
        flatOfBuildingList.add(0, "All")
        initialize()
        observer()
        flatOfBuilding()
        flatSpinner()
        bindng.gatePassToolbar.tvTittle.text = getString(R.string.gate_pass)
        bindng.viewPager.adapter = ScreenSlidePagerAdapter(this)
        TabLayoutMediator(bindng.tabLay, bindng.viewPager) { tab, position ->
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
                    return ManagerOngoingGatePassListFragment(flatOfBuildingId)
                }
                1 -> {
                    return ManagerCompletedGatePassListFragment(flatOfBuildingId)
                }

            }
            return ManagerOngoingGatePassListFragment(flatOfBuildingId)
        }
    }

    private fun initialize() {
        val repo1 = ManagerSideRepo(BaseApplication.apiService)
        manager_viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo1))[ManagerSideViewModel::class.java]
    }

    private fun flatOfBuilding() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        manager_viewModel.flatOfBuildingList(token)

    }

    private fun observer() {
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
                            println("---manager_flatlist$flatOfBuildingList")
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

    private fun flatSpinner() {
        //val genderList = resources.getStringArray(R.array.EditProfile)
        bindng.chooseSpiner.adapter =
            ArrayAdapter(this, R.layout.spinner_dropdown_item, flatOfBuildingList)
        bindng.chooseSpiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                if (bindng.chooseSpiner.selectedItemPosition > 0) {
                    flatOfBuildingId =
                        flatOfBuildingHashMapID.get(bindng.chooseSpiner.selectedItem.toString())
                            .toString()
                    println("---flateId$flatOfBuildingId")
                    bindng.viewPager.adapter =
                        ScreenSlidePagerAdapter(this@ManagerGatePassActivity)

                } else {
                    flatOfBuildingId = ""
                    println("---All$flatOfBuildingId")
                    bindng.viewPager.adapter =
                        ScreenSlidePagerAdapter(this@ManagerGatePassActivity)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun lstnr() {
        bindng.gatePassToolbar.ivBack.setOnClickListener {
//            finish()
            if (prefs.getString(SessionConstants.NOTYTYPE, "") == "GATEPASS_CREATE") {
                prefs.put(SessionConstants.NOTYTYPE, "")
                startActivity(
                    Intent(
                        this,
                        ManagerGatePassActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            } else if (noty == "noty_create_gatepass") {
                noty = ""
                finish()
            } else {
                finish()
            }
        }


    }
}