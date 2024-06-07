package com.application.intercom.user.newflow.steps

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
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.newUser.MyList.ActiveNewPhaseList
import com.application.intercom.databinding.ActivitySteps1Binding
import com.application.intercom.helper.GPSService
import com.application.intercom.user.newflow.adapter.PropertyTypeAdapter
import com.application.intercom.user.newflow.adapter.ResidentAdapter
import com.application.intercom.user.newflow.modal.UserPropertyModel
import com.application.intercom.user.newflow.modal.UserResiModel
import com.application.intercom.user.newflow.modal.UserTestPostModel
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.CommonUtil.checkGPS
import com.application.intercom.utils.SessionConstants
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.io.IOException
import java.util.*

class Steps1Activity : AppCompatActivity(), OnMapReadyCallback,
    PropertyTypeAdapter.PropertyUserClick, ResidentAdapter.ResiClick, GPSService.OnLocationUpdate {
    lateinit var binding: ActivitySteps1Binding
    private var proAdpter: PropertyTypeAdapter? = null
    private var proList = ArrayList<UserPropertyModel>()
    private var resiAdapter: ResidentAdapter? = null
    private var resiList = ArrayList<UserResiModel>()
    private lateinit var handler: Handler
    private lateinit var mMap: GoogleMap
    private var building_Name: String = ""
    private val AUTOCOMPLETE_REQUEST_CODE = 12
    private var newAddress: String = ""
    private var get_lati: Double = 0.0
    private var get_longi: Double = 0.0
    private var mMarker: Marker? = null
    var country: String? = null
    var city: String? = null
    lateinit var map: SupportMapFragment
    private var fields = listOf(
        com.google.android.libraries.places.api.model.Place.Field.ID,
        com.google.android.libraries.places.api.model.Place.Field.NAME,
        com.google.android.libraries.places.api.model.Place.Field.LAT_LNG
    )
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val token: AutocompleteSessionToken = AutocompleteSessionToken.newInstance()
    val RESULT_PERMISSION_LOCATION = 1
    private val PERMISSION_LOCATION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private var rent_key: String = ""
    private var propertyType = "Residentials"
    private var list = kotlin.collections.ArrayList<UserTestPostModel>()
    private var getComm_name = "Office"
    private var getResi_name = "Apartment"
    private var floor: String = ""
    private var total_floor: String = ""
    private var editlist: ActiveNewPhaseList.Data? = null
    private var editFrom: String = ""
    private var editId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySteps1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        Places.initialize(applicationContext, getString(R.string.google_place_api))
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        editFrom = intent.getStringExtra("editFrom").toString()
        editId = intent.getStringExtra("edit_id").toString()
        println("--id$editId")
        if (editFrom.equals("editData")) {
            editlist = intent.getSerializableExtra("editList") as ActiveNewPhaseList.Data?
            println("-----tst$list")
            rent_key = intent.getStringExtra("rentKey").toString()

            if (editlist!!.propertyType.equals("Commercial")) {
                propertyType = "Commercial"
                getComm_name = editlist!!.subPropertyType
                binding.tvComm.setTextColor(ContextCompat.getColor(this, R.color.orange))
                binding.tvComm.background =
                    ContextCompat.getDrawable(this, R.drawable.oragne_strock_with_white_bg)
                binding.tvResidentials.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvResidentials.background =
                    ContextCompat.getDrawable(this, R.drawable.bg_login_type)
                binding.rcyResi.visibility = View.INVISIBLE
                binding.rcyCom.visibility = View.VISIBLE
            } else {
                propertyType = "Residentials"
                getResi_name = editlist!!.subPropertyType
                binding.tvResidentials.setTextColor(ContextCompat.getColor(this, R.color.orange))
                binding.tvResidentials.background =
                    ContextCompat.getDrawable(this, R.drawable.oragne_strock_with_white_bg)
                binding.tvComm.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvComm.background =
                    ContextCompat.getDrawable(this, R.drawable.bg_login_type)
                binding.rcyResi.visibility = View.VISIBLE
                binding.rcyCom.visibility = View.INVISIBLE
            }
            binding.edtFloor.setText(editlist!!.floorLevel.toString())
            binding.edtTotalFloor.setText(editlist!!.totalFloor.toString())
            binding.edtLoc.text = editlist!!.address
            println("----addd${editlist!!.address}")

        } else {
            rent_key = intent.getStringExtra("rentKey").toString()
        }
        initView()
        lstnr()

    }

    private fun initView() {
        map = SupportMapFragment()
        supportFragmentManager.beginTransaction().replace(binding.mapview.id, map)
            .commitAllowingStateLoss()
        map.getMapAsync(this)

        handler = Handler(Looper.myLooper()!!)
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






        binding.toolbar.tvTittle.text = getString(R.string.step1_3)
        binding.commonBtn.tv.text = getString(R.string.next_step)

        binding.rcyCom.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        proAdpter = PropertyTypeAdapter(this, proList, this, getComm_name)
        binding.rcyCom.adapter = proAdpter
        proAdpter!!.notifyDataSetChanged()

        binding.rcyResi.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        resiAdapter = ResidentAdapter(this, resiList, this, getResi_name)
        binding.rcyResi.adapter = resiAdapter
        resiAdapter!!.notifyDataSetChanged()
        checkLocationPermissions()
        binding.edtLoc.text = prefs.getString(
            SessionConstants.KADDRESS,
            GPSService.mLastLocation?.latitude.toString()
        )
        println("-----newGa$get_lati")
        println("-----new$get_longi")


    }

    private fun lstnr() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.edtLoc.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields
            ).build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
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
            binding.rcyCom.visibility = View.INVISIBLE
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
            binding.rcyCom.visibility = View.VISIBLE


        }
        binding.commonBtn.tv.setOnClickListener {
            if (validationData()) {
                if (editFrom.equals("editData")) {
                    floor = binding.edtFloor.text.trim().toString()
                    total_floor = binding.edtTotalFloor.text.trim().toString()
                    if (propertyType.equals("Commercial")) {
                        list.add(
                            UserTestPostModel(
                                null,
                                binding.edtLoc.text.trim().toString(),
                                null,
                                null,
                                null,
                                null,
                                country,
                                city,
                                rent_key,
                                floor.toInt(),
                                get_lati,
                                get_longi,
                                null,
                                "",
                                "",
                                null,
                                propertyType,
                                null,
                                getComm_name,
                                "",
                                total_floor.toInt()
                            )
                        )

                    } else {
                        list.add(
                            UserTestPostModel(
                                null,
                                binding.edtLoc.text.trim().toString(),
                                null,
                                null,
                                null,
                                null,
                                country,
                                city,
                                rent_key,
                                floor.toInt(),
                                get_lati,
                                get_longi,
                                null,
                                "",
                                "",
                                null,
                                propertyType,
                                null,
                                getResi_name,
                                "",
                                total_floor.toInt()
                            )
                        )

                    }
                    println("---list$list")
                    startActivity(
                        Intent(this, Steps2Activity::class.java).putExtra(
                            "testList",
                            list
                        ).putExtra("editFrom", "editData").putExtra("editList", editlist)
                            .putExtra("edit_id", editId)
                    )
                } else {
                    floor = binding.edtFloor.text.trim().toString()
                    total_floor = binding.edtTotalFloor.text.trim().toString()
                    if (propertyType.equals("Commercial")) {
                        list.add(
                            UserTestPostModel(
                                null,
                                binding.edtLoc.text.trim().toString(),
                                null,
                                null,
                                null,
                                null,
                                country,
                                city,
                                rent_key,
                                floor.toInt(),
                                get_lati,
                                get_longi,
                                null,
                                "",
                                "",
                                null,
                                propertyType,
                                null,
                                getComm_name,
                                "",
                                total_floor.toInt()
                            )
                        )

                    } else {

                        list.add(
                            UserTestPostModel(
                                null,
                                binding.edtLoc.text.trim().toString(),
                                null,
                                null,
                                null,
                                null,
                                country,
                                city,
                                rent_key,
                                floor.toInt(),
                                get_lati,
                                get_longi,
                                null,
                                "",
                                "",
                                null,
                                propertyType,
                                null,
                                getResi_name,
                                "",
                                total_floor.toInt()
                            )
                        )

                    }
                    println("---list$list")
                    startActivity(
                        Intent(this, Steps2Activity::class.java).putExtra(
                            "testList",
                            list
                        )
                    )
                }
//
            }

        }
        binding.current.setOnClickListener {
            get_lati = prefs.getString(
                SessionConstants.KLATITUDE, GPSService.mLastLocation?.latitude.toString()
            ).toDouble()
            get_longi = prefs.getString(
                SessionConstants.KLONGITUDE, GPSService.mLastLocation?.longitude.toString()
            ).toDouble()
            map.getMapAsync(this)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
                val place = Autocomplete.getPlaceFromIntent(data)
                get_lati = place.latLng?.latitude ?: 0.0
                get_longi = place.latLng?.longitude ?: 0.0
                mMap.clear()
                val sydney = LatLng(get_lati, get_longi)
                mMarker = mMap.addMarker(MarkerOptions().position(sydney).title(building_Name))
                val zoomLevel = 16.0f
                mMap.minZoomLevel
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))
                binding.edtLoc.text = place.name

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

    override fun onMapReady(p0: GoogleMap) {

        mMap = p0
        val sydney = LatLng(get_lati, get_longi)

        mMarker = mMap.addMarker(
            MarkerOptions().position(sydney).title(building_Name).icon(
                BitmapDescriptorFactory.fromResource(
                    R.drawable.pin_new_one
                )
            )
        )
        val zoomLevel = 16.0f
        mMap.minZoomLevel
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))

        mMap.setOnCameraMoveListener {
            mMarker!!.position = mMap.cameraPosition.target
        }

        mMap.setOnCameraIdleListener {
            get_lati = mMarker?.position!!.latitude
            get_longi = mMarker?.position!!.longitude
            getLocationFromGeoCoder(get_lati, get_longi)
        }


    }


    override fun onResume() {
        GPSService
        super.onResume()
        if (hasAccessFineLocationPermissions(this)) {
            if (checkGPS(this)) {
                GPSService(this, this)
                // handler()

            }
        }
    }

    private val gpsEnableLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (LocationManagerCompat.isLocationEnabled(locationManager)) {
                // request for current location
                GPSService(this, this)
            } else {
                // still show the popup to enable location
                buildAlertMessageNoGps(getString(R.string.enable_gps))
            }
        }

    private fun getLocationFromGeoCoder(lat: Double, long: Double) {
        binding.edtLoc.text = ""
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
                    }
                    addresses[0].countryName?.let {
                        cityName += addresses[0].countryName
                        country = addresses[0].countryName
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        binding.edtLoc.text = cityName
    }

    private fun checkLocationPermissions() {
        if (hasAccessFineLocationPermissions(this)) {
            if (checkGPS(this)) {
                Log.d(ContentValues.TAG, "GPS: ENABLED.....")
                GPSService(this, this)
            } else {
                buildAlertMessageNoGps(getString(R.string.enable_gps))
            }
        } else {
            requestLocationPermissions(this)

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RESULT_PERMISSION_LOCATION) {
            Log.w("TAG", "onRequestPermissionsResult: ")
            if (hasAccessFineLocationPermissions(this)) {
                if (checkGPS(this)) {
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

    override fun onCLickName(name: String) {
        getComm_name = name
        println("----get$getComm_name")


    }

    override fun onClickResiName(name: String) {
        getResi_name = name

    }

    private fun validationData(): Boolean {
        var floor = binding.edtFloor.text.trim().toString()
        var tofloor = binding.edtTotalFloor.text.trim().toString()
        if (binding.edtFloor.text.trim().toString().length < 1 || binding.edtFloor.text.trim()
                .toString().length > 4
        ) {
            Toast.makeText(
                applicationContext, "Please Enter Floor No!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.edtTotalFloor.text!!.trim()
                .toString().length < 1 || binding.edtTotalFloor.text!!.trim().toString().length > 4
        ) {
            Toast.makeText(
                applicationContext, "Please Enter Total Floors !!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (floor > tofloor) {
            Toast.makeText(
                applicationContext, "Floor Level is Greater Then Total Level!!", Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true

    }

}