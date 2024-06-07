package com.application.intercom.user.property

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
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
import com.application.intercom.data.model.remote.PropertyList.PropertyLisRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.ActivityPropertyMapBottomSheetBinding
import com.application.intercom.databinding.FragmentPropertiesBinding
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.user.home.MarkerInfoWindowAdapter
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.user.newflow.UserFilterActivity
import com.application.intercom.user.parking.CompleteParkingToLetDetailsActivity
import com.application.intercom.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import java.util.*


class PropertiesFragment : BaseFragment<FragmentPropertiesBinding>(),
    PropertySortByBottomSheet.Click, PropertyFilterByBottomSheet.CLickFilter,
    PropertyListingAdapter.ClickProperty, OnMapReadyCallback,
    ClusterManager.OnClusterItemClickListener<ClusterItemAdapter?>,
    ClusterManager.OnClusterClickListener<ClusterItemAdapter>, MarkerInfoWindowAdapter.MarkerCLick,
    NewUserPropertyMapAdapter.CLickRcyOnMap {
    private var btnMapList: Boolean = false
    private var mAdapter: PropertyListingAdapter? = null
    private var key: String = ""
    private lateinit var viewModel: UserHomeViewModel
    private var sendList = ArrayList<PropertyLisRes.Data>()
    private var getLatitude: Double = 0.0
    private var getLogitude: Double = 0.0
    private var sortValue: String = ""
    private var building_Name: String = ""
    private var get_dis: String = ""
    private var get_price: String = ""
    private var get_bedRoom: String = ""
    private var get_ft: String = ""
    private var get_bathRoom: String = ""
    private var get_discription: String = ""
    private var get_diste: String = ""
    private var get_filedDetailsId: String = ""
    private var get_buildingId: String = ""
    private var getPhotoList = java.util.ArrayList<String>()
    private var getamem_List = java.util.ArrayList<PropertyLisRes.Data.Amentity>()
    private var get_lati: Double = 0.0
    private var get_longi: Double = 0.0
    private var mMap: GoogleMap? = null
    private var titleName: String = ""
    private var cordicnateList = ArrayList<LatLng>()
    private var clusterItemManager: ClusterManager<ClusterItemAdapter?>? = null
    private var clickedClusterItem: ClusterItemAdapter? = null
    private var marker: Marker? = null
    private var zoomLevel = 14f
    private var isInfoWindowShown = false
    private var get_name_list = java.util.ArrayList<MapDataSendModel>()
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var property_map_bottom_Sheet: ActivityPropertyMapBottomSheetBinding
    private var isSearchHide: Boolean = false
    private lateinit var activity: MainActivity
    private lateinit var tenantactivity: TenantMainActivity
    var drw: DrawerLayout? = null
    private var mapRecyAdapter: NewUserPropertyMapAdapter? = null
//    var map: SupportMapFragment? = null

    /////
    var selectBedAny: String? = null
    var selectBathAny: String? = null
    var startPrice: Int? = null
    var endPrice: Int? = null
    var startSqr: String? = null
    var endSqr: String? = null
    private var get_lati_filter: Double = 0.0
    private var get_longi_filter: Double = 0.0
    private var rent_sell: String? = null
    private var propertyType: String? = null
    private var getpropertyId: String = ""
    override fun lstnr() {
        binding.imageView25.setOnClickListener {
            if (key.equals("tenant_property")) {
               // requireActivity().finish()
                drw!!.openDrawer(GravityCompat.START)
                /* startActivity(
                     Intent(requireContext(), ProfileActivity::class.java).putExtra(
                         "from",
                         "tenant"
                     ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                 )*/
            } else if (key.equals("user_property")) {
                drw!!.openDrawer(GravityCompat.START)
                /*startActivity(
                    Intent(requireContext(), ProfileActivity::class.java).putExtra(
                        "from",
                        "user"
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )*/
//                drw!!.openDrawer(GravityCompat.START)
//                requireActivity().finish()
//                startActivity(
//                    Intent(requireContext(), MainActivity::class.java).putExtra(
//                        "from",
//                        "from_side_home"
//                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
//                )
               // requireActivity().finish()
            } else if (key.equals("filter")) {
//                drw!!.openDrawer(GravityCompat.START)
                requireActivity().finish()
            } else if (key.equals("tenant_filter")) {
                requireActivity().finish()
            }


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun getFragmentBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentPropertiesBinding {

        key = arguments?.getString("key").toString()
        if (key.equals("filter")) {
            rent_sell = arguments?.getString("rent").toString()
            propertyType = arguments?.getString("propertyType").toString()
            selectBedAny = arguments?.getString("bed").toString()
            selectBathAny = arguments?.getString("bath").toString()
            startPrice = arguments?.getInt("startPrice", 0)
            endPrice = arguments?.getInt("endPrice", 0)
            startSqr = arguments?.getString("startSqr").toString()
            endSqr = arguments?.getString("endSqr").toString()
            get_lati = arguments?.getDouble("getLat", 0.0)!!
            get_longi = arguments?.getDouble("getLong", 0.0)!!
        }
        Log.d("Himanshu", "onCreateView: $key")
        Log.d("Himanshu", "get_lati: $get_lati")
        if (key.equals("tenant_property")) {
            tenantactivity = getActivity() as TenantMainActivity
            drw = tenantactivity.requireViewById(R.id.tenantDrw)
        } else if (key.equals("tenant_filter")) {
            tenantactivity = getActivity() as TenantMainActivity
            drw = tenantactivity.requireViewById(R.id.tenantDrw)
        } else {
            activity = getActivity() as MainActivity
            drw = activity.requireViewById(R.id.content)
        }

        /*  getLatitude = prefs.getString(
              SessionConstants.KLATITUDE, GPSService.mLastLocation!!.latitude.toString()
          )
          getLogitude = prefs.getString(
              SessionConstants.KLONGITUDE, GPSService.mLastLocation!!.longitude.toString()
          )*/
        /* titleName = prefs.getString(
             SessionConstants.KADDRESS,
             GPSService.mLastLocation?.latitude.toString()
         )*/
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = requireActivity().getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(Color.BLUE)
        }*/
        return FragmentPropertiesBinding.inflate(inflater, container, false)
    }

    override fun init() {
        CommonUtil.setLightStatusBar(requireActivity())
        LinearSnapHelper().attachToRecyclerView(binding.rcyMap)
        setPropertyAdapter()

        val map = SupportMapFragment()
        childFragmentManager.beginTransaction().replace(binding.mapView.id, map)
            .commitAllowingStateLoss()
        map.getMapAsync(this)
        initialize()
        observer()
        if (key.equals("filter")) {
            getFIlterUserPropertyListList()
        } else {
            getUserPropertyListList()
        }
        binding.layoutFilter.setOnClickListener {
            if (key.equals("tenant_filter")) {
                startActivity(
                    Intent(requireContext(), UserFilterActivity::class.java).putExtra(
                        "from",
                        "tenant"
                    )
                )
            } else {
                startActivity(Intent(requireContext(), UserFilterActivity::class.java))
            }
           /* PropertyFilterByBottomSheet(this).apply {
                show(this@PropertiesFragment.childFragmentManager, PropertyFilterByBottomSheet.TAG)

            }*/


        }
        binding.layoutSort.setOnClickListener {
            PropertySortByBottomSheet(this).apply {
                show(this@PropertiesFragment.childFragmentManager, PropertySortByBottomSheet.TAG)
            }

        }
        binding.ivSearch.setOnClickListener {
//            binding.layoutToolbar.visibility = View.INVISIBLE
//            binding.edtSearch.visibility = View.VISIBLE
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
                binding.tvProperties.visibility = View.VISIBLE
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
                binding.tvProperties.visibility = View.GONE
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
                        var tempFilterList = sendList.filter {
                            if (it.buildingName.isNullOrEmpty()) {
                                it.title.lowercase(Locale.ROOT)
                                    .contains(text.lowercase(Locale.ROOT))
                            } else {
                                it.buildingName.lowercase(Locale.ROOT)
                                    .contains(text.lowercase(Locale.ROOT))
                            }

                        }
                        binding.rvProperty.layoutManager = LinearLayoutManager(requireContext())
                        mAdapter = PropertyListingAdapter(
                            requireContext(),
                            tempFilterList as ArrayList<PropertyLisRes.Data>,
                            this@PropertiesFragment
                        )
                        binding.rvProperty.adapter = mAdapter
                        mAdapter!!.notifyDataSetChanged()
                        if (tempFilterList.isEmpty()) {
                            Toast.makeText(
                                requireContext(), getString(R.string.data_not_found), Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    binding.rvProperty.layoutManager = LinearLayoutManager(requireContext())
                    mAdapter = PropertyListingAdapter(
                        requireContext(), sendList, this@PropertiesFragment
                    )
                    binding.rvProperty.adapter = mAdapter
                    mAdapter!!.notifyDataSetChanged()

                }
            }
        })




        binding.ivMapview.setOnClickListener {
            if (btnMapList) {
                binding.mapView.visibility = View.VISIBLE
                binding.rvProperty.visibility = View.INVISIBLE
                binding.rcyMap.visibility = View.VISIBLE

                binding.ivMapview.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(), R.drawable.ic_list
                    )
                )
                binding.rcyMap.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                mapRecyAdapter = NewUserPropertyMapAdapter(requireContext(), sendList, this)
                binding.rcyMap.adapter = mapRecyAdapter
                binding.rcyMap.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        when (newState) {
                            RecyclerView.SCROLL_STATE_IDLE -> {
                                (binding.rcyMap.layoutManager as LinearLayoutManager).let {
                                    val position = it.findFirstCompletelyVisibleItemPosition()
                                    if (position != -1) {
                                        sendList[position].let { data ->
                                            mMap!!.animateCamera(
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
                mapRecyAdapter!!.notifyDataSetChanged()
                btnMapList = false
            } else {
                binding.mapView.visibility = View.INVISIBLE
                binding.rvProperty.visibility = View.VISIBLE
                binding.rcyMap.visibility = View.INVISIBLE
                binding.ivMapview.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(), R.drawable.ic_mapview
                    )
                )
                btnMapList = true
            }

        }
    }


    override fun observer() {
        viewModel.userPropertyListLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {

                            sendList.clear()
                            sendList.addAll(it.data)
                            mAdapter?.notifiyData(it.data)
                            binding.tvPropertiesCount.text = "( ${it.data.size} + )"
                            if (cordicnateList.size > 0 && get_name_list.size > 0 && getPhotoList.size > 0 && getamem_List.size > 0) {
                                cordicnateList.clear()
                                get_name_list.clear()
                                getPhotoList.clear()
                                getamem_List.clear()
                            }
                            var latLng: LatLng? = null
                            for (i in it.data.indices) {
                                if (it.data.get(i).location == null) {
                                } else {
                                    latLng = LatLng(
                                        it.data.get(i).location.coordinates.get(1).toString()
                                            .toDouble(),
                                        it.data.get(i).location.coordinates.get(0).toString()
                                            .toDouble()
                                    )
                                    val dis22 = it.data[i].distance / 10000.0
                                    //val dis2: Double = String.format("%.1f", dis22).toDouble()
                                    if (it.data[i].addedBy != null) {
                                        get_name_list.add(
                                            MapDataSendModel(
                                                if (it.data[i].buildingName.isNullOrEmpty()) {
                                                    it.data[i].title
                                                } else {
                                                    it.data[i].buildingName
                                                },
                                                dis22,
                                                if (it.data[i].division.isNullOrEmpty()) {
                                                    it.data[i].address
                                                } else {
                                                    it.data[i].division
                                                },
                                                if (it.data[i].district.isNullOrEmpty()) {
                                                    it.data[i].address
                                                } else {
                                                    it.data[i].district
                                                },
                                                it.data[i].bedroom?.let { it } ?: kotlin.run { 0 },
                                                it.data[i].bathroom!!,
                                                it.data[i].sqft,
                                                it.data[i].price,
                                                if (it.data[i].photos.isNullOrEmpty()) {
                                                    ""
                                                } else {
                                                    it.data[i].photos.get(0)
                                                },
                                                it.data[i]._id,
                                                if (it.data[i].flatDetail != null) {
                                                    it.data[i].flatDetail!!.buildingId
                                                } else {
                                                    ""
                                                },
                                                it.data[i].description
                                            )
                                        )
                                        if (!it.data[i].photos.isNullOrEmpty()) {
                                            getPhotoList.add(it.data[i].photos.toString())
                                        }

                                        if (it.data[i].flatInfo != null) {
                                            getamem_List.addAll(it.data[i].flatInfo!!.amentities)
                                        }

                                        cordicnateList.add(latLng)
                                    } else {
                                        get_name_list.add(
                                            MapDataSendModel(
                                                it.data[i].buildingName,
                                                /*dis2*/
                                                0.0,
                                                it.data[i].division,
                                                it.data[i].district,
                                                /*it.data[i].flatDetail!!.bedroom?.let { it }
                                                    ?: kotlin.run { 0 }*/
                                                if (it.data[i].flatDetail != null) {
                                                    it.data[i].flatDetail!!.bedroom ?: 0
                                                } else 0,
                                                if (it.data[i].flatDetail != null) {
                                                    it.data[i].flatDetail!!.bathroom ?: 0
                                                } else {
                                                    0
                                                },
                                                it.data[i].flatDetail!!.sqft,
                                                it.data[i].flatInfo!!.price,
                                                it.data[i].flatInfo!!.photo.get(0),
                                                it.data[i].flatDetail!!._id,
                                                it.data[i].flatDetail!!.buildingId,
                                                it.data[i].description
                                            )
                                        )
                                        getPhotoList.add(it.data[i].flatInfo!!.photo.toString())
                                        getamem_List.addAll(it.data[i].flatInfo!!.amentities)
                                        cordicnateList.add(latLng)
                                    }
                                }
                            }

                            println("----cou${cordicnateList}")
                            println("----name${get_name_list}")
                            println("=====list${it.data}")
                            if (it.data.isEmpty()) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.data_not_found),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                mMap?.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(
                                            sendList[0].latitude,
                                            sendList[0].longitude
                                        ), 14f
                                    )
                                )

                                for (property in sendList) {
                                    val markerPosition =
                                        LatLng(property.latitude, property.longitude)
                                    mMap?.addMarker(
                                        MarkerOptions().position(markerPosition)
                                            .title(if (property.buildingName != null) property.buildingName else property.title)
                                            .snippet(if (property.district != null) "${property.division} ,${property.district}" else property.address)
                                    )?.showInfoWindow()
                                }
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

                            if (key.equals("filter")) {
                                getFIlterUserPropertyListList()
                            } else {
                                getUserPropertyListList()
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

    }

    private fun getUserPropertyListList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = PropertyListUserPostModel(
            /* getLatitude.toDouble(),
             getLogitude.toDouble()*/
            get_lati,
            get_longi,
            minValue = null,
            maxValue = null,
            sort = null,
            parkingStatus = null,
            flatType = null
        )
        viewModel.getUserPropertyListList(token, model)

    }

    private fun getFIlterUserPropertyListList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = PropertyListUserPostModel(

            get_lati,
            get_longi,
            startPrice,
            endPrice,
            "lowToHigh",
            parkingStatus = null,
            flatType = null,
            null,
            null,
            if (!selectBedAny!!.equals("Any")) selectBedAny!!.toInt() else null,
            if (!selectBathAny!!.equals("Any")) selectBathAny!!.toInt() else null,
            propertyType,
            null
        )
        viewModel.getUserPropertyListList(token, model)

    }

    private fun addFav() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )

        viewModel.userAddfavProperty(token, getpropertyId, null)

    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]

    }

    private fun setPropertyAdapter(list: ArrayList<PropertyLisRes.Data> = ArrayList()) {
        binding.rvProperty.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = PropertyListingAdapter(requireContext(), list, this)
        binding.rvProperty.adapter = mAdapter


    }

    override fun onCLickSortProperty(name: String) {
        sortValue = name
        println("----kkkk${sortValue}")
        val token = prefs.getString(
            SessionConstants.TOKEN, /*GPSService.mLastLocation!!.latitude.toString()*/""
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
        viewModel.getUserPropertyListList(token, model)
    }

    override fun onCLickFilter(min: String, max: String, flatType: Int, parkingStatus: String) {
        val token = prefs.getString(
            SessionConstants.TOKEN, /*GPSService.mLastLocation!!.latitude.toString()*/""
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
        viewModel.getUserPropertyListList(token, model)
    }

    override fun onCLickProperty(
        msg: PropertyLisRes.Data,
        position: Int,
        buildingName: String,
        dis: String,
        price: String,
        bedRoom: String,
        ft: String,
        bathRoom: String,
        disT2: String,
        discrption: String,
        flatDetailsId: String,
        buildingId: String,
        photoList: ArrayList<String>,
        amList: ArrayList<PropertyLisRes.Data.Amentity>,
        lati: Double,
        longi: Double,
        addedBy: String?,
         propertyType: String,
         property_Resi: String?
    ) {
        building_Name = buildingName
        get_dis = dis
        get_price = price
        get_bedRoom = bedRoom
        get_ft = ft
        get_bathRoom = bathRoom
        get_diste = disT2
        get_discription = discrption!!
        get_filedDetailsId = flatDetailsId
        get_buildingId = buildingId
        getPhotoList = photoList
        getamem_List = amList
        get_lati = lati
        get_longi = longi

        println("----phpto${getamem_List}")
        startActivity(
            Intent(
                requireContext(), CompleteParkingToLetDetailsActivity::class.java
            ).putExtra("from", key).putExtra("build", building_Name).putExtra("dis", get_dis)
                .putExtra("price", get_price).putExtra("bed", get_bedRoom).putExtra("ft", get_ft)
                .putExtra("bath", get_bathRoom).putExtra("url", getPhotoList)
                .putExtra("dist", get_diste)
                .putExtra("discription", get_discription)
                .putExtra("filedDetails_ID", get_filedDetailsId)
                .putExtra("building_Id", get_buildingId)
                .putExtra("amm_list", getamem_List)
                .putExtra("lati", get_lati)
                .putExtra("longi", get_longi)
                .putExtra("sendList", sendList)
                .putExtra("added", addedBy)
            .putExtra("propertyType",propertyType)
            .putExtra("property_Resi",property_Resi)

        )
    }

    override fun selectFav(propertyId: String) {
        getpropertyId = propertyId
        addFav()
    }


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        mMap?.setOnMarkerClickListener {
            for (position in 0 until sendList.count()) {
                val property = sendList[position]
                if (it.position.latitude == property.latitude && it.position.longitude == property.longitude) {
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
//                    getLatitude,
//                    getLogitude
////                    /* getLatitude.toDouble(),
////                     getLogitude.toDouble()*/
//                ), 10f
//            )
//        )


//        mMap.moveCamera(
//            CameraUpdateFactory.newLatLngZoom(
//                LatLng(
//                    get_lati,
//                    get_lati
//                    /* getLatitude.toDouble(),
//                     getLogitude.toDouble()*/
//                ), 10f
//            )
//        )

//        val sydney = LatLng(get_lati, get_longi)
//        mMap.addMarker(MarkerOptions().position(sydney).title(titleName))
////        for (chh in cordicnateList){
////            val cooo = LatLng(chh,chh)
////            mMap.addMarker(MarkerOptions().position(cooo))
////        }
//        val zoomLevel = 16.0f
//        mMap.minZoomLevel
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))


        /// updateCameraPoistion(zoomLevel)
//        clusterItemManager = ClusterManager(requireContext(), mMap)
        //clusterItemManager!!.setRenderer(MarkerClusterRenderer(this, mMap, clusterItemManager,clickedClusterItem))

        //clusterItemManager!!.setOnClusterClickListener(ClusterItemAdapter?);
//        mMap.addMarker(MarkerOptions().position(sydney).title(titleName))
//        mMap.setOnCameraIdleListener(clusterItemManager)

//        clusterItemManager?.setOnClusterItemClickListener(this)

//        showProjectOnMap()
    }

    private fun updateCameraPoistion(zoomLevel: Float) {
        mMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    getLatitude.toDouble(),
                    getLogitude.toDouble()
                ), zoomLevel
            )
        )
        mMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    getLatitude.toDouble(),
                    getLogitude.toDouble()
                ), zoomLevel
            )
        )
        mMap?.apply {
            marker = addMarker(
                MarkerOptions().position(
                    LatLng(
                        getLatitude.toDouble(),
                        getLogitude.toDouble()
                    )
                ).title("Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.doc))
            )
        }
    }

    private fun showProjectOnMap() {
        for (i in cordicnateList.indices) {
            println("=-------HIii")
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

            /*  Log.d(ContentValues.TAG, "onResponse: $lat $lng")
              val nam = dataa[i].firstName
              val image = dataa[i].profilePic
              val profileId = dataa[i]._id
              val review = dataa[i].rating.toString()
              val desc = dataa[i].description
              val latLng = LatLng(lat!!.toDouble(), lng!!.toDouble())

              val findDistance = dataa[i].distance / 1000


              val df = DecimalFormat("0.00")
              df.roundingMode = RoundingMode.DOWN
              //val roundoff = df.format(findDistance)
              val miles = Uitls.kmToMiles(findDistance.toDouble())
              val roundoff = df.format(dataa[i].distance)
              Log.d(ContentValues.TAG, "init: " + roundoff)
              val dist= roundoff.toString() + " " + "Km"
              val snippet = "$36/year."
              // Log.d(ContentValues.TAG, "onResponse: $image")
  */
            val offsetItem = ClusterItemAdapter(
                lng,
                lat,
                nam,
                dis.toString(),
                add,
                phote,
                bed.toString(),
                bath.toString(),
                ft.toString(),
                price.toString(),
                fieldId,
                buildingId,
                des!!
            )

//            clusterItemManager?.addItem(offsetItem)

        }
    }

    override fun onClusterItemClick(item: ClusterItemAdapter?): Boolean {
        /* marker = clusterItemManager!!.markerCollection.getMarkers()
             .find { it.position == item!!.position }
         if (marker != null) {
             if (!isInfoWindowShown) {
                 val markerInfoWindowAdapter = MarkerInfoWindowAdapter(
                     requireContext(), item, this
                 )
                 mMap.setInfoWindowAdapter(markerInfoWindowAdapter)
                 marker!!.showInfoWindow()
                 mMap.moveCamera(CameraUpdateFactory.newLatLng(marker!!.getPosition()));
                 isInfoWindowShown = true

             } else {
                 marker!!.hideInfoWindow()
                 isInfoWindowShown = false
             }

 //             marker!!.showInfoWindow()
             //property_map_BottomSheet(item)


         }*/
        return true
    }

    override fun onClusterClick(cluster: Cluster<ClusterItemAdapter>?): Boolean {
        return true
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

//                property_map_bottom_Sheet.tvBedroom.text = "${item!!.bed} Bedroom"
//                property_map_bottom_Sheet.tvBathroom.text = "${item!!.bath} Bathroom"
                property_map_bottom_Sheet.tvFit.text = "${item!!.ft} ${getString(R.string.ft)}"
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
                            .putExtra("bath", item.bath).putExtra("url", getPhotoList)
                            .putExtra("dist", item.dist)
                            .putExtra("discription", item.des)
                            .putExtra("filedDetails_ID", item.fieldDetailsId)
                            .putExtra("building_Id", item.buildingId)
                            .putExtra("amm_list", getamem_List)
                            .putExtra("lati", item.lat)
                            .putExtra("longi", item.lng)
                            .putExtra("sendList", sendList)
                    )
                }
            }
        bottomSheetDialog.show()
    }

    override fun onMarkerClick(item: ClusterItemAdapter?) {
//        println("----tetetet$item")
//        marker!!.showInfoWindow()

    }

    override fun onClickMapRcy(
        msg: PropertyLisRes.Data,
        position: Int,
        buildingName: String,
        dis: String,
        price: String,
        bedRoom: String,
        ft: String,
        bathRoom: String,
        disT2: String,
        discrption: String,
        flatDetailsId: String,
        buildingId: String,
        photoList: ArrayList<String>,
        amList: ArrayList<PropertyLisRes.Data.Amentity>,
        lati: Double,
        longi: Double,
        addedBy: String?
    ) {
        startActivity(
            Intent(
                requireContext(), CompleteParkingToLetDetailsActivity::class.java
            ).putExtra("from", key).putExtra("build", buildingName).putExtra("dis", dis)
                .putExtra("price", price).putExtra("bed", bedRoom).putExtra("ft", ft)
                .putExtra("bath", bathRoom).putExtra("url", photoList)
                .putExtra("dist", disT2)
                .putExtra("discription", discrption)
                .putExtra("filedDetails_ID", flatDetailsId)
                .putExtra("building_Id", buildingId)
                .putExtra("amm_list", amList)
                .putExtra("lati", lati)
                .putExtra("longi", longi)
                .putExtra("sendList", sendList)
                .putExtra("added", addedBy)
        )
    }

}