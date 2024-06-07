package com.application.intercom.owner.activity.OwnerParking

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.local.ClusterItemAdapter
import com.application.intercom.data.model.local.PropertyListPostModel.PropertyListUserPostModel
import com.application.intercom.data.model.local.localModel.MapDataSendModel
import com.application.intercom.data.model.remote.UserParkingListRes.UserParkingList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.ActivityOwnerParkingBinding
import com.application.intercom.databinding.ActivityPropertyMapBottomSheetBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.owner.activity.favorate.OwnerTenantFavorateActivity
import com.application.intercom.owner.activity.helpSupport.OwnerHelpSupportActivity
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.activity.ownerProperty.OwnerPropertyActivity
import com.application.intercom.owner.activity.ownerVisitor.OwnerVisitorActivity
import com.application.intercom.owner.activity.ownerbilling.BillingAccountOwnerActivity
import com.application.intercom.owner.activity.ownerbilling.OwnerBillingActivity
import com.application.intercom.owner.activity.properties.OwnerPropertiesActivity
import com.application.intercom.owner.activity.registerComplain.OwnerAddRegisterComplainActivity
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.MyCommunity.TenantMyCommunityActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticBoardActivity
import com.application.intercom.tenant.activity.registerComplain.TenantRegisterComplainActivity
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.user.home.MarkerInfoWindowAdapter
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.user.newflow.UserFilterActivity
import com.application.intercom.user.newflow.fav.UserFavActivity
import com.application.intercom.user.parking.CompleteParkingToLetDetailsActivity
import com.application.intercom.user.parking.NewUserParkingMapAdapter
import com.application.intercom.user.parking.ParkingListingAdapter
import com.application.intercom.user.parking.ParkingSortByBottomSheet
import com.application.intercom.user.property.PropertyFilterByBottomSheet
import com.application.intercom.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import java.util.*

