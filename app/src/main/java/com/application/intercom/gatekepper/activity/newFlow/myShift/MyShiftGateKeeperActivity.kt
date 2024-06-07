package com.application.intercom.gatekepper.activity.newFlow.myShift

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.gateKeeperViewModel.GateKeeperHomeViewModel
import com.application.intercom.data.model.factory.gateKeeperFactory.GateKeeperFactory
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.GateKeeperProfileList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.gateKeeperRepo.GateKeeperHomeRepo
import com.application.intercom.databinding.ActivityMyShiftGateKeeperBinding
import com.application.intercom.gatekepper.Main.MainGateKepperActivity
import com.application.intercom.gatekepper.activity.newFlow.regularEntry.RegularEntryActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntry.SingleEntryActivity
import com.application.intercom.gatekepper.gatekeeperAdapter.myshift.MyShiftGateKeeperAdapter
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.owner.activity.helpSupport.OwnerHelpSupportActivity
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.utils.*

class MyShiftGateKeeperActivity : AppCompatActivity(), ProfileAdapter.ProfileClick {
    lateinit var binding: ActivityMyShiftGateKeeperBinding
    private var adptr: MyShiftGateKeeperAdapter? = null
    private var list = ArrayList<GateKeeperProfileList.Data>()
    private lateinit var viewModel: GateKeeperHomeViewModel

    ///side menu
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null

    /// side menu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyShiftGateKeeperBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nav.constraintLayout10.visibility = View.GONE
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

        } else {
            binding.nav.tvBl.visibility = View.INVISIBLE
            binding.nav.tvEnglish.visibility = View.VISIBLE
        }
        initView()
        listener()
        rcyItem()
    }

    private fun rcyItem() {
        profile_list.add(ProfileModal(R.drawable.home_icon, getString(R.string.home)))
        profile_list.add(ProfileModal(R.drawable.property_icon, getString(R.string.single_entry)))
        profile_list.add(ProfileModal(R.drawable.parking_icon, getString(R.string.regular_entry)))
        profile_list.add(ProfileModal(R.drawable.notics_icon, getString(R.string.my_shift)))
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
        profileAdapter = ProfileAdapter(this, profile_list, "gateKeeper", this)
        binding.nav.rcyProfile.adapter = profileAdapter
        profileAdapter!!.notifyDataSetChanged()
    }

    private fun initView() {
        binding.gateKepperToobar.ivBack.visibility = View.INVISIBLE
        binding.gateKepperToobar.ivmneu.visibility = View.VISIBLE
        initialize()
        observer()
        gateKeeperProfile()
        binding.gateKepperToobar.tvTittle.text = getString(R.string.gatekeeper)


    }

    private fun initialize() {
        val repo = GateKeeperHomeRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, GateKeeperFactory(repo))[GateKeeperHomeViewModel::class.java]

    }

    private fun gateKeeperProfile() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.gateKeeperProfile(token)
    }

    private fun observer() {
        viewModel.gateKeeperProfileLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            list.clear()
                            list.add(it.data)
                            binding.textView59.text = list.size.toString()
                            binding.rcyGateKeeper.layoutManager = LinearLayoutManager(this)
                            adptr = MyShiftGateKeeperAdapter(this, list)
                            binding.rcyGateKeeper.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.textView59.text = "0"
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
//        binding.gateKepperToobar.ivBack.setOnClickListener {
//            finish()
//        }
        binding.gateKepperToobar.ivmneu.setOnClickListener {
            binding.gateShiftDrw.openDrawer(GravityCompat.START)
        }
    }

    override fun onClick(position: Int) {
        when (position) {
            0 -> {
                startActivity(
                    Intent(this, MainGateKepperActivity::class.java).putExtra(
                        "from",
                        "from_side_home"
                    )
                )
                finish()

            }
            1 -> {
                startActivity(Intent(this, SingleEntryActivity::class.java))
//                startActivity(Intent(this, TenantSettingActivity::class.java))
//
            }
            2 -> {
                startActivity(Intent(this, RegularEntryActivity::class.java))
//                startActivity(Intent(this, CreateGatePassActivity::class.java))
            }
            3 -> {
                startActivity(Intent(this, MyShiftGateKeeperActivity::class.java))

            }
            4 -> {
                startActivity(
                    Intent(this, OwnerHelpSupportActivity::class.java).putExtra(
                        "from",
                        "gateKeeper"
                    )
                )

//                startActivity(Intent(this, VisitorHistoryTypeActivity::class.java))
            }
            5 -> {
                startActivity(Intent(this, TenantSettingActivity::class.java))

            }
            6 -> {
                shareIntent(this, "https://intercomapp.page.link/Go1D")
            }
            7 -> {
                startActivity(Intent(this, PrivacyPolicyActivity::class.java))
            }
            8 -> {
                startActivity(Intent(this, TermsOfServiceActivity::class.java))
            }
            9 -> {
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
        if (binding.gateShiftDrw.isDrawerOpen(GravityCompat.START)) {
            binding.gateShiftDrw.closeDrawer(GravityCompat.START)
        }
    }

    override fun onResume() {
        super.onResume()
        closeDrawer()
    }
}