package com.application.intercom.gatekepper.activity.newFlow.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.CommonActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerHomeViewModel.ManagerHomeViewModel
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.ViewModel.gateKeeperViewModel.GateKeeperHomeViewModel
import com.application.intercom.data.model.factory.gateKeeperFactory.GateKeeperFactory
import com.application.intercom.data.model.factory.managerFactory.managerHome.ManagerHomeFactory
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ServiceRepository
import com.application.intercom.data.repository.gateKeeperRepo.GateKeeperHomeRepo
import com.application.intercom.data.repository.managerRepo.managerHomeRepo.ManagerHomeRepo
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityNewGateKeeperMainBinding
import com.application.intercom.gatekepper.activity.createGatePass.CreateGatePassActivity
import com.application.intercom.gatekepper.activity.gatePass.SecondGatePassActivity
import com.application.intercom.gatekepper.activity.gatepassHistory.GatePassHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.VisitorHistoryTypeActivity
import com.application.intercom.gatekepper.activity.newFlow.myShift.MyShiftGateKeeperActivity
import com.application.intercom.gatekepper.activity.newFlow.ownerTenantRegularEntry.OwnerTenantRegularEntryActivity
import com.application.intercom.gatekepper.activity.newFlow.ownerTenantRegularEntryHistory.OwnerTenantRegularEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.regularEntry.RegularEntryActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntry.SingleEntryActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.manager.gatekeeper.CreateGateKepeerActivity
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.profile.ProfileActivity
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.user.service.ServiceFactory
import com.application.intercom.user.service.ServiceViewModel
import com.application.intercom.utils.*

class NewGateKeeperMainActivity : AppCompatActivity(), GestureDetector.OnGestureListener,
    ProfileAdapter.ProfileClick {
    lateinit var binding: ActivityNewGateKeeperMainBinding
    private lateinit var viewModel: GateKeeperHomeViewModel
    private lateinit var gestureDetector: GestureDetector
    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewGateKeeperMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gestureDetector = GestureDetector(this)
        CommonUtil.themeSet(this, window)
        initView()
        listener()
    }

    private fun initView() {
        initialize()
        observer()
        getManagerDetails()
        engSpiner()
        profile_list.add(ProfileModal(R.drawable.property_icon, "Single Entry"))
        profile_list.add(ProfileModal(R.drawable.parking_icon, "Regular Entry"))
        profile_list.add(ProfileModal(R.drawable.service_icon, "Gate Pass"))
        profile_list.add(ProfileModal(R.drawable.community_icon, "Parkings"))
        profile_list.add(ProfileModal(R.drawable.visitor_icon, "Visitor History"))
        profile_list.add(ProfileModal(R.drawable.service_icon, "My Shift"))
        profile_list.add(ProfileModal(R.drawable.notics_icon, "Notice Board"))
        profile_list.add(ProfileModal(R.drawable.help_icon, "Help & Support"))
        profile_list.add(ProfileModal(R.drawable.setting_icon, "Settings"))
        profile_list.add(ProfileModal(R.drawable.share_new_icon, "Share"))
        binding.nav.rcyProfile.layoutManager = LinearLayoutManager(this)
        profileAdapter = ProfileAdapter(this, profile_list, "tenant", this)
        binding.nav.rcyProfile.adapter = profileAdapter
        profileAdapter!!.notifyDataSetChanged()
    }

    private fun initialize() {
        val repo = GateKeeperHomeRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this, GateKeeperFactory(repo)
        )[GateKeeperHomeViewModel::class.java]


    }

    private fun getManagerDetails() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.gateKeeperDetails(token)

    }

    private fun observer() {
        viewModel.gateKeeperDetailsLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            if (it.data.userDetails.fullName.isNullOrEmpty()) {
                                binding.textView164.text = "Hi,Guest"
                                binding.textView165.text = prefs.getString(
                                    SessionConstants.KADDRESS,
                                    GPSService.mLastLocation!!.latitude.toString()
                                )

                            } else {
                                binding.textView164.text = it.data.userDetails.fullName
                                /*binding.textView165.text = prefs.getString(
                                    SessionConstants.KADDRESS,
                                    GPSService.mLastLocation?.latitude.toString()
                                )*/
                                binding.textView165.text = it.data.userDetails.buildingId.address

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

    }

    private fun listener() {
        binding.imageView86.setOnClickListener {
            /*startActivity(
                Intent(this, ProfileActivity::class.java).putExtra(
                    "from",
                    "newGateKeeper"
                )
            )*/
            binding.newGateKeepeerDrw.openDrawer(GravityCompat.START)
        }
        binding.nav.tvEnglish.setOnClickListener {
            binding.nav.tvBl.visibility = View.VISIBLE
            binding.nav.tvEnglish.visibility = View.INVISIBLE
        }
        binding.nav.tvBl.setOnClickListener {
            binding.nav.tvBl.visibility = View.INVISIBLE
            binding.nav.tvEnglish.visibility = View.VISIBLE
        }
        binding.nav.tvPrivacyPolicy.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }
        binding.nav.tvTerms.setOnClickListener {
            startActivity(Intent(this, TermsOfServiceActivity::class.java))
        }
        binding.tvLang.setOnClickListener {
            binding.langSp.performClick()
        }
        binding.imageView88.setOnClickListener {
            startActivity(Intent(this, SingleEntryActivity::class.java))
        }
        binding.ivVisitorHis.setOnClickListener {
            startActivity(Intent(this, VisitorHistoryTypeActivity::class.java))
        }
        binding.ivRegularEntry.setOnClickListener {
            startActivity(Intent(this, RegularEntryActivity::class.java))
        }
        binding.ivgatePass.setOnClickListener {
            startActivity(Intent(this, CreateGatePassActivity::class.java))
        }
        binding.ivgatePasshis.setOnClickListener {
            //startActivity(Intent(this, SecondGatePassActivity::class.java))
            startActivity(Intent(this, GatePassHistoryActivity::class.java))
        }
    }

    private fun engSpiner() {
        val genderList = resources.getStringArray(R.array.LangOne)
        binding.langSp.adapter =
            ArrayAdapter(this, R.layout.spinner_dropdown_item, genderList)
        binding.langSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    val itemSelected = parent.getItemAtPosition(position)
                    binding.tvLang.setText(itemSelected.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
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
        println("----main")
        try {
            val diffY = p1.y - p0.y
            val diffX = p1.x - p0.x
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > swipeThreshold && Math.abs(p2) > swipeVelocityThreshold) {
                    if (diffX > 0) {
//                        startActivity(
//                            Intent(this, ProfileActivity::class.java).putExtra(
//                                "from", "newGateKeeper"
//                            )
//                        )
                        binding.newGateKeepeerDrw.openDrawer(GravityCompat.START)
                    } else {
                        binding.newGateKeepeerDrw.closeDrawer(GravityCompat.START)
                    }
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return true
    }

    override fun onClick(position: Int) {
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
            6 -> {

            }
            7 -> {

            }
            8 -> {
                startActivity(Intent(this, TenantSettingActivity::class.java))
            }
        }
    }

    fun closeDrawer() {
        if (binding.newGateKeepeerDrw.isDrawerOpen(GravityCompat.START)) {
            binding.newGateKeepeerDrw.closeDrawer(GravityCompat.START)
        }
    }

    override fun onResume() {
        super.onResume()
        closeDrawer()
    }
}