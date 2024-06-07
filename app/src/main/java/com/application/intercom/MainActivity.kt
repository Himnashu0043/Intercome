package com.application.intercom

import android.Manifest
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.getUserDetailsViewModel.GetUserDetailsViewModel
import com.application.intercom.data.model.factory.getUserDetailsFactory.GetUserDetailsFactory
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.getUserDetailsRepo.GetUserDetailsRepo
import com.application.intercom.databinding.ActivityMainBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.user.chat.UserChatFragment
import com.application.intercom.user.community.CommunityActivity
import com.application.intercom.user.contact.ContactIntercomActivity
import com.application.intercom.user.home.HomeFragment
import com.application.intercom.user.login.LoginUsingOtpActivity
import com.application.intercom.user.newflow.NewSubUserActivity
import com.application.intercom.user.newflow.UserUpgradActivity
import com.application.intercom.user.newflow.fav.UserFavActivity
import com.application.intercom.user.newflow.newFragment.ListingPropertyFragment
import com.application.intercom.user.parking.ParkingFragment
import com.application.intercom.user.profile.UserProfileFragment
import com.application.intercom.user.property.PropertiesFragment
import com.application.intercom.user.service.UserServiceFragment
import com.application.intercom.utils.*


class MainActivity : AppCompatActivity(), View.OnClickListener, GestureDetector.OnGestureListener,
    ProfileAdapter.ProfileClick {
    private lateinit var binding: ActivityMainBinding
        var doubleBackToExitPressedOnce = false
    val fragUserHome = HomeFragment()
    val fragUserProperty = PropertiesFragment()
    val fragUserService = UserServiceFragment()
    val fragUserParking = ParkingFragment()
    val fragListingProperty = ListingPropertyFragment()

    //     val fragUserMyActivity = UserMyActivityFragment()
    val fragUserMyActivity = UserChatFragment()

    val fragUserProfile = UserProfileFragment()
    private var from1: String = ""
    private var key: String = ""
    private var availableContacts: Int = 0
    private var totalContacts: Int = 0
    private var duration: Int? = 0
    private lateinit var gestureDetector: GestureDetector
    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null
    private val PERMISSION_LOCATION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val RESULT_PERMISSION_LOCATION = 1
    var selectBedAny: String? = null
    var selectBathAny: String? = null
    var startPrice: Int? = null
    var endPrice: Int? = null
    var startSqr: String? = null
    var endSqr: String? = null
    private var get_lati: Double = 0.0
    private var get_longi: Double = 0.0
    private var rent_sell: String? = null
    private var propertyType: String? = null
    private lateinit var getUserDetailsViewModel: GetUserDetailsViewModel

    companion object {
        fun getIntent(context: Context?): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gestureDetector = GestureDetector(this)
//        availableContacts = prefs.getInt(SessionConstants.AVAILABLE_CONTACTS).toInt()
//        totalContacts = prefs.getInt(SessionConstants.TOTALS_CONTACTS).toInt()
//
//        println("---availableContacts$availableContacts")
//        println("---totalContacts${prefs.getInt(SessionConstants.TOTALS_CONTACTS)}")
        initView()
        lstnr()
        initCtrl()

        CommonUtil.themeSet(this, window)
        key = intent.getStringExtra("key").toString()
        from1 = intent.getStringExtra("from").toString()
        rent_sell = intent.getStringExtra("rent").toString()
        propertyType = intent.getStringExtra("propertyType").toString()
        selectBedAny = intent.getStringExtra("bed").toString()
        selectBathAny = intent.getStringExtra("bath").toString()
        startPrice = intent.getIntExtra("startPrice", 0)
        endPrice = intent.getIntExtra("endPrice", 0)
        startSqr = intent.getStringExtra("startSqr").toString()
        endSqr = intent.getStringExtra("endSqr").toString()
        get_lati = intent.getDoubleExtra("getLat", 0.0)
        get_longi = intent.getDoubleExtra("getLong", 0.0)
        println("----from$from1")
        println("----get_lati$get_lati")
        println("----get_longi$get_longi")
        if (intent != null && from1.equals("from_property_details")) {
            onSelectView("UserMyActivity")
        } else if (intent != null && from1.equals("from_side_property")) {
            onSelectView("UserProperty")
        } else if (intent != null && from1.equals("from_filter")) {
            onSelectView("UserProperty")
        } else if (intent != null && from1.equals("from_side_parking")) {
            onSelectView("UserParking")
        } else if (intent != null && from1.equals("from_side_service")) {
            onSelectView("UserService")
        } else if (intent != null && from1.equals("from_side_home")) {
            onSelectView("UserHome")
        } else if (intent != null && from1.equals("from_myList")) {
            onSelectView("UserMyList")
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
        // checkLocationPermissions()
//        onSelectView("UserHome")
//        val fragment = ParkingFragment()
//        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.content, fragment, "")
//        fragmentTransaction.commit()
    }

    private fun initView() {
        initialize()
        obs()

        profile_list.add(ProfileModal(R.drawable.home_icon, getString(R.string.home)))
        profile_list.add(ProfileModal(R.drawable.property_icon, getString(R.string.property)))
        profile_list.add(ProfileModal(R.drawable.parking_icon, getString(R.string.parking)))
        profile_list.add(ProfileModal(R.drawable.fav_icon, getString(R.string.favorites)))
        /*profile_list.add(ProfileModal(R.drawable.visitor_icon, "MyActivity"))
        profile_list.add(ProfileModal(R.drawable.community_icon, "My Community"))
        profile_list.add(ProfileModal(R.drawable.billing_icon, "My Billings"))
        profile_list.add(ProfileModal(R.drawable.visitor_icon, "Visitors"))
        profile_list.add(ProfileModal(R.drawable.notics_icon, "Notice Board"))*/
        profile_list.add(ProfileModal(R.drawable.help_icon, getString(R.string.help_and_support)))
        profile_list.add(ProfileModal(R.drawable.setting_icon, getString(R.string.settings)))
        profile_list.add(ProfileModal(R.drawable.share_new_icon, getString(R.string.share)))
        profile_list.add(ProfileModal(R.drawable.help_icon, getString(R.string.about)))
        profile_list.add(ProfileModal(R.drawable.privacy_icon, getString(R.string.privacy_policy)))
        profile_list.add(ProfileModal(R.drawable.term_icon, getString(R.string.terms_amp_conditions)))
        binding.nav.rcyProfile.layoutManager = LinearLayoutManager(this)
        profileAdapter = ProfileAdapter(this, profile_list, "user", this)
        binding.nav.rcyProfile.adapter = profileAdapter
        profileAdapter!!.notifyDataSetChanged()
//        binding.userHomeToolbar.homeSpiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View,
//                position: Int,
//                id: Long,
//            ) {
////                if (binding.userHomeToolbar.homeSpiner.selectedItemPosition > 0) {
////                    binding.mainNew.visibility = View.VISIBLE
////                } else {
////                    binding.mainNew.visibility = View.GONE
////                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
    }

    private fun initialize() {
        val getUserDetailsrepo = GetUserDetailsRepo(BaseApplication.apiService)
        getUserDetailsViewModel = ViewModelProvider(
            this, GetUserDetailsFactory(getUserDetailsrepo)
        )[GetUserDetailsViewModel::class.java]

    }

    private fun getUserDetails() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        getUserDetailsViewModel.userDetails(token)

    }

    private fun obs() {
        getUserDetailsViewModel.userDetailsLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            if (it.data.userDetails.fullName.isNullOrEmpty()) {
                                availableContacts = it.data.userDetails.availableContacts
                                totalContacts = it.data.userDetails.totalContacts
                                prefs.put(
                                    SessionConstants.AVAILABLE_CONTACTS,
                                    it.data.userDetails.availableContacts
                                )
                                prefs.put(
                                    SessionConstants.TOTALS_CONTACTS,
                                    it.data.userDetails.totalContacts
                                )
                                println("---MianavailableContacts$availableContacts")
                                println("---MianavailableContactstotalContacts${totalContacts}")
                            } else {
                                availableContacts = it.data.userDetails.availableContacts
                                totalContacts = it.data.userDetails.totalContacts
                                prefs.put(
                                    SessionConstants.AVAILABLE_CONTACTS,
                                    it.data.userDetails.availableContacts
                                )
                                prefs.put(
                                    SessionConstants.TOTALS_CONTACTS,
                                    it.data.userDetails.totalContacts
                                )
                                prefs.put(SessionConstants.DURATION, it.data.userDetails.duration)
                                duration = it.data.userDetails.duration
                                println("---MianavailableContacts$availableContacts")
                                println("---MianavailableContactstotalContacts${totalContacts}")

//                                prefs.put(
//                                    SessionConstants.AVAILABLE_CONTACTS,
//                                    it.data.userDetails.availableContacts
//                                )
//                                prefs.put(
//                                    SessionConstants.TOTALS_CONTACTS,
//                                    it.data.userDetails.totalContacts
//                                )
//                                println("---AVAILABLE_CONTACTS${it.data.userDetails.availableContacts}")
                                /* binding.userHomeToolbar.tvHomeDes.text = prefs.getString(
                                     SessionConstants.KADDRESS,
                                     GPSService.mLastLocation?.latitude.toString()
                                 )*/
                            }
                            if (availableContacts > 0) {
                                binding.nav.cardView19.visibility = View.INVISIBLE
                                binding.nav.tvcount.visibility = View.VISIBLE
                                binding.nav.tvcount1.visibility = View.VISIBLE
                                binding.nav.tvdayLeft.visibility = View.VISIBLE
                                binding.nav.tvcount.text = "${availableContacts}/"
                                binding.nav.tvcount1.text = totalContacts.toString()

                                if (duration != null) {
                                    binding.nav.tvdayLeft.text =
                                        "${it.data.userDetails.duration} ${getString(R.string.day_left)}"
                                    println("--duration$duration")
                                }


                            } else {
                                binding.nav.cardView19.visibility = View.VISIBLE
                                binding.nav.tvcount.visibility = View.INVISIBLE
                                binding.nav.tvcount1.visibility = View.INVISIBLE
                                binding.nav.tvdayLeft.visibility = View.INVISIBLE

                            }

                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_503) {
                            val intent = Intent(
                                this,
                                LoginUsingOtpActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
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

    private fun initCtrl() {
        binding.ivHome.setOnClickListener(this)
        binding.ivMylist.setOnClickListener(this)
        binding.ivActivity.setOnClickListener(this)
        binding.ivService.setOnClickListener(this)
        binding.ivHomeprofile.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.iv_home -> {
                onSelectView("UserHome")
            }

            R.id.iv_mylist -> {
                onSelectView("UserMyList")
            }

            R.id.iv_activity -> {
                onSelectView("UserActivity")
            }

            R.id.iv_service -> {
                onSelectView("UserService")

            }

            R.id.iv_homeprofile -> {
                onSelectView("UserProfile")
            }
        }
    }

    private fun onSelectView(from: String) {
        if (from == "UserHome") {
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
            replaceFrag(fragUserHome, "fragUserHome", null)

        } else if (from == "UserMyList") {
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

        } else if (from == "UserActivity") {
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
            bundle.putString("key", "user_activity")
            replaceFrag(fragUserMyActivity, "fragUserParking", bundle)

        } else if (from == "UserService") {
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
            /* if (from.equals("home_service")) {
                 val bundle = Bundle()
                 bundle.putString("key", "home_service")
                 replaceFrag(fragUserService, "fragUserService", bundle)
             }*/ /*else {
                val bundle = Bundle()
                bundle.putString("key", "user")
                replaceFrag(fragUserService, "fragUserService", bundle)
            }*/
            val bundle = Bundle()
            bundle.putString("key", "home_service")
            replaceFrag(fragUserService, "fragUserService", bundle)

        } else if (from == "UserProperty") {
            if (from1.equals("from_filter")) {
                binding.constraintLayout2.visibility = View.GONE
                val bundle = Bundle()
                bundle.putString("key", "filter")
                bundle.putString("rent", rent_sell)
                bundle.putString("propertyType", propertyType)
                bundle.putString("bed", selectBedAny)
                bundle.putString("bath", selectBathAny)
                bundle.putInt("startPrice", startPrice!!)
                bundle.putInt("endPrice", endPrice!!)
                bundle.putString("startSqr", startSqr)
                bundle.putString("endSqr", endSqr)
                bundle.putDouble("getLat", get_lati)
                bundle.putDouble("getLong", get_longi)
                replaceFrag(fragUserProperty, "fragUserProperty", bundle)
            } else {
                binding.constraintLayout2.visibility = View.GONE
                val bundle = Bundle()
                bundle.putString("key", "user_property")
                replaceFrag(fragUserProperty, "fragUserProperty", bundle)
            }


        } else if (from == "UserParking") {
            binding.constraintLayout2.visibility = View.GONE
            val bundle = Bundle()
            bundle.putString("key", "user_parking")
            replaceFrag(fragUserParking, "fragUserParking", bundle)

        } else {
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
            replaceFrag(fragUserProfile, "fragUserProfile", bundle)
        }

    }

    private fun replaceFrag(frag: Fragment, nameTag: String, bundle: Bundle?) {
        if (bundle != null) frag.setArguments(bundle) else frag.setArguments(null)
        supportFragmentManager.beginTransaction().replace(R.id.contentLayoyt, frag, nameTag)
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
                        /* startActivity(
                             Intent(this, ProfileActivity::class.java).putExtra(
                                 "from", "user"
                             )
                         )*/
                        binding.content.openDrawer(GravityCompat.START)
                    } else {
                        binding.content.closeDrawer(GravityCompat.START)
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
                    Intent(this, MainActivity::class.java).putExtra(
                        "from",
                        "from_side_home"
                    )
                )
                binding.content.closeDrawer(GravityCompat.START)
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

    fun closeDrawer() {
        if (binding.content.isDrawerOpen(GravityCompat.START)) {
            binding.content.closeDrawer(GravityCompat.START)
        }
    }

    override fun onResume() {
        super.onResume()
        closeDrawer()
        getUserDetails()
        /* if (hasAccessFineLocationPermissions(this)) {
             if (checkGPS(this)) {
                 GPSService(this, this)

             }
         }*/
    }
////location
    /* private fun checkLocationPermissions() {
         if (hasAccessFineLocationPermissions(this)) {
             if (checkGPS(this)) {
                 Log.d(ContentValues.TAG, "GPS: ENABLED.....")
                 GPSService(this, this)
                 // callScreen()
             } else {
                 buildAlertMessageNoGps(getString(R.string.enable_gps))
             }
         } else {
             startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
             // requestLocationPermissions(requireActivity())
         }
     }
     fun hasAccessFineLocationPermissions(context: Context?): Boolean {
         return (ContextCompat.checkSelfPermission(
             context!!,
             Manifest.permission.ACCESS_FINE_LOCATION
         ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
             context,
             Manifest.permission.ACCESS_COARSE_LOCATION
         ) == PackageManager.PERMISSION_GRANTED)
     }
     fun requestLocationPermissions(activity: Activity?) {
         ActivityCompat.requestPermissions(
             activity!!,
             PERMISSION_LOCATION,
             RESULT_PERMISSION_LOCATION
         )
     }
     private fun buildAlertMessageNoGps(message: String) {
         val builder = AlertDialog.Builder(this)
         builder.setMessage(message)
             .setCancelable(false)
             .setPositiveButton(
                 "Yes"
             ) { dialog, id ->
                 startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
             }
         val alert = builder.create()
         alert.getWindow()?.setBackgroundDrawable(resources?.let {
             ColorDrawable(
                 it.getColor(
                     R.color.white
                 )
             )
         })
         alert.show()
     }
     override fun onRequestPermissionsResult(
         requestCode: Int,
         permissions: Array<String?>,
         grantResults: IntArray
     ) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults)
         *//* for (permission in permissions) {
             if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission!!)) {
                 //denied
                 Log.e("denied", permission)
                 showSettingLocation(this)

             } else {
                 if (ActivityCompat.checkSelfPermission(
                         this,
                         permission) == PackageManager.PERMISSION_GRANTED
                 ) {
                     //allowed
                     Log.e("allowed", permission)
                     getLocation()
                     callScreen()
                 }
                 else {
 //                    callScreen()
                     //set to never ask again
                     Log.e("set to never ask again", permission)
                     //startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                     Toast.makeText(this@SplashActivity, getString(R.string.grant_location_permission_to_use_app), Toast.LENGTH_SHORT).show()
                     // User selected the Never Ask Again Option
                     val intent = Intent()
                     intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                     val uri = Uri.fromParts("package", packageName, null)
                     intent.data = uri
                     startActivityForResult(intent, 0)
                 }
             }
         }*//*
        if (requestCode == RESULT_PERMISSION_LOCATION) {
            if (hasAccessFineLocationPermissions(this)) {
                if (checkGPS(this)) {
                    GPSService(this, this)
                    //callScreen()
                } else {
                    buildAlertMessageNoGps(getString(R.string.enable_gps))
                }
            } else {
                requestLocationPermissions(this)
            }
        }
    }

    override fun onLocationUpdate(latitude: Double, longitude: Double) {

    }*/

////location
//    override fun openServices() {
//        binding.ivHome.setImageDrawable(resources.getDrawable(R.drawable.home_unactive))
//        binding.ivHomeproperty.setImageDrawable(resources.getDrawable(R.drawable.property_unactive))
//        binding.ivHomeservice.setImageDrawable(resources.getDrawable(R.drawable.service_active))
//        binding.ivParking.setImageDrawable(resources.getDrawable(R.drawable.parking_unactive))
//        binding.ivMyActivity.setImageDrawable(resources.getDrawable(R.drawable.myactivity_unactive))
//        val bundle = Bundle()
//        bundle.putString("key","user")
//
//        replaceFrag(fragUserService, "fragUserService", bundle)
//
//    }

    fun shareIntent(activity: Activity?, shearableLink: String?) {
        ShareCompat.IntentBuilder.from(activity!!).setText(shearableLink).setType("text/plain")
            .setChooserTitle("Share with the users").startChooser()
    }
}