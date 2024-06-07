package com.application.intercom

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.application.intercom.baseActivity.prefs
import com.application.intercom.databinding.ActivitySplashBinding
import com.application.intercom.gatekepper.Main.MainGateKepperActivity
import com.application.intercom.gatekepper.activity.newFlow.ownerTenantRegularEntryHistory.OwnerTenantRegularEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.ownerTenantSingleEntryHistory.OwnerTenantSingleEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.regularEntryHistory.RegularEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntryHistory.SingleEntryHistoryActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.manager.bills.ApprovalBillingManagerActivity
import com.application.intercom.manager.complaint.RegisterComplaintsActivity
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.manager.notice.NoticeBoardActivity
import com.application.intercom.manager.visitorAndGatePass.ManagerGatePassActivity
import com.application.intercom.owner.activity.gatepass.OwnerGatePassActivity
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.activity.ownerbilling.OwnerBillingActivity
import com.application.intercom.tenant.activity.MyCommunity.TenantMyCommunityActivity
import com.application.intercom.tenant.activity.billing.TenantBillingsActivity
import com.application.intercom.tenant.activity.chat.ChatDetailsActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticBoardActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticeBoardDetailsActivity
import com.application.intercom.tenant.activity.registerComplain.TenantRegisterComplainActivity
import com.application.intercom.user.login.LoginUsingOtpActivity
import com.application.intercom.utils.AppConstants
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.CommonUtil.checkGPS
import com.application.intercom.utils.CommonUtil.printBundle
import com.application.intercom.utils.PushKeys
import com.application.intercom.utils.SessionConstants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class SplashActivity : AppCompatActivity(), GPSService.OnLocationUpdate {
    private val delayTime: Long = 1000
    private var deviceToken: String? = ""
    private lateinit var binding: ActivitySplashBinding
    val RESULT_PERMISSION_LOCATION = 1
    private val PERMISSION_LOCATION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.printBundle()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            deviceToken = task.result
            Log.d(ContentValues.TAG, "DeviceToken==>>: ${deviceToken} ")
            getSharedPreferences(AppConstants.PREF_NAME, MODE_PRIVATE).edit {
                prefs.put(SessionConstants.DEVICETOKEN, deviceToken!!)
                //putString(AppConstants.DEVICETOKEN, deviceToken)
                apply()
            }
            prefs.put(SessionConstants.DEVICETOKEN, deviceToken!!)
        })
        var lang =
            prefs.getString(SessionConstants.LANG, GPSService.mLastLocation?.latitude.toString())
        println("=====spla$lang")
        if (lang.isEmpty()) {
            lang = Language.ENGLISH.languageCode
            setLocale(lang)
            println("=====spla1$lang")
        }
        setLocale(lang)
        //checkLocationPermissions()
    }


    private fun handler() {
        intent?.extras?.printBundle()
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
            if (prefs.getString(
                    SessionConstants.TOKEN,
                    ""
                ).isNotEmpty() && prefs.getString(
                    SessionConstants.ROLE,
                    ""
                ) == AppConstants.USER
            ) {
                if (intent.extras?.getString("type") == "BUILDING_APPROVE") {
                    startActivity(
                        Intent(this, MainActivity::class.java).putExtra(
                            "from",
                            "from_myList"
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                } else if (intent.extras?.getString("type") == "BUILDING_DENY") {
                    startActivity(
                        Intent(this, MainActivity::class.java).putExtra(
                            "from",
                            "from_myList"
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

            } else if (prefs.getString(
                    SessionConstants.TOKEN,
                    ""
                ).isNotEmpty() && prefs.getString(
                    SessionConstants.ROLE,
                    ""
                ) == AppConstants.OWNER
            ) {
                if (intent.extras?.getString("type") == "NOTICE") {
                    val mIntent = Intent(this, TenantNoticBoardActivity::class.java)
                    mIntent.putExtra("from", "owner")
                    startActivity(mIntent)
                } else if (intent.extras?.getString("type") == "NEW_COMPLAIN_RESOLVE") {
                    val mIntent = Intent(this, TenantRegisterComplainActivity::class.java)
                    startActivity(mIntent)

                } else if (intent.extras?.getString("type") == "BUILDING_APPROVE") {
                    startActivity(
                        Intent(this, OwnerMainActivity::class.java).putExtra(
                            "from",
                            "from_myList"
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                } else if (intent.extras?.getString("type") == "BUILDING_DENY") {
                    startActivity(
                        Intent(this, OwnerMainActivity::class.java).putExtra(
                            "from",
                            "from_myList"
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                } else if (intent.extras?.getString("type") == "REGULAR_CHECKED_IN") {
                    startActivity(
                        Intent(this, OwnerTenantRegularEntryHistoryActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else if (intent.extras?.getString("type") == "REGULAR_CHECKED_OUT") {
                    startActivity(
                        Intent(this, OwnerTenantRegularEntryHistoryActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else if (intent.extras?.getString("type") == "New_Bill_Msg") {
                    startActivity(
                        Intent(this, OwnerBillingActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).putExtra("from", "owner")
                    )
                } else if (intent.extras?.getString("type") == "BILL_APPROVED") {
                    startActivity(
                        Intent(this, OwnerBillingActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).putExtra("from", "owner")
                    )
                } else if (intent.extras?.getString("type") == "NEW_RENT_Bill") {
                    startActivity(
                        Intent(this, OwnerBillingActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).putExtra("from", "owner")
                    )
                } else if (intent.extras?.getString("type") == "New_Chat_Mesg") {
                    startActivity(
                        Intent(this, ChatDetailsActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).putExtra("from", "owner").putExtra("key", "kill_state_noti")
                    )
                } else if (intent.extras?.getString("type") == "CHECKED_OUT") {
                    startActivity(
                        Intent(
                            this,
                            OwnerTenantSingleEntryHistoryActivity::class.java
                        ).putExtra("from", "kill_state").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else if (intent.extras?.getString("type") == "GATEPASS_COMPLETE") {
                    startActivity(
                        Intent(
                            this, OwnerGatePassActivity::class.java
                        ).putExtra("key", "kill_state").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else if (intent.extras?.getString("type") == "LIKED_POST") {
                    startActivity(
                        Intent(
                            this, TenantMyCommunityActivity::class.java
                        ).putExtra("from", "owner").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else if (intent.extras?.getString("type") == "COMMENT_POST") {
                    startActivity(
                        Intent(
                            this, TenantMyCommunityActivity::class.java
                        ).putExtra("from", "owner").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else if (intent.extras?.getString("type") == "POST") {
                    startActivity(
                        Intent(
                            this, TenantMyCommunityActivity::class.java
                        ).putExtra("from", "owner").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else if (intent.extras?.getString("type") == "NEW_BUILDING") {
                    startActivity(
                        Intent(
                            this, OwnerMainActivity::class.java
                        ).putExtra("from", "from_myList").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else if (intent.extras?.getString("type") == "NEW_BILL_PAID") {
                    startActivity(
                        Intent(
                            this, OwnerBillingActivity::class.java
                        ).putExtra("key", "kill_approved").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else if (intent.extras?.getString("type") == "NEW_BILL_NOTIFY") {
                    startActivity(
                        Intent(this, OwnerBillingActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).putExtra("key", "kill_New_All_Notify")
                    )
                } else if (intent.extras?.getString("type") == "BILL_REJECT") {
                    val mIntent = Intent(this, OwnerBillingActivity::class.java).putExtra(
                        "key",
                        "kill_BILL_REJECT"
                    ).putExtra("from", "owner")
                    startActivity(mIntent)
                } else {
                    val mIntent = Intent(this, OwnerMainActivity::class.java)
                    if (intent.extras?.getString("type") == "CHECKED_IN") {
                        mIntent.putExtra(
                            PushKeys.type.name, intent.extras?.getString(PushKeys.type.name)
                        )
                        mIntent.putExtra(
                            PushKeys.visitorName.name,
                            intent.extras?.getString(PushKeys.visitorName.name)
                        )
                        mIntent.putExtra(
                            PushKeys.address.name, intent.extras?.getString(PushKeys.address.name)
                        )
                        mIntent.putExtra(
                            PushKeys.flatName.name, intent.extras?.getString(PushKeys.flatName.name)
                        )
                        mIntent.putExtra(
                            PushKeys.visitCategoryName.name, intent.extras?.getString(PushKeys.visitCategoryName.name)
                        )
                        mIntent.putExtra(
                            PushKeys.mobileNumber.name, intent.extras?.getString(PushKeys.mobileNumber.name)
                        )
                        mIntent.putExtra(
                            PushKeys.note.name, intent.extras?.getString(PushKeys.note.name)
                        )
                        mIntent.putExtra(
                            PushKeys.photo.name, intent.extras?.getString(PushKeys.photo.name)
                        )
                        mIntent.putExtra(
                            PushKeys.visitorId.name, intent.extras?.getString(PushKeys.visitorId.name)
                        )
                    }

                    startActivity(mIntent)
                }
            } else if (prefs.getString(
                    SessionConstants.TOKEN,
                    ""
                ).isNotEmpty() && prefs.getString(
                    SessionConstants.ROLE,
                    ""
                ) == AppConstants.TENANT
            ) {
                if (intent.extras?.getString("type") == "BUILDING_APPROVE") {
                    startActivity(
                        Intent(this, TenantMainActivity::class.java).putExtra(
                            "from",
                            "from_myList"
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                } else if (intent.extras?.getString("type") == "BUILDING_DENY") {
                    startActivity(
                        Intent(this, TenantMainActivity::class.java).putExtra(
                            "from",
                            "from_myList"
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                } else if (intent.extras?.getString("type") == "REGULAR_CHECKED_IN") {
                    startActivity(
                        Intent(this, OwnerTenantRegularEntryHistoryActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).putExtra("from", "tenant")
                    )
                } else if (intent.extras?.getString("type") == "REGULAR_CHECKED_OUT") {
                    startActivity(
                        Intent(this, OwnerTenantRegularEntryHistoryActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).putExtra("from", "tenant")
                    )
                } else if (intent.extras?.getString("type") == "BILL_APPROVED") {
                    startActivity(
                        Intent(this, TenantBillingsActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).putExtra("from", "tenant")
                    )
                } else if (intent.extras?.getString("type") == "NEW_RENT_Bill") {
                    startActivity(
                        Intent(this, TenantBillingsActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).putExtra("from", "tenant")
                    )
                } else if (intent.extras?.getString("type") == "NEW_BILL_PAID_APPROVED") {
                    startActivity(
                        Intent(this, TenantBillingsActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).putExtra("key", "kill_paid")
                    )
                } else if (intent.extras?.getString("type") == "CHECKED_IN") {
                    val mIntent = Intent(this, TenantMainActivity::class.java)
                    mIntent.putExtra(
                        PushKeys.type.name,
                        intent.extras?.getString(PushKeys.type.name)
                    )
                    mIntent.putExtra(
                        PushKeys.visitorName.name,
                        intent.extras?.getString(PushKeys.visitorName.name)
                    )
                    mIntent.putExtra(
                        PushKeys.address.name,
                        intent.extras?.getString(PushKeys.address.name)
                    )
                    mIntent.putExtra(
                        PushKeys.flatName.name,
                        intent.extras?.getString(PushKeys.flatName.name)
                    )
                    mIntent.putExtra(
                        PushKeys.visitCategoryName.name,
                        intent.extras?.getString(PushKeys.visitCategoryName.name)
                    )
                    mIntent.putExtra(
                        PushKeys.mobileNumber.name,
                        intent.extras?.getString(PushKeys.mobileNumber.name)
                    )
                    mIntent.putExtra(
                        PushKeys.note.name,
                        intent.extras?.getString(PushKeys.note.name)
                    )
                    mIntent.putExtra(
                        PushKeys.photo.name,
                        intent.extras?.getString(PushKeys.photo.name)
                    )
                    mIntent.putExtra(
                        PushKeys.visitorId.name,
                        intent.extras?.getString(PushKeys.visitorId.name)
                    )
                    startActivity(mIntent)
                } else if (intent.extras?.getString("type") == "New_Chat_Mesg") {
                    startActivity(
                        Intent(this, ChatDetailsActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).putExtra("from", "tenant").putExtra("key", "kill_state_noti")
                    )
                } else if (intent.extras?.getString("type") == "CHECKED_OUT") {
                    startActivity(
                        Intent(
                            this,
                            OwnerTenantSingleEntryHistoryActivity::class.java
                        ).putExtra("from", "kill_state").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else if (intent.extras?.getString("type") == "GATEPASS_COMPLETE") {
                    startActivity(
                        Intent(
                            this, OwnerGatePassActivity::class.java
                        ).putExtra("key", "kill_state").putExtra("from","tenant").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else if (intent.extras?.getString("type") == "LIKED_POST") {
                    startActivity(
                        Intent(
                            this, TenantMyCommunityActivity::class.java
                        ).putExtra("from", "tenant").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else if (intent.extras?.getString("type") == "COMMENT_POST") {
                    startActivity(
                        Intent(
                            this, TenantMyCommunityActivity::class.java
                        ).putExtra("from", "tenant").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else if (intent.extras?.getString("type") == "POST") {
                    startActivity(
                        Intent(
                            this, TenantMyCommunityActivity::class.java
                        ).putExtra("from", "tenant").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else if (intent.extras?.getString("type") == "NEW_BUILDING") {
                    startActivity(
                        Intent(
                            this, TenantMainActivity::class.java
                        ).putExtra("from", "from_myList").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else if (intent.extras?.getString("type") == "NEW_SERVICE_Bill") {
                    startActivity(
                        Intent(this, TenantBillingsActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).putExtra("key", "kill_Unpaid")
                    )
                } else if (intent.extras?.getString("type") == "PAYMENT_PENDING") {
                    startActivity(
                        Intent(this, TenantBillingsActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).putExtra("key", "kill_pending")
                    )
                } else if (intent.extras?.getString("type") == "NEW_BILL_NOTIFY") {
                    startActivity(
                        Intent(this, TenantBillingsActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).putExtra("key", "kill_New_All_Notify")
                    )
                } else if (intent.extras?.getString("type") == "New_Rent_Bill_Msg") {
                    startActivity(
                        Intent(this, TenantBillingsActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).putExtra("key", "kill_New_Rent_Bill_Msg")
                    )
                } else if (intent.extras?.getString("type") == "NEW_COMPLAIN_RESOLVE") {
                    val mIntent = Intent(this, TenantRegisterComplainActivity::class.java)
                    startActivity(mIntent)
                } else if (intent.extras?.getString("type") == "BILL_REJECT") {
                    val mIntent = Intent(this, TenantBillingsActivity::class.java).putExtra(
                        "key",
                        "kill_BILL_REJECT"
                    )
                    startActivity(mIntent)
                } else {
                    val intent = Intent(this, TenantMainActivity::class.java)
                    startActivity(intent)
                }


            } else if (prefs.getString(
                    SessionConstants.TOKEN,
                    ""
                ).isNotEmpty() && prefs.getString(
                    SessionConstants.ROLE,
                    ""
                ) == AppConstants.MANAGER
            ) {
                if (intent.extras?.getString("type") == "COMPLAIN_DENY") {
                    val mIntent = Intent(this, RegisterComplaintsActivity::class.java)
                    startActivity(mIntent)
                } else if (intent.extras?.getString("type") == "NEW_COMPLAIN") {
                    val mIntent = Intent(this, RegisterComplaintsActivity::class.java)
                    startActivity(mIntent)
                } else if (intent.extras?.getString("type") == "COMPLAIN_CONFIRM") {
                    val mIntent = Intent(this, RegisterComplaintsActivity::class.java)
                    startActivity(mIntent)

                } else if (intent.extras?.getString("type") == "VISITOR_ACCEPT_REJECT") {
                    if (intent.extras?.getString("title") == "Visitor Rejected") {
                        val mIntent = Intent(this, SingleEntryHistoryActivity::class.java)
                        mIntent.putExtra("from", "kill_state_reject")
                        startActivity(mIntent)
                    } else {
                        val mIntent = Intent(this, SingleEntryHistoryActivity::class.java)
                        mIntent.putExtra("from", "kill_state")
                        startActivity(mIntent)
                    }

                } else if (intent.extras?.getString("type") == "GATEPASS_CREATE") {
                    val mIntent = Intent(this, ManagerGatePassActivity::class.java).setFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                    startActivity(mIntent)
                } else if (intent.extras?.getString("type") == "VISITOR_ADDED") {
                    val mIntent = Intent(this, RegularEntryHistoryActivity::class.java).putExtra(
                        "from",
                        "kill_state"
                    ).setFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                    startActivity(mIntent)
                } else if (intent.extras?.getString("type") == "NEW_BILL_PAID") {
                    startActivity(
                        Intent(
                            this, ApprovalBillingManagerActivity::class.java
                        ).putExtra("from","kill_state").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else {
                    val intent = Intent(this, ManagerMainActivity::class.java)
                    startActivity(intent)
                }
            } else if (prefs.getString(
                    SessionConstants.TOKEN,
                    ""
                ).isNotEmpty() && prefs.getString(
                    SessionConstants.ROLE,
                    ""
                ) == AppConstants.GATEKEEPER
            ) {
                if (intent.extras?.getString("type") == "VISITOR_ACCEPT_REJECT") {
                    if (intent.extras?.getString("title") == "Visitor Rejected") {
                        val mIntent = Intent(this, SingleEntryHistoryActivity::class.java)
                        mIntent.putExtra("from", "kill_state_reject")
                        startActivity(mIntent)
                    } else {
                        val mIntent = Intent(this, SingleEntryHistoryActivity::class.java)
                        mIntent.putExtra("from", "kill_state")
                        startActivity(mIntent)
                    }

                } else if (intent.extras?.getString("type") == "VISITOR_ADDED") {
                    val mIntent = Intent(this, RegularEntryHistoryActivity::class.java).putExtra(
                        "from",
                        "kill_state"
                    ).setFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                    startActivity(mIntent)
                } else if (intent.extras?.getString("type") == "GATEPASS_CREATE") {
                    val mIntent = Intent(this, MainGateKepperActivity::class.java).putExtra(
                        "from",
                        "from_gate_create_pass"
                    ).setFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                    startActivity(mIntent)

                } else {
                    val intent = Intent(this, MainGateKepperActivity::class.java)
                    startActivity(intent)
                }


            } else {
                val intent = Intent(this, LoginUsingOtpActivity::class.java)
                startActivity(intent)
            }
        }, delayTime)
    }

    private fun checkLocationPermissions() {
        if (hasAccessFineLocationPermissions(this@SplashActivity)) {
            if (checkGPS(this@SplashActivity)) {
                Log.d(ContentValues.TAG, getString(R.string.gps_enabled))
                GPSService(this, this)
            } else {
                buildAlertMessageNoGps(getString(com.application.intercom.R.string.enable_gps))
            }
        } else {
            requestLocationPermissions(this@SplashActivity)

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
        val builder = AlertDialog.Builder(this@SplashActivity)
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(
                getString(R.string.yes)
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
        if (requestCode == RESULT_PERMISSION_LOCATION) {
            Log.w("TAG", "onRequestPermissionsResult: ")
            if (hasAccessFineLocationPermissions(this@SplashActivity)) {
                if (checkGPS(this@SplashActivity)) {
                    GPSService(this, this)
                    handler()
                } else {
                    buildAlertMessageNoGps(getString(R.string.enable_gps))
                }
            } else {
                handler()

            }
        }
    }

    override fun onLocationUpdate(latitude: Double, longitude: Double) {

    }

    override fun onResume() {
        super.onResume()
        /*if (hasAccessFineLocationPermissions(this@SplashActivity)) {
            if (checkGPS(this@SplashActivity)) {
                GPSService(this, this)
                handler()
            }
        }*/
//        handler()
        getDeepLink()

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
    }

    private fun getDeepLink() {
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
                    } else if (prefs.getString(
                            SessionConstants.TOKEN,
                            ""
                        ).isNotEmpty() && prefs.getString(
                            SessionConstants.ROLE,
                            ""
                        ) == AppConstants.USER
                    ) {
                        prefs.put(
                            SessionConstants.STOREID,
                            intent.data?.toString()?.split("/")?.last() ?: ""
                        )
                        startActivity(
                            Intent(this, MainActivity::class.java)
                        )
                    } else if (prefs.getString(
                            SessionConstants.TOKEN,
                            ""
                        ).isNotEmpty() && prefs.getString(
                            SessionConstants.ROLE,
                            ""
                        ) == AppConstants.GATEKEEPER
                    ) {
                        prefs.put(
                            SessionConstants.STOREID,
                            intent.data?.toString()?.split("/")?.last() ?: ""
                        )
                        startActivity(
                            Intent(this, MainGateKepperActivity::class.java)
                        )
                    } else if (prefs.getString(
                            SessionConstants.TOKEN,
                            ""
                        ).isNotEmpty() && prefs.getString(
                            SessionConstants.ROLE,
                            ""
                        ) == AppConstants.MANAGER
                    ) {
                        Toast.makeText(
                            this,
                            getString(R.string.this_link_is_not_vaild_for_manager),
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(
                            Intent(this, ManagerMainActivity::class.java)
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
                    val array = intent.data?.toString()?.split("/")
                    if ((array?.size ?: 0) >= 2) {
                        prefs.put(SessionConstants.STORENAME, array?.get(array.size - 2) ?: "")
                        prefs.put(
                            SessionConstants.STOREID,
                            intent.data?.toString()?.split("/")?.last() ?: ""
                        )
                        finishAffinity()
                        startActivity(Intent(this, LoginUsingOtpActivity::class.java))
                    }
                    /*prefs.put(
                        SessionConstants.STOREID,
                        intent.data?.toString()?.split("/")?.last() ?: ""
                    )
                    finishAffinity()
                    startActivity(Intent(this, LoginUsingOtpActivity::class.java))*/
                }
            } else if (intent.data?.toString()?.contains("Notice") == true) {
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
                            Intent(this, TenantNoticeBoardDetailsActivity::class.java).putExtra(
                                "from", prefs.getString(
                                    SessionConstants.ROLE, ""
                                )
                            ).putExtra("viewId", communityId)
                        )
                    } else if (prefs.getString(
                            SessionConstants.TOKEN,
                            ""
                        ).isNotEmpty() && prefs.getString(
                            SessionConstants.ROLE,
                            ""
                        ) == AppConstants.MANAGER
                    ) {
                        /*prefs.put(
                            SessionConstants.STOREID,
                            intent.data?.toString()?.split("/")?.last() ?: ""
                        )*/
                        val communityId = intent.data?.toString()?.split("/")?.last() ?: ""
                        startActivity(
                            Intent(this, NoticeBoardActivity::class.java)
                                .putExtra("storeId", communityId)
                        )
                        /*startActivity(
                            Intent(this, NoticeBoardActivity::class.java)
                        )*/
                    } else if (prefs.getString(
                            SessionConstants.TOKEN,
                            ""
                        ).isNotEmpty() && prefs.getString(
                            SessionConstants.ROLE,
                            ""
                        ) == AppConstants.USER
                    ) {
                        prefs.put(
                            SessionConstants.STOREID,
                            intent.data?.toString()?.split("/")?.last() ?: ""
                        )
                        startActivity(
                            Intent(this, MainActivity::class.java)
                        )
                    } else if (prefs.getString(
                            SessionConstants.TOKEN,
                            ""
                        ).isNotEmpty() && prefs.getString(
                            SessionConstants.ROLE,
                            ""
                        ) == AppConstants.GATEKEEPER
                    ) {
                        prefs.put(
                            SessionConstants.STOREID,
                            intent.data?.toString()?.split("/")?.last() ?: ""
                        )
                        startActivity(
                            Intent(this, MainGateKepperActivity::class.java)
                        )
                    } else {
                        val communityId = intent.data?.toString()?.split("/")?.last() ?: ""
                        startActivity(
                            Intent(this, TenantNoticeBoardDetailsActivity::class.java).putExtra(
                                "from", prefs.getString(
                                    SessionConstants.ROLE, ""
                                )
                            )
                                .putExtra("viewId", communityId)
                        )
                    }

                } else {
                    val array = intent.data?.toString()?.split("/")
                    if ((array?.size ?: 0) >= 2) {
                        prefs.put(SessionConstants.STORENAME, array?.get(array.size - 2) ?: "")
                        prefs.put(
                            SessionConstants.STOREID,
                            intent.data?.toString()?.split("/")?.last() ?: ""
                        )
                        finishAffinity()
                        startActivity(Intent(this, LoginUsingOtpActivity::class.java))
                    }
                    /*  prefs.put(
                          SessionConstants.STOREID,
                          intent.data?.toString()?.split("/")?.last() ?: ""
                      )
                      finishAffinity()
                      startActivity(Intent(this, LoginUsingOtpActivity::class.java))*/
                }
            } else handler()
        } else handler()

    }
}