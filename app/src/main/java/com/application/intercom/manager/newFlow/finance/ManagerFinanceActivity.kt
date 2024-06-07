package com.application.intercom.manager.newFlow.finance

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ShareCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.prefs
import com.application.intercom.databinding.ActivityManagerFinanceBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.manager.complaint.RegisterComplaintsActivity
import com.application.intercom.manager.gatekeeper.GateKeeperListingActivity
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.manager.newFlow.expenses.fragment.PaidExpensesManagerFragment
import com.application.intercom.manager.newFlow.expenses.fragment.UnpaidExpensesManagerFragment
import com.application.intercom.manager.newFlow.finance.fragment.AccountFinanceManagerFragment
import com.application.intercom.manager.newFlow.finance.fragment.BillingFinanceManagerFragment
import com.application.intercom.manager.newFlow.finance.fragment.ReportsFinanceManagerFragment
import com.application.intercom.manager.notice.NoticeBoardActivity
import com.application.intercom.manager.visitorAndGatePass.ManagerVisitorGatePassActivity
import com.application.intercom.owner.activity.helpSupport.OwnerHelpSupportActivity
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.utils.SessionConstants
import com.google.android.material.tabs.TabLayoutMediator

class ManagerFinanceActivity : AppCompatActivity(),ProfileAdapter.ProfileClick {
    lateinit var binding: ActivityManagerFinanceBinding
    //side menu
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null

    //side mneu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerFinanceBinding.inflate(layoutInflater)
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
        lstnr()
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
        binding.toolbarFinance.tvTittle.text = getString(R.string.finance)
        binding.toolbarFinance.ivBack.visibility = View.INVISIBLE
        binding.toolbarFinance.ivmneu.visibility = View.VISIBLE
        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.billing)
                }
                1 -> {
                    tab.text = getString(R.string.account)
                }
                2 -> {
                    tab.text = getString(R.string.reports)
                }

            }
        }.attach()

    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return BillingFinanceManagerFragment()
                }
                1 -> {
                    return AccountFinanceManagerFragment()
                }
                2 -> {
                    return ReportsFinanceManagerFragment()
                }
            }
            return BillingFinanceManagerFragment()
        }
    }

    private fun lstnr() {
//        binding.toolbarFinance.ivBack.setOnClickListener {
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
        binding.toolbarFinance.ivmneu.setOnClickListener {
            binding.managerBillingDrw.openDrawer(GravityCompat.START)
        }
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
        if (binding.managerBillingDrw.isDrawerOpen(GravityCompat.START)) {
            binding.managerBillingDrw.closeDrawer(GravityCompat.START)
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
    override fun onResume() {
        super.onResume()
        closeDrawer()
    }
}