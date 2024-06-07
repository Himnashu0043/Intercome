package com.application.intercom.owner.fragment.ownerHome

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.local.owner.commentPost.EditCommentPostModel
import com.application.intercom.data.model.local.owner.commentPost.OwnerCommentPostModel
import com.application.intercom.data.model.local.owner.getCommentPostModel.OwnerGetCommentPostModel
import com.application.intercom.data.model.local.owner.likeCommunty.OwnerLikeCommunityPostModel
import com.application.intercom.data.model.remote.owner.bill.OwnerUnPaidBillListRes
import com.application.intercom.data.model.remote.owner.community.OwnerCommunityListRes
import com.application.intercom.data.model.remote.owner.getComment.OwnerGetCommentList
import com.application.intercom.data.model.remote.owner.ownerHome.OwnerAdvertisementRes
import com.application.intercom.data.model.remote.owner.ownerHome.OwnerDetailsRes
import com.application.intercom.data.model.remote.singleEntryHistory.OwnerTenantSingleEntryHistoryList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.databinding.FragmentOwnerHomeBinding
import com.application.intercom.databinding.OwnerFlatBottomSheetBinding
import com.application.intercom.databinding.TenanatCommentBottomSheetBinding
import com.application.intercom.databinding.UserAdViewpagerItemBinding
import com.application.intercom.gatekepper.activity.newFlow.ownerTenantRegularEntry.OwnerTenantRegularEntryActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.activity.ownerVisitor.OwnerVisitorHistoryActivity
import com.application.intercom.owner.activity.ownerbilling.OwnerBillingActivity
import com.application.intercom.owner.activity.viewPostDetails.ViewPostDetailsActivity
import com.application.intercom.owner.adapter.HomeFlatAdpter
import com.application.intercom.owner.adapter.bill.OwnerUnPaidAdapter
import com.application.intercom.owner.adapter.ownerHome.OwnerExtraAdapter
import com.application.intercom.owner.adapter.ownerHome.OwnerPostingAdapter
import com.application.intercom.tenant.Model.HomeExtraModal
import com.application.intercom.tenant.activity.MyCommunity.TenantMyCommunityActivity
import com.application.intercom.tenant.activity.gateKepper.TenantGateKepperActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticBoardActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticeBoardDetailsActivity
import com.application.intercom.tenant.activity.notification.TenantNotificationActivity
import com.application.intercom.tenant.activity.payment.TenantPaymentActivity
import com.application.intercom.tenant.activity.registerComplain.TenantRegisterComplainActivity
import com.application.intercom.tenant.activity.writePost.TenantWritePostActivity
import com.application.intercom.tenant.adapter.comment.TenantCommentAdapter
import com.application.intercom.tenant.adapter.home.TenantHomeOfferAdapter
import com.application.intercom.tenant.adapter.home.TenantHomeVisitorAdapter
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.user.home.*
import com.application.intercom.user.login.LoginUsingOtpActivity
import com.application.intercom.user.newflow.Sell_RentActivity
import com.application.intercom.user.newflow.UserFilterActivity
import com.application.intercom.utils.*
import com.application.intercom.utils.ExoPlayerUtils.Companion.playVideo
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class OwnerHomeFragment : Fragment(), OwnerExtraAdapter.ExtraClick, OwnerPostingAdapter.Click,
    OwnerUnPaidAdapter.PaidClick, CommunityImgAdapter.ClickImg, HomeFlatAdpter.HomeFlat,
    GPSService.OnLocationUpdate, TenantCommentAdapter.ListClick {
    lateinit var binding: FragmentOwnerHomeBinding
    private var visitorAdapter: TenantHomeVisitorAdapter? = null
    private var offerAdapter: TenantHomeOfferAdapter? = null
    private var unPaidAdapter: OwnerUnPaidAdapter? = null
    private var extra_adapter: OwnerExtraAdapter? = null
    private var posting_adptr: OwnerPostingAdapter? = null
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var flat_by_bottom: OwnerFlatBottomSheetBinding
    private var extraList = ArrayList<HomeExtraModal>()
    private lateinit var viewModel: UserHomeViewModel
    private lateinit var owner_viewModel: OwnerHomeViewModel
    private var addList = ArrayList<OwnerAdvertisementRes.Data.AdvertisementData>()

    private var projectId: String = ""
    private var get_flatId: String = ""
    private var communityList = ArrayList<OwnerCommunityListRes.Data>()
    private var postid: String = ""
    private var flatNameforComment: String = ""
    private var getCommentList = ArrayList<OwnerGetCommentList.Data>()
    private var homeFlatList = ArrayList<OwnerDetailsRes.Data.UserData>()
    lateinit var bottomSheetDialog1: BottomSheetDialog
    private var commentAdapter: TenantCommentAdapter? = null
    private var homeFlatAdapter: HomeFlatAdpter? = null
    lateinit var comment_by_bottom: TenanatCommentBottomSheetBinding
    private var userId: String = ""
    private lateinit var side_owner_viewModel: OwnerSideViewModel
    private var owner_unpaidList = ArrayList<OwnerUnPaidBillListRes.Data.Result>()

    /*private var list = ArrayList<RegularHistoryList.Data.Result>()*/
    private var list = ArrayList<OwnerTenantSingleEntryHistoryList.Data>()
    private var showImg = ArrayList<String>()
    private lateinit var dialog: Dialog
    private var viewPostCountId: String = ""
    private var sendSeletedPostion: Int = -1
    private lateinit var activity: OwnerMainActivity
    var drw: DrawerLayout? = null
    val RESULT_PERMISSION_LOCATION = 1
    private var currentPos1 = 0
    private var currentPos2 = 0
    val handler = Handler()
    var buildingIdForAdvertisement: String = ""

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOwnerHomeBinding.inflate(layoutInflater)
        activity = getActivity() as OwnerMainActivity
        drw = activity.findViewById(R.id.ownerDrw)
        initView()
        lstnr()
        var lang =
            prefs.getString(SessionConstants.LANG, GPSService.mLastLocation?.latitude.toString())
        println("=====home$lang")
        if (lang.isEmpty()) {
            lang = Language.ENGLISH.languageCode
            println("=====home$lang")
            requireContext().setLocale(lang)
        }
        requireContext().setLocale(lang)
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
        return binding.root
    }


    private fun initView() {
        binding.userHomeToolbar.ivhomeChat.visibility = View.INVISIBLE
        initialize()
        observer()

        //new Change
        //unPaidList()
        //new Change


        extraList.add(HomeExtraModal(getString(R.string.complain), R.drawable.complain_img))
        extraList.add(HomeExtraModal(getString(R.string.my_billings), R.drawable.payment_img))
        extraList.add(HomeExtraModal(getString(R.string.gatekeepers), R.drawable.gatekeeper_img))
        extraList.add(HomeExtraModal(getString(R.string.notice), R.drawable.notice_img))
//        binding.userHomeToolbar.imageView69.visibility = View.VISIBLE
//        val genderList = resources.getStringArray(R.array.LangOne)
//        binding.userHomeToolbar.langSp.adapter =
//            ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, genderList)
//        binding.userHomeToolbar.langSp.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>,
//                    view: View?,
//                    position: Int,
//                    id: Long,
//                ) {
//                    val itemSelected = parent.getItemAtPosition(position)
//                    binding.userHomeToolbar.tvLang.setText(itemSelected.toString())
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {}
//            }
//

        binding.rcyExtra.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.HORIZONTAL, false
        )
        extra_adapter = OwnerExtraAdapter(requireContext(), this, extraList)
        binding.rcyExtra.adapter = extra_adapter
        extra_adapter!!.notifyDataSetChanged()


    }

    fun test() {
        if (prefs.getString(
                SessionConstants.STORENAME, GPSService.mLastLocation?.latitude.toString()
            ).isNotEmpty()
        ) {
            if (prefs.getString(
                    SessionConstants.STORENAME, GPSService.mLastLocation?.latitude.toString()
                ) == "Community"
            ) {
                startActivity(
                    Intent(requireContext(), TenantMyCommunityActivity::class.java).putExtra(
                        "from", prefs.getString(
                            SessionConstants.ROLE, ""
                        )
                    ).putExtra(
                        "storeId", prefs.getString(
                            SessionConstants.STOREID, GPSService.mLastLocation?.latitude.toString()
                        )
                    )
                )
                prefs.put(SessionConstants.STORENAME, "")
                prefs.put(SessionConstants.STOREID, "")
            } else /*if (prefs.getString(
                    SessionConstants.STORENAME, GPSService.mLastLocation?.latitude.toString()
                ) == "Notice"
            )*/ {
                startActivity(
                    Intent(requireContext(), TenantNoticeBoardDetailsActivity::class.java).putExtra(
                        "from", prefs.getString(
                            SessionConstants.ROLE, ""
                        )
                    ).putExtra(
                        "viewId", prefs.getString(
                            SessionConstants.STOREID, GPSService.mLastLocation?.latitude.toString()
                        )
                    )
                )
                prefs.put(SessionConstants.STORENAME, "")
                prefs.put(SessionConstants.STOREID, "")
            }
        }
    }

    private fun lstnr() {
//        binding.userHomeToolbar.tvLang.setOnClickListener {
//            binding.userHomeToolbar.langSp.performClick()
//        }
        binding.userHomeToolbar.ivNoti.setOnClickListener {
            startActivity(Intent(requireContext(),TenantNotificationActivity::class.java))
        }
        binding.edtSearch.setOnClickListener {
            startActivity(
                Intent(requireContext(), UserFilterActivity::class.java).putExtra(
                    "from", "owner"
                )
            )
        }
        binding.imageView103.setOnClickListener {
            prefs.put(SessionConstants.NAME, "owner")
            startActivity(Intent(requireContext(), Sell_RentActivity::class.java))
        }
        binding.constraintLayout22.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(), OwnerTenantRegularEntryActivity::class.java
                )
            )
        }
        binding.imageView43.setOnClickListener {
            startActivity(Intent(requireContext(), OwnerVisitorHistoryActivity::class.java))
        }
        binding.textView87.setOnClickListener {
            startActivity(Intent(requireContext(), OwnerVisitorHistoryActivity::class.java))
        }
        binding.userHomeToolbar.ivMenu.setOnClickListener {
            /* startActivity(
                 Intent(requireContext(), ProfileActivity::class.java).putExtra(
                     "from", "owner"
                 ).putExtra("projectId", projectId)
             )*/
            drw?.openDrawer(GravityCompat.START)
        }
        binding.textView89.setOnClickListener {
            startActivity(
                Intent(requireContext(), OwnerBillingActivity::class.java).putExtra(
                    "from", "owner"
                )
            )
        }
        binding.userHomeToolbar.constraintLayout21.setOnClickListener {
            flat_BottomSheet()
        }
       /* binding.userHomeToolbar.imageView69.setOnClickListener {
            flat_BottomSheet()
        }*/
