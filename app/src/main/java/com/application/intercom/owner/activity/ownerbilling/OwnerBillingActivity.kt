package com.application.intercom.owner.activity.ownerbilling

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.ViewModel.tenantViewModel.tenantHomeViewModel.TenantHomeViewModel
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.factory.tenantFactory.TenantHomeFactory
import com.application.intercom.data.model.local.FIlter_MonthsModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.data.repository.tenantRepo.tenantHomeRepo.TenantHomeRepo
import com.application.intercom.databinding.ActivityOwnerBillingBinding
import com.application.intercom.databinding.OwnerFilterBillingBottomSheetBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.owner.activity.OwnerParking.OwnerParkingActivity
import com.application.intercom.owner.activity.favorate.OwnerTenantFavorateActivity
import com.application.intercom.owner.activity.helpSupport.OwnerHelpSupportActivity
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.activity.ownerProperty.OwnerPropertyActivity
import com.application.intercom.owner.activity.ownerVisitor.OwnerVisitorActivity
import com.application.intercom.owner.activity.properties.OwnerPropertiesActivity
import com.application.intercom.tenant.Fragment.Billing.ApprovalFragment
import com.application.intercom.tenant.Fragment.Billing.PaidFragment
import com.application.intercom.tenant.Fragment.Billing.PendingFragment
import com.application.intercom.tenant.Fragment.Billing.UnPaidFragment
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.MyCommunity.TenantMyCommunityActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticBoardActivity
import com.application.intercom.tenant.activity.registerComplain.TenantRegisterComplainActivity
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator

