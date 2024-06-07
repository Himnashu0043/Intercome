package com.application.intercom.user.newflow.fav

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.prefs
import com.application.intercom.databinding.ActivityUserFavBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.user.community.CommunityActivity
import com.application.intercom.user.contact.ContactIntercomActivity
import com.application.intercom.user.newflow.NewSubUserActivity
import com.application.intercom.user.newflow.UserUpgradActivity
import com.application.intercom.utils.SessionConstants
import com.google.android.material.tabs.TabLayoutMediator

class UserFavActivity : AppCompatActivity(), ProfileAdapter.ProfileClick {
    lateinit var binding: ActivityUserFavBinding
    private var from: String = ""
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null
    private var availableContacts: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.ivBack.visibility = View.INVISIBLE
        binding.toolbar.ivmneu.visibility = View.VISIBLE
        from = intent.getStringExtra("from") ?: ""
        availableContacts = prefs.getInt(SessionConstants.AVAILABLE_CONTACTS).toInt()
        var lang =
            prefs.getString(SessionConstants.LANG, GPSService.mLastLocation?.latitude.toString())
        println("=====home$lang")
        if (lang.isEmpty()) {
            lang = Language.ENGLISH.languageCode
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
        lstner()
        rcyItem()
    }

    private fun rcyItem() {
        profile_list.add(ProfileModal(R.drawable.home_icon, getString(R.string.home)))
        profile_list.add(ProfileModal(R.drawable.property_icon, getString(R.string.property)))
        profile_list.add(ProfileModal(R.drawable.parking_icon, getString(R.string.parking)))
        profile_list.add(ProfileModal(R.drawable.fav_icon, getString(R.string.favorites)))
        /*profile_list.add(ProfileModal(R.drawable.visitor_icon, "MyActivity"))
        profile_list.add(ProfileModal(R.drawable.community_icon, "My Community"))
        profile_list.add(ProfileModal(R.drawable.billing_icon, "My Billings"))
        profile_list.add(ProfileModal(R.drawable.visitor_icon, "Visitors"))
        profile_list.add(ProfileModal(R.drawable.notics_icon, "Notice Board"))*/
        profile_list.add(
            ProfileModal(
                R.drawable.help_icon,
                getString(R.string.help_and_support)
            )
        )
        profile_list.add(ProfileModal(R.drawable.setting_icon, getString(R.string.settings)))
        profile_list.add(ProfileModal(R.drawable.share_new_icon, getString(R.string.share)))
        profile_list.add(ProfileModal(R.drawable.help_icon, getString(R.string.about)))
        profile_list.add(
            ProfileModal(
                R.drawable.privacy_icon,
                getString(R.string.privacy_policy)
            )
        )
        profile_list.add(
            ProfileModal(
                R.drawable.term_icon,
                getString(R.string.terms_amp_conditions)
            )
        )
        binding.nav.rcyProfile.layoutManager = LinearLayoutManager(this)
        profileAdapter = ProfileAdapter(this, profile_list, "user", this)
        binding.nav.rcyProfile.adapter = profileAdapter
        profileAdapter!!.notifyDataSetChanged()

    }

