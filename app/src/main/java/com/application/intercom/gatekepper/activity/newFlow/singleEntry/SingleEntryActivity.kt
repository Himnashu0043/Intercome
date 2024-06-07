package com.application.intercom.gatekepper.activity.newFlow.singleEntry

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.ViewModel.gateKeeperViewModel.GateKeeperHomeViewModel
import com.application.intercom.data.model.factory.gateKeeperFactory.GateKeeperFactory
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.gateKeeper.AddSingleEntryGateKeeperPostModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.gateKeeperRepo.GateKeeperHomeRepo
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivitySingleEntryBinding
import com.application.intercom.gatekepper.Main.MainGateKepperActivity
import com.application.intercom.gatekepper.activity.newFlow.home.NewGateKeeperMainActivity
import com.application.intercom.gatekepper.activity.newFlow.myShift.MyShiftGateKeeperActivity
import com.application.intercom.gatekepper.activity.newFlow.regularEntry.RegularEntryActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntryHistory.SingleEntryHistoryActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.manager.complaint.RegisterComplaintsActivity
import com.application.intercom.manager.gatekeeper.GateKeeperListingActivity
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.manager.newFlow.finance.ManagerFinanceActivity
import com.application.intercom.manager.notice.NoticeBoardActivity
import com.application.intercom.manager.visitorAndGatePass.ManagerVisitorGatePassActivity
import com.application.intercom.owner.activity.helpSupport.OwnerHelpSupportActivity
import com.application.intercom.owner.adapter.PhotoUploadAdapter
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner

class SingleEntryActivity : AppCompatActivity(), AWSListner,ProfileAdapter.ProfileClick  {
    lateinit var binding: ActivitySingleEntryBinding
    private lateinit var viewModel: GateKeeperHomeViewModel
    private lateinit var manager_viewModel: ManagerSideViewModel
    private var flatOfBuildingList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingMobileHashMapID: HashMap<String, String> = HashMap()
    private var visitorCategoryList = ArrayList<String>()
    private var visitorCategorygHashMapID: HashMap<String, String> = HashMap()
    private var visitorCategoryId: String = ""
    private var flatOfBuildingId: String = ""
    private var buildingId: String = ""
    private var imagePath: String = ""
    private var visitorName: String = ""
    private var flatName: String = ""
    private var phoneNumber: String = ""
    private var from: String = ""
    ///side menu
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null
    /// side menu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        println("---manager_From$from")
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
        if (from == "manager") {
            profile_list.add(ProfileModal(R.drawable.home_icon, getString(R.string.home)))
            profile_list.add(ProfileModal(R.drawable.community_icon, getString(R.string.complain)))
            profile_list.add(
                ProfileModal(
                    R.drawable.billing_icon, "Billing and Account"
                )
            )
            profile_list.add(
                ProfileModal(
                    R.drawable.visitor_gatepass_icon, getString(R.string.visitors_gatepass)
                )
            )
            profile_list.add(
                ProfileModal(
                    R.drawable.community_icon,
                    getString(R.string.gatekeepers)
                )
            )
            profile_list.add(ProfileModal(R.drawable.notics_icon, getString(R.string.notice_board)))
            profile_list.add(
                ProfileModal(
                    R.drawable.help_icon,
                    getString(R.string.help_and_support)
                )
            )
            profile_list.add(ProfileModal(R.drawable.setting_icon, getString(R.string.settings)))
            profile_list.add(ProfileModal(R.drawable.share_new_icon, getString(R.string.share)))
            profile_list.add(ProfileModal(R.drawable.notics_icon, getString(R.string.about)))
            profile_list.add(
                ProfileModal(
                    R.drawable.privacy_icon,
                    getString(R.string.privacy_policy)
                )
            )
            profile_list.add(
                ProfileModal(
                    R.drawable.term_icon,
                    getString(R.string.terms_and_conditions)
                )
            )

            binding.nav.rcyProfile.layoutManager = LinearLayoutManager(this)
            profileAdapter = ProfileAdapter(this, profile_list, "manager", this)
            binding.nav.rcyProfile.adapter = profileAdapter
            profileAdapter!!.notifyDataSetChanged()
        } else {
            profile_list.add(ProfileModal(R.drawable.home_icon, getString(R.string.home)))
            profile_list.add(
                ProfileModal(
                    R.drawable.property_icon,
                    getString(R.string.single_entry)
                )
            )
            profile_list.add(
                ProfileModal(
                    R.drawable.parking_icon,
                    getString(R.string.regular_entry)
                )
            )
            profile_list.add(ProfileModal(R.drawable.notics_icon, getString(R.string.my_shift)))
            profile_list.add(
                ProfileModal(
                    R.drawable.help_icon,
                    getString(R.string.help_and_support)
                )
            )
            profile_list.add(ProfileModal(R.drawable.setting_icon, getString(R.string.settings)))
            profile_list.add(ProfileModal(R.drawable.share_new_icon, getString(R.string.share)))
            profile_list.add(
                ProfileModal(
                    R.drawable.privacy_icon,
                    getString(R.string.privacy_policy)
                )
            )
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

    }
    private fun initView() {
        binding.toolbar.ivBack.visibility = View.INVISIBLE
        binding.toolbar.ivmneu.visibility = View.VISIBLE
        flatOfBuildingList.add(0, "Choose Flat")
        visitorCategoryList.add(0, "Choose Category")
        initialize()
        observer()
        visitiorCategoryList()
        flatOfBuilding()

        binding.toolbar.tvTittle.text = getString(R.string.single_entry)
        binding.commonBtn.tv.text = getString(R.string.continue_1)
        catSpinner()
        flatSpinner()

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

    private fun visitiorCategoryList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.visitorCategoryList(token)
    }

