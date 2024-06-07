package com.application.intercom.tenant.activity.billing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.local.FIlter_MonthsModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.databinding.ActivityTenantBillingsBinding
import com.application.intercom.databinding.OwnerFilterBillingBottomSheetBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.owner.activity.favorate.OwnerTenantFavorateActivity
import com.application.intercom.owner.activity.helpSupport.OwnerHelpSupportActivity
import com.application.intercom.owner.activity.ownerVisitor.OwnerVisitorActivity
import com.application.intercom.tenant.Fragment.Billing.PendingFragment
import com.application.intercom.tenant.Fragment.Billing.TenantPaidFragment
import com.application.intercom.tenant.Fragment.Billing.UnPaidFragment
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.MyCommunity.TenantMyCommunityActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticBoardActivity
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.user.newflow.UserUpgradActivity
import com.application.intercom.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator

class TenantBillingsActivity : BaseActivity<ActivityTenantBillingsBinding>(),
    ProfileAdapter.ProfileClick {
    private var from: String = ""
    private var key: String = ""
    private var flatOfBuildingId: String = ""
    private var flatOfBuildingList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private lateinit var owner_viewModel: OwnerHomeViewModel
    lateinit var filterBottom: OwnerFilterBillingBottomSheetBinding
    lateinit var bottomSheetDialog: BottomSheetDialog
    private var filter_key: String = ""
    private var months_filter: String = ""

    ///side menu
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null

    ///side menu
    override fun getLayout(): ActivityTenantBillingsBinding {
        return ActivityTenantBillingsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from").toString()
        key = intent.getStringExtra("key").toString()
        println("-------------$from")
        println("-------------$key")
        println("-------------${prefs.getString(SessionConstants.NOTYTYPE, "")}")
        ///side menu
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
        //side menu
        initView()
        lstnr()
        rcyItem()
    }

    private fun rcyItem() {
        profile_list.add(ProfileModal(R.drawable.home_icon, getString(R.string.home)))
        profile_list.add(ProfileModal(R.drawable.property_icon, getString(R.string.property)))
        profile_list.add(ProfileModal(R.drawable.parking_icon, getString(R.string.parking)))
        profile_list.add(ProfileModal(R.drawable.service_icon, getString(R.string.services)))
        profile_list.add(ProfileModal(R.drawable.fav_icon, getString(R.string.favorites)))
        profile_list.add(ProfileModal(R.drawable.community_icon, getString(R.string.my_community)))
//        profile_list.add(ProfileModal(R.drawable.complaint_icon, "Complaint"))
        profile_list.add(ProfileModal(R.drawable.billing_icon, getString(R.string.my_billings)))
        profile_list.add(
            ProfileModal(
                R.drawable.visitor_icon,
                getString(R.string.visitors_gatepass)
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
        profile_list.add(ProfileModal(R.drawable.term_icon, getString(R.string.about)))
        binding.nav.rcyProfile.layoutManager = LinearLayoutManager(this)
        profileAdapter = ProfileAdapter(this, profile_list, "tenant", this)
        binding.nav.rcyProfile.adapter = profileAdapter
        profileAdapter!!.notifyDataSetChanged()
    }

    private fun initView() {
        binding.billingToolbar.ivBack.visibility = View.INVISIBLE
        binding.billingToolbar.ivmneu.visibility = View.VISIBLE
        /* flatOfBuildingList.add(0, "Choose Flat")*/
        flatOfBuildingList.add(0, "All")
        initialize()
        observer()
        flatSpinner()
        binding.billingToolbar.tvTittle.text = getString(R.string.my_billings)
        binding.billingToolbar.tvFilter.visibility = View.VISIBLE
        binding.viewpagr.adapter = ScreenSlidePagerAdapter(this)
        if (prefs.getString(SessionConstants.NOTYTYPE, "") == "BILL_APPROVED") {
            prefs.put(SessionConstants.NOTYTYPE, "")
            binding.viewpagr.setCurrentItem(1, true)
        } else if (prefs.getString(SessionConstants.NOTYTYPE, "") == "NEW_BILL_PAID_APPROVED") {
            prefs.put(SessionConstants.NOTYTYPE, "")
            binding.viewpagr.setCurrentItem(1, true)
        } else if (prefs.getString(SessionConstants.NOTYTYPE, "") == "NEW_BILL_PAID") {
            prefs.put(SessionConstants.NOTYTYPE, "")
            binding.viewpagr.setCurrentItem(1, true)
        } else if (prefs.getString(SessionConstants.NOTYTYPE, "") == "NEW_SERVICE_Bill") {
            prefs.put(SessionConstants.NOTYTYPE, "")
            binding.viewpagr.setCurrentItem(1, true)
        } else if (prefs.getString(SessionConstants.NOTYTYPE, "") == "NEW_BILL_NOTIFY") {
            prefs.put(SessionConstants.NOTYTYPE, "")
            binding.viewpagr.setCurrentItem(0, true)
        } else if (prefs.getString(SessionConstants.NOTYTYPE, "") == "New_Rent_Bill_Msg") {
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
        } else if (key == "kill_New_Rent_Bill_Msg") {
            key = ""
            binding.viewpagr.setCurrentItem(0, true)
        } else if (key == "noti_All_notify") {
            key = ""
            binding.viewpagr.setCurrentItem(0, true)
        } else if (key == "notiy_paid") {
            key = ""
            binding.viewpagr.setCurrentItem(1, true)
        } else if (key == "noti_pending") {
            key = ""
            binding.viewpagr.setCurrentItem(0, true)
        } else if (key == "kill_pending") {
            key = ""
            binding.viewpagr.setCurrentItem(0, true)
        } else if (key == "kill_Unpaid") {
            key = ""
            binding.viewpagr.setCurrentItem(0, true)
        } else if (key == "kill_paid") {
            key = ""
            binding.viewpagr.setCurrentItem(1, true)
        } else if (key == "noti_paid") {
            key = ""
            binding.viewpagr.setCurrentItem(1, true)
        } else if (key == "bank_pay") {
            key = ""
            binding.viewpagr.setCurrentItem(2, true)
        } else if (key == "paid") {
            key = ""
            binding.viewpagr.setCurrentItem(1, true)
        } else {
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


            }

        }.attach()
    }

    private fun initialize() {
        val ownerModel = OwnerHomeRepo(BaseApplication.apiService)
        owner_viewModel = ViewModelProvider(
            this, OwnerHomeFactory(ownerModel)
        )[OwnerHomeViewModel::class.java]

    }

    private fun getTenantFlatList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        owner_viewModel.tenantFlatList(token)
    }

    private fun observer() {
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
                            /*flatOfBuildingList.add(0, "Choose Flat")*/
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


    }

    private fun lstnr() {
       /* binding.billingToolbar.ivBack.setOnClickListener {
            finish()
        }*/
        binding.billingToolbar.ivmneu.setOnClickListener {
            binding.tenantBillingDrw.openDrawer(GravityCompat.START)
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
            startActivity(Intent(this, TenantMainActivity::class.java))
        }
        binding.nav.tvBl.setOnClickListener {
            binding.nav.tvBl.visibility = View.INVISIBLE
            binding.nav.tvEnglish.visibility = View.VISIBLE
            prefs.put(SessionConstants.LANG, Language.ENGLISH.languageCode)
            println("======test${Language.ENGLISH.languageCode}")
            setLocale(Language.ENGLISH.languageCode)
            finishAffinity()
            startActivity(Intent(this, TenantMainActivity::class.java))
        }
        binding.nav.constraintLayout10.setOnClickListener {
            startActivity(Intent(this, UserUpgradActivity::class.java))
        }
        /* binding.payInAdvance.setOnClickListener {
             startActivity(Intent(this, TenantPaymentActivity::class.java))
         }*/

    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {

            when (position) {
                0 -> {
                    return UnPaidFragment(from)
                }
                1 -> {
                    return TenantPaidFragment()

                }
                2 -> {
                    return PendingFragment(from)
                }

            }

            return UnPaidFragment(from)
        }
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
                            flatOfBuildingId = "All"
                            owner_viewModel.ownerFlatBuildingIDLiveData.postValue(flatOfBuildingId)
                        } else {
                            flatOfBuildingId =
                                flatOfBuildingHashMapID.get(binding.mainflatSpiner.selectedItem.toString())
                                    .toString()
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
                    owner_viewModel.ownerFilterKeyLiveData.postValue(FIlter_MonthsModel(filter_key,months_filter))
//                    if (filter_key == "Rent") {
//                        owner_viewModel.ownerFilterKeyLiveData.postValue(filter_key)
//                        filter_key = ""
//                    } else if (filter_key == "Service Charge") {
//                        owner_viewModel.ownerFilterKeyLiveData.postValue(filter_key)
//                        filter_key = ""
//                    } else {
//                        owner_viewModel.ownerBottomMonthsFilterLiveData.postValue(
//                            months_filter
//                        )
//                        months_filter = ""
//                    }
                    bottomSheetDialog.dismiss()
                }
                filterBottom.tvClose.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

                val genderList = resources.getStringArray(R.array.Months)
                filterBottom.flatSpiner.adapter = ArrayAdapter(
                    this@TenantBillingsActivity,
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
                                println("---tenantfilter${months_filter.substring(0..2)}")

                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }


            }
        bottomSheetDialog.show()
    }

    override fun onResume() {
        super.onResume()
        getTenantFlatList()
        closeDrawer()
    }

    override fun onClick(position: Int) {
        when (position) {
            0 -> {
                startActivity(
                    Intent(this, TenantMainActivity::class.java).putExtra(
                        "from",
                        "from_side_home"
                    )
                )

            }
            1 -> {
                startActivity(
                    Intent(this, TenantMainActivity::class.java).putExtra(
                        "from", "from_side_property"
                    )
                )

            }
            2 -> {
                startActivity(
                    Intent(this, TenantMainActivity::class.java).putExtra(
                        "from", "from_side_parking"
                    )
                )


            }
            3 -> {
                startActivity(
                    Intent(this, TenantMainActivity::class.java).putExtra(
                        "from", "from_side_service"
                    )
                )
            }
            4 -> {
                startActivity(
                    Intent(this, OwnerTenantFavorateActivity::class.java).putExtra("from", "tenant")
                )
            }
            5 -> {
                startActivity(
                    Intent(
                        this, TenantMyCommunityActivity::class.java
                    ).putExtra("from", "tenant")/*.putExtra("projectId", projectId)*/
                )
            }
            6 -> {
                startActivity(
                    Intent(this, TenantBillingsActivity::class.java)
                )
            }
            7 -> {
                startActivity(
                    Intent(this, OwnerVisitorActivity::class.java).putExtra(
                        "from", "tenant"
                    )
                )
            }
            8 -> {
                startActivity(Intent(this, TenantNoticBoardActivity::class.java))

            }
            9 -> {
                startActivity(
                    Intent(this, OwnerHelpSupportActivity::class.java).putExtra(
                        "from",
                        "tenant"
                    )
                )
            }
            10 -> {
                startActivity(Intent(this, TenantSettingActivity::class.java))
            }
            11 -> {
                shareIntent(this, "https://intercomapp.page.link/Go1D")
            }
            12 -> {
                startActivity(Intent(this, PrivacyPolicyActivity::class.java))

            }
            13 -> {
                startActivity(Intent(this, TermsOfServiceActivity::class.java))

            }
            14 -> {
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

    fun closeDrawer() {
        if (binding.tenantBillingDrw.isDrawerOpen(GravityCompat.START)) {
            binding.tenantBillingDrw.closeDrawer(GravityCompat.START)
        }
    }
}


