package com.application.intercom.gatekepper.activity.newFlow.regularEntry

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.ViewModel.gateKeeperViewModel.GateKeeperHomeViewModel
import com.application.intercom.data.model.factory.gateKeeperFactory.GateKeeperFactory
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.gateKeeper.AddRegularVisitorEntryPostModel
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.FlatListOfVisitorGateKeeperList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.gateKeeperRepo.GateKeeperHomeRepo
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityRegularEntryBinding
import com.application.intercom.gatekepper.Main.MainGateKepperActivity
import com.application.intercom.gatekepper.activity.newFlow.myShift.MyShiftGateKeeperActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntry.SingleEntryActivity
import com.application.intercom.gatekepper.gatekeeperAdapter.regularEntry.RegularEntryAdapter
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.owner.activity.helpSupport.OwnerHelpSupportActivity
import com.application.intercom.owner.adapter.ShowImgAdapter
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.utils.*

class RegularEntryActivity : AppCompatActivity(), RegularEntryAdapter.RegularClick,
    ProfileAdapter.ProfileClick {
    lateinit var binding: ActivityRegularEntryBinding
    private var adptr: RegularEntryAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    private lateinit var viewModel: GateKeeperHomeViewModel
    private lateinit var manager_viewModel: ManagerSideViewModel
    private var list = ArrayList<FlatListOfVisitorGateKeeperList.Data.Result>()
    private var flatOfBuildingId: String = ""
    private var flatOfBuildingList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatName: String = ""
    private var showphotoAdapter: ShowImgAdapter? = null
    private var from: String = ""

    ///side menu
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null

    /// side menu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegularEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        println("-----rrr_manager$from")
        binding.nav.constraintLayout10.visibility = View.GONE
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

        } else {
            binding.nav.tvBl.visibility = View.INVISIBLE
            binding.nav.tvEnglish.visibility = View.VISIBLE
        }
        initView()
        listener()
        rcyItem()
    }

    private fun rcyItem() {
        profile_list.add(ProfileModal(R.drawable.home_icon, getString(R.string.home)))
        profile_list.add(ProfileModal(R.drawable.property_icon, getString(R.string.single_entry)))
        profile_list.add(ProfileModal(R.drawable.parking_icon, getString(R.string.regular_entry)))
        profile_list.add(ProfileModal(R.drawable.notics_icon, getString(R.string.my_shift)))
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
        profile_list.add(ProfileModal(R.drawable.term_icon, getString(R.string.about)))
        binding.nav.rcyProfile.layoutManager = LinearLayoutManager(this)
        profileAdapter = ProfileAdapter(this, profile_list, "gateKeeper", this)
        binding.nav.rcyProfile.adapter = profileAdapter
        profileAdapter!!.notifyDataSetChanged()
    }

    private fun initView() {
        binding.regularToolbar.ivBack.visibility = View.INVISIBLE
        binding.regularToolbar.ivmneu.visibility = View.VISIBLE
        flatOfBuildingList.add(0, "Choose Flat")
        initialize()
        observer()
        // flatOfBuilding()
        flatSpinner()
        binding.regularToolbar.tvTittle.text = getString(R.string.regular_entry)


    }

    private fun initialize() {
        val repo = GateKeeperHomeRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, GateKeeperFactory(repo))[GateKeeperHomeViewModel::class.java]
        val repo1 = ManagerSideRepo(BaseApplication.apiService)
        manager_viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo1))[ManagerSideViewModel::class.java]

    }

    private fun flatOfBuilding() {
        if (from.equals("manager")) {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            manager_viewModel.flatOfBuildingList(token)
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            viewModel.flatOfBuildingList(token)
        }

    }

    private fun flatlistofVisitor() {
        if (from.equals("manager")) {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            manager_viewModel.managerFlatListOfVisitor(token, flatOfBuildingId)
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            viewModel.flatListOfVisitor(token, flatOfBuildingId)
        }

    }

    private fun observer() {
        viewModel.flatOfBuildingListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            it.data.result.forEach {
                                flatOfBuildingList.add(it.name)
                                flatOfBuildingHashMapID.put(it.name, it._id)

                            }
//                            prefs.put(
//                                SessionConstants.NEWBUILDINGID,
//                                it.data.result[0].buildingId
//                            )
                            println("------ttte${it.data.result[0].buildingId}")
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
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
        viewModel.flatListOfVisitorLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            binding.rcy.visibility = View.VISIBLE
                            binding.constraintLayout.visibility = View.VISIBLE
                            list.clear()
                            list.addAll(it.data.result)
                            for (img in it.data.result) {
                                photo_upload_list.addAll(img.document)
                            }
                            println("----list$photo_upload_list")
                            binding.rcy.layoutManager = LinearLayoutManager(this)
                            adptr = RegularEntryAdapter(this, this, list, flatName)
                            binding.rcy.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                            if (list.isEmpty()) {
                                binding.rcy.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rcy.visibility = View.VISIBLE
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                        } else if (it.status == AppConstants.STATUS_500) {
                            //this.longToast(it.message)
                            binding.rcy.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_404) {
                            //this.longToast(it.message)
                            binding.rcy.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.rcy.visibility = View.GONE
                            binding.constraintLayout.visibility = View.GONE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)

                }
                else -> {}
            }
        })
        viewModel.addRegularVisitorEntryLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            this.longToast(getString(R.string.in_successfully))
                            flatlistofVisitor()
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
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
        viewModel.outRegularVisitorEntryLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            this.longToast(getString(R.string.out_successfully))
                            flatlistofVisitor()
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
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
        ///manager
        manager_viewModel.flatOfBuildingListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            it.data.result.forEach {
                                flatOfBuildingList.add(it.name)
                                flatOfBuildingHashMapID.put(it.name, it._id)

                            }
                            println("---flatlist$flatOfBuildingList")
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
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

        manager_viewModel.managerFlatListOfVisitorLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(this)
                    }
                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                binding.rcy.visibility = View.VISIBLE
                                binding.constraintLayout.visibility = View.VISIBLE
                                list.clear()
                                list.addAll(it.data.result)

                                binding.rcy.layoutManager = LinearLayoutManager(this)
                                adptr = RegularEntryAdapter(this, this, list, flatName)
                                binding.rcy.adapter = adptr
                                adptr!!.notifyDataSetChanged()
                                if (list.isEmpty()) {
                                    binding.rcy.visibility = View.INVISIBLE
                                    binding.lottieEmpty.visibility = View.VISIBLE
                                } else {
                                    binding.rcy.visibility = View.VISIBLE
                                    binding.lottieEmpty.visibility = View.INVISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_500) {
                                // this.longToast(it.message)
                                binding.rcy.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else if (it.status == AppConstants.STATUS_404) {
                                //this.longToast(it.message)
                                binding.rcy.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else if (it.status == AppConstants.STATUS_FAILURE) {
                                binding.rcy.visibility = View.GONE
                                binding.constraintLayout.visibility = View.GONE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            }
                        }
                    }

                    is EmpResource.Failure -> {
                        EmpCustomLoader.hideLoader()
                        binding.lottieEmpty.visibility = View.VISIBLE
                        ErrorUtil.handlerGeneralError(this, it.throwable!!)

                    }
                    else -> {}
                }
            })

        manager_viewModel.managerAddRegularVisitorEntryLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(this)
                    }
                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                this.longToast(getString(R.string.in_successfully))
                                flatlistofVisitor()
                            } else if (it.status == AppConstants.STATUS_500) {
                                this.longToast(it.message)
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
        manager_viewModel.managerOutRegularVisitorEntryLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(this)
                    }
                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                this.longToast(getString(R.string.out_successfully))
                                flatlistofVisitor()
                            } else if (it.status == AppConstants.STATUS_500) {
                                this.longToast(it.message)
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
//        binding.regularToolbar.ivBack.setOnClickListener {
//            finish()
//        }
        binding.regularToolbar.ivmneu.setOnClickListener {
            binding.gateRegularEntryDrw.openDrawer(GravityCompat.START)
        }
    }

    private fun flatSpinner() {
        //val genderList = resources.getStringArray(R.array.EditProfile)
        binding.flatSpiner.adapter =
            ArrayAdapter(this, R.layout.spinner_dropdown_item, flatOfBuildingList)
        binding.flatSpiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                flatOfBuildingId =
                    flatOfBuildingHashMapID.get(binding.flatSpiner.selectedItem.toString())
                        .toString()
                println("---flateId$flatOfBuildingId")
                if (binding.flatSpiner.selectedItemPosition > 0) {
                    flatName = binding.flatSpiner.selectedItem.toString()
                    flatlistofVisitor()
                } /*else {
                    binding.rcy.visibility = View.GONE
                    binding.constraintLayout.visibility = View.GONE
                }*/
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onClick1(position: Int) {
        inPopup(list)
    }

    override fun onAddEntry(flatId: String, visitorId: String) {
        if (from.equals("manager")) {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            val model = AddRegularVisitorEntryPostModel(flatId, visitorId)
            manager_viewModel.managerAddRegularVisitorEntry(token, model)
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            val model = AddRegularVisitorEntryPostModel(flatId, visitorId)
            viewModel.addRegularVisitorEntry(token, model)
        }


    }

    override fun onOutEntry(visitorId: String) {
        if (from.equals("manager")) {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            manager_viewModel.managerOutRegularVisitorEntry(token, visitorId)
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            viewModel.outRegularVisitorEntry(token, visitorId)
        }

    }

    private fun inPopup(listnew: ArrayList<FlatListOfVisitorGateKeeperList.Data.Result>) {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.regular_entry_in_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val tvNotity = dialog.findViewById<TextView>(R.id.tvIn)
        val tvOut = dialog.findViewById<TextView>(R.id.tvOut1)
        val tvName = dialog.findViewById<TextView>(R.id.textView166)
        val tvFlat = dialog.findViewById<TextView>(R.id.textView167)
        val tvPhone = dialog.findViewById<TextView>(R.id.textView170)
        val tvlastDate = dialog.findViewById<TextView>(R.id.textView172)
        val tvFromTime = dialog.findViewById<TextView>(R.id.tvInTime)
        val tvToTime = dialog.findViewById<TextView>(R.id.tvoutTime)
        val tvNote = dialog.findViewById<TextView>(R.id.tvNote)
        val tvDelivery = dialog.findViewById<TextView>(R.id.textView168)
        val img = dialog.findViewById<ImageView>(R.id.imageView91)
        val rcy = dialog.findViewById<RecyclerView>(R.id.rcyPhoto)
        tvDelivery.text = "${listnew.get(0).visitCategoryName} | ${getString(R.string.regular_entry)}"
        tvName.text = listnew.get(0).visitorName
        tvPhone.text = listnew.get(0).mobileNumber
        tvFromTime.text = listnew.get(0).entryTime
        tvToTime.text = listnew.get(0).exitTime
        tvNote.text = listnew.get(0).note
        tvFlat.text = flatName
        val date = setNewFormatDate(listnew.get(0).toDate)
        tvlastDate.text = date
        img.loadImagesWithGlideExt(listnew.get(0).photo)
        photo_upload_list.clear()
        photo_upload_list.addAll(listnew.get(0).document)
        println("----list$photo_upload_list")
        rcy.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        showphotoAdapter = ShowImgAdapter(this, photo_upload_list)
        rcy.adapter = showphotoAdapter
        showphotoAdapter!!.notifyDataSetChanged()
        if (listnew.get(0).currentStatus == "In") {
            tvOut.visibility = View.VISIBLE
            tvNotity.visibility = View.INVISIBLE
        } else {
            tvOut.visibility = View.INVISIBLE
            tvNotity.visibility = View.VISIBLE
        }
        tvNotity.setOnClickListener {
            dialog.dismiss()
            if (from.equals("manager")) {
                val token = prefs.getString(
                    SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
                )
                val model =
                    AddRegularVisitorEntryPostModel(listnew.get(0).flatId, listnew.get(0)._id)
                manager_viewModel.managerAddRegularVisitorEntry(token, model)
            } else {
                val token = prefs.getString(
                    SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
                )
                val model =
                    AddRegularVisitorEntryPostModel(listnew.get(0).flatId, listnew.get(0)._id)
                viewModel.addRegularVisitorEntry(token, model)
            }

        }
        tvOut.setOnClickListener {
            dialog.dismiss()
            if (from.equals("manager")) {
                val token = prefs.getString(
                    SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
                )
                manager_viewModel.managerOutRegularVisitorEntry(token, listnew.get(0)._id)
            } else {
                val token = prefs.getString(
                    SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
                )
                viewModel.outRegularVisitorEntry(token, listnew.get(0)._id)
            }
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    override fun onResume() {
        super.onResume()
        flatOfBuilding()
        closeDrawer()
    }

    override fun onClick(position: Int) {
        when (position) {
            0 -> {
                startActivity(
                    Intent(this, MainGateKepperActivity::class.java).putExtra(
                        "from",
                        "from_side_home"
                    )
                )
                finish()

            }
            1 -> {
                startActivity(Intent(this, SingleEntryActivity::class.java))
//                startActivity(Intent(this, TenantSettingActivity::class.java))
//
            }
            2 -> {
                startActivity(Intent(this, RegularEntryActivity::class.java))
//                startActivity(Intent(this, CreateGatePassActivity::class.java))
            }
            3 -> {
                startActivity(Intent(this, MyShiftGateKeeperActivity::class.java))

            }
            4 -> {
                startActivity(
                    Intent(this, OwnerHelpSupportActivity::class.java).putExtra(
                        "from",
                        "gateKeeper"
                    )
                )

//                startActivity(Intent(this, VisitorHistoryTypeActivity::class.java))
            }
            5 -> {
                startActivity(Intent(this, TenantSettingActivity::class.java))

            }
            6 -> {
                shareIntent(this, "https://intercomapp.page.link/Go1D")
            }
            7 -> {
                startActivity(Intent(this, PrivacyPolicyActivity::class.java))
            }
            8 -> {
                startActivity(Intent(this, TermsOfServiceActivity::class.java))
            }
            9 -> {
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
        if (binding.gateRegularEntryDrw.isDrawerOpen(GravityCompat.START)) {
            binding.gateRegularEntryDrw.closeDrawer(GravityCompat.START)
        }
    }
}