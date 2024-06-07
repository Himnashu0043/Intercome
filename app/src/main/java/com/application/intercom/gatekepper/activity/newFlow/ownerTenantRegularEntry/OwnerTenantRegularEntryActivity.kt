package com.application.intercom.gatekepper.activity.newFlow.ownerTenantRegularEntry

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.ViewModel.tenantViewModel.tenantHomeViewModel.TenantHomeViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.factory.tenantFactory.TenantHomeFactory
import com.application.intercom.data.model.local.owner.addRegularEntry.AddRegularEntryOwnerPostModel
import com.application.intercom.data.model.local.owner.addRegularEntry.EditRegularEntryOwnerPostModel
import com.application.intercom.data.model.remote.owner.regularHistory.RegularHistoryList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.data.repository.tenantRepo.tenantHomeRepo.TenantHomeRepo
import com.application.intercom.databinding.ActivityOwnerTenantRegularEntryBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.helper.setFormatDate
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.owner.adapter.PhotoUploadAdapter
import com.application.intercom.owner.adapter.ShowImgAdapter
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class OwnerTenantRegularEntryActivity : AppCompatActivity(), AWSListner {
    lateinit var binding: ActivityOwnerTenantRegularEntryBinding
    private var from: String = ""
    private var from_edit: String = ""
    private lateinit var viewModel: OwnerSideViewModel
    private lateinit var tenantviewModel: TenantHomeViewModel
    private lateinit var owner_viewModel: OwnerHomeViewModel
    private var flatOfBuildingList = ArrayList<String>()
    private var visitorCategoryList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingBuildingHashMapID: HashMap<String, String> = HashMap()
    private var visitorCategorygHashMapID: HashMap<String, String> = HashMap()
    private var photoAdapter: PhotoUploadAdapter? = null
    private var showphotoAdapter: ShowImgAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    private var visitorCategoryId: String = ""
    private var visitorId: String = ""
    private var flatOfBuildingId: String = ""
    private var buildingId: String = ""
    private var imagePath: String = ""
    private var type: String = ""
    private var main_img: String = ""
    private var visitorName: String = ""
    private var flatName: String = ""
    val TIME_DIALOG_ID = 1111
    val TIME_DIALOG_ID1 = 1112
    private var hour = 0
    private var minute = 0
    var giventime: String? = ""
    var giventimend: String? = ""
    var typeetime = ""
    var fromDate: String = ""
    var toDate: String = ""
    var mobile: String = ""
    var ownerNumber: String = ""
    private var editlist: RegularHistoryList.Data.Result? = null
    private lateinit var select_categorydapter: ArrayAdapter<String>
    private lateinit var select_flat_adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerTenantRegularEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        from_edit = intent.getStringExtra("from_edit").toString()
        ownerNumber = prefs.getString(
            SessionConstants.OWNERNUMBER, GPSService.mLastLocation?.latitude.toString()
        )
        println("-----from$from")
        println("-----fromeee$from_edit")
        initView()
        listener()
    }

    private fun initView() {
        ////edit Data
        if (from_edit.equals("editData")) {
            editlist = intent.getSerializableExtra("editList") as RegularHistoryList.Data.Result?
            println("----edit$editlist")
            binding.childLay.visibility = View.VISIBLE
            imagePath = editlist!!.photo
            binding.edName.setText(editlist!!.visitorName)
            binding.edPhone.setText(editlist!!.mobileNumber)
            if (editlist!!.fromDate != null) {
                fromDate = setNewFormatDate(editlist!!.fromDate).toString()
                toDate = setNewFormatDate(editlist!!.toDate).toString()
                binding.tvFromDate.setText(setFormatDate(editlist!!.fromDate))
                binding.tvToDate.setText(setFormatDate(editlist!!.toDate))
            }
            if (!editlist!!.fromTime.equals("")) {
                giventime = editlist!!.fromTime
                giventimend = editlist!!.toTime
                binding.tvFromTime.setText(editlist!!.fromTime)
                binding.tvToTime.setText(editlist!!.toTime)
            }




            binding.edAddress.setText(editlist!!.address)
            binding.edNumberofVisitor.setText(editlist!!.note)
            binding.imageView90.loadImagesWithGlideExt(editlist!!.photo)
            binding.rcyPhoto.visibility = View.VISIBLE
            photo_upload_list.addAll(editlist!!.document)
            println("-----edddphoto${photo_upload_list}")
            binding.rcyPhoto.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            photoAdapter = PhotoUploadAdapter(this, photo_upload_list)
            binding.rcyPhoto.adapter = photoAdapter
            photoAdapter!!.notifyDataSetChanged()
            visitorId = editlist!!._id


        }
        ////edit Data
        flatOfBuildingList.add(0, "Choose Flat")
        visitorCategoryList.add(0, "Choose Category")
        println("----flatsize$flatOfBuildingList")
        println("----vistory$visitorCategoryList")
        initialize()
        observer()
        visitiorCategoryList()
        if (from.equals("tenant")) {
            binding.textView51112.text = "Note"
            getOwnerDetails()
            getOwnerFlatList()
        } else {
            binding.textView51112.text = "Visiting purpose"
            getOwnerFlatList()
        }
        binding.toolbar.tvTittle.text = getString(R.string.regular_entry)
        catSpinner()
        flatSpinner()

        binding.commonBtn.tv.text = getString(R.string.continue_1)


    }

    private fun initialize() {
        val repo = OwnerSideRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, OwnerSideFactory(repo))[OwnerSideViewModel::class.java]

        val ownerModel = OwnerHomeRepo(BaseApplication.apiService)
        owner_viewModel = ViewModelProvider(
            this, OwnerHomeFactory(ownerModel)
        )[OwnerHomeViewModel::class.java]


        val tenantModel = TenantHomeRepo(BaseApplication.apiService)
        tenantviewModel = ViewModelProvider(
            this, TenantHomeFactory(tenantModel)
        )[TenantHomeViewModel::class.java]

    }

    private fun getOwnerFlatList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        owner_viewModel.ownerFlatList(token)
    }

    private fun visitiorCategoryList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.visitorCategoryList(token)
    }

    private fun getOwnerDetails() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        tenantviewModel.tenantDetails(token)

    }

    private fun addVisitorOwner() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        if (from.equals("tenant")) {
            val model = AddRegularEntryOwnerPostModel(
                binding.edAddress.text.trim().toString(),
                buildingId,
                photo_upload_list,
                flatOfBuildingId,
                if (fromDate.isEmpty()) {
                    binding.tvFromDate.text.trim().toString()
                } else {
                    fromDate
                },
                if (giventime!!.isEmpty()) {
                    binding.tvFromTime.text.trim().toString()
                } else {
                    giventime.toString()
                },
                binding.edPhone.text.trim().toString(),
                binding.edNumberofVisitor.text.trim().toString(),
                imagePath,
                if (toDate.isEmpty()) {
                    binding.tvToDate.text.trim().toString()
                } else {
                    toDate
                },
                if (giventimend!!.isEmpty()) {
                    binding.tvToTime.text.trim().toString()
                } else {
                    giventimend.toString()
                },
                visitorCategoryId,
                visitorName,
                binding.edName.text.trim().toString()
            )
            viewModel.addVisitorTenant(token, model)
        } else {
            val model = AddRegularEntryOwnerPostModel(
                binding.edAddress.text.trim().toString(),
                buildingId,
                photo_upload_list,
                flatOfBuildingId,
                fromDate,
                giventime.toString(),
                binding.edPhone.text.trim().toString(),
                binding.edNumberofVisitor.text.trim().toString(),
                imagePath,
                toDate,
                giventimend.toString(),
                visitorCategoryId,
                visitorName,
                binding.edName.text.trim().toString()
            )
            viewModel.addVisitorOwner(token, model)
        }

    }

    private fun editVisitorOwner() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        if (from.equals("tenant")) {
            val model = EditRegularEntryOwnerPostModel(
                binding.edAddress.text.trim().toString(),
                buildingId,
                photo_upload_list,
                flatOfBuildingId,
                fromDate,
                giventime.toString(),
                binding.edPhone.text.trim().toString(),
                binding.edNumberofVisitor.text.trim().toString(),
                imagePath,
                toDate,
                giventimend.toString(),
                visitorCategoryId,
                visitorName,
                visitorId,
                binding.edName.text.trim().toString()
            )
            viewModel.editVisitorTenant(token, model)
        } else {
            val model = EditRegularEntryOwnerPostModel(
                binding.edAddress.text.trim().toString(),
                buildingId,
                photo_upload_list,
                flatOfBuildingId,
                fromDate,
                giventime.toString(),
                binding.edPhone.text.trim().toString(),
                binding.edNumberofVisitor.text.trim().toString(),
                imagePath,
                toDate,
                giventimend.toString(),
                visitorCategoryId,
                visitorName,
                visitorId,
                binding.edName.text.trim().toString()

            )
            viewModel.editVisitorOwner(token, model)
        }

    }


    private fun observer() {
        owner_viewModel.ownerFlatListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            it.data.forEach {
                                flatOfBuildingList.add(it.name)
                                flatOfBuildingHashMapID.put(it.name, it._id)
                                flatOfBuildingBuildingHashMapID.put(it.name, it.buildingId)

                            }
                            if (from_edit.equals("editData")) {
                                binding.childLay.visibility = View.VISIBLE
                                binding.selectFlatSpiner.setSelection(
                                    select_flat_adapter.getPosition(
                                        editlist!!.flatInfo.get(0).name
                                    )
                                )
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
                            if (from_edit.equals("editData")) {
                                binding.childLay.visibility = View.VISIBLE
                                binding.selectCatogorySpiner.setSelection(
                                    select_categorydapter.getPosition(
                                        editlist!!.visitCategoryName
                                    )
                                )
                                visitorName = binding.selectCatogorySpiner.selectedItem.toString()
                                println("----eddname$visitorName")
                                flatName = binding.selectFlatSpiner.selectedItem.toString()
                                println("----eddnamefff$flatName")
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
        viewModel.addVisitorOwnerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
//                            startActivity(
//                                Intent(
//                                    this, OwnerMainActivity::class.java
//                                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            )
                            finish()
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
        viewModel.editVisitorOwnertLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
//                            startActivity(
//                                Intent(
//                                    this, OwnerTenantRegularEntryHistoryActivity::class.java
//                                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            )
                            finish()
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
        viewModel.addVisitorTenantLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
//                            startActivity(
//                                Intent(
//                                    this, TenantMainActivity::class.java
//                                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            )
                            finish()
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
        viewModel.editVisitorTenantLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
//                            startActivity(
//                                Intent(
//                                    this, TenantMainActivity::class.java
//                                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            )
                            finish()
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
        tenantviewModel.tenantDetailsLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            flatOfBuildingList.add(it.data.userData.get(0).name)
                            flatOfBuildingHashMapID.put(
                                it.data.userData.get(0).name, it.data.userData.get(0)._id
                            )
                            flatOfBuildingBuildingHashMapID.put(
                                it.data.userData.get(0).name, it.data.userData.get(0).buildingId._id
                            )

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
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.tvFromDate.setOnClickListener {
            MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build().apply {
                show(supportFragmentManager, this@OwnerTenantRegularEntryActivity.toString())
                addOnPositiveButtonClickListener {
                    fromDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                        Date(it)
                    )

                    binding.tvFromDate.setText(
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                            Date(it)
                        )
                    )
                }
            }
        }
        binding.tvToDate.setOnClickListener {
            MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build().apply {
                show(supportFragmentManager, this@OwnerTenantRegularEntryActivity.toString())
                addOnPositiveButtonClickListener {
                    toDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                        Date(it)
                    )
                    binding.tvToDate.setText(
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                            Date(it)
                        )
                    )
                }
            }
        }
        binding.tvFromTime.setOnClickListener {
            selectStartTime()
        }
        binding.tvToTime.setOnClickListener {
            endTime()
        }
        binding.commonBtn.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            }
            donePopup()
        }
        binding.imageView90.setOnClickListener {
            showImagePickDialog()
            type = "img"
        }
        binding.imageView901.setOnClickListener {
            showImagePickDialog()
            type = "doc_img"
        }
        binding.imageView17.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${ownerNumber}")
            startActivity(intent)
        }

    }

    private fun donePopup() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.owner_tenant_regular_entry_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val tvDone = dialog.findViewById<TextView>(R.id.tvDone)
        val tvName = dialog.findViewById<TextView>(R.id.textView166)
        val tvFlatName = dialog.findViewById<TextView>(R.id.textView167)
        val tvphone = dialog.findViewById<TextView>(R.id.textView170)
        val tvDate = dialog.findViewById<TextView>(R.id.textView172)
        val tvTime = dialog.findViewById<TextView>(R.id.tvInTime)
        val tvNote = dialog.findViewById<TextView>(R.id.tvNote)
        val rcy = dialog.findViewById<RecyclerView>(R.id.rcyPhoto)
        val img = dialog.findViewById<ImageView>(R.id.imageView91)
        val ivCalling = dialog.findViewById<ImageView>(R.id.imageView100)
        tvName.text = binding.edName.text.trim().toString()
        tvFlatName.text = flatName
        tvphone.text = binding.edPhone.text.trim().toString()
        tvDate.text = "${fromDate} to ${toDate}"
        tvTime.text = "${giventime} to ${giventimend}"
        tvNote.text = binding.edNumberofVisitor.text.trim().toString()
        img.loadImagesWithGlideExt(imagePath)
        rcy.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        showphotoAdapter = ShowImgAdapter(this, photo_upload_list)
        rcy.adapter = showphotoAdapter
        showphotoAdapter!!.notifyDataSetChanged()
        tvDone.setOnClickListener {

            dialog.dismiss()
            if (from.equals("tenant")) {
                if (from_edit.equals("editData")) {
                    editVisitorOwner()
                } else {
                    addVisitorOwner()
                }
            } else {
                if (from_edit.equals("editData")) {
                    editVisitorOwner()
                } else {
                    addVisitorOwner()
                }
            }


        }
        ivCalling.setOnClickListener {
            mobile = binding.edPhone.text.trim().toString()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${mobile}")
            startActivity(intent)

        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    private fun catSpinner() {
        //val genderList = resources.getStringArray(com.application.intercom.R.array.EditProfile)
        //  binding.selectCatogorySpiner.adapter =
        select_categorydapter =
            ArrayAdapter(this, R.layout.spinner_dropdown_item, visitorCategoryList)
        binding.selectCatogorySpiner.adapter = select_categorydapter
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
                        println("----name$visitorName")
                        flatName = binding.selectFlatSpiner.selectedItem.toString()
                    } else {
                        binding.childLay.visibility = View.GONE
                    }

                }


                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun flatSpinner() {
        //val genderList = resources.getStringArray(com.application.intercom.R.array.EditProfile)
//        binding.selectFlatSpiner.adapter =
        select_flat_adapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, flatOfBuildingList)
        binding.selectFlatSpiner.adapter = select_flat_adapter
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
                    if (binding.selectCatogorySpiner.selectedItemPosition > 0 && binding.selectFlatSpiner.selectedItemPosition > 0) {
                        binding.childLay.visibility = View.VISIBLE
                        flatName = binding.selectFlatSpiner.selectedItem.toString()
                        visitorName = binding.selectCatogorySpiner.selectedItem.toString()
                        println("----name$visitorName")
                    } else {
                        binding.childLay.visibility = View.GONE
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    override fun onCreateDialog(id: Int): Dialog? {
        when (id) {
            TIME_DIALOG_ID ->
                // set time picker as current time
                return TimePickerDialog(
                    this, timePickerListener, hour, minute, false
                )
            TIME_DIALOG_ID1 ->
                // set time picker as current time
                return TimePickerDialog(
                    this, timePickerListener, hour, minute, false
                )
        }
        return null
    }

    private val timePickerListener =
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minutes -> // TODO Auto-generated method stub
            hour = hourOfDay
            minute = minutes
            updateTime(hour, minute)
        }

    private fun updateTime(hours: Int, mins: Int) {
        var hours = hours
        var timeSet = ""
        if (hours > 12) {
            hours -= 12
            timeSet = "PM"
        } else if (hours == 0) {
            hours += 12
            timeSet = "AM"
        } else if (hours == 12) timeSet = "PM" else timeSet = "AM"
        var minutes = ""
        minutes = if (mins < 10) "0$mins" else mins.toString()

        // Append in a StringBuilder
        val aTime =
            StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet)
                .toString()


        if (typeetime.equals("start")) {
            giventime = aTime
            binding.tvFromTime.setText(giventime)
        } else if (typeetime.equals("end")) {
            giventimend = aTime
            binding.tvToTime.setText(giventimend)
        }
    }

    private fun endTime() {
        typeetime = "end"
        showDialog(TIME_DIALOG_ID1)
    }

    private fun selectStartTime() {
        typeetime = "start"
        showDialog(TIME_DIALOG_ID)
    }

    private fun validationData(): Boolean {
        if (TextUtils.isEmpty(binding.edName.text!!.trim().toString())) {
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
        } else if (TextUtils.isEmpty(binding.tvFromDate.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_select_from_date), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.tvToDate.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_select_to_date), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.tvFromTime.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_select_from_time), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.tvToTime.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_select_to_time), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edNumberofVisitor.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_visitor), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.imageView90.drawable == null) {
            Toast.makeText(
                applicationContext, getString(R.string.please_select_image), Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val path = data?.getStringExtra("filePath")
        if (requestCode == TakeImageWithCrop.CAMERA_REQUEST) {
            if (!path.isNullOrEmpty()) {
                EmpCustomLoader.showLoader(this)
                AWSUtils(
                    this, path, this
                )
            }
        } else if (requestCode == TakeImageWithCrop.GALLERY_REQUEST) {
            if (!path.isNullOrEmpty()) {
                EmpCustomLoader.showLoader(this)
                AWSUtils(
                    this, path, this
                )
            }
        }
    }

    override fun onAWSLoader(isLoader: Boolean) {
        EmpCustomLoader.showLoader(this)
        binding.rcyPhoto.visibility = View.VISIBLE
    }

    override fun onAWSSuccess(url: String?) {
        if (url != null) {
            EmpCustomLoader.hideLoader()
            if (type.equals("img")) {
                imagePath = url
                println("---imagePath${imagePath}")
                binding.pro.visibility = View.GONE
                binding.imageView90.loadImagesWithGlideExt(imagePath)
            } else if (type.equals("doc_img")) {
                main_img = url
                binding.rcyPhoto.visibility = View.VISIBLE
                binding.pro.visibility = View.GONE
                photo_upload_list.add(main_img)
                println("-----photo${photo_upload_list}")
                binding.rcyPhoto.layoutManager =
                    LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                photoAdapter = PhotoUploadAdapter(this, photo_upload_list)
                binding.rcyPhoto.adapter = photoAdapter
                photoAdapter!!.notifyDataSetChanged()
            }
        }

    }

    override fun onAWSError(error: String?) {
        EmpCustomLoader.hideLoader()
        binding.rcyPhoto.visibility = View.INVISIBLE
    }

    override fun onAWSProgress(progress: Int?) {
        EmpCustomLoader.showLoader(this)
        binding.rcyPhoto.visibility = View.VISIBLE
    }

}