class OwnerParkingActivity : BaseActivity<ActivityOwnerParkingBinding>(),
    ParkingSortByBottomSheet.Click,
    PropertyFilterByBottomSheet.CLickFilter, ParkingListingAdapter.ClickParing, OnMapReadyCallback,
    ClusterManager.OnClusterItemClickListener<ClusterItemAdapter?>,
    ClusterManager.OnClusterClickListener<ClusterItemAdapter>, MarkerInfoWindowAdapter.MarkerCLick,
    NewUserParkingMapAdapter.MapParkingRcy, ProfileAdapter.ProfileClick {
    /////side menu
    private var profile_list = java.util.ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null

    ///side mneu
    override fun getLayout(): ActivityOwnerParkingBinding {
        return ActivityOwnerParkingBinding.inflate(layoutInflater)
    }

    private var btnMapList: Boolean = false
    private var mAdapter: ParkingListingAdapter? = null

    /*private var key: String = "tenant_parking"*/
    private var key: String = "ownerSide_parking"
    private var parking_sendList = ArrayList<UserParkingList.Data>()
    private lateinit var viewModel: UserHomeViewModel
    private var get_lati: Double = 0.0
    private var get_longi: Double = 0.0
    private var sortValue: String = ""
    private var getLatitude: Double = 0.0
    private var getLogitude: Double = 0.0
    private var building_Name: String = ""
    private var get_dis: String = ""
    private var get_price: String = ""
    private var get_bedRoom: String = ""
    private var get_ft: String = ""
    private var get_bathRoom: String = ""
    private var get_url = java.util.ArrayList<String>()
    private var get_discription: String = ""
    private var get_diste: String = ""
    private var get_filedDetailsId: String = ""
    private var get_buildingId: String = ""
    private var get_ParkingId: String = ""
    private var cordicnateList = java.util.ArrayList<LatLng>()
    private var clusterItemManager: ClusterManager<ClusterItemAdapter?>? = null
    private var clickedClusterItem: ClusterItemAdapter? = null
    private var marker: Marker? = null

    private var isInfoWindowShown = false
    private var get_name_list = java.util.ArrayList<MapDataSendModel>()
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var property_map_bottom_Sheet: ActivityPropertyMapBottomSheetBinding
    private lateinit var mMap: GoogleMap
    private var isSearchHide: Boolean = false
    private var parkingMapAdapter: NewUserParkingMapAdapter? = null
    private var getFavParkingId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        getLatitude = prefs.getString(
//            SessionConstants.KLATITUDE,
//            GPSService.mLastLocation!!.latitude.toString()
//        )
//        getLogitude = prefs.getString(
//            SessionConstants.KLONGITUDE,
//            GPSService.mLastLocation!!.longitude.toString()
//        )
        //side mneu
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
                    this, R.drawable.pro_bangla_img
                )
            )
        } else {
            binding.nav.tvBl.visibility = View.INVISIBLE
            binding.nav.tvEnglish.visibility = View.VISIBLE
            binding.nav.imageView96.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.pro_img
                )
            )
        }
        //side menu
        initView()
        listener()
        ///side menu
        rcyItem()
        //sidemenu
        LinearSnapHelper().attachToRecyclerView(binding.rcyMap)
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
        val map = SupportMapFragment()
        this.supportFragmentManager.beginTransaction().replace(binding.mapView.id, map)
            .commitAllowingStateLoss()
        map.getMapAsync(this)
        initialize()
        observer()
        setParkingAdapter()
        getUserParkingListList()
    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]

    }

    private fun listener() {
        /*  binding.imageView25.setOnClickListener {
              finish()
          }*/
        //side menu
        binding.imageView25.setOnClickListener {
            binding.ownerParkingDrw.openDrawer(GravityCompat.START)
        }
        ///side mneu
        binding.layoutFilter.setOnClickListener {
            /* ParkingFilterByBottomSheet().apply {
                 show(supportFragmentManager, ParkingFilterByBottomSheet.TAG)

             }*/
//            PropertyFilterByBottomSheet(this).apply {
//                show(supportFragmentManager, PropertyFilterByBottomSheet.TAG)
//
//            }
            startActivity(
                Intent(this, UserFilterActivity::class.java).putExtra(
                    "from",
                    "owner"
                )
            )


        }
        binding.layoutSort.setOnClickListener {
            /* ParkingSortByBottomSheet(this).apply {
                 show(supportFragmentManager, ParkingSortByBottomSheet.TAG)
             }*/
            ParkingSortByBottomSheet(this).apply {
                show(supportFragmentManager, ParkingSortByBottomSheet.TAG)
            }
        }

        /*binding.ivSearch.setOnClickListener {
            binding.layoutToolbar.visibility = View.INVISIBLE
            binding.edtSearch.visibility = View.VISIBLE
        }*/
        binding.ivSearch.setOnClickListener {
            /* binding.layoutToolbar.visibility = View.INVISIBLE

             binding.edtSearch.visibility = View.VISIBLE*/
            if (isSearchHide) {
                binding.ivSearch.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.search_bg_icon
                    )
                )
                isSearchHide = false
                binding.edtSearch.visibility = View.GONE
                binding.edtSearch.text.clear()
                binding.tvParking.visibility = View.VISIBLE
                binding.tvPropertiesCount.visibility = View.VISIBLE
                binding.imageView25.visibility = View.VISIBLE
//                list_adapter?.notifiyData(serviceList)
            } else {
                binding.ivSearch.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.cross_bg_icon
                    )
                )
                binding.edtSearch.visibility = View.VISIBLE
                binding.imageView25.visibility = View.VISIBLE
                binding.tvParking.visibility = View.GONE
                binding.tvPropertiesCount.visibility = View.GONE

                isSearchHide = true
            }
        }

        binding.ivMapview.setOnClickListener {
            if (btnMapList) {
                binding.mapView.visibility = View.VISIBLE
                binding.rvParking.visibility = View.INVISIBLE
                binding.rcyMap.visibility = View.VISIBLE
                binding.ivMapview.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_list
                    )
                )
                binding.rcyMap.layoutManager =
                    LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                parkingMapAdapter =
                    NewUserParkingMapAdapter(this, parking_sendList, this)
                binding.rcyMap.adapter = parkingMapAdapter

                binding.rcyMap.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        when (newState) {
                            RecyclerView.SCROLL_STATE_IDLE -> {
                                (binding.rcyMap.layoutManager as LinearLayoutManager).let {
                                    val position = it.findFirstCompletelyVisibleItemPosition()
                                    if (position != -1) {
                                        parking_sendList[position].let { data ->
                                            mMap.animateCamera(
                                                CameraUpdateFactory.newLatLngZoom(
                                                    LatLng(
                                                        data.latitude,
                                                        data.longitude
                                                    ), 14f
                                                )
                                            )

                                        }
                                    }

                                }
                            }
                        }
                    }
                })
                btnMapList = false
            } else {
                binding.mapView.visibility = View.INVISIBLE
                binding.rvParking.visibility = View.VISIBLE
                binding.rcyMap.visibility = View.GONE
                binding.ivMapview.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_mapview
                    )
                )
                btnMapList = true
            }

        }
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                var text = p0.toString().trim()
                if (text.isNotEmpty()) {
                    if (text.length > 0) {
                        var tempFilterList = parking_sendList.filter {
                            it.buildingName.lowercase(Locale.ROOT)
                                .contains(text.lowercase(Locale.ROOT))
                        }
                        binding.rvParking.layoutManager =
                            LinearLayoutManager(this@OwnerParkingActivity)
                        mAdapter = ParkingListingAdapter(
                            this@OwnerParkingActivity,
                            tempFilterList as ArrayList<UserParkingList.Data>,
                            this@OwnerParkingActivity
                        )
                        binding.rvParking.adapter = mAdapter
                        mAdapter!!.notifyDataSetChanged()
                        if (tempFilterList.isEmpty()) {
                            Toast.makeText(
                                this@OwnerParkingActivity,
                                "Data Not Found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    binding.rvParking.layoutManager = LinearLayoutManager(this@OwnerParkingActivity)
                    mAdapter = ParkingListingAdapter(
                        this@OwnerParkingActivity,
                        parking_sendList,
                        this@OwnerParkingActivity
                    )
                    binding.rvParking.adapter = mAdapter
                    mAdapter!!.notifyDataSetChanged()

                }
            }
        })

    }

    private fun getUserParkingListList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        val model = PropertyListUserPostModel(
            getLatitude,
            getLogitude,
            minValue = null,
            maxValue = null,
            sort = null,
            parkingStatus = null,
            flatType = null
        )
        viewModel.getUserParkingListList(token, model)
    }

    private fun addFavParking() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.userAddfavProperty(token, null, getFavParkingId)


    }

    private fun observer() {
        viewModel.userParkingListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
//
                            parking_sendList.clear()
                            parking_sendList.addAll(it.data)
                            println("----size${it.data.size}")
                            binding.tvPropertiesCount.text = "( ${it.data.size} + )"
                            mAdapter?.notifiyData(it.data)

                            if (cordicnateList.size > 0 && get_name_list.size > 0) {
                                cordicnateList.clear()
                                get_name_list.clear()
                            }
                            var latLng: LatLng? = null
                            for (i in it.data.indices) {
                                if (it.data.get(i).location == null) {
                                } else {
                                    latLng = LatLng(
                                        it.data.get(i).location.coordinates.get(0).toString()
                                            .toDouble(),
                                        it.data.get(i).location.coordinates.get(1).toString()
                                            .toDouble()
                                    )
                                    val dis22 = it.data[i].distance / 1000
                                    val dis2: Double = String.format("%.1f", dis22).toDouble()
                                    get_name_list.add(
                                        MapDataSendModel(
                                            it.data[i].buildingName,
                                            dis2,
                                            it.data[i].division,
                                            it.data[i].district,
                                            it.data[i].flatDetail.bedroom,
                                            it.data[i].flatDetail.bathroom,
                                            it.data[i].flatDetail.sqft,
                                            it.data[i].parkingInfo.price.toInt(),
                                            if (it.data[i].parkingInfo.parkingImages.isNullOrEmpty())
                                                ""
                                            else it.data[i].parkingInfo.parkingImages.get(0),

                                            it.data[i].flatDetail._id,
                                            it.data[i].flatDetail.buildingId,
                                            it.data[i].description
                                        )
                                    )
                                    get_url.add(it.data[i].parkingInfo.parkingImages.toString())
//                                    getamem_List.addAll(it.data[i].flatInfo.amentities)
                                    cordicnateList.add(latLng)
                                }
                            }
                            if (it.data.isEmpty()) {
                                Toast.makeText(
                                    this,
                                    "Data not Found!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                mMap.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(
                                            parking_sendList[0].latitude,
                                            parking_sendList[0].longitude
                                        ), 14f
                                    )
                                )
                            }


                            for (property in parking_sendList) {
                                val markerPosition = LatLng(property.latitude, property.longitude)
                                mMap.addMarker(
                                    MarkerOptions().position(markerPosition)
                                        .title(property.buildingName)
                                        .snippet("${property.parkingInfo.parkingLocation}-${property.parkingInfo.parking_number}")
                                )?.showInfoWindow()
                            }
                            println("----cou${cordicnateList}")
                            println("----name${get_name_list}")
                            if (it.data.isEmpty()) {
                                Toast.makeText(
                                    this,
                                    "Data not Found!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
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
        viewModel.userAddFavListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getUserParkingListList()
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
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

    private fun setParkingAdapter(list: ArrayList<UserParkingList.Data> = ArrayList()) {
        binding.rvParking.layoutManager = LinearLayoutManager(this)
        mAdapter = ParkingListingAdapter(this, list, this)
        binding.rvParking.adapter = mAdapter

    }

    override fun onCLickParking(
        msg: UserParkingList.Data,
        position: Int,
        buildingName: String,
        dis: String,
        price: String,
        bedRoom: String,
        ft: String,
        bathRoom: String,
        parkingImg: java.util.ArrayList<String>,
        disT2: String,
        discrption: String,
        flatDetailsId: String,
        buildingId: String,
        parkingId: String,
        lati: Double,
        longi: Double
    ) {
        building_Name = buildingName
        get_dis = dis
        get_price = price
        get_bedRoom = bedRoom
        get_ft = ft
        get_bathRoom = bathRoom
        get_url = parkingImg
        get_diste = disT2
        get_discription = discrption
        get_filedDetailsId = flatDetailsId
        get_buildingId = buildingId
        get_ParkingId = parkingId
        get_lati = lati
        get_longi = longi
        startActivity(
            Intent(
                this,
                CompleteParkingToLetDetailsActivity::class.java
            ).putExtra("from", key)
                .putExtra("build", building_Name)
                .putExtra("dis", get_dis)
                .putExtra("price", get_price)
                .putExtra("bed", get_bedRoom)
                .putExtra("ft", get_ft)
                .putExtra("bath", get_bathRoom)
                .putExtra("url", get_url)
                .putExtra("dist", get_diste)
                .putExtra("discription", get_discription)
                .putExtra("filedDetails_ID", get_filedDetailsId)
                .putExtra("building_Id", get_buildingId)
                .putExtra("parking_Id", get_ParkingId)
                .putExtra("lati", get_lati)
                .putExtra("longi", get_longi)
                .putExtra("parking_send_list", parking_sendList)
        )
    }

    override fun selectFavParking(parkingId: String) {
        getFavParkingId = parkingId
        addFavParking()
    }

    override fun onCLickSortProperty(name: String) {
        sortValue = name
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation!!.latitude.toString()
        )
        val model = PropertyListUserPostModel(
            getLatitude.toDouble(),
            getLogitude.toDouble(),
            minValue = null,
            maxValue = null,
            sortValue,
            parkingStatus = null,
            flatType = null
        )
        viewModel.getUserParkingListList(token, model)
    }

    override fun onCLickFilter(min: String, max: String, flatType: Int, parkingStatus: String) {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation!!.latitude.toString()
        )
        val model = PropertyListUserPostModel(
            getLatitude.toDouble(),
            getLogitude.toDouble(),
            min.toInt(),
            max.toInt(),
            sort = null,
            parkingStatus,
            flatType
        )
        viewModel.getUserParkingListList(token, model)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        mMap.setOnMarkerClickListener {
            for (position in 0 until parking_sendList.count()) {
                val parking = parking_sendList[position]
                if (it.position.latitude == parking.latitude && it.position.longitude == parking.longitude) {
                    Log.d("setOnMarkerClickListener", "property position: $position")
                    binding.rcyMap.post {
                        binding.rcyMap.smoothScrollToPosition(position)
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    it.position.latitude,
                                    it.position.longitude
                                ), 14f
                            )
                        )
                    }

                    true
                    break
                }
            }
            true
        }
