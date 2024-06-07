package com.application.intercom.owner.activity.ownerCreateTenant

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.local.owner.OwnerAddTenantPostModel
import com.application.intercom.data.model.local.owner.OwnerUpdateTenantPostModel
import com.application.intercom.data.model.remote.owner.flat.OwnerFlatListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.databinding.ActivityOwnerCreateTenantBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.owner.activity.properties.OwnerPropertiesActivity
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


class OwnerCreateTenantActivity : BaseActivity<ActivityOwnerCreateTenantBinding>(), AWSListner {

    override fun getLayout(): ActivityOwnerCreateTenantBinding {
        return ActivityOwnerCreateTenantBinding.inflate(layoutInflater)
    }

    private var send_PropertyList: OwnerFlatListRes.Data? = null
    private var type: String = ""
    private var main_img: String = ""
    private var nid_one_img: String = ""
    private var nid_two_img: String = ""
    private var ref_one_img: String = ""
    private var ref_two_img: String = ""
    private var bttm_img: String = ""
    private var key: String = ""
    private lateinit var viewModel: OwnerHomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        send_PropertyList =
            intent.getSerializableExtra("send_property_list") as OwnerFlatListRes.Data
        println("----list${send_PropertyList}")
        key = intent.getStringExtra("flag").toString()
        initView()
        listener()
    }

    private fun initView() {
        initialize()
        observer()
        binding.ownerCreateToolbar.tvTittle.text = getString(R.string.create_a_tenant)
        binding.include2.tv.text = getString(R.string.add_tenant)


        ///fetchData
        binding.textView117.text = send_PropertyList!!.buildingInfo[0].address
        binding.textView116.text = send_PropertyList!!.buildingInfo[0].buildingName
        binding.imageView63.loadImagesWithGlideExt(send_PropertyList!!.buildingInfo[0].photos.get(0))
        if (key.equals("edit_tenant")) {
            val date = setNewFormatDate(send_PropertyList!!.tenant[0].dateOfOccupancy)
            binding.edSelectDate.setText(date)
            binding.edTFullName.setText(send_PropertyList!!.tenant[0].fullName)
            binding.edTEmail.setText(send_PropertyList!!.tenant[0].email)
            binding.edTPhone.setText(send_PropertyList!!.tenant[0].mobileNumber)
            binding.edTNID.setText(send_PropertyList!!.tenant[0].tenantNidNumber)
            binding.edTReffNID.setText(send_PropertyList!!.tenant[0].referenceNidNumber)
            /*val bill_date = setNewFormatDate(send_PropertyList!!.tenant[0].billingDate)
            binding.edTBilling.setText(bill_date)*/
            binding.edTMonthly.setText(send_PropertyList!!.tenant[0].roomRent)
            binding.edTParking.setText(send_PropertyList!!.tenant[0].parkingPrice)
            if (send_PropertyList!!.tenant.get(0).includeParking) {
                binding.radioMobile.isChecked = true
            } else {
                binding.radioEmail.isChecked = true
            }
            binding.edPassword.setText(send_PropertyList!!.tenant[0].planPassword ?: "")
            binding.imageView73.loadImagesWithGlideExt(send_PropertyList!!.tenant[0].photo)
            binding.imageView74.loadImagesWithGlideExt(send_PropertyList!!.tenant[0].tenantNidFront)
            binding.imageView75.loadImagesWithGlideExt(send_PropertyList!!.tenant[0].tenantNidBack)
            binding.imageView76.loadImagesWithGlideExt(send_PropertyList!!.tenant[0].referenceNidFront)
            binding.imageView77.loadImagesWithGlideExt(send_PropertyList!!.tenant[0].referenceNidBack)
            binding.imageView78.loadImagesWithGlideExt(send_PropertyList!!.tenant[0].photo)
            binding.edTFullName.isEnabled = false
            binding.edTEmail.isEnabled = false
            binding.edTPhone.isEnabled = false
            binding.edPassword.isEnabled = false
            binding.edTNID.isEnabled = false
            binding.edTParking.isEnabled = false
            binding.edTReffNID.isEnabled = false
            binding.edTMonthly.isEnabled = false
            binding.ownerCreateToolbar.tvTittle.text = "View Tenant"
            binding.ownerCreateToolbar.tvText.visibility = View.VISIBLE
            binding.ownerCreateToolbar.tvText.text = "Edit Tenant"

        }

    }

    private fun initialize() {
        val repo = OwnerHomeRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this, OwnerHomeFactory(repo)
        )[OwnerHomeViewModel::class.java]
    }

    private fun addOwnerTenant() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        val parking = binding.edTParking.text.trim().toString()
        val rent = binding.edTMonthly.text.trim().toString()
        val model = OwnerAddTenantPostModel(
            send_PropertyList!!.buildingId,
            send_PropertyList!!._id,
            binding.edTFullName.text.trim().toString(),
            binding.edTEmail.text.trim().toString(),
            binding.edTPhone.text.trim().toString(),
            binding.edTNID.text.trim().toString(),
            nid_one_img,
            nid_two_img,
            binding.edTReffNID.text.trim().toString(),
            ref_one_img,
            ref_two_img,
            if (binding.radioMobile.isChecked) true else false,
            /*binding.edTBilling.text.trim().toString(),*/
           /* rent.toInt(),*/
            parking.toInt(),
            main_img,
            binding.edSelectDate.text.trim().toString(),
            bttm_img,
            binding.edPassword.text.trim().toString()


        )
        viewModel.addOwnerTenant(token, model)

    }

    private fun updateOwnerTenant() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        val parking = binding.edTParking.text.trim().toString()
        val rent = binding.edTMonthly.text.trim().toString()
        val model = OwnerUpdateTenantPostModel(
            send_PropertyList!!.tenant.get(0)._id,
            bttm_img,
            /*binding.edTBilling.text.trim().toString(),*/
            send_PropertyList!!.buildingId,
            binding.edSelectDate.text.trim().toString(),
            binding.edTEmail.text.trim().toString(),
            send_PropertyList!!._id,
            binding.edTFullName.text.trim().toString(),
            if (binding.radioMobile.isChecked) true else false,
            binding.edTPhone.text.trim().toString(),
            parking.toInt(),
            main_img,
            ref_two_img,
            ref_one_img,
            binding.edTReffNID.text.trim().toString(),
           /* rent.toInt(),*/
            nid_two_img,
            nid_one_img,
            binding.edTNID.text.trim().toString(),
            binding.edPassword.text.trim().toString()
        )
        viewModel.updateOwnerTenant(token, model)

    }

    private fun observer() {
        viewModel.addOwnerTenantLiveData.observe(this) {
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
                                    OwnerPropertiesActivity::class.java
                                )/*.putExtra("from", "from_side_property")*/.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                            finish()
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_401) {
                            this.longToast(it.message)
                        } else {
                            CommonUtil.showSnackBar(this, "Something went wrong!!")
                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        }
        viewModel.updateOwnerTenantLiveData.observe(this) {
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
                                    OwnerPropertiesActivity::class.java
                                )/*.putExtra("from", "from_side_property")*/.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_401) {
                            this.longToast(it.message)
                        } else {
                            CommonUtil.showSnackBar(this, "Something went wrong!!")
                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        }

    }

    private fun listener() {
        binding.ownerCreateToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.include2.tv.setOnClickListener {
            if (validationData()) {
                if (key.equals("edit")) {
                    updateOwnerTenant()
                } else {
                    addOwnerTenant()
                }
                //return@setOnClickListener

            }

        }
        binding.ownerCreateToolbar.tvText.setOnClickListener {
            key = "edit"
            binding.ownerCreateToolbar.tvTittle.text = "Edit Tenant"
            binding.ownerCreateToolbar.tvText.text = "Edit"
            binding.edSelectDate.isClickable = true
            binding.radioEmail.isClickable = true
            binding.radioMobile.isClickable = true
            binding.edTBilling.isClickable = true
            binding.imageView73.isClickable = true
            binding.imageView74.isClickable = true
            binding.imageView75.isClickable = true
            binding.imageView76.isClickable = true
            binding.imageView77.isClickable = true
            binding.imageView78.isClickable = true
            binding.edTFullName.isEnabled = true
            binding.edTEmail.isEnabled = true
            binding.edTPhone.isEnabled = true
            binding.edPassword.isEnabled = true
            binding.edTNID.isEnabled = true
            binding.edTParking.isEnabled = true
            binding.edTReffNID.isEnabled = true
            binding.edTMonthly.isEnabled = true
        }
        binding.radioEmail.setOnClickListener {
            if (key == "edit_tenant") {
                binding.radioEmail.isClickable = false
                binding.radioMobile.isChecked = false
            } else {
                binding.radioEmail.isChecked = true
                binding.radioMobile.isChecked = false
            }

        }
        binding.radioMobile.setOnClickListener {
            if (key == "edit_tenant") {
                binding.radioEmail.isClickable = false
                binding.radioMobile.isChecked = false
            } else {
                binding.radioMobile.isChecked = true
                binding.radioEmail.isChecked = false

            }

        }
        binding.edSelectDate.setOnClickListener {
            if (key == "edit_tenant") {
                binding.edSelectDate.isClickable = false
            } else {
                MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build()
                    .apply {
                        show(supportFragmentManager, this@OwnerCreateTenantActivity.toString())
                        addOnPositiveButtonClickListener {
                            binding.edSelectDate.setText(
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    Date(it)
                                )
                            )
                        }
                    }
            }

        }
        binding.edTBilling.setOnClickListener {
            if (key == "edit_tenant") {
                binding.edTBilling.isClickable = false
            } else {
                MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build()
                    .apply {
                        show(supportFragmentManager, this@OwnerCreateTenantActivity.toString())
                        addOnPositiveButtonClickListener {
                            binding.edTBilling.setText(
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    Date(it)
                                )
                            )

                        }
                    }
            }

        }
        binding.imageView73.setOnClickListener {
            if (key == "edit_tenant") {
                binding.imageView73.isClickable = false
            } else {
                showImagePickDialog()
                type = "main_img"
            }

        }
        binding.imageView74.setOnClickListener {
            if (key == "edit_tenant") {
                binding.imageView74.isClickable = false
            } else {
                showImagePickDialog()
                type = "nid_one_img"
            }

        }
        binding.imageView75.setOnClickListener {
            if (key == "edit_tenant") {
                binding.imageView75.isClickable = false
            } else {
                showImagePickDialog()
                type = "nid_two_img"
            }

        }
        binding.imageView76.setOnClickListener {
            if (key == "edit_tenant") {
                binding.imageView76.isClickable = false
            } else {
                showImagePickDialog()
                type = "ref_one_img"
            }

        }
        binding.imageView77.setOnClickListener {
            if (key == "edit_tenant") {
                binding.imageView77.isClickable = false
            } else {
                showImagePickDialog()
                type = "ref_two_img"
            }

        }
        binding.imageView78.setOnClickListener {
            if (key == "edit_tenant") {
                binding.imageView78.isClickable = false
            } else {
                showImagePickDialog()
                type = "bottom_img"
            }
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
                binding.imageView73.setImageURI(currentImagePath)
//                binding.imageView73.loadImagesWithGlideExt(url)
            } else if (type.equals("nid_one_img")) {
                nid_one_img = url
                println("---nid_one_img${nid_one_img}")
                binding.pro.visibility = View.GONE
                binding.imageView74.setImageURI(currentImagePath)
//                binding.imageView74.loadImagesWithGlideExt(url)
            } else if (type.equals("nid_two_img")) {
                nid_two_img = url
                println("---nid_two_img${nid_two_img}")
                binding.pro.visibility = View.GONE
                //binding.imageView75.loadImagesWithGlideExt(url)
                binding.imageView75.setImageURI(currentImagePath)
            } else if (type.equals("ref_one_img")) {
                ref_one_img = url
                println("---ref_one_img${ref_one_img}")
                binding.pro.visibility = View.GONE
                //binding.imageView76.loadImagesWithGlideExt(url)
                binding.imageView76.setImageURI(currentImagePath)
            } else if (type.equals("ref_two_img")) {
                ref_two_img = url
                println("---ref_two_img${ref_two_img}")
                binding.pro.visibility = View.GONE
                //binding.imageView77.loadImagesWithGlideExt(url)
                binding.imageView77.setImageURI(currentImagePath)
            } else if (type.equals("bottom_img")) {
                bttm_img = url
                println("---bttm_img${bttm_img}")
                binding.pro.visibility = View.GONE
                //binding.imageView78.loadImagesWithGlideExt(url)
                binding.imageView78.setImageURI(currentImagePath)
            }

        }
    }

    override fun onAWSError(error: String?) {
        EmpCustomLoader.hideLoader()
    }

    override fun onAWSProgress(progress: Int?) {
        EmpCustomLoader.showLoader(this)

    }

    private fun validationData(): Boolean {
        if (binding.edTFullName.text.trim().toString().length < 4) {
            Toast.makeText(
                applicationContext, "Please Enter Name!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } /*else if (binding.radioEmail.isChecked || binding.radioMobile.isChecked) {
            Toast.makeText(
                applicationContext,
                "Please Allot Parking!!",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }*/ else if (binding.imageView73.drawable == null) {
            Toast.makeText(
                applicationContext,
                "Please Select Image!!",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edTEmail.text.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Email Id!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (!Validator.isValidEmail(
                CommonUtil.getProperText(
                    binding.edTEmail
                )
            )
        ) {
            Toast.makeText(this, "Invalid Email Id!!", Toast.LENGTH_SHORT).show()
            return false

        } else if (TextUtils.isEmpty(binding.edTPhone.text!!.trim().toString())) {
            Toast.makeText(
                applicationContext,
                "Please Enter Phone Number !!",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.edTPhone.text!!.trim()
                .toString().length < 10 || binding.edTPhone.text!!.trim()
                .toString().length > 12
        ) {
            Toast.makeText(
                applicationContext,
                "Please Enter Valid Phone Number !!",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edPassword.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Password!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edTNID.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter NID Number!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edTReffNID.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Reference NID Number!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } /*else if (TextUtils.isEmpty(binding.edTBilling.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Select Billing Date!!", Toast.LENGTH_SHORT
            ).show()
            return false
        }*/ else if (TextUtils.isEmpty(binding.edSelectDate.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Select Occupancy Date!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } /*else if (TextUtils.isEmpty(binding.edTMonthly.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Monthly Room Rent!!", Toast.LENGTH_SHORT
            ).show()
            return false
        }*/ else if (TextUtils.isEmpty(binding.edTParking.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Monthly Rent Parking!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.imageView74.drawable == null) {
            Toast.makeText(
                applicationContext,
                "Please Select NID Front Side Image!!",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.imageView75.drawable == null) {
            Toast.makeText(
                applicationContext,
                "Please Select NID Back Side Image!!",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.imageView76.drawable == null) {
            Toast.makeText(
                applicationContext,
                "Please Select Reference NID Front Side Image!!",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.imageView77.drawable == null) {
            Toast.makeText(
                applicationContext,
                "Please Select Reference NID Back Side Image!!",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.imageView78.drawable == null) {
            Toast.makeText(
                applicationContext,
                "Please Select Rent Agreement Image!!",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true

    }
}