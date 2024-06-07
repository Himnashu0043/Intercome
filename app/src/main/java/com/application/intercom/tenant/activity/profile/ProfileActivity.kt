package com.application.intercom.tenant.activity.profile

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.CommonActivity
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.getUserDetailsViewModel.GetUserDetailsViewModel
import com.application.intercom.data.model.factory.getUserDetailsFactory.GetUserDetailsFactory
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.getUserDetailsRepo.GetUserDetailsRepo
import com.application.intercom.databinding.ActivityProfileBinding
import com.application.intercom.databinding.MyPropertyBottomSheetBinding
import com.application.intercom.gatekepper.activity.createGatePass.CreateGatePassActivity
import com.application.intercom.gatekepper.activity.newFlow.VisitorHistoryTypeActivity
import com.application.intercom.gatekepper.activity.newFlow.myShift.MyShiftGateKeeperActivity
import com.application.intercom.gatekepper.activity.newFlow.regularEntry.RegularEntryActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntry.SingleEntryActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.manager.bills.MyBillingsActivity
import com.application.intercom.manager.complaint.RegisterComplaintsActivity
import com.application.intercom.manager.gatekeeper.ManagerGateKeeperActivity
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.manager.notice.NoticeBoardActivity
import com.application.intercom.manager.service_charge.ServiceChargeActivity
import com.application.intercom.manager.service_charge.ServiceChargesDetailsActivity
import com.application.intercom.owner.activity.OwnerParking.OwnerParkingActivity
import com.application.intercom.owner.activity.helpSupport.OwnerHelpSupportActivity
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.activity.ownerProperty.OwnerPropertyActivity
import com.application.intercom.owner.activity.ownerVisitor.OwnerVisitorActivity
import com.application.intercom.owner.activity.ownerbilling.OwnerBillingActivity
import com.application.intercom.owner.activity.registerComplain.OwnerAddRegisterComplainActivity
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.MyCommunity.TenantMyCommunityActivity
import com.application.intercom.tenant.activity.billing.TenantBillingsActivity
import com.application.intercom.tenant.activity.gateKepper.TenantGateKepperActivity
import com.application.intercom.tenant.activity.help.TenantSecondHelpActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.tenant.activity.noticBoard.SecondTenantNoticeBoardActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticBoardActivity
import com.application.intercom.tenant.activity.registerComplain.TenantAddRegisterComplainActivity
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.activity.visitor.TenantVisitorActivity
import com.application.intercom.tenant.adapter.MyPropertyAdapter
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.user.community.CommunityActivity
import com.application.intercom.user.contact.ContactIntercomActivity
import com.application.intercom.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileActivity : AppCompatActivity(), ProfileAdapter.ProfileClick,
    GestureDetector.OnGestureListener {
    lateinit var binding: ActivityProfileBinding
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null
    private var from: String = ""
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var property_by_bottom: MyPropertyBottomSheetBinding
    private var propertyAdapter: MyPropertyAdapter? = null
    private lateinit var viewModel: GetUserDetailsViewModel
    private var email: String = ""
    private var number: String = ""
    private var name: String = ""
    private var url: String = ""
    private var projectId: String = ""
    private lateinit var gestureDetector: GestureDetector
    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gestureDetector = GestureDetector(this)
        from = intent.getStringExtra("from").toString()
        projectId = intent.getStringExtra("projectId").toString()

        println("----from$from")
        println("----projectId$projectId")
        projectId = prefs.getString(
            SessionConstants.PROJECTID,
            GPSService.mLastLocation?.latitude.toString()
        )
        initView()
        lstnr()
        CommonUtil.themeSet(this, window)
    }

    private fun initView() {
        initialize()
        getUserDetails()
        observer()
        if (from == "owner") {
            profile_list.add(ProfileModal(R.drawable.home_icon, "Home"))
            profile_list.add(ProfileModal(R.drawable.property_icon, "Property"))
            profile_list.add(ProfileModal(R.drawable.parking_icon, "Parking"))
            profile_list.add(ProfileModal(R.drawable.service_icon, "Services"))
            profile_list.add(ProfileModal(R.drawable.property_icon, "My Property"))
            profile_list.add(ProfileModal(R.drawable.parking_icon, "My Parking"))
            profile_list.add(ProfileModal(R.drawable.visitor_icon, "My Activity"))
            profile_list.add(ProfileModal(R.drawable.community_icon, "My Community"))
            profile_list.add(ProfileModal(R.drawable.billing_icon, "Complaint"))
            profile_list.add(ProfileModal(R.drawable.community_icon, "My Billing"))
            profile_list.add(ProfileModal(R.drawable.visitor_icon, "Visitors"))
            profile_list.add(ProfileModal(R.drawable.notics_icon, "Notice Board"))
            profile_list.add(ProfileModal(R.drawable.help_icon, "Help & Support"))
        } else if (from == "manager") {
            profile_list.add(ProfileModal(R.drawable.home_icon, "Home"))
            profile_list.add(ProfileModal(R.drawable.property_icon, "Property"))
            profile_list.add(ProfileModal(R.drawable.parking_icon, "Parking"))
            profile_list.add(ProfileModal(R.drawable.service_icon, "Services"))
            profile_list.add(ProfileModal(R.drawable.community_icon, "Manage Complains"))
            profile_list.add(ProfileModal(R.drawable.billing_icon, "Manage Billings"))
           // profile_list.add(ProfileModal(R.drawable.visitor_icon, "Manage Visitors & Gatepass"))
            profile_list.add(ProfileModal(R.drawable.service_icon, "Service Charges"))
            profile_list.add(ProfileModal(R.drawable.notics_icon, "Notice Board"))
            profile_list.add(ProfileModal(R.drawable.help_icon, "Help & Support"))
        } else if (from == "user") {
            profile_list.add(ProfileModal(R.drawable.home_icon, "Home"))
            profile_list.add(ProfileModal(R.drawable.property_icon, "Property"))
            profile_list.add(ProfileModal(R.drawable.parking_icon, "Parking"))
            profile_list.add(ProfileModal(R.drawable.service_icon, "Services"))
            profile_list.add(ProfileModal(R.drawable.visitor_icon, "MyActivity"))
            profile_list.add(ProfileModal(R.drawable.community_icon, "My Community"))
//            profile_list.add(ProfileModal(R.drawable.complaint_icon, "Complaint"))
            profile_list.add(ProfileModal(R.drawable.billing_icon, "My Billings"))
            profile_list.add(ProfileModal(R.drawable.visitor_icon, "Visitors"))
            profile_list.add(ProfileModal(R.drawable.notics_icon, "Notice Board"))
            profile_list.add(ProfileModal(R.drawable.help_icon, "Help & Support"))
        } else if (from == "newGateKeeper") {
            profile_list.add(ProfileModal(R.drawable.property_icon, "Single Entry"))
            profile_list.add(ProfileModal(R.drawable.parking_icon, "Regular Entry"))
            profile_list.add(ProfileModal(R.drawable.service_icon, "Gate Pass"))
            profile_list.add(ProfileModal(R.drawable.community_icon, "Parkings"))
            profile_list.add(ProfileModal(R.drawable.visitor_icon, "Visitor History"))
            profile_list.add(ProfileModal(R.drawable.service_icon, "My Shift"))
            profile_list.add(ProfileModal(R.drawable.notics_icon, "Notice Board"))
            profile_list.add(ProfileModal(R.drawable.help_icon, "Help & Support"))
        } else {
            profile_list.add(ProfileModal(R.drawable.home_icon, "Home"))
            profile_list.add(ProfileModal(R.drawable.property_icon, "Property"))
            profile_list.add(ProfileModal(R.drawable.parking_icon, "Parking"))
            profile_list.add(ProfileModal(R.drawable.service_icon, "Services"))
            profile_list.add(ProfileModal(R.drawable.visitor_icon, "MyActivity"))
            profile_list.add(ProfileModal(R.drawable.community_icon, "My Community"))
            profile_list.add(ProfileModal(R.drawable.complaint_icon, "Complaint"))
            profile_list.add(ProfileModal(R.drawable.billing_icon, "My Billings"))
            profile_list.add(ProfileModal(R.drawable.visitor_icon, "Visitors"))
            profile_list.add(ProfileModal(R.drawable.notics_icon, "Notice Board"))
            profile_list.add(ProfileModal(R.drawable.help_icon, "Help & Support"))
        }


        binding.rcyProfile.layoutManager = LinearLayoutManager(this)
        profileAdapter = ProfileAdapter(this, profile_list, from, this)
        binding.rcyProfile.adapter = profileAdapter
        profileAdapter!!.notifyDataSetChanged()

    }


    private fun lstnr() {
        binding.tvTerms.setOnClickListener {
            startActivity(Intent(this, TermsOfServiceActivity::class.java))
        }
        binding.tvPrivacyPolicy.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }
        binding.imageView4.setOnClickListener {
            if (from.equals("user")) {
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            } else {
                finish()
            }

        }
        binding.tvSetting.setOnClickListener {
            startActivity(Intent(this, TenantSettingActivity::class.java))
        }
        binding.imageView5.setOnClickListener {
            startActivity(
                Intent(this, TenantSecondProfileActivity::class.java).putExtra(
                    "name",
                    name
                ).putExtra("email", email).putExtra("number", number).putExtra("from", from)
                    .putExtra("url", url)
            )
        }


    }

    private fun initialize() {
        val repo = GetUserDetailsRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this,
            GetUserDetailsFactory(repo)
        )[GetUserDetailsViewModel::class.java]


    }

    private fun getUserDetails() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.userDetails(token)

    }

    private fun observer() {
        viewModel.userDetailsLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            if (it.data.userDetails.fullName.isNullOrEmpty()) {
                                binding.textView21.text = "Guest User"
                            } else {
                                url = it.data.userDetails.profilePic
                                name = it.data.userDetails.fullName
                                email = it.data.userDetails.email?:""
                                number = it.data.userDetails.phoneNumber
                                binding.textView21.text = it.data.userDetails.fullName
                                binding.imageView5.loadImagesWithGlideExt(it.data.userDetails.profilePic)
                            }
//                            url = it.data.userDetails.profilePic
//                            name = it.data.userDetails.fullName
//                            email = it.data.userDetails.email
//                            number = it.data.userDetails.phoneNumber
//                            binding.textView21.text = it.data.userDetails.fullName
//                            binding.imageView5.loadImagesWithGlideExt(it.data.userDetails.profilePic)

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

    override fun onClick(position: Int) {
        if (from == "user") {
//            binding.tvPrivacyPolicy.setOnClickListener {
//                startActivity(Intent(this, PrivacyPolicyActivity::class.java))
//
//            }
//            binding.tvPrivacyPolicy.setOnClickListener {
//                startActivity(Intent(this, TermsOfServiceActivity::class.java))
//
//            }
            when (position) {
                0 -> {
                    startActivity(
                        Intent(this, MainActivity::class.java).putExtra(
                            "from",
                            "from_side_home"
                        )
                    )
                    /*startActivity(
                        Intent(this, MainActivity::class.java).putExtra(
                            "from",
                            "from_side_property"
                        )
                    )*/
                }
                1 -> {
                    startActivity(
                        Intent(this, MainActivity::class.java).putExtra(
                            "from",
                            "from_side_property"
                        )
                    )

                }
                2 -> {
                    startActivity(
                        Intent(this, MainActivity::class.java).putExtra(
                            "from",
                            "from_side_parking"
                        )
                    )

                }
                3 -> {
                    startActivity(
                        Intent(this, MainActivity::class.java).putExtra(
                            "from",
                            "from_side_service"
                        )
                    )

                }
                4 -> {
                    /*startActivity(
                        Intent(this, CommunityActivity::class.java).putExtra(
                            "from",
                            "billing"
                        )
                    )*/
                    startActivity(
                        Intent(this, MainActivity::class.java).putExtra(
                            "from",
                            "from_property_details"
                        )
                    )

                }
                5 -> {
                    startActivity(
                        Intent(this, CommunityActivity::class.java)
                    )
                    /*startActivity(
                        Intent(this, CommunityActivity::class.java).putExtra(
                            "from",
                            "visitor"
                        )
                    )*/
                }
                6 -> {
                    startActivity(
                        Intent(this, CommunityActivity::class.java).putExtra(
                            "from",
                            "billing"
                        )
                    )
                    /*startActivity(
                        Intent(this, CommunityActivity::class.java).putExtra(
                            "from",
                            "notice"
                        )
                    )*/
                }
                7 -> {
                    startActivity(
                        Intent(this, CommunityActivity::class.java).putExtra(
                            "from",
                            "visitor"
                        )
                    )
                    /*startActivity(
                        Intent(this, ContactIntercomActivity::class.java)
                    )*/
                }
                8 -> {
                    startActivity(
                        Intent(this, CommunityActivity::class.java).putExtra(
                            "from",
                            "notice"
                        )
                    )

                }
                9 -> {
                    startActivity(
                        Intent(this, ContactIntercomActivity::class.java)
                    )
                }
            }

        } else if (from == "owner") {
            when (position) {
                0 -> {
                    startActivity(
                        Intent(this, OwnerMainActivity::class.java).putExtra(
                            "from",
                            "from_side_home"
                        )
                    )
                    /*startActivity(
                        Intent(this, OwnerPropertyActivity::class.java)
                    )*/
                    /*property_BottomSheet()*/
                }
                1 -> {
                    startActivity(
                        Intent(this, OwnerPropertyActivity::class.java)
                    )

                }
                2 -> {
                    startActivity(
                        Intent(this, OwnerParkingActivity::class.java)
                    )

                }
                3 -> {
                    startActivity(
                        Intent(this, OwnerMainActivity::class.java).putExtra(
                            "from",
                            "from_side_service"
                        )
                    )

                }
                4 -> {
                    startActivity(
                        Intent(this, OwnerMainActivity::class.java).putExtra(
                            "from",
                            "from_side_property"
                        )
                    )

                }
                5 -> {
                    startActivity(
                        Intent(this, OwnerMainActivity::class.java).putExtra(
                            "from",
                            "from_side_parking"
                        )
                    )

                }
                6 -> {
                    startActivity(
                        Intent(this, OwnerMainActivity::class.java).putExtra(
                            "from",
                            "from_property_details"
                        )
                    )
                    // startActivity(Intent(this, OwnerAddRegisterComplainActivity::class.java))

                }
                7 -> {
                    startActivity(
                        Intent(
                            this,
                            TenantMyCommunityActivity::class.java
                        ).putExtra("from", from).putExtra("projectId", projectId)
                    )
                    // startActivity(Intent(this, TenantBillingsActivity::class.java))

                }
                8 -> {
                    startActivity(Intent(this, OwnerAddRegisterComplainActivity::class.java))
                    //startActivity(Intent(this, TenantVisitorActivity::class.java))

                }
                9 -> {
                    startActivity(
                        Intent(this, OwnerBillingActivity::class.java).putExtra(
                            "from",
                            "owner"
                        )
                    )
                    /*startActivity(
                        Intent(
                            this,
                            TenantNoticBoardActivity::class.java
                        ).putExtra("from", from)
                    )*/
                }
                10 -> {
                    startActivity(Intent(this, OwnerVisitorActivity::class.java))
                    //startActivity(Intent(this, OwnerHelpSupportActivity::class.java))
                }
                11 -> {
                    startActivity(
                        Intent(
                            this,
                            TenantNoticBoardActivity::class.java
                        ).putExtra("from", from)
                    )
                }
                12 -> {
                    startActivity(Intent(this, OwnerHelpSupportActivity::class.java))
                }
            }
        } else if (from == "manager") {
            when (position) {
                0 -> {
                    startActivity(
                        Intent(this, ManagerMainActivity::class.java).putExtra(
                            "from",
                            "from_side_home"
                        )
                    )
                }
                1 -> {
                    startActivity(
                        Intent(this, ManagerMainActivity::class.java).putExtra(
                            "from",
                            "from_side_property"
                        )
                    )

                }
                2 -> {
                    startActivity(
                        Intent(this, ManagerMainActivity::class.java).putExtra(
                            "from",
                            "from_side_parking"
                        )
                    )

                }
                3 -> {
                    startActivity(
                        Intent(this, ManagerMainActivity::class.java).putExtra(
                            "from",
                            "from_side_service"
                        )
                    )

                }
                4 -> {
                    startActivity(
                        Intent(this, RegisterComplaintsActivity::class.java)
                    )

                }
                5 -> {
                    startActivity(
                        Intent(this, MyBillingsActivity::class.java)
                    )

                }
                6 -> {
                    startActivity(
                        Intent(this, ServiceChargeActivity::class.java)
                    )
                }
                7 -> {
                    startActivity(
                        Intent(this, NoticeBoardActivity::class.java)
                    )
                }
                8 -> {
                    startActivity(Intent(this, ContactIntercomActivity::class.java))

                }


            }
        } else if (from == "newGateKeeper") {
            when (position) {
                0 -> {
                    startActivity(Intent(this, SingleEntryActivity::class.java))
                }
                1 -> {
                    startActivity(Intent(this, RegularEntryActivity::class.java))
                }
                2 -> {
                    startActivity(Intent(this, CreateGatePassActivity::class.java))
                }
                3 -> {

                }
                4 -> {
                    startActivity(Intent(this, VisitorHistoryTypeActivity::class.java))
                }
                5 -> {
                    startActivity(Intent(this, MyShiftGateKeeperActivity::class.java))
                }
            }

        } else {
            when (position) {
                0 -> {
                    startActivity(
                        Intent(this, TenantMainActivity::class.java).putExtra(
                            "from",
                            "from_side_home"
                        )
                    )
                }
                1 -> {
                    startActivity(
                        Intent(this, TenantMainActivity::class.java).putExtra(
                            "from",
                            "from_side_property"
                        )
                    )

                }
                2 -> {
                    startActivity(
                        Intent(this, TenantMainActivity::class.java).putExtra(
                            "from",
                            "from_side_parking"
                        )
                    )

                }
                3 -> {
                    startActivity(
                        Intent(this, TenantMainActivity::class.java).putExtra(
                            "from",
                            "from_side_service"
                        )
                    )
                    /*startActivity(Intent(this, TenantMyCommunityActivity::class.java).putExtra("from", from).putExtra("projectId", projectId))*/
                }
                4 -> {
                    startActivity(
                        Intent(this, TenantMainActivity::class.java).putExtra(
                            "from",
                            "from_property_details"
                        )
                    )

                    /* startActivity(Intent(this, TenantAddRegisterComplainActivity::class.java).putExtra("from","tenant"))*/
                }
                5 -> {
                    startActivity(
                        Intent(
                            this,
                            TenantMyCommunityActivity::class.java
                        ).putExtra("from", from).putExtra("projectId", projectId)
                    )

                    /*startActivity(Intent(this, TenantBillingsActivity::class.java))*/
                }
                6 -> {
                    startActivity(
                        Intent(
                            this,
                            TenantAddRegisterComplainActivity::class.java
                        ).putExtra("from", "tenant")
                    )

                    /*startActivity(Intent(this, TenantVisitorActivity::class.java))*/
                }
                7 -> {
                    startActivity(
                        Intent(this, TenantBillingsActivity::class.java)
                    )

                    /* startActivity(Intent(this, TenantNoticBoardActivity::class.java)*//*.putExtra("from", from*///)
                }
                8 -> {
                    //startActivity(Intent(this, TenantVisitorActivity::class.java))
                    startActivity(
                        Intent(this, OwnerVisitorActivity::class.java).putExtra(
                            "from",
                            "tenant"
                        )
                    )


                }
                9 -> {
                    startActivity(Intent(this, TenantNoticBoardActivity::class.java))

                }
                10 -> {
                    startActivity(Intent(this, TenantSecondHelpActivity::class.java))
                }
            }
        }

    }

    private fun property_BottomSheet() {
        property_by_bottom = MyPropertyBottomSheetBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme).apply {
                setContentView(property_by_bottom.root)
                property_by_bottom.rcyProperty.layoutManager =
                    LinearLayoutManager(this@ProfileActivity)
                propertyAdapter = MyPropertyAdapter(this@ProfileActivity)
                property_by_bottom.rcyProperty.adapter = propertyAdapter
                propertyAdapter!!.notifyDataSetChanged()

                property_by_bottom.include.tv.text = "+ Add Property"

                property_by_bottom.include.tv.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }
                property_by_bottom.tvClose.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }
            }
        bottomSheetDialog.show()
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
        try {
            val diffY = p1.y - p0.y
            val diffX = p1.x - p0.x
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > swipeThreshold && Math.abs(p2) > swipeVelocityThreshold) {
                    if (diffX > 0) {

                    } else {
                        finish()

                    }
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return true
    }


}