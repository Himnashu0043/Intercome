package com.application.intercom.owner.activity.gatepass

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.ViewModel.tenantViewModel.tenantHomeViewModel.TenantHomeViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.factory.tenantFactory.TenantHomeFactory
import com.application.intercom.data.model.local.owner.gatePass.OwnerAddGatePassPostModal
import com.application.intercom.data.model.local.owner.gatePass.OwnerEditGatePassPostModel
import com.application.intercom.data.model.remote.owner.gatePass.OwnerGatepassList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.data.repository.tenantRepo.tenantHomeRepo.TenantHomeRepo
import com.application.intercom.databinding.ActivityOwnerCreateGatePassBinding
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.helper.setFormatDate
import com.application.intercom.owner.adapter.PhotoUploadAdapter
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class OwnerCreateGatePassActivity : BaseActivity<ActivityOwnerCreateGatePassBinding>(), AWSListner {

    override fun getLayout(): ActivityOwnerCreateGatePassBinding {
       return ActivityOwnerCreateGatePassBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: OwnerSideViewModel
    private lateinit var tenantviewModel: TenantHomeViewModel
    private lateinit var owner_viewModel: OwnerHomeViewModel
    private var flatOfBuildingList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingBuildingHashMapID: HashMap<String, String> = HashMap()
    private lateinit var select_flat_adapter: ArrayAdapter<String>
    private var flatOfBuildingId: String = ""
    private var from: String = ""
    private var imagePath: String = ""
    private var photoAdapter: PhotoUploadAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    val TIME_DIALOG_ID = 1111
    private var hour = 0
    private var minute = 0
    var giventime: String? = ""
    var fromDate: String = ""
    var key: String = ""
    var gatePassId: String = ""
    var edit_list: OwnerGatepassList.Data.Result? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from").toString()
        key = intent.getStringExtra("key").toString()
        println("----key$key")
        println("----from$from")
        if (key.equals("edit_tenant")) {
            edit_list = intent.getSerializableExtra("editList") as OwnerGatepassList.Data.Result
            println("-----eddtenant$edit_list")
            gatePassId = edit_list!!._id
            flatOfBuildingId = edit_list!!.flatId
            binding.edContact.setText(edit_list!!.contactName)
            binding.edWriteDescription.setText(edit_list!!.description)
            binding.edContactPhoneName.setText(edit_list!!.phoneNumber)
            binding.edExitDate.setText(setFormatDate(edit_list!!.toDate))
            binding.edExitTime.setText(edit_list!!.exitTime)
            fromDate = setFormatDate(edit_list!!.toDate).toString()
            giventime = edit_list!!.exitTime

            photo_upload_list.addAll(edit_list!!.photo)
            binding.rcyPhoto.visibility = View.VISIBLE
            binding.rcyPhoto.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            photoAdapter = PhotoUploadAdapter(this, photo_upload_list)
            binding.rcyPhoto.adapter = photoAdapter
            photoAdapter!!.notifyDataSetChanged()
        } else if (key.equals("edit_owner")) {
            edit_list = intent.getSerializableExtra("editList") as OwnerGatepassList.Data.Result
            println("-----eddo$edit_list")
            gatePassId = edit_list!!._id
            flatOfBuildingId = edit_list!!.flatId
            binding.edContact.setText(edit_list!!.contactName)
            binding.edWriteDescription.setText(edit_list!!.description)
            binding.edContactPhoneName.setText(edit_list!!.phoneNumber)
            binding.edExitDate.setText(setFormatDate(edit_list!!.toDate))
            binding.edExitTime.setText(edit_list!!.exitTime)
            fromDate = setFormatDate(edit_list!!.toDate).toString()
            giventime = edit_list!!.exitTime

            photo_upload_list.addAll(edit_list!!.photo)
            binding.rcyPhoto.visibility = View.VISIBLE
            binding.rcyPhoto.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            photoAdapter = PhotoUploadAdapter(this, photo_upload_list)
            binding.rcyPhoto.adapter = photoAdapter
            photoAdapter!!.notifyDataSetChanged()
        }
        initView()
        lstnr()
    }

    private fun initView() {
        flatOfBuildingList.add(0, "Choose Flat")
        binding.createToolbar.tvTittle.text = getString(R.string.create_gatepass)
        initialize()
        observer()

        if (from.equals("tenant")) {
            getTenantFlatList()
        } else if (key.equals("edit_tenant")) {
            getTenantFlatList()
        } else {
            getOwnerFlatList()

        }

        flatSpinner()

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
            SessionConstants.TOKEN, ""
        )
        owner_viewModel.ownerFlatList(token)
    }

    private fun getTenantFlatList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        owner_viewModel.tenantFlatList(token)
    }

    private fun createGatePass() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = OwnerAddGatePassPostModal(
            binding.edContact.text.trim().toString(),
            binding.edWriteDescription.text.trim().toString(),
            fromDate,
            giventime.toString(),
            flatOfBuildingId,
            binding.edContactPhoneName.text.trim().toString(),
            photo_upload_list
        )
        viewModel.addGatePassOwner(token, model)
    }

    private fun editGatePassOwner() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = OwnerEditGatePassPostModel(
            binding.edContact.text.trim().toString(),
            binding.edWriteDescription.text.trim().toString(),
            fromDate,
            giventime.toString(),
            flatOfBuildingId,
            gatePassId,
            binding.edContactPhoneName.text.trim().toString(),
            photo_upload_list
        )
        viewModel.editGatePassOwner(token, model)
    }

    private fun editGatePassTenant() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = OwnerEditGatePassPostModel(
            binding.edContact.text.trim().toString(),
            binding.edWriteDescription.text.trim().toString(),
            fromDate,
            giventime.toString(),
            flatOfBuildingId,
            gatePassId,
            binding.edContactPhoneName.text.trim().toString(),
            photo_upload_list
        )
        viewModel.editGatePassTenant(token, model)
    }

    private fun createGatePassTenant() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = OwnerAddGatePassPostModal(
            binding.edContact.text.trim().toString(),
            binding.edWriteDescription.text.trim().toString(),
            fromDate,
            giventime.toString(),
            flatOfBuildingId,
            binding.edContactPhoneName.text.trim().toString(),
            photo_upload_list
        )
        viewModel.addGatePassTenant(token, model)
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
                            if (key.equals("edit_owner")) {
                                binding.mainNew.visibility = View.VISIBLE
                                binding.chooseSpiner.setSelection(
                                    select_flat_adapter.getPosition(
                                        edit_list!!.flatInfo.name
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
        owner_viewModel.tenantFlatListLiveData.observe(this, androidx.lifecycle.Observer {
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
                            if (key.equals("edit_tenant")) {
                                println("==================$key")
                                binding.mainNew.visibility = View.VISIBLE
                                binding.chooseSpiner.setSelection(
                                    select_flat_adapter.getPosition(
                                        edit_list!!.flatInfo.name
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
        viewModel.addGatePassOwnerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(
                                    this,
                                    OwnerGatePassActivity::class.java
                                )
                            )
                            finish()
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
        viewModel.addGatePassTenantLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(this, OwnerGatePassActivity::class.java).putExtra(
                                    "from",
                                    from
                                )
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                            finish()
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
        viewModel.editGatePassOwnerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(this, OwnerGatePassActivity::class.java)
                            )
                            finish()
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
        viewModel.editGatePassTenantLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(
                                    this,
                                    OwnerGatePassActivity::class.java
                                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra(
                                        "from",
                                        "tenant"
                                    )
                            )
                            finish()
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

    private fun flatSpinner() {
        //val genderList = resources.getStringArray(com.application.intercom.R.array.EditProfile)
//        binding.chooseSpiner.adapter =
        select_flat_adapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, flatOfBuildingList)
        binding.chooseSpiner.adapter = select_flat_adapter
        binding.chooseSpiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                flatOfBuildingId =
                    flatOfBuildingHashMapID.get(binding.chooseSpiner.selectedItem.toString())
                        .toString()
                println("---flateId$flatOfBuildingId")

                if (binding.chooseSpiner.selectedItemPosition > 0) {
                    binding.mainNew.visibility = View.VISIBLE

                } else {
                    binding.mainNew.visibility = View.GONE
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
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
        if (resultCode == RESULT_OK) {
            if (!path.isNullOrEmpty()) {
                AWSUtils(
                    this, path, this
                )
            }
        } else if (requestCode == TakeImageWithCrop.GALLERY_REQUEST) {
            if (!path.isNullOrEmpty()) {
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
            imagePath = url!!
            binding.rcyPhoto.visibility = View.VISIBLE
            EmpCustomLoader.hideLoader()
            photo_upload_list.add(imagePath)
            println("-----photo${photo_upload_list}")
            binding.rcyPhoto.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            photoAdapter = PhotoUploadAdapter(this, photo_upload_list)
            binding.rcyPhoto.adapter = photoAdapter
            photoAdapter!!.notifyDataSetChanged()


        }
    }

    override fun onAWSError(error: String?) {
        EmpCustomLoader.hideLoader()
        binding.rcyPhoto.visibility = View.INVISIBLE
    }

    override fun onAWSProgress(progress: Int?) {
        EmpCustomLoader.showLoader(this)
        binding.rcyPhoto.visibility = View.VISIBLE
        Log.e("progress", progress!!.toString())
    }

    private fun lstnr() {
        binding.createToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.profileImg.setOnClickListener {
            showImagePickDialog()
        }
        binding.edExitTime.setOnClickListener {
            showDialog(TIME_DIALOG_ID)
        }
        binding.edExitDate.setOnClickListener {
            MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build().apply {
                show(supportFragmentManager, this@OwnerCreateGatePassActivity.toString())
                addOnPositiveButtonClickListener {
                    fromDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                        Date(it)
                    )

                    binding.edExitDate.setText(
                        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                            Date(it)
                        )
                    )
                }
            }
        }
        binding.commonBtn.tv.setOnClickListener {
            if (photo_upload_list.isEmpty()) {
                Toast.makeText(this, getString(R.string.please_select_img), Toast.LENGTH_SHORT).show()
            } else if (!validationData()) {
                return@setOnClickListener
            } else if (from.equals("tenant")) {
                createGatePassTenant()
            } else if (from.equals("owner")) {
                createGatePass()
            } else if (key.equals("edit_tenant")) {
                editGatePassTenant()
            } else {
                editGatePassOwner()
            }

        }

    }

    override fun onCreateDialog(id: Int): Dialog? {
        when (id) {
            TIME_DIALOG_ID ->
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

        giventime = aTime
        binding.edExitTime.setText(giventime)
        println("----time$giventime")

    }

    private fun validationData(): Boolean {
        if (binding.edContact.text.trim().toString().length < 4) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_name), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edContactPhoneName.text!!.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_phone_number), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.edContactPhoneName.text!!.trim()
                .toString().length < 10 || binding.edContactPhoneName.text!!.trim()
                .toString().length > 12
        ) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_valid_phone_number), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edWriteDescription.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_description), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edExitDate.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_select_exit_date), Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edExitTime.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, getString(R.string.please_select_exit_time), Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true

    }
}