package com.application.intercom.user.parking

import android.content.Intent
import android.os.Build
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.BaseFragment
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.local.ClusterItemAdapter
import com.application.intercom.data.model.local.PropertyListPostModel.PropertyListUserPostModel
import com.application.intercom.data.model.local.localModel.MapDataSendModel
import com.application.intercom.data.model.remote.UserParkingListRes.UserParkingList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.ActivityPropertyMapBottomSheetBinding
import com.application.intercom.databinding.FragmentParkingBinding
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.user.home.MarkerInfoWindowAdapter
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
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
import kotlin.collections.ArrayList


class ParkingFragment : BaseFragment<FragmentParkingBinding>(), ParkingSortByBottomSheet.Click,
    PropertyFilterByBottomSheet.CLickFilter, ParkingListingAdapter.ClickParing, OnMapReadyCallback,
    ClusterManager.OnClusterItemClickListener<ClusterItemAdapter?>,
    ClusterManager.OnClusterClickListener<ClusterItemAdapter>, MarkerInfoWindowAdapter.MarkerCLick,
    NewUserParkingMapAdapter.MapParkingRcy {
    private var btnMapList: Boolean = false
    private var mAdapter: ParkingListingAdapter? = null
    private var key: String = ""
    private lateinit var viewModel: UserHomeViewModel
    private var parking_sendList = ArrayList<UserParkingList.Data>()
    private var sortValue: String = ""
    private var getLatitude: String = ""
    private var getLogitude: String = ""
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
    private var get_lati: Double = 0.0
    private var get_longi: Double = 0.0
    private var mMap: GoogleMap? = null
    private var titleName: String = ""
    private var cordicnateList = java.util.ArrayList<LatLng>()
    private var clusterItemManager: ClusterManager<ClusterItemAdapter?>? = null
    private var clickedClusterItem: ClusterItemAdapter? = null
    private var marker: Marker? = null

    private var isInfoWindowShown = false
    private var get_name_list = java.util.ArrayList<MapDataSendModel>()
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var property_map_bottom_Sheet: ActivityPropertyMapBottomSheetBinding
    private var isSearchHide: Boolean = false
    private lateinit var activity: MainActivity
    private lateinit var tenantactivity: TenantMainActivity
    var drw: DrawerLayout? = null
    private var getFavParkingId: String = ""
    private var parkingMapAdapter: NewUserParkingMapAdapter? = null
    override fun lstnr() {
        binding.imageView25.setOnClickListener {
            if (key.equals("tenant_parking")) {
                drw!!.openDrawer(GravityCompat.START)
                /* startActivity(
                     Intent(requireContext(), ProfileActivity::class.java).putExtra(
                         "from",
                         "tenant"
                     ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                 )*/
//                drw!!.openDrawer(GravityCompat.START)
               // requireActivity().finish()
            } else if (key.equals("user_parking")) {
                drw!!.openDrawer(GravityCompat.START)
                /* startActivity(
                     Intent(requireContext(), ProfileActivity::class.java).putExtra(
                         "from",
                         "user"
                     ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                 )*/
//                drw!!.openDrawer(GravityCompat.START)
              //  requireActivity().finish()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentParkingBinding {
        key = arguments?.getString("key").toString()
        Log.d("Himanshu", "parking_onCreateView: $key")
        getLatitude = prefs.getString(
            SessionConstants.KLATITUDE,
            /*GPSService.mLastLocation!!.latitude.toString()*/""
        )
        getLogitude = prefs.getString(
            SessionConstants.KLONGITUDE,
            /* GPSService.mLastLocation!!.longitude.toString()*/""
        )
        if (key.equals("tenant_parking")) {
            tenantactivity = getActivity() as TenantMainActivity
            drw = tenantactivity.requireViewById(R.id.tenantDrw)
        } else {
            activity = getActivity() as MainActivity
            drw = activity.requireViewById(R.id.content)
        }

        return FragmentParkingBinding.inflate(inflater, container, false)
    }

    override fun init() {
        CommonUtil.setLightStatusBar(requireActivity())
        LinearSnapHelper().attachToRecyclerView(binding.rcyMap)
        initialize()
        setParkingAdapter()
        getUserParkingListList()

        val map = SupportMapFragment()

        childFragmentManager.beginTransaction().replace(binding.mapView.id, map)
            .commitAllowingStateLoss()
        map.getMapAsync(this)
        binding.layoutFilter.setOnClickListener {
//            ParkingFilterByBottomSheet().apply {
//                show(this@ParkingFragment.childFragmentManager, ParkingFilterByBottomSheet.TAG)
//            }
            PropertyFilterByBottomSheet(this).apply {
                show(this@ParkingFragment.childFragmentManager, PropertyFilterByBottomSheet.TAG)

            }


        }
        binding.layoutSort.setOnClickListener {
            ParkingSortByBottomSheet(this).apply {
                show(this@ParkingFragment.childFragmentManager, ParkingSortByBottomSheet.TAG)
            }
        }
        binding.ivSearch.setOnClickListener {
            /* binding.layoutToolbar.visibility = View.INVISIBLE

             binding.edtSearch.visibility = View.VISIBLE*/
            if (isSearchHide) {
                binding.ivSearch.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
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
                        requireContext(),
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
                        binding.rvParking.layoutManager = LinearLayoutManager(requireContext())
                        mAdapter = ParkingListingAdapter(
                            requireContext(),
                            tempFilterList as ArrayList<UserParkingList.Data>, this@ParkingFragment
                        )
                        binding.rvParking.adapter = mAdapter
                        mAdapter!!.notifyDataSetChanged()
                        if (tempFilterList.isEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                "Data Not Found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    binding.rvParking.layoutManager = LinearLayoutManager(requireContext())
                    mAdapter = ParkingListingAdapter(
                        requireContext(),
                        parking_sendList,
                        this@ParkingFragment
                    )
                    binding.rvParking.adapter = mAdapter
                    mAdapter!!.notifyDataSetChanged()

                }
            }
        })


        binding.ivMapview.setOnClickListener {
            if (btnMapList) {
                binding.mapView.visibility = View.VISIBLE
                binding.rvParking.visibility = View.INVISIBLE
                binding.rcyMap.visibility = View.VISIBLE

                binding.ivMapview.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_list
                    )
                )
                binding.rcyMap.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                parkingMapAdapter =
                    NewUserParkingMapAdapter(requireContext(), parking_sendList, this)
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
                                            mMap?.animateCamera(
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
                parkingMapAdapter!!.notifyDataSetChanged()
                btnMapList = false
            } else {
                binding.mapView.visibility = View.INVISIBLE
                binding.rvParking.visibility = View.VISIBLE
                binding.rcyMap.visibility = View.GONE

                binding.ivMapview.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_mapview
                    )
                )
                btnMapList = true
            }

        }
    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]

    }

    private fun getUserParkingListList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            /*  GPSService.mLastLocation!!.latitude.toString()*/""
        )
        val model = PropertyListUserPostModel(
            /* getLatitude.toDouble(),
             getLogitude.toDouble(),*/
            get_lati,
            get_longi,
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

    override fun observer() {
        viewModel.userParkingListLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
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
                            println("----cou${cordicnateList}")
                            println("----name${get_name_list}")
                            if (it.data.isEmpty()) {
                                Toast.makeText(
                                    requireContext(),
                                    "Data not Found!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            mMap?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        parking_sendList[0].latitude,
                                        parking_sendList[0].longitude
                                    ), 14f
                                )
                            )

                            for (property in parking_sendList) {
                                val markerPosition = LatLng(property.latitude, property.longitude)
                                mMap?.addMarker(
                                    MarkerOptions().position(markerPosition)
                                        .title(property.buildingName)
                                        .snippet("${property.parkingInfo.parkingLocation}-${property.parkingInfo.parking_number}")
                                )?.showInfoWindow()
                            }
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireContext().longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })
        viewModel.userAddFavListLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getUserParkingListList()
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireContext().longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }

                else -> {}
            }
        })
    }

    private fun setParkingAdapter(list: ArrayList<UserParkingList.Data> = ArrayList()) {
        binding.rvParking.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = ParkingListingAdapter(requireContext(), list, this)
        binding.rvParking.adapter = mAdapter

    }

    override fun onCLickSortProperty(name: String) {
        sortValue = name
        val token = prefs.getString(
            SessionConstants.TOKEN,
            /* GPSService.mLastLocation!!.latitude.toString()*/""
        )
        val model = PropertyListUserPostModel(
            /* getLatitude.toDouble(),
             getLogitude.toDouble(),*/
            get_lati,
            get_longi,
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
            /*GPSService.mLastLocation!!.latitude.toString()*/""
        )
        val model = PropertyListUserPostModel(
            /* getLatitude.toDouble(),
             getLogitude.toDouble(),*/
            get_lati,
            get_longi,
            min.toInt(),
            max.toInt(),
            sort = null,
            parkingStatus,
            flatType
        )
        viewModel.getUserParkingListList(token, model)
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
                requireContext(),
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

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        mMap?.setOnMarkerClickListener {
            for (position in 0 until parking_sendList.count()) {
                val parking = parking_sendList[position]
                if (it.position.latitude == parking.latitude && it.position.longitude == parking.longitude) {
                    Log.d("setOnMarkerClickListener", "property position: $position")
                    binding.rcyMap.post {
                        binding.rcyMap.smoothScrollToPosition(position)
                        mMap?.animateCamera(
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
//                    /* getLatitude.toDouble(),
//                     getLogitude.toDouble()*/
//                    get_lati,
//                    get_longi
//                ), 10f
//            )
//        )
//        clusterItemManager = ClusterManager(requireContext(), mMap)
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
                    requireContext(), item, this
                )
                mMap?.setInfoWindowAdapter(markerInfoWindowAdapter)
                marker!!.showInfoWindow()
                mMap?.moveCamera(CameraUpdateFactory.newLatLng(marker!!.getPosition()));
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
            ActivityPropertyMapBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme).apply {
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
                            requireContext(), CompleteParkingToLetDetailsActivity::class.java
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
                requireContext(),
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
}