package com.application.intercom.user.newflow

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.application.intercom.R
import com.application.intercom.data.model.remote.newUser.MyList.ActiveNewPhaseList
import com.application.intercom.databinding.ActivityLetGetReadyBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.user.newflow.steps.Steps1Activity
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.CommonUtil.checkGPS
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LetGetReadyActivity : AppCompatActivity() {
    lateinit var binding: ActivityLetGetReadyBinding
    private var rent_sell = "To-Let"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val RESULT_PERMISSION_LOCATION = 1
    private val PERMISSION_LOCATION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private var list: ActiveNewPhaseList.Data? = null
    private var editFrom: String = ""
    private var editId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLetGetReadyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        editFrom = intent.getStringExtra("editFrom").toString()
        editId = intent.getStringExtra("edit_id").toString()
        println("---id$editId")
        if (editFrom.equals("editData")) {
            list = intent.getSerializableExtra("editList") as ActiveNewPhaseList.Data?
            println("-----tst$list")
            if (list!!.flatStatus.equals("To-Let")) {
                binding.radioButton2.isChecked = true
                binding.cardRent.background =
                    ContextCompat.getDrawable(this, R.drawable.oragne_strock_with_white_bg)
                rent_sell = "To-Let"

            } else {
                binding.radioButton21.isChecked = true
                binding.cardSell.background =
                    ContextCompat.getDrawable(this, R.drawable.oragne_strock_with_white_bg)
                rent_sell = "Sale"
            }
        } else {
            binding.radioButton2.isChecked = true
            binding.cardRent.background =
                ContextCompat.getDrawable(this, R.drawable.oragne_strock_with_white_bg)
            rent_sell = "To-Let"
        }

        initView()
        listener()
    }

    private fun initView() {
        /*fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermissions()*/
    }

    private fun listener() {
        binding.imageView107.setOnClickListener {
            finish()
        }
        binding.tvContinue.setOnClickListener {
            if (editFrom.equals("editData")) {
                startActivity(
                    Intent(this, Steps1Activity::class.java).putExtra(
                        "rentKey",
                        rent_sell
                    ).putExtra("editFrom", "editData").putExtra("editList", list).putExtra("edit_id",editId)
                )
            } else {
                startActivity(
                    Intent(this, Steps1Activity::class.java).putExtra(
                        "rentKey",
                        rent_sell
                    )
                )
            }

        }
        binding.cardRent.setOnClickListener {
            rent_sell = "To-Let"
            binding.radioButton21.isChecked = false
            binding.radioButton2.isChecked = true
            binding.cardRent.background =
                ContextCompat.getDrawable(this, R.drawable.oragne_strock_with_white_bg)
            binding.cardSell.background = ContextCompat.getDrawable(this,
                R.drawable.bg_login_type_btn
            )
            binding.cardSell.cardElevation = 20F
        }
        binding.cardSell.setOnClickListener {
            rent_sell = "Sale"
            binding.radioButton2.isChecked = false
            binding.radioButton21.isChecked = true
            binding.cardRent.background = ContextCompat.getDrawable(this,R.drawable.bg_login_type_btn)
            binding.cardRent.cardElevation = 20F
            binding.cardSell.background =
                ContextCompat.getDrawable(this, R.drawable.oragne_strock_with_white_bg)
        }

        binding.radioButton2.setOnClickListener {
            rent_sell = "To-Let"
            binding.radioButton21.isChecked = false
            binding.cardRent.background =
                ContextCompat.getDrawable(this, R.drawable.oragne_strock_with_white_bg)
            binding.cardSell.background = ContextCompat.getDrawable(this,
                R.drawable.bg_login_type_btn
            )
            binding.cardSell.cardElevation = 20F
        }
        binding.radioButton21.setOnClickListener {
            rent_sell = "Sale"
            binding.radioButton2.isChecked = false
            binding.cardRent.background = ContextCompat.getDrawable(this,R.drawable.bg_login_type_btn)
            binding.cardRent.cardElevation = 20F
            binding.cardSell.background =
                ContextCompat.getDrawable(this, R.drawable.oragne_strock_with_white_bg)
        }
    }

    /*private fun checkLocationPermissions() {
        if (hasAccessFineLocationPermissions(this)) {
            if (CommonUtil.checkGPS(this)) {
                Log.d(ContentValues.TAG, "GPS: ENABLED.....")
                GPSService(this, this)
            } else {
                buildAlertMessageNoGps(getString(com.application.intercom.R.string.enable_gps))
            }
        } else {
            requestLocationPermissions(this)

        }
    }

    fun hasAccessFineLocationPermissions(context: Context?): Boolean {
        return (ContextCompat.checkSelfPermission(
            context!!, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    fun requestLocationPermissions(activity: Activity?) {
        ActivityCompat.requestPermissions(
            activity!!, PERMISSION_LOCATION, RESULT_PERMISSION_LOCATION
        )
    }

    private fun buildAlertMessageNoGps(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message).setCancelable(false).setPositiveButton(
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
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RESULT_PERMISSION_LOCATION) {
            Log.w("TAG", "onRequestPermissionsResult: ")
            if (hasAccessFineLocationPermissions(this)) {
                if (CommonUtil.checkGPS(this)) {
                    GPSService(this, this)

                } else {
                    buildAlertMessageNoGps(getString(R.string.enable_gps))
                }
            } else {
                //  handler()

            }
        }

    }

    override fun onLocationUpdate(latitude: Double, longitude: Double) {

    }
*/
   /* override fun onResume() {
        GPSService
        super.onResume()
        if (hasAccessFineLocationPermissions(this)) {
            if (checkGPS(this)) {
                GPSService(this, this)
            }
        }
    }*/

}