//        mMap.moveCamera(
//            CameraUpdateFactory.newLatLngZoom(
//                LatLng(
//                    getLatitude.toDouble(),
//                    getLogitude.toDouble()
//                ), 10f
//            )
//        )
//        clusterItemManager = ClusterManager(this, mMap)
//        mMap.setOnCameraIdleListener(clusterItemManager)
//
//        clusterItemManager?.setOnClusterItemClickListener(this)
//
//        showProjectOnMap()
    }

    private fun showProjectOnMap() {
        for (i in cordicnateList.indices) {

            val lat = cordicnateList[i].latitude
            val lng = cordicnateList[i].longitude
            val nam = get_name_list[i].buildingName
            val dis = get_name_list[i].distance
            val add = "${get_name_list[i].division} , ${get_name_list[i].district}"
            val price = get_name_list[i].price
            val bed = get_name_list[i].bedroom
            val bath = get_name_list[i].bathroom
            val ft = get_name_list[i].sqft
            val phote = get_name_list[i].photo
            val fieldId = get_name_list[i]._id
            val buildingId = get_name_list[i].buildingId
            val des = get_name_list[i].description
            println("---lat$lat")
            println("---lng$lng")
            val offsetItem = ClusterItemAdapter(
                lng, lat, nam, dis.toString(), add,
                phote,
                bed.toString(),
                bath.toString(),
                ft.toString(),
                price.toString(),
                fieldId,
                buildingId,
                des!!
            )

            clusterItemManager?.addItem(offsetItem)
        }
    }

    override fun onClusterItemClick(item: ClusterItemAdapter?): Boolean {
        marker = clusterItemManager!!.markerCollection.getMarkers()
            .find { it.position == item!!.position }
        if (marker != null) {
            if (!isInfoWindowShown) {
                val markerInfoWindowAdapter = MarkerInfoWindowAdapter(
                    this, item, this
                )
                mMap.setInfoWindowAdapter(markerInfoWindowAdapter)
                marker!!.showInfoWindow()
                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker!!.getPosition()));
                isInfoWindowShown = true

            } else {
                marker!!.hideInfoWindow()
                isInfoWindowShown = false
            }
            // marker.showInfoWindow()
            property_map_BottomSheet(item)
        }
        return true
    }

    override fun onClusterClick(cluster: Cluster<ClusterItemAdapter>?): Boolean {
        return true
    }

    override fun onMarkerClick(item: ClusterItemAdapter?) {

    }

    private fun property_map_BottomSheet(item: ClusterItemAdapter?) {
        property_map_bottom_Sheet =
            ActivityPropertyMapBottomSheetBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme).apply {
                setContentView(property_map_bottom_Sheet.root)
                Handler().postDelayed({
                    marker!!.hideInfoWindow()
                }, 1000)

                property_map_bottom_Sheet.tvBedroom.text = "${item!!.bed} Bedroom"
                property_map_bottom_Sheet.tvBathroom.text = "${item!!.bath} Bathroom"
                property_map_bottom_Sheet.tvFit.text = "${item!!.ft} ft"
                property_map_bottom_Sheet.tvLocation.text = item.distic
                property_map_bottom_Sheet.tvDistance.text = "${item.dist} km"
                property_map_bottom_Sheet.tvPropertyPrice.text = "৳${item.price}"
                property_map_bottom_Sheet.tvPropertyName.text = item.name
                property_map_bottom_Sheet.ivProperty.loadImagesWithGlideExt(item.photo)
                property_map_bottom_Sheet.mapBottomLay.setOnClickListener {
                    startActivity(
                        Intent(
                            this@OwnerParkingActivity,
                            CompleteParkingToLetDetailsActivity::class.java
                        ).putExtra("from", key).putExtra("build", item.name)
                            .putExtra("dis", item.distic)
                            .putExtra("price", item.price)
                            .putExtra("bed", item.bed)
                            .putExtra("ft", item.ft)
                            .putExtra("bath", item.bath)
                            .putExtra("url", get_url)
                            .putExtra("dist", item.dist)
                            .putExtra("discription", item.des)
                            .putExtra("filedDetails_ID", item.fieldDetailsId)
                            .putExtra("building_Id", item.buildingId)
//                            .putExtra("amm_list", getamem_List)
                            .putExtra("lati", item.lat)
                            .putExtra("longi", item.lng)
                            .putExtra("parking_send_list", parking_sendList)
                    )
                }
            }
        bottomSheetDialog.show()
    }

    override fun onMapRcyClick(
        msg: UserParkingList.Data,
        position: Int,
        buildingName: String,
        dis: String,
        price: String,
        bedRoom: String,
        ft: String,
        bathRoom: String,
        parkingImg: java.util.ArrayList<String>,
        disT2: String,
        discrption: String,
        flatDetailsId: String,
        buildingId: String,
        parkingId: String,
        lati: Double,
        longi: Double
    ) {
        startActivity(
            Intent(
                this,
                CompleteParkingToLetDetailsActivity::class.java
            ).putExtra("from", key)
                .putExtra("build", buildingName)
                .putExtra("dis", dis)
                .putExtra("price", price)
                .putExtra("bed", bedRoom)
                .putExtra("ft", ft)
                .putExtra("bath", bathRoom)
                .putExtra("url", parkingImg)
                .putExtra("dist", disT2)
                .putExtra("discription", discrption)
                .putExtra("filedDetails_ID", flatDetailsId)
                .putExtra("building_Id", buildingId)
                .putExtra("parking_Id", parkingId)
                .putExtra("lati", lati)
                .putExtra("longi", longi)
                .putExtra("parking_send_list", parking_sendList)
        )
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

    fun shareIntent(activity: Activity?, shearableLink: String?) {
        ShareCompat.IntentBuilder
            .from(activity!!)
            .setText(shearableLink)
            .setType("text/plain")
            .setChooserTitle("Share with the users")
            .startChooser()
    }

    fun closeDrawer() {
        if (binding.ownerParkingDrw.isDrawerOpen(GravityCompat.START)) {
            binding.ownerParkingDrw.closeDrawer(GravityCompat.START)
        }
    }

    override fun onResume() {
        super.onResume()
        closeDrawer()
    }
}
