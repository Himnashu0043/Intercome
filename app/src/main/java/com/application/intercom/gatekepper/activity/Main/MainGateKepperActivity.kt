package  com.application.intercom.gatekepper.Main

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
import com.application.intercom.baseActivity.prefs
import com.application.intercom.databinding.ActivityMainGateKepperBinding
import com.application.intercom.gatekepper.Fragment.Home.GateKeeperHomeFragment
import com.application.intercom.gatekepper.Fragment.Home.NewGatePassListFragment
import com.application.intercom.gatekepper.Fragment.newFragment.newVisitor.NewVisitorGateKeeperFragment
import com.application.intercom.gatekepper.activity.newFlow.myShift.MyShiftGateKeeperActivity
import com.application.intercom.gatekepper.activity.newFlow.regularEntry.RegularEntryActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntry.SingleEntryActivity
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
import com.application.intercom.user.profile.UserProfileFragment
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.SessionConstants

class MainGateKepperActivity : AppCompatActivity(), View.OnClickListener,
    GestureDetector.OnGestureListener, ProfileAdapter.ProfileClick {
    lateinit var binding: ActivityMainGateKepperBinding
    var doubleBackToExitPressedOnce = false
    val fragGateHome = GateKeeperHomeFragment()

    //    val fragGateVisitor = GateAddVisitorFragment()
    val fragGateVisitor = NewVisitorGateKeeperFragment()

    //    val fragGateGatePass = GateKeeperGatePassFragment()
    val fragGateGatePass = NewGatePassListFragment()
    val fragProfilePass = UserProfileFragment()
    private var from: String = ""
    private lateinit var gestureDetector: GestureDetector
    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null

    companion object {
        fun getIntent(context: Context?): Intent {
            return Intent(context, MainGateKepperActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainGateKepperBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        gestureDetector = GestureDetector(this)
        binding.nav.constraintLayout10.visibility = View.GONE
        initView()
        lstnr()
        initCtrl()
        from = intent.getStringExtra("from").toString()
        if (intent != null && from.equals("from_gate_home")) {
            onSelectView("GatePassVisitor")
        } else if (intent != null && from.equals("from_gate_create_pass")) {
            onSelectView("GatePass")
        } else if (intent != null && from.equals("from_side_home")) {
            onSelectView("GatePassHome")
        } else {
            onSelectView("GatePassHome")
        }
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

    }

    private fun initView() {
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
        profileAdapter = ProfileAdapter(this, profile_list, "tenant", this)
        binding.nav.rcyProfile.adapter = profileAdapter
        profileAdapter!!.notifyDataSetChanged()
    }

    private fun initCtrl() {
        /* binding.ivGateKeeperHome.setOnClickListener(this)
         binding.ivGateKeeperVisitor.setOnClickListener(this)
         binding.ivGateKeeperGatePass.setOnClickListener(this)
         binding.ivGateKeeperProfile.setOnClickListener(this)*/
        binding.constraintLayout271.setOnClickListener(this)
        binding.constraintLayout281.setOnClickListener(this)
        binding.constraintLayout291.setOnClickListener(this)
        binding.constraintLayout301.setOnClickListener(this)

    }

    private fun lstnr() {
        binding.nav.tvEnglish.setOnClickListener {
            binding.nav.tvBl.visibility = View.VISIBLE
            binding.nav.tvEnglish.visibility = View.INVISIBLE
            prefs.put(SessionConstants.LANG, Language.BANGLA.languageCode)
            println("======test${Language.BANGLA.languageCode}")
            setLocale(Language.BANGLA.languageCode)
            finishAffinity()
            startActivity(Intent(this, MainGateKepperActivity::class.java))
        }
        binding.nav.tvBl.setOnClickListener {
            binding.nav.tvBl.visibility = View.INVISIBLE
            binding.nav.tvEnglish.visibility = View.VISIBLE
            prefs.put(SessionConstants.LANG, Language.ENGLISH.languageCode)
            println("======test${Language.ENGLISH.languageCode}")
            setLocale(Language.ENGLISH.languageCode)
            finishAffinity()
            startActivity(Intent(this, MainGateKepperActivity::class.java))
        }
    }

    private fun onSelectView(from: String) {
        if (from == "GatePassHome") {
            /* binding.ivGateKeeperHome.setImageDrawable(resources.getDrawable(R.drawable.home_active))
             binding.ivGateKeeperVisitor.setImageDrawable(resources.getDrawable(R.drawable.visitor_unactive))
             binding.ivGateKeeperGatePass.setImageDrawable(resources.getDrawable(R.drawable.gatepass_unactive))
             binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.new_gatekeeper_un_profile))*/
            binding.ivGateKeeperHome.setImageDrawable(resources.getDrawable(R.drawable.without_name_home_active))
            binding.ivGateKeeperVisitor.setImageDrawable(resources.getDrawable(R.drawable.without_name_visitor_unactive))
            binding.ivGateKeeperGatePass.setImageDrawable(resources.getDrawable(R.drawable.without_name_gatepass_unactive))
            binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.without_name_profile_unactive))
            binding.textView253.setTextColor(ContextCompat.getColor(this, R.color.orange))
            binding.tvVisitorName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvGatePassName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvProfileName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            replaceFrag(fragGateHome, "fragHome", null)

        } else if (from == "GatePassVisitor") {
            /* binding.ivGateKeeperHome.setImageDrawable(resources.getDrawable(R.drawable.home_unactive))
             binding.ivGateKeeperVisitor.setImageDrawable(resources.getDrawable(R.drawable.visitor_active))
             binding.ivGateKeeperGatePass.setImageDrawable(resources.getDrawable(R.drawable.gatepass_unactive))
             binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.new_gatekeeper_un_profile))*/
            binding.ivGateKeeperHome.setImageDrawable(resources.getDrawable(R.drawable.without_name_home_unactive))
            binding.ivGateKeeperVisitor.setImageDrawable(resources.getDrawable(R.drawable.without_name_visitor_active))
            binding.ivGateKeeperGatePass.setImageDrawable(resources.getDrawable(R.drawable.without_name_gatepass_unactive))
            binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.without_name_profile_unactive))
            binding.textView253.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvVisitorName.setTextColor(ContextCompat.getColor(this, R.color.orange))
            binding.tvGatePassName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvProfileName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            replaceFrag(fragGateVisitor, "fragVisitor", null)

        } else if (from == "GatePass") {
            /* binding.ivGateKeeperHome.setImageDrawable(resources.getDrawable(R.drawable.home_unactive))
             binding.ivGateKeeperVisitor.setImageDrawable(resources.getDrawable(R.drawable.visitor_unactive))
             binding.ivGateKeeperGatePass.setImageDrawable(resources.getDrawable(R.drawable.gatepass_active))
             binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.new_gatekeeper_un_profile))*/
            binding.ivGateKeeperHome.setImageDrawable(resources.getDrawable(R.drawable.without_name_home_unactive))
            binding.ivGateKeeperVisitor.setImageDrawable(resources.getDrawable(R.drawable.without_name_visitor_unactive))
            binding.ivGateKeeperGatePass.setImageDrawable(resources.getDrawable(R.drawable.without_name_gatepass_active))
            binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.without_name_profile_unactive))
            binding.textView253.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvVisitorName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvGatePassName.setTextColor(ContextCompat.getColor(this, R.color.orange))
            binding.tvProfileName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            replaceFrag(fragGateGatePass, "fragGatePass", null)

        } else {
            /* binding.ivGateKeeperHome.setImageDrawable(resources.getDrawable(R.drawable.home_unactive))
             binding.ivGateKeeperVisitor.setImageDrawable(resources.getDrawable(R.drawable.visitor_unactive))
             binding.ivGateKeeperGatePass.setImageDrawable(resources.getDrawable(R.drawable.gatepass_unactive))
             binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.new_gatekeeper_active_profile))*/
            binding.ivGateKeeperHome.setImageDrawable(resources.getDrawable(R.drawable.without_name_home_unactive))
            binding.ivGateKeeperVisitor.setImageDrawable(resources.getDrawable(R.drawable.without_name_visitor_unactive))
            binding.ivGateKeeperGatePass.setImageDrawable(resources.getDrawable(R.drawable.without_name_gatepass_unactive))
            binding.ivGateKeeperProfile.setImageDrawable(resources.getDrawable(R.drawable.without_name_profile_active))
            binding.textView253.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvVisitorName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvGatePassName.setTextColor(ContextCompat.getColor(this, R.color.dark_c))
            binding.tvProfileName.setTextColor(ContextCompat.getColor(this, R.color.orange))
            replaceFrag(fragProfilePass, "fragProfile", null)

        }

    }

    private fun replaceFrag(frag: Fragment, nameTag: String, bundle: Bundle?) {
        if (bundle != null) frag.setArguments(bundle) else frag.setArguments(null)
        supportFragmentManager.beginTransaction().replace(R.id.gateKeeper_layout, frag, nameTag)
            .addToBackStack(nameTag).commit()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.constraintLayout271 -> {
                onSelectView("GatePassHome")
            }
            R.id.constraintLayout281 -> {
                onSelectView("GatePassVisitor")
            }
            R.id.constraintLayout291 -> {
                onSelectView("GatePass")

            }
            R.id.constraintLayout301 -> {
                onSelectView("GatePassProfile")

            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.finishAffinity()
            return
        }
        if (supportFragmentManager.findFragmentByTag("fragHome") != null) {
            onSelectView("GatePassHome")
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
//                                "from", "newGateKeeper"
//                            )
//                        )
                        binding.newGateKeepeerDrw.openDrawer(GravityCompat.START)
                    } else {
                        binding.newGateKeepeerDrw.closeDrawer(GravityCompat.START)
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

    fun closeDrawer() {
        if (binding.newGateKeepeerDrw.isDrawerOpen(GravityCompat.START)) {
            binding.newGateKeepeerDrw.closeDrawer(GravityCompat.START)
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