class OwnerBillingActivity : BaseActivity<ActivityOwnerBillingBinding>(),
    ProfileAdapter.ProfileClick {
    override fun getLayout(): ActivityOwnerBillingBinding {
        return ActivityOwnerBillingBinding.inflate(layoutInflater)
    }

    private var flatOfBuildingId: String = ""
    private var flatOfBuildingList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private var from: String = ""
    private var filter_key: String = ""
    private var months_filter: String = ""
    lateinit var filterBottom: OwnerFilterBillingBottomSheetBinding
    lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var tenantviewModel: TenantHomeViewModel
    private lateinit var owner_viewModel: OwnerHomeViewModel
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null
    private var key: String = ""

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from").toString()
        key = intent.getStringExtra("key") ?: ""
        println("=========from$from")
        println("=========key$key")
        CommonUtil.themeSet(this, window)
        var lang =
            prefs.getString(SessionConstants.LANG, GPSService.mLastLocation?.latitude.toString())
        println("=====home$lang")
        if (lang.isEmpty()) {
            lang = Language.BANGLA.languageCode
            println("=====home$lang")
            setLocale(lang)
        }
        if (lang == "bn") {
            binding.nav.tvBl.visibility = View.VISIBLE
            binding.nav.tvEnglish.visibility = View.INVISIBLE
            binding.nav.imageView96.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.pro_bangla_img
                )
            )
        } else {
            binding.nav.tvBl.visibility = View.INVISIBLE
            binding.nav.tvEnglish.visibility = View.VISIBLE
            binding.nav.imageView96.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.pro_img
                )
            )
        }
        initView()
        lstnr()
        rcyItem()
    }

    private fun initView() {
        /* flatOfBuildingList.add(0, "Choose Flat")*/
        flatOfBuildingList.add(0, "All")
        initialize()
        observer()
        flatSpinner()

        binding.billingToolbar.ivBack.visibility = View.INVISIBLE
        binding.billingToolbar.ivmneu.visibility = View.VISIBLE
        binding.billingToolbar.tvTittle.text = getString(R.string.my_billings)
        binding.billingToolbar.tvFilter.visibility = View.VISIBLE
        binding.viewpagr.adapter = ScreenSlidePagerAdapter(this)
        if (prefs.getString(SessionConstants.NOTYTYPE, "") == "BILL_APPROVED") {
            prefs.put(SessionConstants.NOTYTYPE, "")
            binding.viewpagr.setCurrentItem(1, true)
        } else if (prefs.getString(SessionConstants.NOTYTYPE, "") == "NEW_BILL_PAID") {
            prefs.put(SessionConstants.NOTYTYPE, "")
            binding.viewpagr.setCurrentItem(3, true)
        } else if (prefs.getString(SessionConstants.NOTYTYPE, "") == "NEW_BILL_NOTIFY") {
            prefs.put(SessionConstants.NOTYTYPE, "")
            binding.viewpagr.setCurrentItem(0, true)
        } else if (prefs.getString(SessionConstants.NOTYTYPE, "") == "BILL_REJECT") {
            prefs.put(SessionConstants.NOTYTYPE, "")
            binding.viewpagr.setCurrentItem(0, true)
        } else if (key == "kill_BILL_REJECT") {
            key = ""
            binding.viewpagr.setCurrentItem(0, true)
        } else if (key == "notiy_billReject") {
            key = ""
            binding.viewpagr.setCurrentItem(0, true)
        } else if (key == "kill_New_All_Notify") {
            key = ""
            binding.viewpagr.setCurrentItem(0, true)
        } else if (key == "noti_All_notify") {
            key = ""
            binding.viewpagr.setCurrentItem(0, true)
        } else if (key == "kill_approved") {
            key = ""
            ///chnages
            // binding.viewpagr.setCurrentItem(3, true)
           // binding.viewpagr.setCurrentItem(1, true)
            binding.viewpagr.setCurrentItem(3, true)
        } else if (key == "notiy_paid") {
            key = ""
            ///chnages
            // binding.viewpagr.setCurrentItem(3, true)
            binding.viewpagr.setCurrentItem(1, true)
            ///chnages
        } else if (key == "noti_approved") {
            key = ""
            binding.viewpagr.setCurrentItem(3, true)
        } else if (key == "noti_pending") {
            key = ""
            binding.viewpagr.setCurrentItem(0, true)
        } else if (key == "bank_pay") {
            key = ""
            binding.viewpagr.setCurrentItem(2, true)
        } else if (key == "pending") {
            key = ""
            binding.viewpagr.setCurrentItem(2, true)
        } else if (key == "paid") {
            key = ""
            binding.viewpagr.setCurrentItem(1, true)
        } else if (key == "approve") {
            key = ""
            binding.viewpagr.setCurrentItem(3, true)
        } else if (key == "rent") {
            key = ""
            binding.viewpagr.setCurrentItem(2, true)

        } else {
            key = ""
            binding.viewpagr.setCurrentItem(0, true)
        }
        TabLayoutMediator(binding.tabLay, binding.viewpagr) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.unpaid)
                }
                1 -> {
                    tab.text = getString(R.string.paid)

                }
                2 -> {
                    tab.text = getString(R.string.pending)

                }
                3 -> {
                    tab.text = getString(R.string.approval)

                }

            }
        }.attach()
    }

    private fun rcyItem() {
        profile_list.add(ProfileModal(R.drawable.home_icon, getString(R.string.home)))
        profile_list.add(ProfileModal(R.drawable.property_icon, getString(R.string.my_properties)))
        profile_list.add(ProfileModal(R.drawable.parking_icon, getString(R.string.property)))
        profile_list.add(ProfileModal(R.drawable.service_icon, getString(R.string.parking)))
//        profile_list.add(ProfileModal(R.drawable.property_icon, "My Property"))
//        profile_list.add(ProfileModal(R.drawable.parking_icon, "My Parking"))
        profile_list.add(ProfileModal(R.drawable.fav_icon, getString(R.string.favorites)))
        profile_list.add(ProfileModal(R.drawable.community_icon, getString(R.string.my_community)))
        profile_list.add(ProfileModal(R.drawable.billing_icon, getString(R.string.complain)))
        profile_list.add(
            ProfileModal(
                R.drawable.visitor_icon,
                getString(R.string.visitors_gatepass)
            )
        )
        profile_list.add(
            ProfileModal(
                R.drawable.term_icon,
                "Billing Account"
            )
        )
        profile_list.add(ProfileModal(R.drawable.notics_icon, getString(R.string.notice_board)))
        profile_list.add(ProfileModal(R.drawable.help_icon, getString(R.string.help_and_support)))
        profile_list.add(ProfileModal(R.drawable.setting_icon, getString(R.string.settings)))
        profile_list.add(ProfileModal(R.drawable.share_new_icon, getString(R.string.share)))
        profile_list.add(ProfileModal(R.drawable.privacy_icon, getString(R.string.privacy_policy)))
        profile_list.add(
            ProfileModal(
                R.drawable.term_icon,
                getString(R.string.terms_and_conditions)
            )
        )
        profile_list.add(
            ProfileModal(
                R.drawable.term_icon,
                getString(R.string.about)
            )
        )
        binding.nav.rcyProfile.layoutManager = LinearLayoutManager(this)
        profileAdapter = ProfileAdapter(this, profile_list, "owner", this)
        binding.nav.rcyProfile.adapter = profileAdapter
        profileAdapter!!.notifyDataSetChanged()
    }

    private fun initialize() {
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
                            flatOfBuildingList.add(0, "Choose Flat")
                            flatOfBuildingList.add(1, "All")
                            it.data.forEach {
                                flatOfBuildingList.add(it.name)
                                flatOfBuildingHashMapID.put(it.name, it._id)


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

    private fun lstnr() {
//        binding.billingToolbar.ivBack.setOnClickListener {
//            finish()
//        }
        binding.billingToolbar.ivmneu.setOnClickListener {
            binding.billingDrw.openDrawer(GravityCompat.START)
        }
        binding.billingToolbar.tvFilter.setOnClickListener {
            filter()
        }
        binding.nav.tvEnglish.setOnClickListener {
            binding.nav.tvBl.visibility = View.VISIBLE
            binding.nav.tvEnglish.visibility = View.INVISIBLE
            prefs.put(SessionConstants.LANG, Language.BANGLA.languageCode)
            println("======test${Language.BANGLA.languageCode}")
            setLocale(Language.BANGLA.languageCode)
            finishAffinity()
            startActivity(Intent(this, OwnerMainActivity::class.java))
        }
        binding.nav.tvBl.setOnClickListener {
            binding.nav.tvBl.visibility = View.INVISIBLE
            binding.nav.tvEnglish.visibility = View.VISIBLE
            prefs.put(SessionConstants.LANG, Language.ENGLISH.languageCode)
            println("======test${Language.ENGLISH.languageCode}")
            setLocale(Language.ENGLISH.languageCode)
            finishAffinity()
            startActivity(Intent(this, OwnerMainActivity::class.java))
        }
        /* binding.payInAdvance.setOnClickListener {
             startActivity(Intent(this, TenantPaymentActivity::class.java))
         }*/

    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {

            when (position) {
                0 -> {
                    return UnPaidFragment(from)
                }
                1 -> {
                    return PaidFragment(from)
                }
                2 -> {
                    return PendingFragment(from)
                }
                3 -> {
                    return ApprovalFragment(from)
                }
            }


            return UnPaidFragment(from)
        }
    }

    fun filter() {
        filterBottom = OwnerFilterBillingBottomSheetBinding.inflate(LayoutInflater.from(this))
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
                    owner_viewModel.ownerFilterKeyLiveData.postValue(
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
                    this@OwnerBillingActivity,
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
                                println("---filter${months_filter.substring(0..2)}")

                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }


            }
        bottomSheetDialog.show()
    }

    private fun flatSpinner() {
        //val genderList = resources.getStringArray(com.application.intercom.R.array.EditProfile)
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
                            flatOfBuildingId =
                                flatOfBuildingHashMapID.get(binding.mainflatSpiner.selectedItem.toString())
                                    .toString()
                            owner_viewModel.ownerFlatBuildingIDLiveData.postValue(flatOfBuildingId)
                        } else {
                            flatOfBuildingId = "All"
                            owner_viewModel.ownerFlatBuildingIDLiveData.postValue(flatOfBuildingId)
                        }
                    }*/

                    if (binding.mainflatSpiner.selectedItemPosition > 0) {
                        flatOfBuildingId =
                            flatOfBuildingHashMapID.get(binding.mainflatSpiner.selectedItem.toString())
                                .toString()
                        owner_viewModel.ownerFlatBuildingIDLiveData.postValue(flatOfBuildingId)
                    } else {
                        flatOfBuildingId = "All"
                        owner_viewModel.ownerFlatBuildingIDLiveData.postValue(flatOfBuildingId)
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    override fun onResume() {
        super.onResume()
        if (from == "tenant") {
            getTenantFlatList()
        } else {
            getOwnerFlatList()
        }
        closeDrawer()
    }

    fun closeDrawer() {
        if (binding.billingDrw.isDrawerOpen(GravityCompat.START)) {
            binding.billingDrw.closeDrawer(GravityCompat.START)
        }
    }

    override fun onClick(position: Int) {
        when (position) {
            0 -> {
                startActivity(
                    Intent(this, OwnerMainActivity::class.java).putExtra(
                        "from", "from_side_home"
                    )
                )
            }
            1 -> {
                startActivity(
                    Intent(this, OwnerPropertiesActivity::class.java)
                )
            }
            2 -> {
                startActivity(
                    Intent(this, OwnerPropertyActivity::class.java)
                )
            }
            3 -> {
                startActivity(
                    Intent(this, OwnerParkingActivity::class.java)
                )
            }
            4 -> {
                startActivity(Intent(this, OwnerTenantFavorateActivity::class.java))
            }
            5 -> {
                startActivity(
                    Intent(
                        this, TenantMyCommunityActivity::class.java
                    ).putExtra("from", "owner")/*.putExtra("projectId", projectId)*/
                )
            }
            6 -> {
                startActivity(
                    Intent(
                        this, TenantRegisterComplainActivity::class.java
                    ).putExtra("from", "owner")
                )
            }
            7 -> {
                /*startActivity(
                    Intent(this, OwnerBillingActivity::class.java).putExtra(
                        "from", "owner"
                    )
                )*/
                startActivity(
                    Intent(this, OwnerVisitorActivity::class.java).putExtra(
                        "from", "owner"
                    )
                )

            }
            8 -> {
                startActivity(
                    Intent(
                        this, BillingAccountOwnerActivity::class.java
                    )
                )
            }
            9 -> {
//                startActivity(
//                    Intent(
//                        this, TenantNoticBoardActivity::class.java
//                    ).putExtra("from", "owner")
//                )
                startActivity(
                    Intent(
                        this, TenantNoticBoardActivity::class.java
                    ).putExtra("from", "owner")
                )
            }
            10 -> {
                startActivity(Intent(this, OwnerHelpSupportActivity::class.java))
            }
            11 -> {
                startActivity(Intent(this, TenantSettingActivity::class.java))

            }
            12 -> {
                shareIntent(this, "https://intercomapp.page.link/Go1D")
            }
            13 -> {
                startActivity(Intent(this, PrivacyPolicyActivity::class.java))


            }
            14 -> {
                startActivity(Intent(this, TermsOfServiceActivity::class.java))
            }
            15 -> {
                startActivity(Intent(this, AboutUsActivity::class.java))

            }

        }
    }

    fun shareIntent(activity: Activity?, shearableLink: String?) {
        ShareCompat.IntentBuilder
            .from(activity!!)
            .setText(shearableLink)
            .setType("text/plain")
            .setChooserTitle("Share with the users")
            .startChooser()
    }
}