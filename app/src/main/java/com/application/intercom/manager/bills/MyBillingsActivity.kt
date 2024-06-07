package com.application.intercom.manager.bills

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.ViewModel.gateKeeperViewModel.GateKeeperHomeViewModel
import com.application.intercom.data.model.factory.gateKeeperFactory.GateKeeperFactory
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.FIlter_MonthsModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.gateKeeperRepo.GateKeeperHomeRepo
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityMyBillingsBinding
import com.application.intercom.databinding.ManagerFilterBillingBottomSheetBinding
import com.application.intercom.databinding.OwnerFilterBillingBottomSheetBinding
import com.application.intercom.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator


class MyBillingsActivity : BaseActivity<ActivityMyBillingsBinding>() {
    private var key: String = ""
    override fun getLayout(): ActivityMyBillingsBinding {
        return ActivityMyBillingsBinding.inflate(layoutInflater)
    }

    private lateinit var manager_viewModel: ManagerSideViewModel
    private var flatOfBuildingList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingId: String = ""
    lateinit var filterBottom: ManagerFilterBillingBottomSheetBinding
    lateinit var bottomSheetDialog: BottomSheetDialog
    private var filter_key: String = ""
    private var months_filter: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBillingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        listener()


    }

    private fun initView() {
        key = intent.getStringExtra("key") ?: ""
        binding.layoutFilter.visibility = View.VISIBLE
        flatOfBuildingList.add(0, "All")
        //flatOfBuildingList.add(1, "All")
        initialize()
        observer()
        flatOfBuilding()
        flatSpinner()
        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)
        if (key == "paid") {
            key = ""
            binding.viewPager.setCurrentItem(2, true)
            binding.tvTittle.text = "Paid Billing"
        } else if (key == "pending") {
            key = ""
            binding.viewPager.setCurrentItem(1, true)
            binding.tvTittle.text = "Approval Billing"
        } else {
            key = ""
            binding.viewPager.setCurrentItem(0, true)
            binding.tvTittle.text = "Unpaid Billing"
        }


        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "UnPaid"
                }
                1 -> {
                    tab.text = getString(R.string.approval)
                }

                2 -> {
                    tab.text = getString(R.string.paid)
                }

            }
        }.attach()
        binding.btnLogin.tv.text = getString(R.string.add_billings_1)
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(this, R.color.orange))

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
        binding.mainflatSpiner.adapter =
            ArrayAdapter(this, R.layout.spinner_dropdown_item, flatOfBuildingList)
        binding.mainflatSpiner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long,
                ) {

                    /*if (binding.mainflatSpiner.selectedItemPosition > 0) {
                        if (binding.mainflatSpiner.selectedItem.equals("All")) {
                            flatOfBuildingId = "All"
                            println("---All$flatOfBuildingId")
                           manager_viewModel.managerFlatBuildingIDLiveData.postValue(flatOfBuildingId)
                        } else {
                            flatOfBuildingId =
                                flatOfBuildingHashMapID.get(binding.mainflatSpiner.selectedItem.toString())
                                    .toString()
                            println("---flateId$flatOfBuildingId")
                            manager_viewModel.managerFlatBuildingIDLiveData.postValue(flatOfBuildingId)
                        }

                    }*/
                    if (binding.mainflatSpiner.selectedItemPosition > 0) {
                        flatOfBuildingId =
                            flatOfBuildingHashMapID.get(binding.mainflatSpiner.selectedItem.toString())
                                .toString()
                        println("---flateId$flatOfBuildingId")
                        manager_viewModel.managerFlatBuildingIDLiveData.postValue(flatOfBuildingId)
                    } else {
                        flatOfBuildingId = "All"
                        println("---All$flatOfBuildingId")
                        manager_viewModel.managerFlatBuildingIDLiveData.postValue(flatOfBuildingId)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun listener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnLogin.tv.setOnClickListener {
            startActivity(
                Intent(this, AddBillsActivity::class.java)
            )
        }
        binding.layoutFilter.setOnClickListener {
            filter()
        }
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return ManagerPendingBillsFragment()
                }
                1 -> {
                    return ManagerApprovalBillsFragment()
                }
                2 -> {
                    return ManagerPaidBillsFragment()
                }
            }
            return ManagerPendingBillsFragment()
        }
    }

    fun filter() {
        filterBottom = ManagerFilterBillingBottomSheetBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme).apply {
                setContentView(filterBottom.root)
                filterBottom.commonBtn.tv.text = getString(R.string.apply)
                filterBottom.checkBox3.setOnClickListener {
                    filterBottom.checkBox3.isChecked = true
                    filterBottom.checkBox31.isChecked = false
                    filter_key = "Rent"
                }
                filterBottom.checkBox31.setOnClickListener {
                    filterBottom.checkBox31.isChecked = true
                    filterBottom.checkBox3.isChecked = false
                    filter_key = "Service"
                }
                filterBottom.commonBtn.tv.setOnClickListener {
                    manager_viewModel.managerFilterKeyLiveData.postValue(
                        FIlter_MonthsModel(
                            filter_key,
                            months_filter
                        )
                    )
                    filter_key = ""
                    months_filter = ""
                    bottomSheetDialog.dismiss()
                }
                filterBottom.tvClose.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

                val genderList = resources.getStringArray(R.array.Months)
                filterBottom.flatSpiner.adapter = ArrayAdapter(
                    this@MyBillingsActivity,
                    R.layout.spinner_dropdown_item,
                    genderList
                )
                filterBottom.flatSpiner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long,
                        ) {


                            if (filterBottom.flatSpiner.selectedItemPosition > 0) {
                                months_filter =
                                    filterBottom.flatSpiner.selectedItem.toString().substring(0..2)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }


            }
        bottomSheetDialog.show()
    }
}