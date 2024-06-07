package com.application.intercom.manager.newFlow

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.manager.managerSide.newflow.AddExpensesManagerPostModel
import com.application.intercom.data.model.remote.manager.managerSide.newflow.EditUnPaidManagerPostModel
import com.application.intercom.data.model.remote.manager.managerSide.newflow.UnPaidExpensesManagerRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityAddExpensesManagerBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.manager.newFlow.expenses.ManagerExpensesActivity
import com.application.intercom.owner.adapter.PhotoUploadAdapter
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddExpensesManagerActivity : AppCompatActivity(), AWSListner {
    lateinit var binding: ActivityAddExpensesManagerBinding
    private var imagePath: String = ""
    private var photoAdapter: PhotoUploadAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    private lateinit var viewModel: ManagerSideViewModel
    private var categoryId: String = ""
    private var buildingId: String = ""
    private var categoryName: String = ""
    private var categoryList = ArrayList<String>()
    private var categoryHashMapID: HashMap<String, String> = HashMap()
    private var already: String = ""
    private var billId: String = ""
    private var from: String = ""
    private var editList: UnPaidExpensesManagerRes.Data.Result? = null
    private lateinit var selectFlatspinnerAdapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpensesManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        from = intent.getStringExtra("from") ?: ""
        if (from == "edit") {
            editList =
                intent.getSerializableExtra("editList") as UnPaidExpensesManagerRes.Data.Result?
            println("=======$editList")
            if (!editList?.categoryId.isNullOrEmpty()) {
                binding.edtDesi.visibility = View.GONE
                binding.tvSelectFlat1.visibility = View.GONE
                billId = editList?._id ?: ""
                buildingId = editList?.buildingId ?: ""
                binding.edtDesi.setText(editList?.expenseName ?: "")
                binding.edtAmount.setText(editList?.expenseAmount?.toString())
                binding.edWriteDescription.setText(editList?.expenseDetail ?: "")
                binding.edtDate.setText(setNewFormatDate(editList?.date ?: ""))
                /*binding.rcyPhoto.visibility = View.VISIBLE*/
                imagePath = editList?.uploadBill?.get(0).toString()
                binding.ivUpload.visibility = View.VISIBLE
                binding.ivUpload.loadImagesWithGlideExt(imagePath)
                /*photo_upload_list.add(imagePath)
                println("-----photo${photo_upload_list}")
                binding.rcyPhoto.layoutManager =
                    LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                photoAdapter = PhotoUploadAdapter(this, photo_upload_list)
                binding.rcyPhoto.adapter = photoAdapter
                photoAdapter!!.notifyDataSetChanged()*/
            } else {
                billId = editList?._id ?: ""
                buildingId = editList?.buildingId ?: ""
                categoryName = "Other"
                binding.edtDesi.visibility = View.VISIBLE
                binding.tvSelectFlat1.visibility = View.VISIBLE
                binding.edtDesi.setText(editList?.expenseName ?: "")
                binding.edtAmount.setText(editList?.expenseAmount?.toString())
                binding.edWriteDescription.setText(editList?.expenseDetail ?: "")
                binding.edtDate.setText(setNewFormatDate(editList?.date ?: ""))
                /*binding.rcyPhoto.visibility = View.VISIBLE*/
                binding.ivUpload.visibility = View.VISIBLE
                imagePath = editList?.uploadBill?.get(0).toString()
                binding.ivUpload.loadImagesWithGlideExt(imagePath)
                /*photo_upload_list.add(imagePath)
                println("-----photo${photo_upload_list}")
                binding.rcyPhoto.layoutManager =
                    LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                photoAdapter = PhotoUploadAdapter(this, photo_upload_list)
                binding.rcyPhoto.adapter = photoAdapter
                photoAdapter!!.notifyDataSetChanged()*/

            }
        }
        buildingId = prefs.getString(SessionConstants.NEWBUILDINGID, "")
        println("====bu$buildingId")
        initView()
        lstnr()
    }

    private fun initView() {
        categoryList.add(0, "Select Category")
        initialize()
        observer()
        expensesCategoryList()
        categorySpinner()
        binding.btnLogin.tv.text = getString(R.string.already_paid)
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(this, R.color.orange))

        binding.btnLogin1.tv.text = getString(R.string.add_bill)
        binding.btnLogin1.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.btnLogin1.tv.setTextColor(ContextCompat.getColor(this, R.color.orange))
    }

    private fun lstnr() {
        binding.imageView4.setOnClickListener {
            finish()
        }
        binding.edUpload.setOnClickListener {
            //dialogMedia()
            showImagePickDialog()
        }
        binding.btnLogin.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            }
            already = "alReady"
            paidAlreadyExpenses()
            //startActivity(Intent(this, DetailPaymentManagerActivity::class.java))
        }
        binding.btnLogin1.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            } else if (from == "edit") {
                editExpenses()
            } else {
                addExpenses()
            }

            /*startActivity(Intent(this, DetailPaymentManagerActivity::class.java))*/

        }
        binding.edtDate.setOnClickListener {
            MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build().apply {
                show(supportFragmentManager, this@AddExpensesManagerActivity.toString())
                addOnPositiveButtonClickListener {
                    binding.edtDate.setText(
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                            Date(it)
                        )
                    )
                }
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
                EmpCustomLoader.showLoader(this)
                currentImagePath = Uri.parse(path)
                AWSUtils(
                    this, path, this
                )
            }
        } else if (requestCode == TakeImageWithCrop.GALLERY_REQUEST) {
            if (!path.isNullOrEmpty()) {
                EmpCustomLoader.showLoader(this)
                currentImagePath = Uri.parse(path)
                AWSUtils(
                    this, path, this
                )
            }
        } /*else if (requestCode == PICK_VIDEO_REQUESTT) {
            val selectedVideoUri: Uri? = data?.data
            if (selectedVideoUri != null) {
                val videoPath: String = getRealPathFromURI(selectedVideoUri).toString().trim()
                uploadVideoToS3(videoPath)
            }
        }*/
    }

    override fun onAWSLoader(isLoader: Boolean) {
        EmpCustomLoader.showLoader(this)
    }

    override fun onAWSSuccess(url: String?) {
        if (url != null) {
            imagePath = url
            println("---url$url")
            /*binding.rcyPhoto.visibility = View.VISIBLE*/
            EmpCustomLoader.hideLoader()
            binding.ivUpload.visibility = View.VISIBLE
            binding.ivUpload.loadImagesWithGlideExt(currentImagePath.toString())
            /*photo_upload_list.add(imagePath)
            println("-----photo${photo_upload_list}")
            binding.rcyPhoto.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            photoAdapter = PhotoUploadAdapter(this, photo_upload_list)
            binding.rcyPhoto.adapter = photoAdapter
            photoAdapter!!.notifyDataSetChanged()*/

        }
    }

    override fun onAWSError(error: String?) {
        EmpCustomLoader.hideLoader()
        binding.ivUpload.visibility = View.VISIBLE
        /* binding.rcyPhoto.visibility = View.INVISIBLE*/
        Log.e("error", error ?: "")
    }

    override fun onAWSProgress(progress: Int?) {
        EmpCustomLoader.showLoader(this)
        binding.ivUpload.visibility = View.VISIBLE
        /*binding.rcyPhoto.visibility = View.VISIBLE*/
        Log.e("progress", progress!!.toString())
    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

    private fun expensesCategoryList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.expensesCategoryList(token)

    }

    private fun addExpenses() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        if (categoryName == "Other") {
            val model = AddExpensesManagerPostModel(
                "Add",
                buildingId,
                null,
                binding.edtDate.text.trim().toString(),
                binding.edtAmount.text.trim().toString(),
                binding.edWriteDescription.text.trim().toString(),
                binding.edtDesi.text.trim().toString(),
                imagePath,
                prefs.getString(SessionConstants.PROJECTID, "")
            )
            viewModel.addExpensesManager(token, model)
        } else {
            val model = AddExpensesManagerPostModel(
                "Add",
                buildingId,
                categoryId,
                binding.edtDate.text.trim().toString(),
                binding.edtAmount.text.trim().toString(),
                binding.edWriteDescription.text.trim().toString(),
                categoryName,
                imagePath,
                prefs.getString(SessionConstants.PROJECTID, "")
            )
            viewModel.addExpensesManager(token, model)
        }


    }

    private fun editExpenses() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        if (categoryName == "Other") {
            val model = EditUnPaidManagerPostModel(
                billId,
                buildingId,
                null,
                binding.edtDate.text.trim().toString(),
                binding.edtAmount.text.trim().toString(),
                binding.edWriteDescription.text.trim().toString(),
                binding.edtDesi.text.trim().toString(),
                imagePath


            )
            viewModel.editUnPaidManager(token, model)
        } else {
            val model = EditUnPaidManagerPostModel(
                billId,
                buildingId,
                categoryId,
                binding.edtDate.text.trim().toString(),
                binding.edtAmount.text.trim().toString(),
                binding.edWriteDescription.text.trim().toString(),
                categoryName,
                imagePath
            )
            viewModel.editUnPaidManager(token, model)
        }


    }


    private fun paidAlreadyExpenses() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        if (categoryName == "Other") {
            val model = AddExpensesManagerPostModel(
                "Add",
                buildingId,
                null,
                binding.edtDate.text.trim().toString(),
                binding.edtAmount.text.trim().toString(),
                binding.edWriteDescription.text.trim().toString(),
                binding.edtDesi.text.trim().toString(),
                imagePath,
                prefs.getString(SessionConstants.PROJECTID, "")
            )
            viewModel.addExpensesManager(token, model)
        } else {
            val model = AddExpensesManagerPostModel(
                "Add",
                buildingId,
                categoryId,
                binding.edtDate.text.trim().toString(),
                binding.edtAmount.text.trim().toString(),
                binding.edWriteDescription.text.trim().toString(),
                categoryName,
                imagePath,
                prefs.getString(SessionConstants.PROJECTID, "")
            )
            viewModel.addExpensesManager(token, model)
        }


    }

    private fun observer() {
        viewModel.expensesCategoryListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            for (data in it.data) {
                                categoryList.add(data.name)
                                categoryHashMapID.put(data.name, data._id)
                            }
                            categoryList.add("Other")
//                            selectFlatspinnerAdapter.clear()
//                            selectFlatspinnerAdapter.addAll(categoryList)
//                            selectFlatspinnerAdapter.notifyDataSetChanged()
                            selectFlatspinnerAdapter =
                                ArrayAdapter(this, R.layout.spinner_dropdown_item, categoryList)
                            binding.spinnerSelectFlat.adapter = selectFlatspinnerAdapter
                            println("=====listCat$categoryList")
                            if (from == "edit") {
                                if (!editList?.categoryId.isNullOrEmpty()) {
                                    binding.spinnerSelectFlat.setSelection(
                                        selectFlatspinnerAdapter.getPosition(
                                            editList?.expenseName ?: ""
                                        )
                                    )
                                } else {
                                    binding.spinnerSelectFlat.setSelection(
                                        selectFlatspinnerAdapter.getPosition(
                                            "Other"
                                        )
                                    )

                                }
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
        viewModel.addExpensesManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            if (already == "alReady") {
                                startActivity(
                                    Intent(
                                        this, ManagerPaymentActivity::class.java
                                    ).putExtra("expenseId", it.data._id ?: "")
                                        .putExtra("amount", it.data.expenseAmount ?: "")
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                )
                                finish()
                            } else {
                                startActivity(
                                    Intent(
                                        this, ManagerExpensesActivity::class.java
                                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                )
                                finish()
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
        viewModel.editUnPaidManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            if (already == "alReady") {
                                startActivity(
                                    Intent(
                                        this, ManagerPaymentActivity::class.java
                                    )/*.putExtra("expenseId", billId)
                                        .putExtra("amount", editList!!.expenseAmount ?: "")*/
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                )
                                finish()
                            } else {
                                startActivity(
                                    Intent(
                                        this, ManagerExpensesActivity::class.java
                                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                )
                                finish()
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
    }

    private fun categorySpinner() {
        //val genderList = resources.getStringArray(R.array.EditProfile)
        selectFlatspinnerAdapter =
            ArrayAdapter(this, R.layout.spinner_dropdown_item, categoryList)
        binding.spinnerSelectFlat.adapter = selectFlatspinnerAdapter
        binding.spinnerSelectFlat.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (binding.spinnerSelectFlat.selectedItemPosition > 0) {
                        categoryId =
                            categoryHashMapID.get(binding.spinnerSelectFlat.selectedItem.toString())
                                .toString()
                        categoryName = binding.spinnerSelectFlat.selectedItem.toString()
                        if (categoryName == "Other") {
                            binding.edtDesi.visibility = View.VISIBLE
                            binding.tvSelectFlat1.visibility = View.VISIBLE
                        } else {
                            binding.edtDesi.visibility = View.GONE
                            binding.tvSelectFlat1.visibility = View.GONE
                        }

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun validationData(): Boolean {
        if (categoryName == "Other") {
            if (TextUtils.isEmpty(binding.edtDesi.text?.trim().toString())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_other_category),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtAmount.text?.trim().toString())) {
                Toast.makeText(
                    applicationContext, getString(R.string.please_enter_amount), Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtDate.text?.trim().toString())) {
                Toast.makeText(
                    applicationContext, getString(R.string.please_select_date), Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edWriteDescription.text?.trim().toString())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_description),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        } else {
            if (binding.spinnerSelectFlat.selectedItemPosition.equals(0)) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_category),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtAmount.text?.trim().toString())) {
                Toast.makeText(
                    applicationContext, getString(R.string.please_enter_amount), Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edtDate.text?.trim().toString())) {
                Toast.makeText(
                    applicationContext, getString(R.string.please_select_date), Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edWriteDescription.text?.trim().toString())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_enter_description),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }

        return true

    }

}