//        binding.userHomeToolbar.ivhomeChat.setOnClickListener {
//            startActivity(
//                Intent(requireContext(), OwnerChatActivity::class.java)
//            )
//        }
        binding.imageView79.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(), TenantWritePostActivity::class.java
                ).putExtra("from", "ownerHome")
            )
        }
    }

    private fun initialize() {
        LinearSnapHelper().attachToRecyclerView(binding.viewPager1)
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]

        val ownerModel = OwnerHomeRepo(BaseApplication.apiService)
        owner_viewModel = ViewModelProvider(
            this, OwnerHomeFactory(ownerModel)
        )[OwnerHomeViewModel::class.java]

        val sideownerModel = OwnerSideRepo(BaseApplication.apiService)
        side_owner_viewModel = ViewModelProvider(
            this, OwnerSideFactory(sideownerModel)
        )[OwnerSideViewModel::class.java]


    }

    private fun getUserAdvertisementList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        owner_viewModel.ownerAdvertisement(token, buildingIdForAdvertisement)
    }

    private fun getOwnerDetails() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        owner_viewModel.ownerDetails(token)

    }

    private fun getOwnerCommunityList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        /*  val model = OwnerCommunityPostModel(projectId,null)*/
        owner_viewModel.ownerCommunity(token, hashMapOf("projectId" to projectId))
    }

    private fun getCommentList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        val model = OwnerGetCommentPostModel(postid)
        side_owner_viewModel.getCommentOwnerList(token, model)
    }

    /*private fun unPaidList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        side_owner_viewModel.unPaidOwnerList(token, "Unpaid")
    }*/

    /*private fun regularVisitorHistoryList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        side_owner_viewModel.regularVisitorHistoryOwner(token, "Ongoing", get_flatId)
    }*/

    private fun ownerSingleVisitorHistoryList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        side_owner_viewModel.singleVisitorHistoryOwner(token, "Active", get_flatId)
    }

    private fun viewPostCountOwner() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )

        owner_viewModel.viewPostCountOwner(token, viewPostCountId)
    }

    private fun observer() {
        owner_viewModel.ownerAdvertisementLiveData.observe(requireActivity(), Observer {
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
                            addList.addAll(it.data[0].advertisementData)
                            println("======ddydy$addList")
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
                        } else if (it.status == AppConstants.STATUS_404) {
                            binding.viewPager1.visibility = View.GONE
                            binding.tabLayout1.visibility = View.GONE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
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
        owner_viewModel.ownerDetailsLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            homeFlatList.clear()
                            homeFlatList.addAll(it.data.userData)
                            println("----List$homeFlatList")
                            if (it.data.userDetails.fullName.isNullOrEmpty()) {
                                binding.userHomeToolbar.tvTittle.text = getString(R.string.hi_guest)
                                binding.userHomeToolbar.tvHomeDes.text = prefs.getString(
                                    SessionConstants.KADDRESS,
                                    GPSService.mLastLocation!!.latitude.toString()
                                )
                            } else {
                                binding.userHomeToolbar.imageView59.loadImagesWithGlideExt(it.data.userDetails.profilePic)
                                binding.userHomeToolbar.imageView69.visibility = View.VISIBLE
                                binding.userHomeToolbar.tvTittle.text = it.data.userDetails.fullName
                                /*if (!it.data.userData.get(0).is_home) {
                                    flat_BottomSheet()
                                }*/
                                var isHome = true
                                it.data.userData.forEach {
                                    if (it.is_home) {
                                        isHome = false
                                    }
                                }
                                if (isHome) {
                                    flat_BottomSheet()
                                }
                                /* if (!it.data.userData.isNullOrEmpty()) {
                                     binding.userHomeToolbar.tvHomeDes.text =
                                         it.data.userData[0].name ?: ""
                                     projectId = it.data.userData[0].buildingId.projectId._id
                                     get_flatId = it.data.userData[0]._id
                                     println("----flatID$get_flatId")
                                     prefs.put(
                                         SessionConstants.FLATID,
                                         it.data.userData[0]._id
                                     )
                                     prefs.put(
                                         SessionConstants.PROJECTID,
                                         it.data.userData[0].buildingId.projectId._id
                                     )
                                     prefs.put(
                                         SessionConstants.USERDATAID, it.data.userData[0]._id
                                     )
                                     println("----userDataId${it.data.userData[0]._id}")
                                     buildingIdForAdvertisement = it.data.userData[0].buildingId._id
                                     prefs.put(
                                         SessionConstants.BUILDINGID, it.data.userData[0].buildingId._id
                                     )
                                     println("-----buildingId${it.data.userData[0].buildingId._id}")
                                     userId = it.data.userDetails._id
                                     prefs.put(
                                         SessionConstants.USERID, it.data.userDetails._id
                                     )
                                     println("----userId$userId")
                                     prefs.put(
                                         SessionConstants.PROFILEPIC, it.data.userData[0].owner.profilePic
                                     )
                                     println("-----pppject${projectId}")
                                 }*/

                                binding.userHomeToolbar.tvHomeDes.text =
                                    it.data.userData[0].name ?: ""
                                projectId = it.data.userData[0].buildingId.projectId._id
                                prefs.put(
                                    SessionConstants.PROJECTID,
                                    it.data.userData[0].buildingId.projectId._id

                                )
                                get_flatId = it.data.userData[0]._id
                                println("----flatID$get_flatId")
                                prefs.put(
                                    SessionConstants.FLATID,
                                    it.data.userData[0]._id
                                )
                                prefs.put(
                                    SessionConstants.USERDATAID, it.data.userData[0]._id
                                )
                                println("----userDataId${it.data.userData[0]._id}")
                                buildingIdForAdvertisement = it.data.userData[0].buildingId._id
                                prefs.put(
                                    SessionConstants.BUILDINGID, it.data.userData[0].buildingId._id
                                )
                                println("-----buildingId${it.data.userData[0].buildingId._id}")
                                userId = it.data.userDetails._id
                                prefs.put(
                                    SessionConstants.USERID, it.data.userDetails._id
                                )
                                println("----userId$userId")
                                prefs.put(
                                    SessionConstants.PROFILEPIC,
                                    it.data.userData[0].owner.profilePic
                                )
                                println("-----pppject${projectId}")

                            }
                            prefs.setBoolean(
                                SessionConstants.SUBSCRIPTION,
                                it.data.userDetails.subscription_active
                            )
                            for (iss in it.data.userData) {
                                if (iss.is_home) {
                                    binding.userHomeToolbar.tvHomeDes.text =
                                        iss.name ?: ""
                                    println("=======${iss.name ?: ""}")
                                    projectId = iss.buildingId.projectId._id
                                    prefs.put(
                                        SessionConstants.PROJECTID,
                                        iss.buildingId.projectId._id

                                    )
                                    prefs.put(
                                        SessionConstants.USERDATAID, iss._id
                                    )
                                }
                            }
                            test()
                            /*regularVisitorHistoryList()*/
                            getUserAdvertisementList()
                            ownerSingleVisitorHistoryList()
                            getOwnerCommunityList()
                        } else if (it.status == AppConstants.STATUS_404) {
                            // requireActivity().longToast(it.message)
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
        owner_viewModel.likeownerCommunityLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {

                        } else if (it.status == AppConstants.STATUS_404) {

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
        owner_viewModel.ownerCommunityLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            communityList.clear()
                            communityList.addAll(it.data)
                            binding.rcyPosting.layoutManager = LinearLayoutManager(
                                requireContext()
                            )
                            posting_adptr =
                                OwnerPostingAdapter(requireContext(), communityList, this, this)
                            binding.rcyPosting.adapter = posting_adptr
                            posting_adptr!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            //requireActivity().longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                }
                else -> {}
            }
        })
        side_owner_viewModel.getCommentOwnerListLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList.clear()
                            getCommentList.addAll(it.data)
                            commentAdapter!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            // requireContext().longToast(it.message)
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
        side_owner_viewModel.commentPostOwnerListLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList()
                            getOwnerCommunityList()
                            commentAdapter!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            //  requireContext().longToast(it.message)
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
        side_owner_viewModel.unPaidOwnerListLiveData.observe(requireActivity(),
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(requireActivity())
                    }

                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {

                                owner_unpaidList.clear()
                                owner_unpaidList.addAll(it.data.result.take(3))
                                binding.rcyUnpadBill.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        RecyclerView.HORIZONTAL,
                                        false
                                    )
                                unPaidAdapter = OwnerUnPaidAdapter(
                                    requireContext(), owner_unpaidList, "", this, ""
                                )
                                binding.rcyUnpadBill.adapter = unPaidAdapter
                                unPaidAdapter!!.notifyDataSetChanged()


                            } else if (it.status == AppConstants.STATUS_500) {
                                requireContext().longToast(it.message)
                            } else if (it.status == AppConstants.STATUS_404) {
                                // requireContext().longToast(it.message)
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
        /*side_owner_viewModel.regularVisitorHistoryOwnerLiveData.observe(
            requireActivity(),
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(requireActivity())
                    }

                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                list.clear()
                                list.addAll(it.data.result)
                                binding.rcyViewAll.visibility = View.VISIBLE
                                binding.tvNoActiveVisitor.visibility = View.INVISIBLE
                                binding.rcyViewAll.layoutManager = LinearLayoutManager(
                                    requireContext(), RecyclerView.HORIZONTAL, false
                                )
                                visitorAdapter = TenantHomeVisitorAdapter(requireContext(), list)
                                binding.rcyViewAll.adapter = visitorAdapter
                                visitorAdapter!!.notifyDataSetChanged()

                            } else if (it.status == AppConstants.STATUS_404) {
                                requireContext().longToast(it.message)
                            } else if (it.status == AppConstants.STATUS_FAILURE) {
                                //requireContext().longToast(it.message)
                                binding.rcyViewAll.visibility = View.INVISIBLE
                                binding.tvNoActiveVisitor.visibility = View.VISIBLE
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
            })*/
        side_owner_viewModel.singleVisitorHistoryOwnerLiveData.observe(
            requireActivity(),
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(requireActivity())
                    }
                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                list.clear()
                                it.data.forEach {
                                    if (it.visitorStatus.equals("Accept")) {
                                        list.add(it)
                                    }
                                }
                                binding.rcyViewAll.visibility = View.VISIBLE
                                if (list.isEmpty()) {
                                    binding.tvNoActiveVisitor.visibility = View.VISIBLE
                                } else {
                                    binding.tvNoActiveVisitor.visibility = View.INVISIBLE
                                }

                                binding.rcyViewAll.layoutManager = LinearLayoutManager(
                                    requireContext(), RecyclerView.HORIZONTAL, false
                                )
                                visitorAdapter = TenantHomeVisitorAdapter(requireContext(), list)
                                binding.rcyViewAll.adapter = visitorAdapter
                                visitorAdapter!!.notifyDataSetChanged()
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
        owner_viewModel.viewPostCountOwnerLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(
                                    requireContext(), ViewPostDetailsActivity::class.java
                                ).putExtra(
                                    "postId", postid
                                ).putExtra("from", "ownerHome")
                            )
                        } else if (it.status == AppConstants.STATUS_404) {

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

        side_owner_viewModel.deleteCommentOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList()
                            getOwnerCommunityList()
                            commentAdapter!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
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
        side_owner_viewModel.editCommentOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList()
                            getOwnerCommunityList()
                            commentAdapter!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
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
        owner_viewModel.setAsHomeOwnerLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
//                            getOwnerFlatList()
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
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
    }

    override fun onClick(position: Int) {
        when (position) {
            0 -> {
                startActivity(
                    Intent(
                        requireContext(), TenantRegisterComplainActivity::class.java
                    ).putExtra("from", "owner")
                )
            }
            1 -> {
//                flat_BottomSheet()
                startActivity(
                    Intent(requireContext(), OwnerBillingActivity::class.java).putExtra(
                        "from", "owner"
                    )
                )
            }
            2 -> {
                startActivity(Intent(requireContext(), TenantGateKepperActivity::class.java))
            }
            3 -> {
                startActivity(
                    Intent(
                        requireContext(), TenantNoticBoardActivity::class.java
                    ).putExtra("from", "owner")
                )
            }
        }

    }

    private fun flat_BottomSheet() {
        flat_by_bottom = OwnerFlatBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme).apply {
                setContentView(flat_by_bottom.root)

                flat_by_bottom.rcyFlat.layoutManager = LinearLayoutManager(requireContext())
                homeFlatAdapter = HomeFlatAdpter(
                    requireContext(),
                    homeFlatList,
                    this@OwnerHomeFragment


                )
                flat_by_bottom.rcyFlat.adapter = homeFlatAdapter
                homeFlatAdapter!!.notifyDataSetChanged()
                var isHome = false
                homeFlatList.forEach {
                    if (it.is_home) {
                        isHome = true
                    }
                }
                if (!isHome) {
                    flat_by_bottom.tvClose.visibility = View.INVISIBLE
                } else {
                    flat_by_bottom.tvClose.visibility = View.VISIBLE
                }
                flat_by_bottom.tvClose.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

            }
        bottomSheetDialog.show()
    }

    override fun onPostCLick(position: Int, id: String, postStatus: Boolean, flatName: String) {
        postid = id
        flatNameforComment = flatName
        getCommentList()
        println("----postID${postid}")
        comment_BottomSheet(postStatus)

    }

    override fun isLike(
        likedBy: String,
        postBy: String,
        status: String,
        position: Int,
        myLikeStatus: Boolean,
        flatName: String
    ) {
        var count = communityList[position].likesCount ?: 0
        if (status == "dislike") {
            count--
            communityList[position].likesCount = count
        } else {
            count++
            communityList[position].likesCount = count
        }

        communityList[position].myLikeStatus = myLikeStatus
        binding.rcyPosting.layoutManager = LinearLayoutManager(
            requireContext()
        )
        posting_adptr = OwnerPostingAdapter(requireContext(), communityList, this, this)
        binding.rcyPosting.adapter = posting_adptr
        posting_adptr!!.notifyDataSetChanged()
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        val model = OwnerLikeCommunityPostModel(likedBy, postBy, status, flatName)
        owner_viewModel.likeownerCommunity(token, model)
    }

    override fun viewDetailCommunityOwner(postId: String) {
//        postid = postId
//        viewPostCountId = postId
//        viewPostCountOwner()
    }

    private fun comment_BottomSheet(postStatus: Boolean) {
        getCommentList()
        comment_by_bottom =
            TenanatCommentBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        bottomSheetDialog1 =
            BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme).apply {
                setContentView(comment_by_bottom.root)
                comment_by_bottom.rcyComment.layoutManager = LinearLayoutManager(requireContext())
                commentAdapter = TenantCommentAdapter(
                    requireContext(),
                    getCommentList,
                    postStatus,
                    this@OwnerHomeFragment

                )
                comment_by_bottom.rcyComment.adapter = commentAdapter
                commentAdapter!!.notifyDataSetChanged()

                comment_by_bottom.imageView30.setOnClickListener {
                    bottomSheetDialog1.dismiss()
                }
                comment_by_bottom.imageView8.setOnClickListener {
                    val data = comment_by_bottom.edName.text.trim().toString()
                    if (data.isNotEmpty()) {
                        val token = prefs.getString(
                            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
                        )
                        val model = OwnerCommentPostModel(
                            comment_by_bottom.edName.text.trim().toString(),
                            userId,
                            postid,
                            flatNameforComment
                        )
                        side_owner_viewModel.commentPostOwnerList(token, model)
                        comment_by_bottom.edName.setText("")
                    } else {
                        Toast.makeText(
                            requireContext(), "Please Enter Comment!!", Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        bottomSheetDialog1.show()
    }

    override fun onOwnerClick(id: String, position: Int) {
        startActivity(
            Intent(
                requireContext(), TenantPaymentActivity::class.java
            ).putExtra("billingId", id).putExtra("from", "owner")
                .putExtra("ownerBillList", owner_unpaidList[position])
        )
    }

    override fun onNotifyOwner(id: String, position: Int) {

    }

    override fun onViewReceipt(refno: String, uploadDocument: String) {

    }

    override fun onReject(billID: String) {

    }

    override fun onOwnertoTenantNotify(billID: String, position: Int) {

    }

    override fun onResume() {
        super.onResume()
        getOwnerDetails()
        getOwnerCommunityList()
        /*if (hasAccessFineLocationPermissions(requireContext())) {
            if (CommonUtil.checkGPS(requireContext())) {
                GPSService(requireContext(), this)
                getOwnerDetails()
                regularVisitorHistoryList()
                getOwnerCommunityList()
            }
        }
        checkLocationPermissions()*/

    }

    override fun showImg(img: String) {
        showImg = img as ArrayList<String>
        dialogProile()
    }



    private fun dialogProile() {
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_profile_owner)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
      /*  val viewPager = dialog.findViewById<ViewPager>(R.id.view_pager1)
        val tab = dialog.findViewById<TabLayout>(R.id.tabLayout1)*/
        val imgGallery = dialog.findViewById<ImageView>(R.id.ivProImg)
        imgGallery.loadImagesWithGlideExt(showImg.toString())
//        viewPager.adapter =
//            UserPropertyViewPagerAdapter(requireContext(), showImg)
//        tab.setupWithViewPager(viewPager, true)
//        val runnable = Runnable {
//            if (currentPos == showImg.size - 1) currentPos = 0
//            else currentPos++
//            if (viewPager != null) {
//                viewPager.setCurrentItem(currentPos, true)
//            }
//        }
//
//        Timer().schedule(object : TimerTask() {
//            override fun run() {
//                handler.post(runnable)
//            }
//        }, CommonUtil.DELAY_MS, CommonUtil.PERIOD_MS)
//

        dialog.show()

    }

    override fun onClickFlat(
        name: String,
        projectgetId: String,
        seletedPostion: Int,
        flatId: String,
        buildingId: String
    ) {
        sendSeletedPostion = seletedPostion
        projectId = projectgetId
        println("---projectId$projectId")
        println("---ClickbuildingId$buildingId")
        get_flatId = flatId
        buildingIdForAdvertisement = buildingId
        binding.userHomeToolbar.tvHomeDes.text = name
        getUserAdvertisementList()
        getOwnerCommunityList()
        ownerSingleVisitorHistoryList()
        setMyHome()
        //regularVisitorHistoryList()
        bottomSheetDialog.dismiss()

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

    private fun buildAlertMessageNoGps(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { dialog, id ->
                val i = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts(
                        "package",
                        context!!.packageName, null
                    )
                )

                startActivityForResult(i, 1001)
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

    override fun onLocationUpdate(latitude: Double, longitude: Double) {

    }

    private fun alertMessageNoGps() {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.alert_message_no_gps_popup)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val tvYes = dialog.findViewById<TextView>(R.id.tvYes)
        tvYes.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

        }


        dialog.show()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RESULT_PERMISSION_LOCATION) {
            Log.w("TAG", "onRequestPermissionsResult: ")
            if (hasAccessFineLocationPermissions(requireContext())) {
                if (CommonUtil.checkGPS(requireContext())) {
                    GPSService(requireContext(), this)
                }
            } else {
                buildAlertMessageNoGps(getString(R.string.location))

            }
        }
    }

    private fun checkLocationPermissions() {
        if (!hasAccessFineLocationPermissions(requireContext())) {
            buildAlertMessageNoGps(getString(R.string.location))
        } else if (!CommonUtil.checkGPS(requireContext())) {
            alertMessageNoGps()
        }
    }

    override fun commentEdit(position: Int, commentId: String, comment: String) {
        editPopup(commentId, comment)
    }

    override fun commentDelete(position: Int, commentId: String) {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        side_owner_viewModel.deleteCommentOwner(token, commentId, postid)
    }

    private fun editPopup(commentId: String, comment: String) {
        val dialog = this.let { Dialog(requireContext()) }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.edit_comment_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val editData = dialog.findViewById<EditText>(R.id.editText)
        val okBtn = dialog.findViewById<TextView>(R.id.tvOkComment)
        val cancelBtn = dialog.findViewById<TextView>(R.id.tvCancelComment)
        editData.setText(comment)
        okBtn.setOnClickListener {
            dialog.dismiss()
            val token = prefs.getString(
                SessionConstants.TOKEN,
                ""
            )
            val model = EditCommentPostModel(
                editData.text.trim().toString(),
                commentId,
                userId,
                postid
            )
            side_owner_viewModel.editCommentOwner(token, model)
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }


        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun setMyHome() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        owner_viewModel.setAsHomeOwner(token, get_flatId)
    }

    inner class VideoAdapter(
        val con: Context,
        val list: ArrayList<OwnerAdvertisementRes.Data.AdvertisementData>,
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
                viewBinding.imageView52.setOnClickListener {
                    val openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(list[position].url)
                    con.startActivity(openURL)
                }
            }
        }

        override fun onViewAttachedToWindow(holder: VideoViewHolder) {
            super.onViewAttachedToWindow(holder)
            holder.apply {
                simpleExoPlayer?.volume = 0f
                val old = list[position].image
                println("====orl$old")
                if (old.contains("mp4")) {
                    viewBinding.videoView.visibility = View.VISIBLE
                    viewBinding.imageView52.visibility = View.INVISIBLE
                    viewBinding.ivMute.visibility = View.VISIBLE
                    viewBinding.ivunMute.visibility = View.INVISIBLE
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
                    viewBinding.ivMute.visibility = View.INVISIBLE
                    viewBinding.ivunMute.visibility = View.INVISIBLE
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


