package com.application.intercom.manager.gatekeeper

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.manager.managerSide.gateKeeper.ManagerCreateGateKeeperPostModel
import com.application.intercom.data.model.local.manager.managerSide.gateKeeper.ManagerEditGateKeeperPostModel
import com.application.intercom.data.model.remote.manager.managerSide.ManagerGateKepperListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityCreateGateKepeerBinding
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner


class CreateGateKepeerActivity : AppCompatActivity(), AWSListner {

    private lateinit var binding: ActivityCreateGateKepeerBinding
    val TIME_DIALOG_ID = 1111
    val TIME_DIALOG_ID1 = 1112
    private var hour = 0
    private var minute = 0
    var giventime: String? = ""
    var giventimend: String? = ""
    var typeetime = ""

    private var type: String = ""
    private var main_img: String = ""
    private var nid_one_img: String = ""
    private var nid_two_img: String = ""
    private var doc_one_img: String = ""
    private var doc_two_img: String = ""
    private var ref_one_img: String = ""
    private var ref_two_img: String = ""
    private lateinit var viewModel: ManagerSideViewModel
    private var key: String = ""
    private var gateKeeper_list: ManagerGateKepperListRes.Data.Result? = null
    private var id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGateKepeerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        key = intent.getStringExtra("from").toString()
        if (key.equals("edit")) {
            gateKeeper_list =
                intent.getSerializableExtra("gateKeeperList") as ManagerGateKepperListRes.Data.Result
            id = gateKeeper_list!!._id
            println("---id$id")
            editfetchData()
        } else {
            binding.toolbar.tvTittle.text = getString(R.string.create_gatekeeper)
            binding.btnAddGatekeeper.tv.text = getString(R.string.add_gatekeeper)
        }
        initView()
        listener()
    }

    private fun editfetchData() {
        binding.edtUploadAdditionalDocument.setText(gateKeeper_list!!.document)
        binding.edtFatherName.setText(gateKeeper_list!!.fatherName)
        binding.edtFullName.setText(gateKeeper_list!!.fullName)
        binding.edtMobileNumber.setText(gateKeeper_list!!.mobileNumber)
        binding.edtMotherName.setText(gateKeeper_list!!.motherName)
        binding.edtNidNumber.setText(gateKeeper_list!!.nidNumber)
        binding.edtRefMobileNumber.setText(gateKeeper_list!!.referenceMobile)
        binding.edtRefFullName.setText(gateKeeper_list!!.referenceName)
        binding.edtRefNidNumber.setText(gateKeeper_list!!.referenceNidNumber)
        binding.edtShiftStartTime.setText(gateKeeper_list!!.shiftStartTime)
        binding.edtTime.setText(gateKeeper_list!!.shiftEndTime)
        binding.edtReEnterPassword.isEnabled = false
        binding.edtGeneratePassword.isEnabled = false
        binding.edtReEnterPassword.setText(gateKeeper_list!!.plainPassword)
        binding.edtGeneratePassword.setText(gateKeeper_list!!.plainPassword)
        binding.ivUploadPhoto.loadImagesWithGlideExt(gateKeeper_list!!.photo)
        binding.ivNidFrontSide.loadImagesWithGlideExt(gateKeeper_list!!.nidFront)
        binding.ivNidBackSide.loadImagesWithGlideExt(gateKeeper_list!!.nidBack)
        binding.ivRefFrontSide.loadImagesWithGlideExt(gateKeeper_list!!.referenceNidFront)
        binding.ivRefBackSide.loadImagesWithGlideExt(gateKeeper_list!!.referenceNidBack)
        binding.ivUploadFrontSide.loadImagesWithGlideExt(gateKeeper_list!!.documentFront)
        binding.ivUploadBackSide.loadImagesWithGlideExt(gateKeeper_list!!.documentBack)
        main_img = gateKeeper_list!!.photo
        nid_one_img = gateKeeper_list!!.nidFront
        nid_two_img = gateKeeper_list!!.nidBack
        ref_one_img = gateKeeper_list!!.referenceNidFront
        ref_two_img = gateKeeper_list!!.referenceNidBack
        doc_one_img = gateKeeper_list!!.documentFront
        doc_two_img = gateKeeper_list!!.documentBack
        binding.toolbar.tvTittle.text = getString(R.string.update_gatekeeper)
        binding.btnAddGatekeeper.tv.text = getString(R.string.update_gatekeeper)
    }

    private fun initView() {
        initialize()
        observer()


    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

    private fun createGateKeeper() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = ManagerCreateGateKeeperPostModel(
            binding.edtUploadAdditionalDocument.text.trim().toString(),
            doc_two_img,
            doc_one_img,
            binding.edtFatherName.text.trim().toString(),
            binding.edtFullName.text.trim().toString(),
            binding.edtMobileNumber.text.trim().toString(),
            binding.edtMotherName.text.trim().toString(),
            nid_two_img,
            nid_one_img,
            binding.edtNidNumber.text.trim().toString(),
            binding.edtReEnterPassword.text.trim().toString(),
            main_img,
            binding.edtRefMobileNumber.text.trim().toString(),
            binding.edtRefFullName.text.trim().toString(),
            ref_two_img,
            ref_one_img,
            binding.edtRefMobileNumber.text.trim().toString(),
            binding.edtTime.text.trim().toString(),
            binding.edtShiftStartTime.text.trim().toString()

        )
        viewModel.createGateKeeper(token, model)
    }

    private fun editGateKeeper() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = ManagerEditGateKeeperPostModel(
            id,
            binding.edtUploadAdditionalDocument.text.trim().toString(),
            doc_two_img,
            doc_one_img,
            binding.edtFatherName.text.trim().toString(),
            binding.edtFullName.text.trim().toString(),
            binding.edtMobileNumber.text.trim().toString(),
            binding.edtMotherName.text.trim().toString(),
            nid_two_img,
            nid_one_img,
            binding.edtNidNumber.text.trim().toString(),
            binding.edtReEnterPassword.text.trim().toString(),
            main_img,
            binding.edtRefMobileNumber.text.trim().toString(),
            binding.edtRefFullName.text.trim().toString(),
            ref_two_img,
            ref_one_img,
            binding.edtRefMobileNumber.text.trim().toString(),
            binding.edtTime.text.trim().toString(),
            binding.edtShiftStartTime.text.trim().toString()


        )
        viewModel.editGateKeeper(token, model)
    }

    private fun observer() {
        viewModel.createGateKeeperLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
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
        viewModel.editGateKeeperLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
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
    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.btnAddGatekeeper.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            } else if (key.equals("edit")) {
                editGateKeeper()
            } else {
                createGateKeeper()
            }

        }
        binding.edtTime.setOnClickListener {
            endTime()
        }
        binding.edtShiftStartTime.setOnClickListener {
            selectStartTime()
        }
        binding.ivUploadPhoto.setOnClickListener {
            showImagePickDialog()
            type = "main_img"
        }
        binding.ivNidFrontSide.setOnClickListener {
            showImagePickDialog()
            type = "nid_one_img"
        }
        binding.ivNidBackSide.setOnClickListener {
            showImagePickDialog()
            type = "nid_two_img"
        }
        binding.ivRefFrontSide.setOnClickListener {
            showImagePickDialog()
            type = "ref_one_img"
        }
        binding.ivRefBackSide.setOnClickListener {
            showImagePickDialog()
            type = "ref_two_img"
        }
        binding.ivUploadFrontSide.setOnClickListener {
            showImagePickDialog()
            type = "doc_one_img"
        }
        binding.ivUploadBackSide.setOnClickListener {
            showImagePickDialog()
            type = "doc_two_img"
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

    private fun endTime() {
        typeetime = "end"
        showDialog(TIME_DIALOG_ID)
    }

    private fun selectStartTime() {
        typeetime = "start"
        showDialog(TIME_DIALOG_ID)
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
            binding.edtShiftStartTime.setText(giventime)
        } else if (typeetime.equals("end")) {
            giventimend = aTime
            binding.edtTime.setText(giventimend)
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
    private var currentImagePath: Uri = Uri.EMPTY
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val path = data?.getStringExtra("filePath")
        if (resultCode == RESULT_OK) {
            if (!path.isNullOrEmpty()) {
                currentImagePath = Uri.parse(path)
                EmpCustomLoader.showLoader(this)
                AWSUtils(
                    this, path, this
                )
            }
        } else if (requestCode == TakeImageWithCrop.GALLERY_REQUEST) {
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
        if (key.equals("edit")) {
            if (binding.edtFullName.text.trim().toString().length < 4) {
                Toast.makeText(
                    applicationContext, getString(R.string.please_enter_name), Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.edtFatherName.text.trim().toString().length < 4) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_father_name),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.edtMotherName.text.trim().toString().length < 4) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_mother_name),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.ivUploadPhoto.drawable == null) {
                Toast.makeText(
                    applicationContext, getString(R.string.please_select_image), Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtMobileNumber.text!!.trim().toString())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_phone_number),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.edtMobileNumber.text!!.trim()
                    .toString().length < 10 || binding.edtMobileNumber.text!!.trim()
                    .toString().length > 12
            ) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_valid_phone_number),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtRefMobileNumber.text!!.trim().toString())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_reference_phone_number),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.edtMobileNumber.text!!.trim()
                    .toString().length < 10 || binding.edtMobileNumber.text!!.trim()
                    .toString().length > 12
            ) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_valid_reference_phone_number),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtNidNumber.text?.trim().toString())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_nid_number),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtRefNidNumber.text?.trim().toString())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_refr_nid_number),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(
                    binding.edtUploadAdditionalDocument.text?.trim().toString()
                )
            ) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_additional_document),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtShiftStartTime.text?.trim().toString())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_shift_date),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtRefFullName.text?.trim().toString())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_ref_name),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.ivNidFrontSide.drawable == null) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_nid_front_side_img),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.ivNidBackSide.drawable == null) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_nid_back_side_img),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.ivRefFrontSide.drawable == null) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_ref_nid_front_side_img),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.ivRefBackSide.drawable == null) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_fre_nid_back_side_img),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.ivUploadFrontSide.drawable == null) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_additi_docu_front_side_img),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.ivUploadBackSide.drawable == null) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_additi_docu_back_side_img),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        } else {
            val createpassword: String = binding.edtGeneratePassword.text?.trim().toString()
            val cnfrmPassword: String = binding.edtReEnterPassword.text?.trim().toString()
            if (binding.edtFullName.text.trim().toString().length < 4) {
                Toast.makeText(
                    applicationContext, getString(R.string.please_enter_name), Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.edtFatherName.text.trim().toString().length < 4) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_father_name),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.edtMotherName.text.trim().toString().length < 4) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_mother_name),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.ivUploadPhoto.drawable == null) {
                Toast.makeText(
                    applicationContext, getString(R.string.please_select_image), Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtMobileNumber.text!!.trim().toString())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_phone_number),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.edtMobileNumber.text!!.trim()
                    .toString().length < 10 || binding.edtMobileNumber.text!!.trim()
                    .toString().length > 12
            ) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_valid_phone_number),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtRefMobileNumber.text!!.trim().toString())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_reference_phone_number),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.edtMobileNumber.text!!.trim()
                    .toString().length < 10 || binding.edtMobileNumber.text!!.trim()
                    .toString().length > 12
            ) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_valid_reference_phone_number),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtGeneratePassword.text?.trim().toString())) {
                Toast.makeText(
                    this,
                    getString(R.string.please_enter_generate_password),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtReEnterPassword.text?.trim().toString())) {
                Toast.makeText(
                    this,
                    getString(R.string.please_enter_reenter_password),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (!createpassword.equals(cnfrmPassword)) {
                Toast.makeText(
                    this,
                    getString(R.string.reEnter_password_does_not_match),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtNidNumber.text?.trim().toString())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_nid_number),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtRefNidNumber.text?.trim().toString())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_refr_nid_number),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(
                    binding.edtUploadAdditionalDocument.text?.trim().toString()
                )
            ) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_additional_document),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtShiftStartTime.text?.trim().toString())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_shift_date),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtRefFullName.text?.trim().toString())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_ref_name),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.ivNidFrontSide.drawable == null) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_nid_front_side_img),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.ivNidBackSide.drawable == null) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_nid_back_side_img),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.ivRefFrontSide.drawable == null) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_ref_nid_front_side_img),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.ivRefBackSide.drawable == null) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_fre_nid_back_side_img),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.ivUploadFrontSide.drawable == null) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_additi_docu_front_side_img),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.ivUploadBackSide.drawable == null) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_additi_docu_back_side_img),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }

        return true

    }

    override fun onAWSLoader(isLoader: Boolean) {
       EmpCustomLoader.showLoader(this)
    }

    override fun onAWSSuccess(url: String?) {
        if (url != null) {
            EmpCustomLoader.hideLoader()
            if (type.equals("main_img")) {
                main_img = url
                println("---main${main_img}")
                binding.pro.visibility = View.GONE
//                binding.ivUploadPhoto.loadImagesWithGlideExt(url)
                binding.ivUploadPhoto.setImageURI(currentImagePath)
            } else if (type.equals("nid_one_img")) {
                nid_one_img = url
                println("---nid_one_img${nid_one_img}")
                binding.pro.visibility = View.GONE
                binding.ivNidFrontSide.setImageURI(currentImagePath)
            } else if (type.equals("nid_two_img")) {
                nid_two_img = url
                println("---nid_two_img${nid_two_img}")
                binding.pro.visibility = View.GONE
                binding.ivNidBackSide.setImageURI(currentImagePath)
            } else if (type.equals("doc_one_img")) {
                doc_one_img = url
                println("---doc_one_img${doc_one_img}")
                binding.pro.visibility = View.GONE
                binding.ivUploadFrontSide.setImageURI(currentImagePath)
            } else if (type.equals("doc_two_img")) {
                doc_two_img = url
                println("---doc_two_img${doc_two_img}")
                binding.pro.visibility = View.GONE
                binding.ivUploadBackSide.setImageURI(currentImagePath)
            } else if (type.equals("ref_one_img")) {
                ref_one_img = url
                println("---ref_one_img${ref_one_img}")
                binding.pro.visibility = View.GONE
                binding.ivRefFrontSide.setImageURI(currentImagePath)
            } else if (type.equals("ref_two_img")) {
                ref_two_img = url
                println("---ref_two_img${ref_two_img}")
                binding.pro.visibility = View.GONE
                binding.ivRefBackSide.setImageURI(currentImagePath)
            } /*else if (type.equals("bottom_img")) {
                bttm_img = url
                println("---bttm_img${bttm_img}")
                binding.pro.visibility = View.GONE
                binding.imageView78.loadImagesWithGlideExt(url)
            }*/

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

}