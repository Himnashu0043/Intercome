package com.application.intercom.manager.gatekeeper

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
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.remote.manager.managerSide.ManagerGateKepperListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityGateKeeperListingBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.manager.complaint.RegisterComplaintsActivity
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.manager.newFlow.finance.ManagerFinanceActivity
import com.application.intercom.manager.notice.NoticeBoardActivity
import com.application.intercom.manager.visitorAndGatePass.ManagerVisitorGatePassActivity
import com.application.intercom.owner.activity.helpSupport.OwnerHelpSupportActivity
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.utils.AppConstants
import com.application.intercom.utils.EmpCustomLoader
import com.application.intercom.utils.ErrorUtil
import com.application.intercom.utils.SessionConstants

class GateKeeperListingActivity : AppCompatActivity(), ManagerGateKeeperListingAdapter.CLick,
    ProfileAdapter.ProfileClick {
    private var mAdapter: ManagerGateKeeperListingAdapter? = null
    private lateinit var viewModel: ManagerSideViewModel
    private lateinit var binding: ActivityGateKeeperListingBinding
    private var gateKeeperList = ArrayList<ManagerGateKepperListRes.Data.Result>()

    //side menu
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null

    //side mneu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGateKeeperListingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ///side menu
        var lang =
            prefs.getString(SessionConstants.LANG, GPSService.mLastLocation?.latitude.toString())
        println("=====home$lang")
        if (lang.isEmpty()) {
            lang = Language.ENGLISH.languageCode
            println("=====home$lang")
            setLocale(lang)
        } else {
            setLocale(lang)
        }
        if (lang == "bn") {
            binding.nav.tvBl.visibility = View.VISIBLE
            binding.nav.tvEnglish.visibility = View.INVISIBLE
        } else {
            binding.nav.tvBl.visibility = View.INVISIBLE
            binding.nav.tvEnglish.visibility = View.VISIBLE
        }
        //side mneu
        initView()
        listener()
        rcyItem()

    }

    private fun rcyItem() {
        profile_list.add(ProfileModal(R.drawable.home_icon, getString(R.string.home)))
        profile_list.add(ProfileModal(R.drawable.community_icon, getString(R.string.complain)))
        profile_list.add(
            ProfileModal(
                R.drawable.billing_icon, "Billing and Account"
            )
        )
        profile_list.add(
            ProfileModal(
                R.drawable.visitor_gatepass_icon, getString(R.string.visitors_gatepass)
            )
        )
        profile_list.add(ProfileModal(R.drawable.community_icon, getString(R.string.gatekeepers)))
        profile_list.add(ProfileModal(R.drawable.notics_icon, getString(R.string.notice_board)))
        profile_list.add(ProfileModal(R.drawable.help_icon, getString(R.string.help_and_support)))
        profile_list.add(ProfileModal(R.drawable.setting_icon, getString(R.string.settings)))
        profile_list.add(ProfileModal(R.drawable.share_new_icon, getString(R.string.share)))
        profile_list.add(ProfileModal(R.drawable.notics_icon, getString(R.string.about)))
        profile_list.add(ProfileModal(R.drawable.privacy_icon, getString(R.string.privacy_policy)))
        profile_list.add(
            ProfileModal(
                R.drawable.term_icon,
                getString(R.string.terms_and_conditions)
            )
        )

        binding.nav.rcyProfile.layoutManager = LinearLayoutManager(this)
        profileAdapter = ProfileAdapter(this, profile_list, "manager", this)
        binding.nav.rcyProfile.adapter = profileAdapter
        profileAdapter!!.notifyDataSetChanged()
    }

    private fun initView() {
        binding.toolbar.ivBack.visibility = View.INVISIBLE
        binding.toolbar.ivmneu.visibility = View.VISIBLE
        initialize()
        observer()


        binding.toolbar.tvTittle.text = getString(R.string.gatekeeper)
        //setAdapter()
    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

    private fun getGateKeeperList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.gateKeeperList(token)
    }

    private fun observer() {
        viewModel.gateKeeperListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            gateKeeperList.clear()
                            gateKeeperList.addAll(it.data.result)
                            setAdapter(gateKeeperList)
                            if (gateKeeperList.isEmpty()) {
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                        } else if (it.status == AppConstants.STATUS_500) {
                           // this.longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_404) {
                           // this.longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else {
                            binding.lottieEmpty.visibility = View.VISIBLE
                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })
    }

    private fun listener() {
//        binding.toolbar.ivBack.setOnClickListener {
//            finish()
//        }
        binding.nav.tvEnglish.setOnClickListener {
            binding.nav.tvBl.visibility = View.VISIBLE
            binding.nav.tvEnglish.visibility = View.INVISIBLE
            prefs.put(SessionConstants.LANG, Language.BANGLA.languageCode)
            println("======test${Language.BANGLA.languageCode}")
            setLocale(Language.BANGLA.languageCode)
            finishAffinity()
            startActivity(Intent(this, ManagerMainActivity::class.java))
        }
        binding.nav.tvBl.setOnClickListener {
            binding.nav.tvBl.visibility = View.INVISIBLE
            binding.nav.tvEnglish.visibility = View.VISIBLE
            prefs.put(SessionConstants.LANG, Language.ENGLISH.languageCode)
            println("======test${Language.ENGLISH.languageCode}")
            setLocale(Language.ENGLISH.languageCode)
            finishAffinity()
            startActivity(Intent(this, ManagerMainActivity::class.java))
        }
        binding.toolbar.ivmneu.setOnClickListener {
            binding.managerGateKeeperDrw.openDrawer(GravityCompat.START)
        }
        binding.tvAddGatekeeper.setOnClickListener {
            startActivity(Intent(this, CreateGateKepeerActivity::class.java))
        }
    }

    private fun setAdapter(list: ArrayList<ManagerGateKepperListRes.Data.Result> = ArrayList()) {
        binding.rvGatekeeperList.layoutManager = LinearLayoutManager(this)
        mAdapter = ManagerGateKeeperListingAdapter(this, list, this)
        binding.rvGatekeeperList.adapter = mAdapter

    }

    override fun onResume() {
        super.onResume()
        getGateKeeperList()
        closeDrawer()
    }

    override fun onEditClick(position: Int) {
        startActivity(
            Intent(this, CreateGateKepeerActivity::class.java).putExtra(
                "gateKeeperList",
                gateKeeperList[position]
            ).putExtra("from", "edit")
        )
    }

    override fun onClick(position: Int) {
        when (position) {
            0 -> {
                startActivity(
                    Intent(this, ManagerMainActivity::class.java).putExtra(
                        "from",
                        "from_side_home"
                    )
                )
            }
            1 -> {
                startActivity(
                    Intent(this, RegisterComplaintsActivity::class.java)
                )

            }
            2 -> {
                startActivity(
                    Intent(this, ManagerFinanceActivity::class.java)
                )

            }
            3 -> {
                startActivity(Intent(this, ManagerVisitorGatePassActivity::class.java))

            }
            4 -> {
                startActivity(
                    Intent(this, GateKeeperListingActivity::class.java)
                )


            }
            5 -> {
                startActivity(Intent(this, NoticeBoardActivity::class.java))


            }
            6 -> {
                startActivity(
                    Intent(this, OwnerHelpSupportActivity::class.java).putExtra(
                        "from", "Manager"
                    )
                )
            }
            7 -> {
                startActivity(Intent(this, TenantSettingActivity::class.java))
            }
            8 -> {
                shareIntent(this, "https://intercomapp.page.link/Go1D")


            }
            9 -> {
                startActivity(Intent(this, AboutUsActivity::class.java))


            }
            10 -> {
                startActivity(Intent(this, PrivacyPolicyActivity::class.java))


            }
            11 -> {
                startActivity(Intent(this, TermsOfServiceActivity::class.java))
            }


        }
    }

    fun closeDrawer() {
        if (binding.managerGateKeeperDrw.isDrawerOpen(GravityCompat.START)) {
            binding.managerGateKeeperDrw.closeDrawer(GravityCompat.START)
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