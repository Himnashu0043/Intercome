package com.application.intercom.owner.activity.gatepass

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.ViewModel.tenantViewModel.tenantHomeViewModel.TenantHomeViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.factory.tenantFactory.TenantHomeFactory
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.data.repository.tenantRepo.tenantHomeRepo.TenantHomeRepo
import com.application.intercom.databinding.ActivityOwnerGatePassBinding
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.utils.*
import com.google.android.material.tabs.TabLayoutMediator

class OwnerGatePassActivity : BaseActivity<ActivityOwnerGatePassBinding>(){

    override fun getLayout(): ActivityOwnerGatePassBinding {
        return ActivityOwnerGatePassBinding.inflate(layoutInflater)
    }


    private var from: String = ""
    private var key: String = ""
    private lateinit var viewModel: OwnerSideViewModel
    private lateinit var tenantviewModel: TenantHomeViewModel
    private lateinit var owner_viewModel: OwnerHomeViewModel
    private var flatOfBuildingList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingBuildingHashMapID: HashMap<String, String> = HashMap()
    private lateinit var select_flat_adapter: ArrayAdapter<String>
    private var flatOfBuildingId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from").toString()
        key = intent.getStringExtra("key").toString()
        println("----from$from")
        initView()
        lstnr()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        flatOfBuildingList.add(0, "All")
        initialize()
        observer()


        flatSpinner()
        binding.toolbar.tvTittle.text = getString(R.string.gatepass)
        binding.toolbar.tvText.visibility = View.VISIBLE
        binding.toolbar.tvText.text = getString(R.string.create)

        binding.viewpagr.adapter = ScreenSlidePagerAdapter(this)
        if (prefs.getString(SessionConstants.NOTYTYPE, "") == "GATEPASS_COMPLETE") {
            binding.viewpagr.setCurrentItem(1, true)
        } else if (key == "kill_state") {
            binding.viewpagr.setCurrentItem(1, true)
        } else {
            binding.viewpagr.setCurrentItem(0, true)
        }
        TabLayoutMediator(binding.tabLay, binding.viewpagr) { tab, position ->
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
                    return OngoingGatePassOwnerFragment(flatOfBuildingId)
                }
                1 -> {
                    return CompletedGatePassOwnerFragment(flatOfBuildingId)

                }
            }
            return OngoingGatePassOwnerFragment(flatOfBuildingId)
        }
    }
    private fun initialize() {
        val repo = OwnerSideRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, OwnerSideFactory(repo))[OwnerSideViewModel::class.java]

        val ownerModel = OwnerHomeRepo(BaseApplication.apiService)
        owner_viewModel = ViewModelProvider(
            this, OwnerHomeFactory(ownerModel)
        )[OwnerHomeViewModel::class.java]


        val tenantModel = TenantHomeRepo(BaseApplication.apiService)
        tenantviewModel = ViewModelProvider(
            this, TenantHomeFactory(tenantModel)
        )[TenantHomeViewModel::class.java]

    }

    private fun getOwnerFlatList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        owner_viewModel.ownerFlatList(token)
    }

    private fun getTenantFlatList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        owner_viewModel.tenantFlatList(token)
    }



    private fun observer() {
        owner_viewModel.ownerFlatListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            flatOfBuildingList.clear()
                            flatOfBuildingList.add(0, "All")
                            it.data.forEach {
                                flatOfBuildingList.add(it.name)
                                flatOfBuildingHashMapID.put(it.name, it._id)
                                flatOfBuildingBuildingHashMapID.put(it.name, it.buildingId)

                            }

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
        owner_viewModel.tenantFlatListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            flatOfBuildingList.clear()
                            flatOfBuildingList.add(0, "All")
                            it.data.forEach {
                                flatOfBuildingList.add(it.name)
                                flatOfBuildingHashMapID.put(it.name, it._id)
                                flatOfBuildingBuildingHashMapID.put(it.name, it.buildingId)

                            }
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
        //val genderList = resources.getStringArray(com.application.intercom.R.array.EditProfile)
        binding.selectFlatSpiner.adapter =
            ArrayAdapter(this, R.layout.spinner_dropdown_item, flatOfBuildingList)
        binding.selectFlatSpiner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long,
                ) {

                    if (binding.selectFlatSpiner.selectedItemPosition > 0) {
                        flatOfBuildingId =
                            flatOfBuildingHashMapID.get(binding.selectFlatSpiner.selectedItem.toString())
                                .toString()
                        println("---flateId$flatOfBuildingId")
                        binding.viewpagr.adapter =
                            ScreenSlidePagerAdapter(this@OwnerGatePassActivity)
                    } else {
                        flatOfBuildingId = ""
                        binding.viewpagr.adapter =
                            ScreenSlidePagerAdapter(this@OwnerGatePassActivity)
                        println("---All$flatOfBuildingId")
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun lstnr() {
        binding.toolbar.tvText.setOnClickListener {
            startActivity(
                Intent(this, OwnerCreateGatePassActivity::class.java).putExtra(
                    "from",
                    from
                )
            )
        }
        binding.toolbar.ivBack.setOnClickListener {
            // finish()
            if (prefs.getString(SessionConstants.NOTYTYPE, "") == "GATEPASS_COMPLETE") {
                prefs.put(SessionConstants.NOTYTYPE, "")
                if (from == "tenant") {
                    startActivity(
                        Intent(
                            this,
                            TenantMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                } else {
                    startActivity(
                        Intent(
                            this,
                            OwnerMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                }
            } else if (key == "kill_state") {
                if (prefs.getString(SessionConstants.ROLE, "") == "tenant") {
                    startActivity(
                        Intent(
                            this,
                            TenantMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                } else {
                    startActivity(
                        Intent(
                            this,
                            OwnerMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                }
            } else {
                finish()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (from == "tenant") {
            getTenantFlatList()
        } else {
            getOwnerFlatList()
        }
    }


}