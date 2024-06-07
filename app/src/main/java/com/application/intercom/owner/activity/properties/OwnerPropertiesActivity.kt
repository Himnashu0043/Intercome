package com.application.intercom.owner.activity.properties

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.prefs
import com.application.intercom.databinding.ActivityOwnerPropertiesBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.owner.activity.OwnerParking.OwnerParkingActivity
import com.application.intercom.owner.activity.favorate.OwnerTenantFavorateActivity
import com.application.intercom.owner.activity.helpSupport.OwnerHelpSupportActivity
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.activity.ownerProperty.OwnerPropertyActivity
import com.application.intercom.owner.activity.ownerVisitor.OwnerVisitorActivity
import com.application.intercom.owner.activity.ownerbilling.BillingAccountOwnerActivity
import com.application.intercom.owner.activity.ownerbilling.OwnerBillingActivity
import com.application.intercom.owner.activity.registerComplain.OwnerAddRegisterComplainActivity
import com.application.intercom.owner.fragment.ownerHome.OwnerParkingFragment
import com.application.intercom.owner.fragment.ownerHome.OwnerPropertyFragment
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.MyCommunity.TenantMyCommunityActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticBoardActivity
import com.application.intercom.tenant.activity.registerComplain.TenantRegisterComplainActivity
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.user.newflow.UserUpgradActivity
import com.application.intercom.user.newflow.fav.UserFavActivity
import com.application.intercom.utils.SessionConstants
import com.google.android.material.tabs.TabLayoutMediator

class OwnerPropertiesActivity : BaseActivity<ActivityOwnerPropertiesBinding>(),
    ProfileAdapter.ProfileClick {
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null
    override fun getLayout(): ActivityOwnerPropertiesBinding {
        return ActivityOwnerPropertiesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        rcyItem()

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
       // profile_list.add(ProfileModal(R.drawable.community_icon, getString(R.string.my_billings)))
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

    private fun initView() {
        binding.toolbar.ivBack.visibility = View.INVISIBLE
        binding.toolbar.ivmneu.visibility = View.VISIBLE
        binding.toolbar.tvTittle.text = getString(R.string.my_properties)
        binding.viewpagr.adapter = ScreenSlidePagerAdapter(this)
        TabLayoutMediator(binding.tabLay, binding.viewpagr) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.my_properties)
                }
                1 -> {
                    tab.text = getString(R.string.my_parkings)

                }


            }
        }.attach()
        binding.toolbar.ivBack.setOnClickListener {

            startActivity(
                Intent(this, OwnerMainActivity::class.java).putExtra(
                    "from",
                    "from_side_home"
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }
        binding.toolbar.ivmneu.setOnClickListener {
            binding.ownerMyPropertyDrw.openDrawer(GravityCompat.START)
        }
        binding.nav.tvEnglish.setOnClickListener {
            binding.nav.tvBl.visibility = View.VISIBLE
            binding.nav.tvEnglish.visibility = View.INVISIBLE
            prefs.put(SessionConstants.LANG, Language.BANGLA.languageCode)
            println("======test${Language.BANGLA.languageCode}")
            setLocale(Language.BANGLA.languageCode)
            finishAffinity()
            startActivity(Intent(this,OwnerMainActivity::class.java))
        }
        binding.nav.tvBl.setOnClickListener {
            binding.nav.tvBl.visibility = View.INVISIBLE
            binding.nav.tvEnglish.visibility = View.VISIBLE
            prefs.put(SessionConstants.LANG, Language.ENGLISH.languageCode)
            println("======test${Language.ENGLISH.languageCode}")
            setLocale(Language.ENGLISH.languageCode)
            finishAffinity()
            startActivity(Intent(this,OwnerMainActivity::class.java))
        }
        binding.nav.constraintLayout10.setOnClickListener {
            startActivity(Intent(this, UserUpgradActivity::class.java))
        }
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {

            when (position) {
                0 -> {
                    return OwnerPropertyFragment()
                }
                1 -> {
                    return OwnerParkingFragment()
                }

            }


            return OwnerPropertyFragment()
        }
    }

    fun closeDrawer() {
        if (binding.ownerMyPropertyDrw.isDrawerOpen(GravityCompat.START)) {
            binding.ownerMyPropertyDrw.closeDrawer(GravityCompat.START)
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
                        this,
                        TenantRegisterComplainActivity::class.java
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
                        "from",
                        "owner"
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

    override fun onResume() {
        super.onResume()
        closeDrawer()
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