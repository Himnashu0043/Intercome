package com.application.intercom.user.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.BaseFragment
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.getUserDetailsViewModel.GetUserDetailsViewModel
import com.application.intercom.data.model.factory.getUserDetailsFactory.GetUserDetailsFactory
import com.application.intercom.data.model.local.PropertyListPostModel.PropertyListUserPostModel
import com.application.intercom.data.model.local.localModel.LocalAmentitiesModel
import com.application.intercom.data.model.remote.PropertyList.PropertyLisRes
import com.application.intercom.data.model.remote.UserAdvertimentNewResponse
import com.application.intercom.data.model.remote.UserAdvertismentResponse
import com.application.intercom.data.model.remote.UserParkingListRes.UserParkingList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ServiceRepository
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.data.repository.getUserDetailsRepo.GetUserDetailsRepo
import com.application.intercom.databinding.FragmentHomeBinding
import com.application.intercom.databinding.UserAdViewpagerItemBinding
import com.application.intercom.db.entity.ServicesCategoryTable
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.tenant.activity.notification.TenantNotificationActivity
import com.application.intercom.user.chat.UserChatActivity
import com.application.intercom.user.login.LoginUsingOtpActivity
import com.application.intercom.user.newflow.Sell_RentActivity
import com.application.intercom.user.newflow.UserFilterActivity
import com.application.intercom.user.parking.CompleteParkingToLetDetailsActivity
import com.application.intercom.user.service.ServiceFactory
import com.application.intercom.user.service.ServiceViewModel
import com.application.intercom.utils.*
import com.application.intercom.utils.CommonUtil.DELAY_MS
import com.application.intercom.utils.CommonUtil.PERIOD_MS
import com.application.intercom.utils.ExoPlayerUtils.Companion.playVideo
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import java.util.*
import javax.xml.transform.dom.DOMLocator
import kotlin.collections.ArrayList