    private fun addSingleEntry() {
        if (from.equals("manager")) {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            val model = AddSingleEntryGateKeeperPostModel(
                binding.edAddress.text.trim().toString(),
                buildingId,
                flatOfBuildingId,
                binding.edPhone.text.trim().toString(),
                binding.edNumberofVisitor.text.trim().toString(),
                imagePath,
                visitorCategoryId,
                visitorName,
                binding.edName.text.trim().toString()

            )
            manager_viewModel.managerAddSingleEntry(token, model)
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            val model = AddSingleEntryGateKeeperPostModel(
                binding.edAddress.text.trim().toString(),
                buildingId,
                flatOfBuildingId,
                binding.edPhone.text.trim().toString(),
                binding.edNumberofVisitor.text.trim().toString(),
                imagePath,
                visitorCategoryId,
                visitorName,
                binding.edName.text.trim().toString()

            )
            viewModel.addSingleEntry(token, model)
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
                                flatOfBuildingBuildingHashMapID.put(it.name, it.buildingId)
                                flatOfBuildingMobileHashMapID.put(
                                    it.name,
                                    it.ownerInfo.get(0).phoneNumber
                                )
                            }
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
        viewModel.visitorCategoryListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            it.data.forEach {
                                visitorCategoryList.add(it.categoryName)
                                visitorCategorygHashMapID.put(it.categoryName, it._id)

                            }
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
        viewModel.addSingleEntryLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            detailsPopup()
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
        ////manager
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
                                flatOfBuildingBuildingHashMapID.put(it.name, it.buildingId)
                                flatOfBuildingMobileHashMapID.put(
                                    it.name,
                                    ""
                                )
                            }
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
        manager_viewModel.managerAddSingleEntryLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            detailsPopup()
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
        binding.commonBtn.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            }
            detailsPopup()
        }
