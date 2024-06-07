package com.application.intercom.user.newflow

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.prefs
import com.application.intercom.databinding.ActivityUserFilterBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.owner.activity.ownerProperty.OwnerPropertyActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.user.newflow.adapter.FilterCommercialAdapter
import com.application.intercom.user.newflow.adapter.FilterResiAdapter
import com.application.intercom.user.newflow.adapter.SelectLocAdapter
import com.application.intercom.user.newflow.modal.UserPropertyModel
import com.application.intercom.user.newflow.modal.UserResiModel
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.SessionConstants
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.io.IOException
import java.util.*

class UserFilterActivity : AppCompatActivity(), FilterCommercialAdapter.PropertyUserClick,
    FilterResiAdapter.ResiClick, GPSService.OnLocationUpdate {
    lateinit var binding: ActivityUserFilterBinding
    private var selectAdpter: SelectLocAdapter? = null
    private var proAdpter: FilterCommercialAdapter? = null
    private var resiAdpter: FilterResiAdapter? = null
    private var rent_sell = "To-Let"
    private var proList = ArrayList<UserPropertyModel>()
    private var resiList = java.util.ArrayList<UserResiModel>()
    private var fields = listOf(
        com.google.android.libraries.places.api.model.Place.Field.ID,
        com.google.android.libraries.places.api.model.Place.Field.NAME,
        com.google.android.libraries.places.api.model.Place.Field.LAT_LNG
    )
    val token: AutocompleteSessionToken = AutocompleteSessionToken.newInstance()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var propertyType = "Residentials"
    private var getComm_name = "Office"
    private var getResi_name = "Apartment"
    var country: String? = null
    var city: String = ""
    private var get_lati: Double = 0.0
    private var get_longi: Double = 0.0
    private val AUTOCOMPLETE_REQUEST_CODE = 12
    val RESULT_PERMISSION_LOCATION = 1
    private val PERMISSION_LOCATION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    )
    var cityList = ArrayList<String>()
    var selectBedAny = "Any"
    var selectBathAny = "Any"
    var startPrice: String = "0"
    var endPrice: String = "1000000"
    var startSqr: String = "0"
    var endSqr: String = "1000000"
    var from: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Places.initialize(applicationContext, getString(R.string.google_place_api))
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        from = intent.getStringExtra("from").toString()
        println("----from$from")
        // binding.textView215.text = prefs.getString(SessionConstants.COUNTRY, "")
        initView()
        listener()
    }

    private fun initView() {
        binding.commonBtn.tv.text = getString(R.string.show_properties)
        checkLocationPermissions()
        proList.add(UserPropertyModel(getString(R.string.office)))
        proList.add(UserPropertyModel(getString(R.string.shop)))
        proList.add(UserPropertyModel(getString(R.string.floor)))
        proList.add(UserPropertyModel(getString(R.string.building)))
        proList.add(UserPropertyModel(getString(R.string.factory)))
        proList.add(UserPropertyModel(getString(R.string.warehouse)))
        proList.add(UserPropertyModel(getString(R.string.plot)))


        resiList.add(UserResiModel(getString(R.string.apartment)))
        resiList.add(UserResiModel(getString(R.string.roommates)))
        resiList.add(UserResiModel(getString(R.string.rooms)))
        resiList.add(UserResiModel(getString(R.string.house)))
        resiList.add(UserResiModel(getString(R.string.plot)))



        binding.rcyPro.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        proAdpter = FilterCommercialAdapter(this, proList, this)
        binding.rcyPro.adapter = proAdpter
        proAdpter!!.notifyDataSetChanged()

        binding.rcyResi.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        resiAdpter = FilterResiAdapter(this, resiList, this)
        binding.rcyResi.adapter = resiAdpter
        resiAdpter!!.notifyDataSetChanged()

    }

    private fun listener() {
        binding.textView216.setOnClickListener {
            if (cityList.count() > 0) {
                cityList.clear()
                selectAdpter!!.notifyDataSetChanged()
            }
            binding.textView215.text = ""
        }
        binding.textView212.setOnClickListener {

            if (cityList.count() > 0) {
                cityList.clear()
                selectAdpter!!.notifyDataSetChanged()
            }
            binding.textView215.text = ""
            selectBedAny = "Any"
            loginTypeSelection(
                binding.tvMember,
                binding.tvManager,
                binding.tvGatekeeper,
                binding.tv3,
                binding.tv4
            )
            selectBathAny = "Any"
            loginTypeSelection1(
                binding.tvMember1,
                binding.tvManager1,
                binding.tvGatekeeper1,
                binding.tv31,
                binding.tv41
            )
        }
        binding.rangeSlider.addOnChangeListener { slider, value, fromUser ->
            binding.textView220.text = "৳${slider.values[0].toInt().toString().plus("-")}"
            binding.textView219.text = "৳${slider.values[1].toInt()} Cr"
            startPrice = slider.values[0].toInt().toString()
            endPrice = slider.values[1].toInt().toString()
        }
        binding.rangeSlider1.addOnChangeListener { slider, value, fromUser ->
            binding.textView22331.text = slider.values[0].toInt().toString().plus("-")
            binding.textView2233.text = "${slider.values[1].toInt()}lac (sqft)"
            startSqr = slider.values[0].toInt().toString()
            endSqr = slider.values[1].toInt().toString()

        }
        binding.tvMember.setOnClickListener {
            selectBedAny = "Any"
            loginTypeSelection(
                binding.tvMember,
                binding.tvManager,
                binding.tvGatekeeper,
                binding.tv3,
                binding.tv4
            )
        }
        binding.tvManager.setOnClickListener {
            selectBedAny = "1"
            loginTypeSelection(
                binding.tvManager,
                binding.tvMember,
                binding.tvGatekeeper,
                binding.tv3,
                binding.tv4
            )
        }
        binding.tvGatekeeper.setOnClickListener {
            selectBedAny = "2"
            loginTypeSelection(
                binding.tvGatekeeper,
                binding.tvMember,
                binding.tvManager,
                binding.tv3,
                binding.tv4
            )
        }
        binding.tv3.setOnClickListener {
            selectBedAny = "3"
            loginTypeSelection(
                binding.tv3,
                binding.tvGatekeeper,
                binding.tvMember,
                binding.tvManager,
                binding.tv4
            )
        }
        binding.tv4.setOnClickListener {
            selectBedAny = "4"
            loginTypeSelection(
                binding.tv4,
                binding.tvGatekeeper,
                binding.tvMember,
                binding.tvManager,
                binding.tv3
            )
        }

        binding.tvMember1.setOnClickListener {
            selectBathAny = "Any"
            loginTypeSelection1(
                binding.tvMember1,
                binding.tvManager1,
                binding.tvGatekeeper1,
                binding.tv31,
                binding.tv41
            )
        }
        binding.tvManager1.setOnClickListener {
            selectBathAny = "1"
            loginTypeSelection1(
                binding.tvManager1,
                binding.tvMember1,
                binding.tvGatekeeper1,
                binding.tv31,
                binding.tv41
            )
        }
        binding.tvGatekeeper1.setOnClickListener {
            selectBathAny = "2"
            loginTypeSelection1(
                binding.tvGatekeeper1,
                binding.tvMember1,
                binding.tvManager1,
                binding.tv31,
                binding.tv41
            )
        }
        binding.tv31.setOnClickListener {
            selectBathAny = "3"
            loginTypeSelection1(
                binding.tv31,
                binding.tvGatekeeper1,
                binding.tvMember1,
                binding.tvManager1,
                binding.tv41
            )
        }
        binding.tv41.setOnClickListener {
            selectBathAny = "4"
            println("-----ss$selectBathAny")
            loginTypeSelection1(
                binding.tv41,
                binding.tvGatekeeper1,
                binding.tvMember1,
                binding.tvManager1,
                binding.tv31
            )
        }
        binding.imageView119.setOnClickListener {
            finish()
        }
        binding.tvForSale.setOnClickListener {
            rent_sell = "Sale"
            binding.tvForSale.setTextColor(ContextCompat.getColor(this, R.color.orange))
            binding.tvForSale.background =
                ContextCompat.getDrawable(this, R.drawable.oragne_strock_with_white_bg)
            binding.tvForRent.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.tvForRent.background = ContextCompat.getDrawable(this, R.drawable.bg_login_type)
        }
        binding.tvForRent.setOnClickListener {
            rent_sell = "To-Let"
            binding.tvForRent.setTextColor(ContextCompat.getColor(this, R.color.orange))
            binding.tvForRent.background =
                ContextCompat.getDrawable(this, R.drawable.oragne_strock_with_white_bg)
            binding.tvForSale.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.tvForSale.background = ContextCompat.getDrawable(this, R.drawable.bg_login_type)
        }

        binding.tvResidentials.setOnClickListener {
            propertyType = "Residentials"
            getResi_name = "Apartment"
            binding.tvResidentials.setTextColor(ContextCompat.getColor(this, R.color.orange))
            binding.tvResidentials.background =
                ContextCompat.getDrawable(this, R.drawable.oragne_strock_with_white_bg)
            binding.tvComm.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.tvComm.background = ContextCompat.getDrawable(this, R.drawable.bg_login_type)
            binding.rcyResi.visibility = View.VISIBLE
            binding.rcyPro.visibility = View.INVISIBLE
        }
        binding.tvComm.setOnClickListener {
            propertyType = "Commercial"
            getComm_name = "Office"
            binding.tvComm.setTextColor(ContextCompat.getColor(this, R.color.orange))
            binding.tvComm.background =
                ContextCompat.getDrawable(this, R.drawable.oragne_strock_with_white_bg)
            binding.tvResidentials.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.tvResidentials.background =
                ContextCompat.getDrawable(this, R.drawable.bg_login_type)
            binding.rcyResi.visibility = View.GONE
            binding.rcyPro.visibility = View.VISIBLE
        }
        binding.edtSearch.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields
            ).build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)

        }
        binding.imageView121.setOnClickListener {
            get_lati = prefs.getString(
                SessionConstants.KLATITUDE, GPSService.mLastLocation?.latitude.toString()
            ).toDouble()
            get_longi = prefs.getString(
                SessionConstants.KLONGITUDE, GPSService.mLastLocation?.longitude.toString()
            ).toDouble()
            binding.textView215.text = prefs.getString(SessionConstants.COUNTRY, "")
        }
        binding.commonBtn.tv.setOnClickListener {
            if (from.equals("owner")) {
                startActivity(
                    Intent(this, OwnerPropertyActivity::class.java).putExtra("from", "from_filter")
                        .putExtra("rent", rent_sell).putExtra("propertyType", propertyType)
                        .putExtra("bed", selectBedAny).putExtra("bath", selectBathAny)
                        .putExtra("startPrice", startPrice.toInt())
                        .putExtra("endPrice", endPrice.toInt()).putExtra("startSqr", startSqr)
                        .putExtra("endSqr", endSqr).putExtra("getLat", get_lati)
                        .putExtra("getLong", get_longi)
                )
            } else if (from.equals("tenant")) {
                startActivity(
                    Intent(this, TenantMainActivity::class.java).putExtra("from", "from_filter")
                        .putExtra("rent", rent_sell).putExtra("propertyType", propertyType)
                        .putExtra("bed", selectBedAny).putExtra("bath", selectBathAny)
                        .putExtra("startPrice", startPrice.toInt())
                        .putExtra("endPrice", endPrice.toInt()).putExtra("startSqr", startSqr)
                        .putExtra("endSqr", endSqr).putExtra("getLat", get_lati)
                        .putExtra("getLong", get_longi)
                )
            } else {
                startActivity(
                    Intent(this, MainActivity::class.java).putExtra("from", "from_filter")
                        .putExtra("rent", rent_sell).putExtra("propertyType", propertyType)
                        .putExtra("bed", selectBedAny).putExtra("bath", selectBathAny)
                        .putExtra("startPrice", startPrice.toInt())
                        .putExtra("endPrice", endPrice.toInt()).putExtra("startSqr", startSqr)
                        .putExtra("endSqr", endSqr).putExtra("getLat", get_lati)
                        .putExtra("getLong", get_longi)
                )
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
                val place = Autocomplete.getPlaceFromIntent(data)
                get_lati = place.latLng?.latitude ?: 0.0
                get_longi = place.latLng?.longitude ?: 0.0
                println("---get_lati$get_lati")
                println("---get_longi$get_longi")
                getLocationFromGeoCoder(get_lati, get_longi)
                /* mMap.clear()
                 val sydney = LatLng(get_lati, get_longi)
                 mMarker = mMap.addMarker(MarkerOptions().position(sydney).title(building_Name))
                 val zoomLevel = 16.0f
                 mMap.minZoomLevel
                 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))
                 binding.edtLoc.text = place.name*/
                // binding.edtSearch.text = place.name

            } else if (requestCode == AutocompleteActivity.RESULT_ERROR) {
                val status: Status = Autocomplete.getStatusFromIntent(data)
                Log.i(ContentValues.TAG, status.statusMessage!!)

            } else {
                Log.i(ContentValues.TAG, "statusMessage!!")
            }
        } else {
            Log.i(ContentValues.TAG, "statusMessage!!")
        }

    }

    private fun getLocationFromGeoCoder(lat: Double, long: Double) {

        var cityName = ""
        runOnUiThread {
            val gcd = Geocoder(this, Locale.getDefault())
            var addresses: List<Address>? = null
            try {
                addresses = gcd.getFromLocation(lat, long, 5)
                if (addresses!!.isNotEmpty()) {
                    addresses[0].subLocality?.let {
                        cityName += addresses[0].subLocality + ","
                    }
                    addresses[0].locality?.let {
                        cityName += addresses[0].locality + ","
                        city = addresses[0].locality

                        cityList.add(city)
                        binding.rcySearchLocal.layoutManager =
                            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                        selectAdpter = SelectLocAdapter(this, cityList)
                        binding.rcySearchLocal.adapter = selectAdpter
                        selectAdpter!!.notifyDataSetChanged()
                    }
                    addresses[0].countryName?.let {
                        cityName += addresses[0].countryName
                        country = addresses[0].countryName
                        //binding.textView215.text = country
                    }
                }
                println("-----city$cityList")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        binding.textView215.text = city
    }

    private fun loginTypeSelection(
        activeTab: TextView,
        inActiveTabOne: TextView,
        inActiveTabTwo: TextView,
        inActiveTabThree: TextView,
        inActiveTabfour: TextView,
    ) {
        activeTab.setTextColor(ContextCompat.getColor(this, R.color.white))
        activeTab.background = ContextCompat.getDrawable(this, R.drawable.common_btn_bg)
        inActiveTabOne.setTextColor(ContextCompat.getColor(this, R.color.black))
        inActiveTabOne.setBackgroundResource(0)
        inActiveTabTwo.setTextColor(ContextCompat.getColor(this, R.color.black))
        inActiveTabTwo.setBackgroundResource(0)
        inActiveTabThree.setTextColor(ContextCompat.getColor(this, R.color.black))
        inActiveTabThree.setBackgroundResource(0)
        inActiveTabfour.setTextColor(ContextCompat.getColor(this, R.color.black))
        inActiveTabfour.setBackgroundResource(0)


    }

    private fun loginTypeSelection1(
        activeTab: TextView,
        inActiveTabOne: TextView,
        inActiveTabTwo: TextView,
        inActiveTabThree: TextView,
        inActiveTabfour: TextView,
    ) {
        activeTab.setTextColor(ContextCompat.getColor(this, R.color.white))
        activeTab.background = ContextCompat.getDrawable(this, R.drawable.common_btn_bg)
        inActiveTabOne.setTextColor(ContextCompat.getColor(this, R.color.black))
        inActiveTabOne.setBackgroundResource(0)
        inActiveTabTwo.setTextColor(ContextCompat.getColor(this, R.color.black))
        inActiveTabTwo.setBackgroundResource(0)
        inActiveTabThree.setTextColor(ContextCompat.getColor(this, R.color.black))
        inActiveTabThree.setBackgroundResource(0)
        inActiveTabfour.setTextColor(ContextCompat.getColor(this, R.color.black))
        inActiveTabfour.setBackgroundResource(0)


    }

    override fun onCLickName(name: String) {
        getComm_name = name
    }

    override fun onClickResiName(name: String) {
        getResi_name = name
    }

    private fun checkLocationPermissions() {
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
            gpsEnableLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
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

    private val gpsEnableLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (isLocationEnabled(locationManager)) {
                // request for current location
                GPSService(this, this)
            } else {
                // still show the popup to enable location
                buildAlertMessageNoGps(getString(R.string.enable_gps))
            }
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

    override fun onResume() {
        GPSService
        super.onResume()
        if (hasAccessFineLocationPermissions(this)) {
            if (CommonUtil.checkGPS(this)) {
                GPSService(this, this)
                checkLocationPermissions()
            }
        }
    }
}