class HomeFragment() : BaseFragment<FragmentHomeBinding>(), HomePropertyAdapter.ClickProperty,
    HomeParkingAdapter.ClickParing {
    private var homePropertyAdapter: HomePropertyAdapter? = null
    private var homePropertyOfferBannerAdapter: HomePropertyOfferBannerAdapter? = null
    private var homeTopOfferBannerAdapter: HomeTopOfferBannerAdapter? = null
    private var homeBottomOfferBannerAdapter: HomeBottomOfferBannerAdapter? = null
    private var homeServicesAdapter: HomeServicesAdapter? = null
    private lateinit var viewModel: UserHomeViewModel
    private lateinit var serviceViewModel: ServiceViewModel
    private lateinit var getUserDetailsViewModel: GetUserDetailsViewModel
    private var addList = ArrayList<UserAdvertimentNewResponse.Data.TopUser.AdvertisementData>()
    private var middleAdvList = ArrayList<UserAdvertimentNewResponse.Data.MiddleUser.AdvertisementData>()
    private var thirdAdvList = ArrayList<UserAdvertimentNewResponse.Data.BottomUser.AdvertisementData>()

    //private var homeServicesOfferBannerAdapter: HomeServicesOfferBannerAdapter? = null
    private var homeParkingAdapter: HomeParkingAdapter? = null
    private var sendList = ArrayList<PropertyLisRes.Data>()
    private var parking_sendList = ArrayList<UserParkingList.Data>()
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
    private var getPhotoList = java.util.ArrayList<String>()
    private var getamem_List = java.util.ArrayList<PropertyLisRes.Data.Amentity>()
    private var get_lati: Double = 0.0
    private var get_longi: Double = 0.0
    private lateinit var activity: MainActivity
    var drw: DrawerLayout? = null
    private var currentPos = 0
    private var currentPos1 = 0
    private var currentPos2 = 0
    val handler = Handler()
    private var email: String = ""
    private var number: String = ""
    private var name: String = ""
    private var url: String = ""
    private var getFlatId: String = ""
    private var getFlatParkingId: String = ""
    private var lang: String = ""
    @RequiresApi(Build.VERSION_CODES.P)
    override fun getFragmentBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentHomeBinding {
        activity = getActivity() as MainActivity
        drw = activity.requireViewById(R.id.content)
        val test = prefs.getString(
            SessionConstants.STOREID, GPSService.mLastLocation?.latitude.toString()
        )
        println("======$test")
        if (prefs.getString(
                SessionConstants.STOREID, GPSService.mLastLocation?.latitude.toString()
            ).isNotEmpty()
        ) {
            prefs.put(SessionConstants.STOREID, "")
            Toast.makeText(
                requireContext(),
                getString(R.string.this_link_is_not_valid_for_user),
                Toast.LENGTH_SHORT
            ).show()
        }
        lang =
            prefs.getString(SessionConstants.LANG, GPSService.mLastLocation?.latitude.toString())
        println("=====home$lang")
        if (lang.isEmpty()) {
            lang = Language.BANGLA.languageCode
            println("=====home$lang")
            requireContext().setLocale(lang)
        }
        requireContext().setLocale(lang)
        /**/
        return FragmentHomeBinding.inflate(inflater, container, false)

    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]
        val serviceRepo = ServiceRepository(BaseApplication.apiService)
        serviceViewModel =
            ViewModelProvider(this, ServiceFactory(serviceRepo))[ServiceViewModel::class.java]
        val getUserDetailsrepo = GetUserDetailsRepo(BaseApplication.apiService)
        getUserDetailsViewModel = ViewModelProvider(
            this, GetUserDetailsFactory(getUserDetailsrepo)
        )[GetUserDetailsViewModel::class.java]

    }

    private fun getUserAdvertisementList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,/* GPSService.mLastLocation?.latitude.toString()*/""
        )
        viewModel.getUserAdvertisementList(token)
        serviceViewModel.getServicesListAndSearch(token, "")
    }

    private fun getUserPropertyListList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, /*GPSService.mLastLocation?.latitude.toString()*/""
        )
        val model = PropertyListUserPostModel(
            getLatitude, getLogitude
        )
        viewModel.getUserPropertyListList(token, model)
    }

    private fun getUserParkingListList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = PropertyListUserPostModel(getLatitude, getLogitude)
        viewModel.getUserParkingListList(token, model)
    }

    private fun getUserDetails() {
        val token = prefs.getString(
            SessionConstants.TOKEN, /*GPSService.mLastLocation?.latitude.toString()*/""
        )
        getUserDetailsViewModel.userDetails(token)

    }

    private fun addFavProperty() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.userAddfavProperty(token, getFlatId, null)

    }

    private fun addFavParking() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.userAddfavProperty(token, null, getFlatParkingId)


    }

    override fun init() {
        initialize()
        getUserAdvertisementList()
        setHomeServicesOfferAdapter()
        //setHomePropertiesOfferAdapter()
        setHomeParkingAdapter()
        //  setTopHomeOfferBannerAdapter()
        //  setBottomHomeOfferBannerAdapter()
        setPropertiesAdapter()
        setServicesAdapter()
        if (lang == "bn") {
            binding.imageView103.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.post_property_free_bangla_img
                )
            )
        } else {
            binding.imageView103.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.post_property_free_img
                )
            )
        }
        binding.ivService.setOnClickListener {
            startActivity(
                Intent(requireContext(), MainActivity::class.java).putExtra(
                    "from", "from_side_service"
                )
            )
        }
        binding.ivPropertyBuilding.setOnClickListener {
            startActivity(
                Intent(requireContext(), MainActivity::class.java).putExtra(
                    "from", "from_side_property"
                )
            )
        }
        binding.ivParking.setOnClickListener {
            startActivity(
                Intent(requireContext(), MainActivity::class.java).putExtra(
                    "from", "from_side_parking"
                )
            )
        }
        binding.tvPropertyViewAll.setOnClickListener {
            startActivity(
                Intent(requireContext(), MainActivity::class.java).putExtra(
                    "from", "from_side_property"
                )
            )
        }
        binding.tvParkingToLetViewAll.setOnClickListener {
            startActivity(
                Intent(requireContext(), MainActivity::class.java).putExtra(
                    "from", "from_side_parking"
                )
            )
        }
        binding.tvServiceViewAll.setOnClickListener {
            startActivity(
                Intent(requireContext(), MainActivity::class.java).putExtra(
                    "from", "from_side_service"
                )
            )
        }
        binding.edtSearch.setOnClickListener {
            startActivity(Intent(requireContext(), UserFilterActivity::class.java))
        }

        /*val genderList = resources.getStringArray(R.array.LangOne)
        binding.userHomeToolbar.langSp.adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, genderList)
        binding.userHomeToolbar.langSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    val itemSelected = parent.getItemAtPosition(position)
                    binding.userHomeToolbar.tvLang.setText(itemSelected.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
*/
    }

    override fun lstnr() {
        /* binding.userHomeToolbar.tvLang.setOnClickListener {
             binding.userHomeToolbar.langSp.performClick()
         }*/
        binding.imageView103.setOnClickListener {
            startActivity(Intent(requireContext(), Sell_RentActivity::class.java))
        }
        binding.userHomeToolbar.ivMenu.setOnClickListener {
            drw!!.openDrawer(GravityCompat.START)
//            startActivity(
//                Intent(requireContext(), ProfileActivity::class.java).putExtra(
//                    "from", "user"
//                )
//            )
        }
        binding.userHomeToolbar.ivhomeChat.setOnClickListener {
            /*startActivity(
                Intent(
                    requireContext(), com.application.intercom.MainActivity::class.java
                ).putExtra(
                    "from", "from_property_details"
                )
            )*/
            startActivity(
                Intent(
                    requireContext(), UserChatActivity::class.java
                )
            )
        }
        binding.userHomeToolbar.ivNoti.setOnClickListener {
            startActivity(Intent(requireContext(),TenantNotificationActivity::class.java))
        }
        /* binding.userHomeToolbar.imageView59.setOnClickListener {
             startActivity(
                 Intent(requireContext(), TenantSecondProfileActivity::class.java).putExtra(
                     "name",
                     name
                 ).putExtra("email", email).putExtra("number", number).putExtra("from", "user")
                     .putExtra("url", url)
             )
         }*/
    }

    private fun setTopHomeOfferBannerAdapter(list: ArrayList<UserAdvertismentResponse.Data> = ArrayList()) {
//        binding.rvTopHomeOfferBanner.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        homeTopOfferBannerAdapter = HomeTopOfferBannerAdapter(requireContext(), list)
//        binding.rvTopHomeOfferBanner.adapter = homeTopOfferBannerAdapter
    }

    private fun setBottomHomeOfferBannerAdapter(list: ArrayList<UserAdvertismentResponse.Data> = ArrayList()) {
//        binding.rvBottomHomeOfferBanner.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        homeBottomOfferBannerAdapter = HomeBottomOfferBannerAdapter(requireContext(), list)
//        binding.rvBottomHomeOfferBanner.adapter = homeBottomOfferBannerAdapter
    }

    private fun setPropertiesAdapter(list: ArrayList<PropertyLisRes.Data> = ArrayList()) {
        binding.rvHomeProperty.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        homePropertyAdapter = HomePropertyAdapter(requireContext(), list, this)
        binding.rvHomeProperty.adapter = homePropertyAdapter

    }

    private fun setHomePropertiesOfferAdapter(list: ArrayList<UserAdvertismentResponse.Data> = ArrayList()) {
//        binding.rvHomePropertyOfferBanner.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        homePropertyOfferBannerAdapter = HomePropertyOfferBannerAdapter(requireContext(), list)
//        binding.rvHomePropertyOfferBanner.adapter = homePropertyOfferBannerAdapter

    }

    private fun setServicesAdapter(list: ArrayList<ServicesCategoryTable> = ArrayList()) {
        binding.rvHomeService.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        homeServicesAdapter = HomeServicesAdapter(requireContext(), list)
        binding.rvHomeService.adapter = homeServicesAdapter

    }

    private fun setHomeServicesOfferAdapter(list: ArrayList<String> = ArrayList()) {
//    binding.rvHomeServiceOfferBanner.layoutManager =
//        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//    homeServicesOfferBannerAdapter = HomeServicesOfferBannerAdapter(requireContext(), list)
//    binding.rvHomeServiceOfferBanner.adapter = homeServicesOfferBannerAdapter

    }

    private fun setHomeParkingAdapter(list: ArrayList<UserParkingList.Data> = ArrayList()) {
        binding.rvHomeParkingToLet.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        homeParkingAdapter = HomeParkingAdapter(requireContext(), list, this)
        binding.rvHomeParkingToLet.adapter = homeParkingAdapter

    }

    override fun observer() {
        viewModel.userAdvertisementLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            /* homeTopOfferBannerAdapter?.notifiyData(it.data)
                             homeBottomOfferBannerAdapter?.notifiyData(it.data)
                             homePropertyOfferBannerAdapter?.notifiyData(it.data)*/

                            addList.clear()
                            addList.addAll(it.data.topUser[0].advertisementData)
                            println("======${it.data}")
                            if (addList.isEmpty()) {
                                binding.viewPager1.visibility = View.GONE
                                binding.tabLayout1.visibility = View.GONE
                            } else {
                                binding.tabLayout1.removeAllTabs()
                                addList.forEach {
                                    val newTab = binding.tabLayout1.newTab()
                                    binding.tabLayout1.addTab(newTab)
                                }

                                val layoutManager = LinearLayoutManager(
                                    requireContext(), LinearLayoutManager.HORIZONTAL, false
                                )

                                binding.viewPager1.addOnScrollListener(object :
                                    RecyclerView.OnScrollListener() {
                                    override fun onScrollStateChanged(
                                        recyclerView: RecyclerView, newState: Int
                                    ) {
                                        super.onScrollStateChanged(recyclerView, newState)
                                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                            val currentPosition =
                                                layoutManager.findFirstCompletelyVisibleItemPosition()
                                            binding.tabLayout1.selectTab(
                                                binding.tabLayout1.getTabAt(
                                                    currentPosition
                                                ), true
                                            )
                                        }
                                    }
                                })
                                val videoAdapter = VideoAdapter(requireContext(), addList) {
                                    var currentPosition =
                                        layoutManager.findFirstCompletelyVisibleItemPosition()
                                    if (currentPosition == (addList.size - 1)) {
                                        currentPosition = 0
                                    } else currentPosition += 1
                                    binding.viewPager1.smoothScrollToPosition(currentPosition)
                                }
                                binding.viewPager1.layoutManager = layoutManager
                                binding.viewPager1.adapter = videoAdapter

                            }
                            //////first ViewPager
                            /*binding.viewPager1.adapter =
                                UserTopAdvitAdapter(requireContext(), addList)
                            binding.tabLayout1.setupWithViewPager(binding.viewPager1, true)
                            val runnable = Runnable {
                                if (currentPos == it.data.topUser.size - 1) currentPos = 0
                                else currentPos++
                                if (binding.viewPager1 != null) {
                                    binding.viewPager1.setCurrentItem(currentPos, true)
                                }
                            }

                            Timer().schedule(object : TimerTask() {
                                override fun run() {
                                    handler.post(runnable)
                                }
                            }, DELAY_MS, PERIOD_MS)*/
                            ////second viewPager

                           /* middleAdvList.clear()
                            middleAdvList.addAll(it.data.middleUser)
                             binding.viewPager11.adapter =
                                 UserMiddleAdvAdapter(requireContext(), middleAdvList)
                             binding.tabLayout12.setupWithViewPager(binding.viewPager11, true)
                             val runnable1 = Runnable {
                                 if (currentPos1 == it.data.middleUser.size - 1) currentPos1 = 0
                                 else currentPos1++
                                 if (binding.viewPager11 != null) {
                                     binding.viewPager11.setCurrentItem(currentPos1, true)
                                 }
                             }

                             Timer().schedule(object : TimerTask() {
                                 override fun run() {
                                     handler.post(runnable1)
                                 }
                             }, DELAY_MS, PERIOD_MS)*/
                            middleAdvList.clear()
                            middleAdvList.addAll(it.data.middleUser[0].advertisementData)
                            println("======${it.data}")
                            if (middleAdvList.isEmpty()) {
                                binding.viewPager11.visibility = View.GONE
                                binding.tabLayout12.visibility = View.GONE
                            } else {
                                binding.tabLayout12.removeAllTabs()
                                middleAdvList.forEach {
                                    val newTab = binding.tabLayout12.newTab()
                                    binding.tabLayout12.addTab(newTab)
                                }

                                val layoutManager = LinearLayoutManager(
                                    requireContext(), LinearLayoutManager.HORIZONTAL, false
                                )

                                binding.viewPager11.addOnScrollListener(object :
                                    RecyclerView.OnScrollListener() {
                                    override fun onScrollStateChanged(
                                        recyclerView: RecyclerView, newState: Int
                                    ) {
                                        super.onScrollStateChanged(recyclerView, newState)
                                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                            val currentPosition =
                                                layoutManager.findFirstCompletelyVisibleItemPosition()
                                            binding.tabLayout12.selectTab(
                                                binding.tabLayout12.getTabAt(
                                                    currentPosition
                                                ), true
                                            )
                                        }
                                    }
                                })
                                val videoAdapter = VideoAdapter1(requireContext(), middleAdvList) {
                                    var currentPosition =
                                        layoutManager.findFirstCompletelyVisibleItemPosition()
                                    if (currentPosition == (middleAdvList.size - 1)) {
                                        currentPosition = 0
                                    } else currentPosition += 1
                                    binding.viewPager11.smoothScrollToPosition(currentPosition)
                                }
                                binding.viewPager11.layoutManager = layoutManager
                                binding.viewPager11.adapter = videoAdapter

                            }

                            ////third viewpager
                            thirdAdvList.clear()
                            thirdAdvList.addAll(it.data.bottomUser[0].advertisementData)
                            println("======${it.data}")
                            if (thirdAdvList.isEmpty()) {
                                binding.viewPager111.visibility = View.GONE
                                binding.tabLayout123.visibility = View.GONE
                            } else {
                                binding.tabLayout123.removeAllTabs()
                                thirdAdvList.forEach {
                                    val newTab = binding.tabLayout123.newTab()
                                    binding.tabLayout123.addTab(newTab)
                                }

                                val layoutManager = LinearLayoutManager(
                                    requireContext(), LinearLayoutManager.HORIZONTAL, false
                                )

                                binding.viewPager111.addOnScrollListener(object :
                                    RecyclerView.OnScrollListener() {
                                    override fun onScrollStateChanged(
                                        recyclerView: RecyclerView, newState: Int
                                    ) {
                                        super.onScrollStateChanged(recyclerView, newState)
                                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                            val currentPosition =
                                                layoutManager.findFirstCompletelyVisibleItemPosition()
                                            binding.tabLayout123.selectTab(
                                                binding.tabLayout123.getTabAt(
                                                    currentPosition
                                                ), true
                                            )
                                        }
                                    }
                                })
                                val videoAdapter = VideoAdapter2(requireContext(), thirdAdvList) {
                                    var currentPosition =
                                        layoutManager.findFirstCompletelyVisibleItemPosition()
                                    if (currentPosition == (thirdAdvList.size - 1)) {
                                        currentPosition = 0
                                    } else currentPosition += 1
                                    binding.viewPager111.smoothScrollToPosition(currentPosition)
                                }
                                binding.viewPager111.layoutManager = layoutManager
                                binding.viewPager111.adapter = videoAdapter

                            }
                            /*thirdAdvList.clear()
                            thirdAdvList.addAll(it.data.bottomUser[0].advertisementData)

                            binding.viewPager111.adapter =
                                UserThirdAdvAdapter(requireContext(), thirdAdvList)
                            binding.tabLayout123.setupWithViewPager(binding.viewPager111, true)
                            val runnable11 = Runnable {
                                if (currentPos2 == it.data.bottomUser.size - 1) currentPos2 = 0
                                else currentPos2++
                                if (binding.viewPager111 != null) {
                                    binding.viewPager111.setCurrentItem(currentPos2, true)
                                }
                            }

                            Timer().schedule(object : TimerTask() {
                                override fun run() {
                                    handler.post(runnable11)
                                }
                            }, DELAY_MS, PERIOD_MS)*/
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.viewPager1.visibility = View.GONE
                            binding.tabLayout1.visibility = View.GONE
                        } else if (it.status == AppConstants.STATUS_500) {
                            binding.viewPager1.visibility = View.GONE
                            binding.tabLayout1.visibility = View.GONE
                        } else {
                            binding.viewPager1.visibility = View.GONE
                            binding.tabLayout1.visibility = View.GONE
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
                            homePropertyAdapter?.notifiyData(it.data)
                            if (it.data.isEmpty()) {
                                binding.tvProperty.visibility = View.GONE
                                binding.tvPropertyViewAll.visibility = View.GONE
                                binding.rvHomeProperty.visibility = View.GONE

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
        viewModel.userParkingListLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            parking_sendList.clear()
                            parking_sendList.addAll(it.data)
                            homeParkingAdapter?.notifiyData(it.data)
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
                            if (getFlatParkingId.isEmpty()) {
                                getUserPropertyListList()
                            } else {
                                getUserParkingListList()
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
        getUserDetailsViewModel.userDetailsLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            if (it.data.userDetails.fullName.isNullOrEmpty()) {
                                binding.userHomeToolbar.tvTittle.text = "Hi,Guest"
                                /* binding.userHomeToolbar.tvHomeDes.text = prefs.getString(
                                     SessionConstants.KADDRESS,
                                     GPSService.mLastLocation?.latitude.toString()
                                 )*/
//                                    "Amrapali, Sector 50, Noida"
                            } else {
                                binding.userHomeToolbar.tvTittle.text = it.data.userDetails.fullName
                                println("----name${it.data.userDetails.fullName}")
                                /* binding.userHomeToolbar.tvHomeDes.text = prefs.getString(
                                     SessionConstants.KADDRESS,
                                     GPSService.mLastLocation?.latitude.toString()
                                 )*/
                            }
                            binding.userHomeToolbar.imageView59.loadImagesWithGlideExt(it.data.userDetails.profilePic)
                            url = it.data.userDetails.profilePic
                            name = it.data.userDetails.fullName
                            email = it.data.userDetails.email ?: ""
                            number = it.data.userDetails.phoneNumber
                            prefs.setBoolean(
                                SessionConstants.SUBSCRIPTION,
                                it.data.userDetails.subscription_active
                            )
                            prefs.put(
                                SessionConstants.USERID, it.data.userDetails._id
                            )
                            prefs.put(
                                SessionConstants.PROFILEPIC, it.data.userDetails.profilePic
                            )

                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_503) {
                            val intent = Intent(
                                requireContext(),
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
                    ErrorUtil.handlerGeneralError(requireActivity(), it.throwable!!)
                }
                else -> {}
            }
        })
        serviceViewModel.serviceLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            homeServicesAdapter?.notifiyData(it.data.docs)
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
        amList: java.util.ArrayList<PropertyLisRes.Data.Amentity>,
        lati: Double,
        longi: Double,
        addedBy: String?,
        propertyType: String,
        property_Resi: String?
    ) {
        println("-----pppp${price}")
        building_Name = buildingName
        get_dis = dis
        get_price = price
        get_bedRoom = bedRoom
        get_ft = ft
        get_bathRoom = bathRoom
        get_diste = disT2
        get_discription = discrption
        get_filedDetailsId = flatDetailsId
        get_buildingId = buildingId
        getPhotoList = photoList
        getamem_List = amList
        get_lati = lati
        get_longi = longi

        startActivity(
            Intent(
                context, CompleteParkingToLetDetailsActivity::class.java
            ).putExtra("from", "user_property").putExtra("build", building_Name)
                .putExtra("dis", get_dis).putExtra("price", get_price).putExtra("bed", get_bedRoom)
                .putExtra("ft", get_ft).putExtra("bath", get_bathRoom).putExtra("url", getPhotoList)
                .putExtra("dist", get_diste).putExtra("discription", get_discription)
                .putExtra("filedDetails_ID", get_filedDetailsId)
                .putExtra("building_Id", get_buildingId).putExtra("amm_list", getamem_List)
                .putExtra("lati", get_lati).putExtra("longi", get_longi)
                .putExtra("sendList", sendList)
                .putExtra("added", addedBy)
                .putExtra("propertyType", propertyType)
                .putExtra("property_Resi", property_Resi)

        )
    }

    override fun addFavLstnr(propertyId: String) {
        getFlatId = propertyId
        addFavProperty()
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
        println("-----pppp${price}")
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
                context, CompleteParkingToLetDetailsActivity::class.java
            ).putExtra("from", "user_parking").putExtra("build", building_Name)
                .putExtra("dis", get_dis).putExtra("price", get_price).putExtra("bed", get_bedRoom)
                .putExtra("ft", get_ft).putExtra("bath", get_bathRoom).putExtra("url", get_url)
                .putExtra("dist", get_diste).putExtra("discription", get_discription)
                .putExtra("filedDetails_ID", get_filedDetailsId)
                .putExtra("building_Id", get_buildingId).putExtra("parking_Id", get_ParkingId)
                .putExtra("lati", get_lati).putExtra("longi", get_longi)
                .putExtra("parking_send_list", parking_sendList)
        )
    }

    override fun addFavParkingLstnr(parkingId: String) {
        getFlatParkingId = parkingId
        addFavParking()
    }


    override fun onResume() {
        super.onResume()

        getUserDetails()
        getUserPropertyListList()
        getUserParkingListList()


    }

    inner class VideoAdapter(
        val con: Context,
        val list: java.util.ArrayList<UserAdvertimentNewResponse.Data.TopUser.AdvertisementData>,
        private val handle: () -> Unit = {}
    ) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

        inner class VideoViewHolder(val viewBinding: UserAdViewpagerItemBinding) :
            RecyclerView.ViewHolder(viewBinding.root) {

            var simpleExoPlayer: ExoPlayer? = null

            init {
                viewBinding.ivMute.setOnClickListener {
                    if (viewBinding.ivMute.visibility == View.VISIBLE) {
                        viewBinding.ivMute.visibility = View.INVISIBLE
                        viewBinding.ivunMute.visibility = View.VISIBLE
                        simpleExoPlayer?.volume = 1f
                    }
                }
                viewBinding.ivunMute.setOnClickListener {
                    if (viewBinding.ivunMute.visibility == View.VISIBLE) {
                        viewBinding.ivunMute.visibility = View.INVISIBLE
                        viewBinding.ivMute.visibility = View.VISIBLE
                        simpleExoPlayer?.volume = 0f
                    }
                }
            }
        }

        override fun onViewAttachedToWindow(holder: VideoViewHolder) {
            super.onViewAttachedToWindow(holder)
            holder.apply {
                simpleExoPlayer?.volume = 0f
                val old = list[position].image
                println("======${list[position].image}")
                if (old.contains("mp4")) {
                    viewBinding.videoView.visibility = View.VISIBLE
                    viewBinding.imageView52.visibility = View.INVISIBLE
                    simpleExoPlayer = viewBinding.videoView.playVideo(old, Player.REPEAT_MODE_OFF) {
                        if (it == Player.STATE_BUFFERING) {
                            viewBinding.pro.visibility = View.VISIBLE
                        } else {
                            viewBinding.pro.visibility = View.GONE
                        }
                        if (it == Player.STATE_ENDED) {
                            handle.invoke()
                        }
                    }
                } else {
                    viewBinding.pro.visibility = View.GONE
                    viewBinding.videoView.visibility = View.INVISIBLE
                    viewBinding.imageView52.visibility = View.VISIBLE
                    viewBinding.imageView52.loadImagesWithGlideExt(old)
                }
            }
        }

        override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
            super.onViewDetachedFromWindow(holder)
            holder.simpleExoPlayer?.stop()
            holder.simpleExoPlayer?.release()
            holder.simpleExoPlayer = null
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
            return VideoViewHolder(
                UserAdViewpagerItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
            holder.apply {
                holder.viewBinding.card.setOnClickListener {
                    val openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(list[position].url)
                    context?.startActivity(openURL)
                }
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }


    }
    inner class VideoAdapter1(
        val con: Context,
        val list: java.util.ArrayList<UserAdvertimentNewResponse.Data.MiddleUser.AdvertisementData>,
        private val handle: () -> Unit = {}
    ) : RecyclerView.Adapter<VideoAdapter1.VideoViewHolder>() {

        inner class VideoViewHolder(val viewBinding: UserAdViewpagerItemBinding) :
            RecyclerView.ViewHolder(viewBinding.root) {

            var simpleExoPlayer: ExoPlayer? = null

            init {
                viewBinding.ivMute.setOnClickListener {
                    if (viewBinding.ivMute.visibility == View.VISIBLE) {
                        viewBinding.ivMute.visibility = View.INVISIBLE
                        viewBinding.ivunMute.visibility = View.VISIBLE
                        simpleExoPlayer?.volume = 1f
                    }
                }
                viewBinding.ivunMute.setOnClickListener {
                    if (viewBinding.ivunMute.visibility == View.VISIBLE) {
                        viewBinding.ivunMute.visibility = View.INVISIBLE
                        viewBinding.ivMute.visibility = View.VISIBLE
                        simpleExoPlayer?.volume = 0f
                    }
                }
            }
        }

        override fun onViewAttachedToWindow(holder: VideoViewHolder) {
            super.onViewAttachedToWindow(holder)
            holder.apply {
                simpleExoPlayer?.volume = 0f
                val old = list[position].image
                println("======${list[position].image}")
                if (old.contains("mp4")) {
                    viewBinding.videoView.visibility = View.VISIBLE
                    viewBinding.imageView52.visibility = View.INVISIBLE
                    simpleExoPlayer = viewBinding.videoView.playVideo(old, Player.REPEAT_MODE_OFF) {
                        if (it == Player.STATE_BUFFERING) {
                            viewBinding.pro.visibility = View.VISIBLE
                        } else {
                            viewBinding.pro.visibility = View.GONE
                        }
                        if (it == Player.STATE_ENDED) {
                            handle.invoke()
                        }
                    }
                } else {
                    viewBinding.pro.visibility = View.GONE
                    viewBinding.videoView.visibility = View.INVISIBLE
                    viewBinding.imageView52.visibility = View.VISIBLE
                    viewBinding.imageView52.loadImagesWithGlideExt(old)
                }
            }
        }

        override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
            super.onViewDetachedFromWindow(holder)
            holder.simpleExoPlayer?.stop()
            holder.simpleExoPlayer?.release()
            holder.simpleExoPlayer = null
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
            return VideoViewHolder(
                UserAdViewpagerItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
            holder.apply {
                holder.viewBinding.card.setOnClickListener {
                    val openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(list[position].url)
                    context?.startActivity(openURL)
                }
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }


    }
    inner class VideoAdapter2(
        val con: Context,
        val list: java.util.ArrayList<UserAdvertimentNewResponse.Data.BottomUser.AdvertisementData>,
        private val handle: () -> Unit = {}
    ) : RecyclerView.Adapter<VideoAdapter2.VideoViewHolder>() {

        inner class VideoViewHolder(val viewBinding: UserAdViewpagerItemBinding) :
            RecyclerView.ViewHolder(viewBinding.root) {

            var simpleExoPlayer: ExoPlayer? = null

            init {
                viewBinding.ivMute.setOnClickListener {
                    if (viewBinding.ivMute.visibility == View.VISIBLE) {
                        viewBinding.ivMute.visibility = View.INVISIBLE
                        viewBinding.ivunMute.visibility = View.VISIBLE
                        simpleExoPlayer?.volume = 1f
                    }
                }
                viewBinding.ivunMute.setOnClickListener {
                    if (viewBinding.ivunMute.visibility == View.VISIBLE) {
                        viewBinding.ivunMute.visibility = View.INVISIBLE
                        viewBinding.ivMute.visibility = View.VISIBLE
                        simpleExoPlayer?.volume = 0f
                    }
                }
            }
        }

        override fun onViewAttachedToWindow(holder: VideoViewHolder) {
            super.onViewAttachedToWindow(holder)
            holder.apply {
                simpleExoPlayer?.volume = 0f
                val old = list[position].image
                println("======${list[position].image}")
                if (old.contains("mp4")) {
                    viewBinding.videoView.visibility = View.VISIBLE
                    viewBinding.imageView52.visibility = View.INVISIBLE
                    simpleExoPlayer = viewBinding.videoView.playVideo(old, Player.REPEAT_MODE_OFF) {
                        if (it == Player.STATE_BUFFERING) {
                            viewBinding.pro.visibility = View.VISIBLE
                        } else {
                            viewBinding.pro.visibility = View.GONE
                        }
                        if (it == Player.STATE_ENDED) {
                            handle.invoke()
                        }
                    }
                } else {
                    viewBinding.pro.visibility = View.GONE
                    viewBinding.videoView.visibility = View.INVISIBLE
                    viewBinding.imageView52.visibility = View.VISIBLE
                    viewBinding.imageView52.loadImagesWithGlideExt(old)
                }
            }
        }

        override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
            super.onViewDetachedFromWindow(holder)
            holder.simpleExoPlayer?.stop()
            holder.simpleExoPlayer?.release()
            holder.simpleExoPlayer = null
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
            return VideoViewHolder(
                UserAdViewpagerItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
            holder.apply {
                holder.viewBinding.card.setOnClickListener {
                    val openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(list[position].url)
                    context?.startActivity(openURL)
                }
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }


    }

}