    private fun initView() {
        binding.toolbar.tvTittle.text = getString(R.string.fav_properties)

        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)
        /*   binding.viewPager.setCurrentItem(2,true)*/
        TabLayoutMediator(binding.tabLay, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.fav_property)
                }
                1 -> {
                    tab.text = getString(R.string.fav_parking)
                }

            }
        }.attach()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return FavPropertyFragment()
                }
                1 -> {
                    return FavParkingFragment()
                }
            }
            return FavPropertyFragment()
        }

    }


    private fun lstner() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.toolbar.ivmneu.setOnClickListener {
            binding.userFavDrw.openDrawer(GravityCompat.START)
        }
        binding.nav.tvEnglish.setOnClickListener {
            binding.nav.tvBl.visibility = View.VISIBLE
            binding.nav.tvEnglish.visibility = View.INVISIBLE
            prefs.put(SessionConstants.LANG, Language.BANGLA.languageCode)
            println("======test${Language.BANGLA.languageCode}")
            setLocale(Language.BANGLA.languageCode)
            finishAffinity()
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.nav.tvBl.setOnClickListener {
            binding.nav.tvBl.visibility = View.INVISIBLE
            binding.nav.tvEnglish.visibility = View.VISIBLE
            prefs.put(SessionConstants.LANG, Language.ENGLISH.languageCode)
            println("======test${Language.ENGLISH.languageCode}")
            setLocale(Language.ENGLISH.languageCode)
            finishAffinity()
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.nav.constraintLayout10.setOnClickListener {
            if (availableContacts > 0) {
                startActivity(Intent(this, UserUpgradActivity::class.java))

            } else {
                startActivity(
                    Intent(this, NewSubUserActivity::class.java).putExtra("from", "user_side_menu")
                )
            }

        }
        binding.nav.tvBecome.setOnClickListener {
            startActivity(
                Intent(this, CommunityActivity::class.java)
            )
        }

    }

    override fun onClick(position: Int) {
        when (position) {
            0 -> {

                startActivity(
                    Intent(this, MainActivity::class.java).putExtra(
                        "from",
                        "from_side_home"
                    )
                )
                binding.userFavDrw.closeDrawer(GravityCompat.START)
                /*startActivity(
                    Intent(this, MainActivity::class.java).putExtra(
                        "from",
                        "from_side_property"
                    )
                )*/
            }

            1 -> {
                startActivity(
                    Intent(this, MainActivity::class.java).putExtra(
                        "from",
                        "from_side_property"
                    )
                )

            }

            2 -> {
                /* startActivity(
                     Intent(this, MainActivity::class.java).putExtra(
                         "from",
                         "from_side_parking"
                     )
                 )*/
                startActivity(
                    Intent(this, MainActivity::class.java).putExtra(
                        "from",
                        "from_side_parking"
                    )
                )

            }

            3 -> {
                startActivity(
                    Intent(this, UserFavActivity::class.java)
                )


            }

            4 -> {

                /*startActivity(
                    Intent(this, CommunityActivity::class.java).putExtra(
                        "from",
                        "billing"
                    )
                )*/
                /* startActivity(
                     Intent(this, MainActivity::class.java).putExtra(
                         "from",
                         "from_property_details"
                     )
                 )*/
                startActivity(
                    Intent(this, ContactIntercomActivity::class.java)
                )

            }

            5 -> {

                /*startActivity(
                    Intent(this, CommunityActivity::class.java)
                )*/
                /*startActivity(
                    Intent(this, CommunityActivity::class.java).putExtra(
                        "from",
                        "visitor"
                    )
                )*/
                startActivity(Intent(this, TenantSettingActivity::class.java))
            }

            6 -> {
//                val openURL = Intent(Intent.ACTION_VIEW)
//                openURL.data = Uri.parse("https://intercomapp.page.link/Go1D")
//                startActivity(openURL)
                shareIntent(this, "https://intercomapp.page.link/Go1D")
                /* startActivity(
                     Intent(this, CommunityActivity::class.java).putExtra(
                         "from",
                         "billing"
                     )
                 )*/
                /*startActivity(
                    Intent(this, CommunityActivity::class.java).putExtra(
                        "from",
                        "notice"
                    )
                )*/
            }

            7 -> {
                startActivity(Intent(this, AboutUsActivity::class.java))
                /*startActivity(
                    Intent(this, CommunityActivity::class.java).putExtra(
                        "from",
                        "visitor"
                    )
                )*/
                /*startActivity(
                    Intent(this, ContactIntercomActivity::class.java)
                )*/
            }

            8 -> {
                /* startActivity(
                     Intent(this, CommunityActivity::class.java).putExtra(
                         "from",
                         "notice"
                     )
                 )*/
                startActivity(Intent(this, PrivacyPolicyActivity::class.java))
            }

            9 -> {
                /*startActivity(
                    Intent(this, ContactIntercomActivity::class.java)
                )*/
                startActivity(Intent(this, TermsOfServiceActivity::class.java))
            }

            10 -> {
                /* startActivity(Intent(this, TenantSettingActivity::class.java))*/
            }

            11 -> {

            }
        }
    }

    fun shareIntent(activity: Activity?, shearableLink: String?) {
        ShareCompat.IntentBuilder.from(activity!!).setText(shearableLink).setType("text/plain")
            .setChooserTitle("Share with the users").startChooser()
    }

    fun closeDrawer() {
        if (binding.userFavDrw.isDrawerOpen(GravityCompat.START)) {
            binding.userFavDrw.closeDrawer(GravityCompat.START)
        }
    }

    override fun onResume() {
        super.onResume()
        closeDrawer()
    }
}