//        binding.toolbar.ivBack.setOnClickListener {
//            finish()
//        }
        binding.toolbar.ivmneu.setOnClickListener {
            binding.gateSingleEntryDrw.openDrawer(GravityCompat.START)
        }
        binding.imageView90.setOnClickListener {
            showImagePickDialog()
        }
        binding.imageView17.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${phoneNumber}")
            startActivity(intent)
        }

    }

    private fun catSpinner() {
        // val genderList = resources.getStringArray(com.application.intercom.R.array.EditProfile)
        binding.selectCatogorySpiner.adapter =
            ArrayAdapter(this, R.layout.spinner_dropdown_item, visitorCategoryList)
        binding.selectCatogorySpiner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long,
                ) {
                    visitorCategoryId =
                        visitorCategorygHashMapID.get(binding.selectCatogorySpiner.selectedItem.toString())
                            .toString()
                    println("---visitorId$visitorCategoryId")
                    if (binding.selectFlatSpiner.selectedItemPosition > 0 && binding.selectCatogorySpiner.selectedItemPosition > 0) {
                        binding.childLay.visibility = View.VISIBLE
                        visitorName = binding.selectCatogorySpiner.selectedItem.toString()
                        flatName = binding.selectFlatSpiner.selectedItem.toString()
                        println("----name$visitorName")
                    } else {
                        binding.childLay.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun flatSpinner() {
        // val genderList = resources.getStringArray(com.application.intercom.R.array.EditProfile)
        binding.selectFlatSpiner.adapter =
            ArrayAdapter(this, R.layout.spinner_dropdown_item, flatOfBuildingList)
        binding.selectFlatSpiner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long,
                ) {
                    flatOfBuildingId =
                        flatOfBuildingHashMapID.get(binding.selectFlatSpiner.selectedItem.toString())
                            .toString()
                    println("---flateId$flatOfBuildingId")
                    buildingId =
                        flatOfBuildingBuildingHashMapID.get(binding.selectFlatSpiner.selectedItem.toString())
                            .toString()
                    println("---buildingId$buildingId")
                    phoneNumber =
                        flatOfBuildingMobileHashMapID.get(binding.selectFlatSpiner.selectedItem.toString())
                            .toString()
                    println("---phoneNumber$phoneNumber")
                    if (binding.selectCatogorySpiner.selectedItemPosition > 0 && binding.selectFlatSpiner.selectedItemPosition > 0) {
                        binding.childLay.visibility = View.VISIBLE
                        flatName = binding.selectFlatSpiner.selectedItem.toString()
                        visitorName = binding.selectCatogorySpiner.selectedItem.toString()
                    } else {
                        binding.childLay.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun detailsPopup() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.single_entry_details_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)

        val tvNotity = dialog.findViewById<TextView>(R.id.tvnotify)
        val img = dialog.findViewById<ImageView>(R.id.imageView91)
        val tvVisitorName = dialog.findViewById<TextView>(R.id.textView166)
        val tvphone = dialog.findViewById<TextView>(R.id.textView170)
        val tvAddess = dialog.findViewById<TextView>(R.id.textView172)
        val tvNote = dialog.findViewById<TextView>(R.id.textView174)
        val tvFlate = dialog.findViewById<TextView>(R.id.textView167)
        val tvDelivery = dialog.findViewById<TextView>(R.id.textView168)
        val tvEdit = dialog.findViewById<TextView>(R.id.tvEdit)
        tvVisitorName.text = binding.edName.text.trim().toString()
        tvphone.text = binding.edPhone.text.trim().toString()
        tvAddess.text = binding.edAddress.text.trim().toString()
        tvNote.text = binding.edNumberofVisitor.text.trim().toString()
        tvFlate.text = flatName
        tvDelivery.text = "${visitorName} | Single Entry"
        img.loadImagesWithGlideExt(imagePath.toString())

        tvNotity.setOnClickListener {
            dialog.dismiss()
//            startActivity(
//                Intent(
//                    this, MainGateKepperActivity::class.java
//                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            )
            finish()
            addSingleEntry()

        }
        tvEdit.setOnClickListener {
            dialog.dismiss()
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
        dialog.show()

    }

    private fun showImagePickDialog() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setMessage("Choose image")
        dialog.setPositiveButton(
            "Gallery"
        ) { _, _ ->
            val intent = Intent(this, TakeImageWithCrop::class.java)
            intent.putExtra("from", "gallery")
            startActivityForResult(intent, TakeImageWithCrop.GALLERY_REQUEST)
        }
        dialog.setNegativeButton(
            "Camera"
        ) { _, _ ->


            val intent = Intent(this, TakeImageWithCrop::class.java)
            intent.putExtra("from", "camera")
            startActivityForResult(intent, TakeImageWithCrop.CAMERA_REQUEST)


        }
        dialog.setNeutralButton(
            "Cancel"
        ) { dialog, which -> dialog.dismiss() }
        dialog.show()
    }
    private var currentImagePath: Uri = Uri.EMPTY
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val path = data?.getStringExtra("filePath")
            if (!path.isNullOrEmpty()) {
                currentImagePath = Uri.parse(path)
                EmpCustomLoader.showLoader(this)
                AWSUtils(
                    this, path, this
                )
            }
        } else if (requestCode == TakeImageWithCrop.GALLERY_REQUEST) {
            val path = data?.getStringExtra("filePath")
            if (!path.isNullOrEmpty()) {
                currentImagePath = Uri.parse(path)
                EmpCustomLoader.showLoader(this)
                AWSUtils(
                    this, path, this
                )
            }
        }
    }

    private fun validationData(): Boolean {
        if (binding.edName.text.trim().toString().length < 4) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_name), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edPhone.text!!.trim().toString())) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_enter_phone_number),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.edPhone.text!!.trim()
                .toString().length < 10 || binding.edPhone.text!!.trim().toString().length > 12
        ) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_enter_valid_phone_number),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edAddress.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_address), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edNumberofVisitor.text?.trim().toString())) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_enter_number_of_visitor),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.imageView90.drawable == null) {
            Toast.makeText(
                applicationContext, getString(R.string.please_select_img), Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true

    }

    override fun onAWSLoader(isLoader: Boolean) {
        EmpCustomLoader.showLoader(this)
    }

    override fun onAWSSuccess(url: String?) {
        if (url != null) {
            EmpCustomLoader.hideLoader()
            imagePath = url!!
            println("===========imd$imagePath")
//            binding.imageView90.loadImagesWithGlideExt(imagePath)
            binding.imageView90.setImageURI(currentImagePath)


        }
    }

    override fun onAWSError(error: String?) {

        EmpCustomLoader.hideLoader()
        Log.e("error", error ?: "")
    }

    override fun onAWSProgress(progress: Int?) {
        EmpCustomLoader.showLoader(this)
        Log.e("progress", progress!!.toString())
    }

    override fun onClick(position: Int) {
        if (from == "manager") {
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
                        Intent(this, RegisterComplaintsActivity::class.java)
                    )

                }
                2 -> {
                    startActivity(
                        Intent(this, ManagerFinanceActivity::class.java)
                    )

                }
                3 -> {
                    startActivity(Intent(this, ManagerVisitorGatePassActivity::class.java))

                }
                4 -> {
                    startActivity(
                        Intent(this, GateKeeperListingActivity::class.java)
                    )


                }
                5 -> {
                    startActivity(Intent(this, NoticeBoardActivity::class.java))


                }
                6 -> {
                    startActivity(
                        Intent(this, OwnerHelpSupportActivity::class.java).putExtra(
                            "from", "Manager"
                        )
                    )
                }
                7 -> {
                    startActivity(Intent(this, TenantSettingActivity::class.java))
                }
                8 -> {
                    shareIntent(this, "https://intercomapp.page.link/Go1D")


                }
                9 -> {
                    startActivity(Intent(this, AboutUsActivity::class.java))


                }
                10 -> {
                    startActivity(Intent(this, PrivacyPolicyActivity::class.java))


                }
                11 -> {
                    startActivity(Intent(this, TermsOfServiceActivity::class.java))
                }


            }
        } else {
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
        if (binding.gateSingleEntryDrw.isDrawerOpen(GravityCompat.START)) {
            binding.gateSingleEntryDrw.closeDrawer(GravityCompat.START)
        }
    }

    override fun onResume() {
        super.onResume()
        closeDrawer()
    }
}