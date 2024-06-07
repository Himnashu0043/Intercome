package com.application.intercom.manager.main

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.prefs
import com.application.intercom.databinding.ActivityManagerMainBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.manager.complaint.RegisterComplaintsActivity
import com.application.intercom.manager.gatekeeper.GateKeeperListingActivity
import com.application.intercom.manager.home.ManagerHomeFragment
import com.application.intercom.manager.newFlow.finance.ManagerFinanceActivity
import com.application.intercom.manager.notice.NoticeBoardActivity
import com.application.intercom.manager.parking.ParentManagerParkingFragment
import com.application.intercom.manager.property.ParentManagerPropertyFragment
import com.application.intercom.manager.visitorAndGatePass.ManagerVisitorGatePassActivity
import com.application.intercom.owner.activity.helpSupport.OwnerHelpSupportActivity
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.user.profile.UserProfileFragment
import com.application.intercom.user.service.UserServiceFragment
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.SessionConstants

class ManagerMainActivity : BaseActivity<ActivityManagerMainBinding>(), View.OnClickListener,
    GestureDetector.OnGestureListener, ProfileAdapter.ProfileClick {     
    var doubleBackToExitPressedOnce = false
    val fragManagerHome = ManagerHomeFragment()
    val fragManagerProperty = ParentManagerPropertyFragment()
    val fragManagerService = UserServiceFragment()
    val fragManagerParking = ParentManagerParkingFragment()
    val fragUserProfile = UserProfileFragment()
    private var from: String = ""
    private lateinit var gestureDetector: GestureDetector
    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null

    companion object {
        fun getIntent(context: Context?): Intent {
            return Intent(context, ManagerMainActivity::class.java)
        }
    }

    override fun getLayout(): ActivityManagerMainBinding {
        return ActivityManagerMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gestureDetector = GestureDetector(this)
        CommonUtil.themeSet(this, window)
        initView()
        lstnr()
        initCtrl()
        binding.nav.constraintLayout10.visibility = View.GONE
        from = intent.getStringExtra("from").toString()
        if (intent != null && from.equals("from_side_property")) {
            onSelectView("UserProperty")
        } else if (intent != null && from.equals("from_side_parking")) {
            onSelectView("UserParking")
        } else if (intent != null && from.equals("from_side_service")) {
            onSelectView("UserService")
        } else if (intent != null && from.equals("from_side_home")) {
            onSelectView("UserHome")
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

    }

    private fun initView() {
        profile_list.add(ProfileModal(R.drawable.home_icon, getString(R.string.home)))
//        profile_list.add(ProfileModal(R.drawable.property_icon, "Property"))
//        profile_list.add(ProfileModal(R.drawable.parking_icon, "Parking"))
//        profile_list.add(ProfileModal(R.drawable.service_icon, "Services"))
        profile_list.add(ProfileModal(R.drawable.community_icon, getString(R.string.complain)))
        profile_list.add(ProfileModal(
            R.drawable.billing_icon, "Billing and Account"
        ))
//        profile_list.add(
//            ProfileModal(
//                R.drawable.billing_icon, "Billing and Account"
//            )
//        )
        profile_list.add(
            ProfileModal(
                R.drawable.visitor_gatepass_icon, getString(R.string.visitors_gatepass)
            )
        )
        profile_list.add(ProfileModal(R.drawable.community_icon, getString(R.string.gatekeepers)))
        //profile_list.add(ProfileModal(R.drawable.service_icon, getString(R.string.service_charge)))
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

    private fun lstnr() {
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
    }

    private fun initCtrl() {
        binding.constraintLayout27.setOnClickListener(this)
        binding.constraintLayout28.setOnClickListener(this)
        binding.constraintLayout29.setOnClickListener(this)
        binding.constraintLayout30.setOnClickListener(this)
        binding.constraintLayout31.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.constraintLayout27 -> {
                onSelectView("UserHome")
            }
            R.id.constraintLayout28 -> {
                onSelectView("UserProperty")
            }
            R.id.constraintLayout29 -> {
                onSelectView("UserService")
            }
            R.id.constraintLayout30 -> {
                onSelectView("UserParking")
            }
            R.id.constraintLayout31 -> {
                onSelectView("UserProfile")
            }

        }
    }

    private fun onSelectView(from: String) {
        if (from == "UserHome") {
//            binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.home_active))
//            binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.property_unactive))
//            binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.service_unactive))
//            binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.parking_unactive))
//            binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.new_user_profile_unactive))
            binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.without_name_home_active))
            binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.without_name_property_unactive))
            binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.without_name_service_unactive))
            binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.without_name_parking_unactive))
            binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.without_name_profile_unactive))
            binding.textView253.setTextColor(ContextCompat.getColor(this, R.color.orange))
            binding.tvPropertyName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvServiceName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvParkingName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvProfileName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            replaceFrag(fragManagerHome, "fragManagerHome", null)

        } else if (from == "UserProperty") {
            /*binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.home_unactive))
            binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.property_active))
            binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.service_unactive))
            binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.parking_unactive))
            binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.new_user_profile_unactive))*/
            binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.without_name_home_unactive))
            binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.without_name_property_active))
            binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.without_name_service_unactive))
            binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.without_name_parking_unactive))
            binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.without_name_profile_unactive))
            binding.textView253.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvPropertyName.setTextColor(ContextCompat.getColor(this, R.color.orange))
            binding.tvServiceName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvParkingName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvProfileName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            val bundle = Bundle()
            bundle.putString("key", "manager_Property")
            replaceFrag(fragManagerProperty, "fragManagerProperty", bundle)

        } else if (from == "UserParking") {
            /* binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.home_unactive))
             binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.property_unactive))
             binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.service_unactive))
             binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.parking_active))
             binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.new_user_profile_unactive))*/
            binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.without_name_home_unactive))
            binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.without_name_property_unactive))
            binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.without_name_service_unactive))
            binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.without_name_parking_active))
            binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.without_name_profile_unactive))
            binding.textView253.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvPropertyName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvServiceName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvParkingName.setTextColor(ContextCompat.getColor(this, R.color.orange))
            binding.tvProfileName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            val bundle = Bundle()
            bundle.putString("key", "manager_parking")
            replaceFrag(fragManagerParking, "fragManagerParking", bundle)

        } else if (from == "UserService") {
            /* binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.home_unactive))
             binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.property_unactive))
             binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.service_active))
             binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.parking_unactive))
 *//*
            binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.new_gatekeeper_un_profile))
*//*
            binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.new_user_profile_unactive))*/
            binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.without_name_home_unactive))
            binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.without_name_property_unactive))
            binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.without_name_service_active))
            binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.without_name_parking_unactive))
            binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.without_name_profile_unactive))
            binding.textView253.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvPropertyName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvServiceName.setTextColor(ContextCompat.getColor(this, R.color.orange))
            binding.tvParkingName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvProfileName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            val bundle = Bundle()
            bundle.putString("key", "manager_service")
            replaceFrag(fragManagerService, "fragManagerService", bundle)

        } else {
            /*binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.home_unactive))
            binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.property_unactive))
            binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.service_unactive))
            binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.parking_unactive))
            binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.new_user_profile_active))*/
            binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.without_name_home_unactive))
            binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.without_name_property_unactive))
            binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.without_name_service_unactive))
            binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.without_name_parking_unactive))
            binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.without_name_profile_active))
            binding.textView253.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvPropertyName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvServiceName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvParkingName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvProfileName.setTextColor(ContextCompat.getColor(this, R.color.orange))
            replaceFrag(fragUserProfile, "fragUserProfile", null)
            /* binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.new_user_home_unactive))
             binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.new_user_mylist_unactive))
             binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.new_user_activity_unactive))
             binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.new_user_service_unactive))
             binding.ivHomeprofile.setImageDrawable(resources.getDrawable(R.drawable.new_user_profile_active))*/
            /*val bundle = Bundle()
            bundle.putString("key", "user_profile")
            replaceFrag(fragUserProfile, "fragUserProfile", bundle)*/
        }

    }

    private fun replaceFrag(frag: Fragment, nameTag: String, bundle: Bundle?) {
        if (bundle != null) frag.setArguments(bundle) else frag.setArguments(null)
        supportFragmentManager.beginTransaction().replace(R.id.manager_Layout, frag, nameTag)
            .addToBackStack(nameTag).commit()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.finishAffinity()
            return
        }
        if (supportFragmentManager.findFragmentByTag("fragManagerHome") != null) {
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
//                        startActivity(
//                            Intent(this, ProfileActivity::class.java).putExtra(
//                                "from", "manager"
//                            )
//                        )
                        binding.managerDrw.openDrawer(GravityCompat.START)
                    } else {
                        binding.managerDrw.closeDrawer(GravityCompat.START)
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
//                startActivity(
//                    Intent(this, MyBillingsActivity::class.java)
//                )
                startActivity(
                   Intent(this,  ManagerFinanceActivity::class.java)
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
        if (binding.managerDrw.isDrawerOpen(GravityCompat.START)) {
            binding.managerDrw.closeDrawer(GravityCompat.START)
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