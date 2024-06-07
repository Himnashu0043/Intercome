package com.application.intercom.owner.activity.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.prefs
import com.application.intercom.databinding.ActivityOwnerMainBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.owner.activity.OwnerParking.OwnerParkingActivity
import com.application.intercom.owner.activity.chat.OwnerChatFragment
import com.application.intercom.owner.activity.favorate.OwnerTenantFavorateActivity
import com.application.intercom.owner.activity.helpSupport.OwnerHelpSupportActivity
import com.application.intercom.owner.activity.ownerProperty.OwnerPropertyActivity
import com.application.intercom.owner.activity.ownerVisitor.OwnerVisitorActivity
import com.application.intercom.owner.activity.ownerbilling.BillingAccountOwnerActivity
import com.application.intercom.owner.activity.properties.OwnerPropertiesActivity
import com.application.intercom.owner.fragment.ownerHome.OwnerHomeFragment
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
import com.application.intercom.user.newflow.newFragment.ListingPropertyFragment
import com.application.intercom.user.profile.UserProfileFragment
import com.application.intercom.user.service.UserServiceFragment
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.SessionConstants

class OwnerMainActivity : BaseActivity<ActivityOwnerMainBinding>(), View.OnClickListener,
    GestureDetector.OnGestureListener, ProfileAdapter.ProfileClick {
    var doubleBackToExitPressedOnce = false
    val fragOwnerHome = OwnerHomeFragment()
    val fragOwnerProperty = OwnerPropertyFragment()
    val fragOwnerService = UserServiceFragment()
    val fragOwnerParking = OwnerParkingFragment()
    val fragUserMyActivity = OwnerChatFragment()

    //   val fragOwnerMyActivity = UserMyActivityFragment()
    val fragListingProperty = ListingPropertyFragment()
    val fragUserProfile = UserProfileFragment()
    private var from: String = ""
    private lateinit var gestureDetector: GestureDetector
    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null
    override fun getLayout(): ActivityOwnerMainBinding {
        return ActivityOwnerMainBinding.inflate(layoutInflater)
    }

    companion object {
        fun getIntent(context: Context?): Intent {
            return Intent(context, OwnerMainActivity::class.java)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gestureDetector = GestureDetector(this)
        initView()
        lstnr()
        initCtrl()
        CommonUtil.themeSet(this, window)
        from = intent.getStringExtra("from").toString()
        if (intent != null && from.equals("from_property_details")) {
            onSelectView("UserMyActivity")
        } else if (intent != null && from.equals("from_side_property")) {
            onSelectView("UserProperty")
        } else if (intent != null && from.equals("from_side_parking")) {
            onSelectView("UserParking")
        } else if (intent != null && from.equals("from_side_service")) {
            onSelectView("UserService")
        } else if (intent != null && from.equals("from_side_home")) {
            onSelectView("UserHome")
        } else if (intent != null && from.equals("from_myList")) {
            onSelectView("UserMyList")
        } else if (intent != null && from.equals("from_noti_side")) {
            onSelectView("UserActivity")
        } else {
            onSelectView("UserHome")
        }
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


    }

   /* fun getDeepLink() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )

        if (intent.data != null) {
            if (intent.data?.toString()?.contains("Community") == true) {
                if (token.isNotEmpty()) {
                    if (prefs.getString(
                            SessionConstants.TOKEN,
                            ""
                        ).isNotEmpty() && prefs.getString(
                            SessionConstants.ROLE,
                            ""
                        ) == AppConstants.OWNER
                    ) {
                        val communityId = intent.data?.toString()?.split("/")?.last() ?: ""
                        startActivity(
                            Intent(this, TenantMyCommunityActivity::class.java).putExtra(
                                "from", prefs.getString(
                                    SessionConstants.ROLE, ""
                                )
                            ).putExtra("storeId", communityId)
                        )
                    } else {
                        val communityId = intent.data?.toString()?.split("/")?.last() ?: ""
                        startActivity(
                            Intent(this, TenantMyCommunityActivity::class.java).putExtra(
                                "from", prefs.getString(
                                    SessionConstants.ROLE, ""
                                )
                            ).putExtra("storeId", communityId)
                        )
                    }

                } else {
                    intent.data?.toString()?.split("/")?.last() ?: ""
                    finishAffinity()
                    startActivity(Intent(this, LoginUsingOtpActivity::class.java))
                }
            }
        }

    }*/

    private fun initView() {
        profile_list.add(ProfileModal(R.drawable.home_icon, getString(R.string.home)))
        profile_list.add(ProfileModal(R.drawable.property_icon, getString(R.string.my_properties)))
        profile_list.add(ProfileModal(R.drawable.parking_icon, getString(R.string.property)))
        profile_list.add(ProfileModal(R.drawable.service_icon, getString(R.string.parking)))
//        profile_list.add(ProfileModal(R.drawable.property_icon, "My Property"))
//        profile_list.add(ProfileModal(R.drawable.parking_icon, "My Parking"))
        profile_list.add(ProfileModal(R.drawable.fav_icon, getString(R.string.favorites)))
        profile_list.add(ProfileModal(R.drawable.community_icon, getString(R.string.my_community)))
        profile_list.add(ProfileModal(R.drawable.billing_icon, getString(R.string.complain)))
        //profile_list.add(ProfileModal(R.drawable.community_icon, getString(R.string.my_billings)))
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

    private fun initCtrl() {
        binding.constraintLayout27.setOnClickListener(this)
        binding.constraintLayout28.setOnClickListener(this)
        binding.constraintLayout29.setOnClickListener(this)
        binding.constraintLayout30.setOnClickListener(this)
        binding.constraintLayout31.setOnClickListener(this)
        /* binding.ivHomeproperty.setOnClickListener(this)
                binding.ivHomeservice.setOnClickListener(this)
                binding.ivParking.setOnClickListener(this)
                binding.ivMyActivity.setOnClickListener(this)*/
    }

    private fun lstnr() {
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
        binding.nav.tvPrivacyPolicy.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }
        binding.nav.tvTerms.setOnClickListener {
            startActivity(Intent(this, TermsOfServiceActivity::class.java))
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.constraintLayout27 -> {
                onSelectView("UserHome")
            }
            /* R.id.iv_homeproperty -> {
                 onSelectView("UserProperty")
             }
             R.id.iv_homeservice -> {
                 onSelectView("UserService")
             }
             R.id.iv_parking -> {
                 onSelectView("UserParking")

             }
             R.id.iv_myActivity -> {
                 onSelectView("UserMyActivity")
             }*/
            R.id.constraintLayout28 -> {
                onSelectView("UserMyList")
            }
            R.id.constraintLayout29 -> {
                onSelectView("UserActivity")
            }
            R.id.constraintLayout30 -> {
                onSelectView("UserService")

            }
            R.id.constraintLayout31 -> {
                onSelectView("UserProfile")
            }
        }
    }

    private fun onSelectView(from: String) {
        if (from == "UserHome") {
            /*binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.new_user_home_active))
            *//*binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.property_unactive))
            binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.service_unactive))
            binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.parking_unactive))
            binding.ivMyActivity.setImageDrawable(resources.getDrawable(R.drawable.myactivity_unactive))*//*
            binding.ivMylist.setImageDrawable(resources.getDrawable(R.drawable.new_user_mylist_unactive))
            binding.ivActivity.setImageDrawable(resources.getDrawable(R.drawable.new_user_activity_unactive))
            binding.ivService.setImageDrawable(resources.getDrawable(R.drawable.new_user_service_unactive))
            binding.ivHomeprofile.setImageDrawable(resources.getDrawable(R.drawable.new_user_profile_unactive))*/
            binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.without_name_home_active))
            binding.ivMylist.setImageDrawable(resources.getDrawable(R.drawable.without_name_list_unactive))
            binding.ivActivity.setImageDrawable(resources.getDrawable(R.drawable.without_name_activity_unactive))
            binding.ivService.setImageDrawable(resources.getDrawable(R.drawable.without_name_service_unactive))
            binding.ivHomeprofile.setImageDrawable(resources.getDrawable(R.drawable.without_name_profile_unactive))
            binding.textView253.setTextColor(ContextCompat.getColor(this, R.color.orange))
            binding.tvListName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvActivityName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvServiceName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvProfileName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            replaceFrag(fragOwnerHome, "fragUserHome", null)

        } else if (from == "UserProperty") {
            binding.constraintLayout2.visibility = View.GONE
            binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.new_user_home_unactive))
            /* binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.property_active))
             binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.service_unactive))
             binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.parking_unactive))
             binding.ivMyActivity.setImageDrawable(resources.getDrawable(R.drawable.myactivity_unactive))*/
            binding.ivMylist.setImageDrawable(resources.getDrawable(R.drawable.new_user_mylist_active))
            binding.ivActivity.setImageDrawable(resources.getDrawable(R.drawable.new_user_activity_unactive))
            binding.ivService.setImageDrawable(resources.getDrawable(R.drawable.new_user_service_unactive))
            binding.ivHomeprofile.setImageDrawable(resources.getDrawable(R.drawable.new_user_profile_unactive))
            /*val bundle = Bundle()
            bundle.putString("key", "userProperty")*/
            replaceFrag(fragOwnerProperty, "fragUserProperty", null)

        } else if (from == "UserActivity") {
            /* binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.home_unactive))
             binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.property_unactive))
             binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.service_unactive))
             binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.parking_active))
             binding.ivMyActivity.setImageDrawable(resources.getDrawable(R.drawable.myactivity_unactive))*/
            /*binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.new_user_home_unactive))
            binding.ivMylist.setImageDrawable(resources.getDrawable(R.drawable.new_user_mylist_unactive))
            binding.ivActivity.setImageDrawable(resources.getDrawable(R.drawable.new_user_activity_active))
            binding.ivService.setImageDrawable(resources.getDrawable(R.drawable.new_user_service_unactive))
            binding.ivHomeprofile.setImageDrawable(resources.getDrawable(R.drawable.new_user_profile_unactive))*/
            binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.without_name_home_unactive))
            binding.ivMylist.setImageDrawable(resources.getDrawable(R.drawable.without_name_list_unactive))
            binding.ivActivity.setImageDrawable(resources.getDrawable(R.drawable.without_name_activity_active))
            binding.ivService.setImageDrawable(resources.getDrawable(R.drawable.without_name_service_unactive))
            binding.ivHomeprofile.setImageDrawable(resources.getDrawable(R.drawable.without_name_profile_unactive))
            binding.textView253.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvListName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvActivityName.setTextColor(ContextCompat.getColor(this, R.color.orange))
            binding.tvServiceName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvProfileName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            val bundle = Bundle()
            bundle.putString("key", "owner_activity")
            replaceFrag(fragUserMyActivity, "fragUserMyActivity", null)

        } else if (from == "UserService") {
            /* binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.home_unactive))
             binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.property_unactive))
             binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.service_active))
             binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.parking_unactive))
             binding.ivMyActivity.setImageDrawable(resources.getDrawable(R.drawable.myactivity_unactive))*/
            /* binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.new_user_home_unactive))
             binding.ivMylist.setImageDrawable(resources.getDrawable(R.drawable.new_user_mylist_unactive))
             binding.ivActivity.setImageDrawable(resources.getDrawable(R.drawable.new_user_activity_unactive))
             binding.ivService.setImageDrawable(resources.getDrawable(R.drawable.new_user_service_active))
             binding.ivHomeprofile.setImageDrawable(resources.getDrawable(R.drawable.new_user_profile_unactive))*/
            binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.without_name_home_unactive))
            binding.ivMylist.setImageDrawable(resources.getDrawable(R.drawable.without_name_list_unactive))
            binding.ivActivity.setImageDrawable(resources.getDrawable(R.drawable.without_name_activity_unactive))
            binding.ivService.setImageDrawable(resources.getDrawable(R.drawable.without_name_service_active))
            binding.ivHomeprofile.setImageDrawable(resources.getDrawable(R.drawable.without_name_profile_unactive))
            binding.textView253.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvListName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvActivityName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvServiceName.setTextColor(ContextCompat.getColor(this, R.color.orange))
            binding.tvProfileName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            val bundle = Bundle()
            bundle.putString("key", "user")
            replaceFrag(fragOwnerService, "fragUserService", bundle)

        } else if (from == "UserProperty") {
            binding.constraintLayout2.visibility = View.GONE
            val bundle = Bundle()
            bundle.putString("key", "user_property")
            replaceFrag(fragOwnerProperty, "fragUserProperty", bundle)

        } else if (from == "UserParking") {
            binding.constraintLayout2.visibility = View.GONE
            val bundle = Bundle()
            bundle.putString("key", "user_parking")
            replaceFrag(fragOwnerParking, "fragUserParking", bundle)

        } else if (from == "UserMyList") {
            /*binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.new_user_home_unactive))
            binding.ivMylist.setImageDrawable(resources.getDrawable(R.drawable.new_user_mylist_active))
            binding.ivActivity.setImageDrawable(resources.getDrawable(R.drawable.new_user_activity_unactive))
            binding.ivService.setImageDrawable(resources.getDrawable(R.drawable.new_user_service_unactive))
            binding.ivHomeprofile.setImageDrawable(resources.getDrawable(R.drawable.new_user_profile_unactive))*/
            binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.without_name_home_unactive))
            binding.ivMylist.setImageDrawable(resources.getDrawable(R.drawable.without_name_list_active))
            binding.ivActivity.setImageDrawable(resources.getDrawable(R.drawable.without_name_activity_unactive))
            binding.ivService.setImageDrawable(resources.getDrawable(R.drawable.without_name_service_unactive))
            binding.ivHomeprofile.setImageDrawable(resources.getDrawable(R.drawable.without_name_profile_unactive))
            binding.textView253.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvListName.setTextColor(ContextCompat.getColor(this, R.color.orange))
            binding.tvActivityName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvServiceName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvProfileName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            val bundle = Bundle()
            bundle.putString("key", "user_mylist")
            replaceFrag(fragListingProperty, "fragListingProperty", bundle)
        } else {
            /* binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.home_unactive))
             binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.property_unactive))
             binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.service_unactive))
             binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.parking_unactive))
             binding.ivMyActivity.setImageDrawable(resources.getDrawable(R.drawable.my_activity_active))*/
            /*  binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.new_user_home_unactive))
              binding.ivMylist.setImageDrawable(resources.getDrawable(R.drawable.new_user_mylist_unactive))
              binding.ivActivity.setImageDrawable(resources.getDrawable(R.drawable.new_user_activity_unactive))
              binding.ivService.setImageDrawable(resources.getDrawable(R.drawable.new_user_service_unactive))
              binding.ivHomeprofile.setImageDrawable(resources.getDrawable(R.drawable.new_user_profile_active))*/
            binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.without_name_home_unactive))
            binding.ivMylist.setImageDrawable(resources.getDrawable(R.drawable.without_name_list_unactive))
            binding.ivActivity.setImageDrawable(resources.getDrawable(R.drawable.without_name_activity_unactive))
            binding.ivService.setImageDrawable(resources.getDrawable(R.drawable.without_name_service_unactive))
            binding.ivHomeprofile.setImageDrawable(resources.getDrawable(R.drawable.without_name_profile_active))
            binding.textView253.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvListName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvActivityName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvServiceName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvProfileName.setTextColor(ContextCompat.getColor(this, R.color.orange))
            val bundle = Bundle()
            bundle.putString("key", "user_profile")
            //replaceFrag(fragOwnerMyActivity, "fragUserMyActivity", bundle)
            replaceFrag(fragUserProfile, "fragUserProfile", bundle)
        }

    }

    private fun replaceFrag(frag: Fragment, nameTag: String, bundle: Bundle?) {
        if (bundle != null) frag.setArguments(bundle) else frag.setArguments(null)
        supportFragmentManager.beginTransaction().replace(R.id.owner_Layout, frag, nameTag)
            .addToBackStack(nameTag).commit()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.finishAffinity()
            return
        }
        if (supportFragmentManager.findFragmentByTag("fragUserHome") != null) {
            onSelectView("UserHome")
        }
        Toast.makeText(this, getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = true
        }, 1000)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (gestureDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun onDown(p0: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent) {
        return
    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent) {
        return
    }

    override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        println("----main")
        try {
            val diffY = p1.y - p0.y
            val diffX = p1.x - p0.x
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > swipeThreshold && Math.abs(p2) > swipeVelocityThreshold) {
                    if (diffX > 0) {
                        binding.ownerDrw.openDrawer(GravityCompat.START)
                    } else {
                        binding.ownerDrw.closeDrawer(GravityCompat.START)
                    }
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return true
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

    fun closeDrawer() {
        if (binding.ownerDrw.isDrawerOpen(GravityCompat.START)) {
            binding.ownerDrw.closeDrawer(GravityCompat.START)
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