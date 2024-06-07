package com.application.intercom.tenant.activity.registerComplain

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
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import com.application.intercom.R
import com.application.intercom.aws.AWSConstants
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.ViewModel.tenantViewModel.TenantSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.tenantFactory.TenantSideFactory
import com.application.intercom.data.model.local.owner.registerComplain.OwnerRegisterComplainPostModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ServiceRepository
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.data.repository.tenantRepo.TenantSideRepo
import com.application.intercom.databinding.ActivityTenantAddRegisterComplainBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.owner.adapter.PhotoUploadAdapter
import com.application.intercom.owner.adapter.SpinnerAdapterBrands
import com.application.intercom.user.service.ServiceFactory
import com.application.intercom.user.service.ServiceViewModel
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner
import java.io.File


class TenantAddRegisterComplainActivity : BaseActivity<ActivityTenantAddRegisterComplainBinding>(), AWSListner {
    private lateinit var viewModel: ServiceViewModel
    override fun getLayout(): ActivityTenantAddRegisterComplainBinding {
        return ActivityTenantAddRegisterComplainBinding.inflate(layoutInflater)
    }

    private lateinit var regi: TenantSideViewModel
    private var serviceCategoryList = ArrayList<String>()
    private var serviceCategoryHashMapID: HashMap<String, String> = HashMap()
    private var serviceCategoryId: String = ""
    private var imagePath: String = ""
    private var buildingId: String = ""
    private var photoAdapter: PhotoUploadAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    private var from: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buildingId = prefs.getString(
            SessionConstants.BUILDINGID,
           ""
        )
        println("---tenantBuildId$buildingId")
        from = intent.getStringExtra("from").toString()
        println("----from$from")
        initView()
        lstnr()
    }

    private fun initView() {
        serviceCategoryList.add(0, "Select Service Category")
        init()
        observer()
        getServicesList()
        binding.complainToolbar.tvTittle.text = getString(R.string.register_copmlain)
        binding.commonBtn.tv.text = getString(R.string.register_copmlain)

    }

    private fun spinnerForSubCategoryList(memberList: List<String>) {
        val adapter =
            SpinnerAdapterBrands(
                this,
                R.layout.spinner_dropdown_item, memberList
            )
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = onItemSelectedStateListenerMemberList

    }

    private var onItemSelectedStateListenerMemberList: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            adapterView: AdapterView<*>?,
            view: View,
            postion: Int,
            l: Long
        ) {
            val itemSelected = adapterView!!.getItemAtPosition(postion) as String
            binding.edService.text = itemSelected

            serviceCategoryId =
                serviceCategoryHashMapID.get(binding.spinner.selectedItem.toString()).toString()
            println("---memberId$serviceCategoryId")

        }

        override fun onNothingSelected(adapterView: AdapterView<*>?) {}
    }

    private fun init() {
        val repo = ServiceRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, ServiceFactory(repo))[ServiceViewModel::class.java]

        val register = TenantSideRepo(BaseApplication.apiService)
        regi = ViewModelProvider(
            this,
            TenantSideFactory(register)
        )[TenantSideViewModel::class.java]
    }

    private fun getServicesList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        viewModel.getServicesListAndSearch(token, "")
    }

    private fun registerComplain() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        val model = OwnerRegisterComplainPostModel(
            binding.edName.text.trim().toString(),
            binding.edWriteDescription.text.trim().toString(),
            photo_upload_list,
            buildingId
        )
        regi.tenantRegisterComplain(token, model)
    }

    private fun observer() {
        viewModel.serviceLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            it.data.docs.forEach {
                                serviceCategoryList.add(it.category_name.toString())
                                serviceCategoryHashMapID.put(it.category_name.toString(), it._id)
                            }
                            spinnerForSubCategoryList(serviceCategoryList)
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
        regi.tenantRegisterComplainLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
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


    private fun lstnr() {
        binding.edService.setOnClickListener {
            binding.spinner.performClick()
        }
        /*binding.textView96.setOnClickListener {
            startActivity(Intent(this, TenantRegisterComplainActivity::class.java).putExtra("from",from))
        }*/
        binding.commonBtn.tv.setOnClickListener {
            finish()
        }
        binding.complainToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.commonBtn.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            }
            registerComplain()
        }
        binding.edUpload.setOnClickListener {
            showImagePickDialog()
        }
    }

    private fun validationData(): Boolean {
        if (binding.edName.text.trim().toString().length < 4) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_enter_name),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edWriteDescription.text?.trim().toString())
        ) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_description),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } /*else if (binding.spinner.selectedItemPosition.equals(0)) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_enter_service_category),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }*/

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
            imagePath = url!!
            EmpCustomLoader.hideLoader()
            binding.rcyPhoto.visibility = View.VISIBLE
            binding.pro.visibility = View.GONE
            photo_upload_list.add(currentImagePath.toString())
            println("-----photo${photo_upload_list}")
            binding.rcyPhoto.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            photoAdapter = PhotoUploadAdapter(this, photo_upload_list)
            binding.rcyPhoto.adapter = photoAdapter
            photoAdapter!!.notifyDataSetChanged()

        }
    }

    override fun onAWSError(error: String?) {
        //binding.pro.visibility = View.GONE
        EmpCustomLoader.hideLoader()
        binding.rcyPhoto.visibility = View.INVISIBLE
        Log.e("error", error ?: "")
    }

    override fun onAWSProgress(progress: Int?) {
        EmpCustomLoader.showLoader(this)
        binding.rcyPhoto.visibility = View.VISIBLE
        Log.e("progress", progress!!.toString())
    }

   /* private fun uploadVideoToS3(videoPath: String) {
        val credentialsProvider = CognitoCachingCredentialsProvider(
            applicationContext,
            AWSConstants.IDENTITY_POOL_ID,
            AWSConstants.REGION
        )
        val s3Client = AmazonS3Client(credentialsProvider)

        val file = File(videoPath)
        val fileName = file.name.toString().trim()

        val transferUtility = TransferUtility.builder()
            .context(applicationContext)
            .s3Client(s3Client)
            .build()

        val uploadObserver = transferUtility.upload(AWSConstants.BUCKET_NAME, fileName, file)
        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    when (state) {
                        TransferState.COMPLETED -> {
                            onAWSSuccess(AWSConstants.IMAGE_URL.plus(fileName))
                            println("----sst$state")

                        }
                        TransferState.CANCELED, TransferState.FAILED -> {
                            onAWSError("AWS " + state.name)
                        }
                        TransferState.WAITING_FOR_NETWORK -> {
                            onAWSError("Please turn on internet")
                        }
                        else -> {
                            onAWSError(state.name)
                        }
                    }
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                val percentDone = (bytesCurrent.toFloat() / bytesTotal.toFloat()) * 100
                // Update progress if needed
            }

            override fun onError(id: Int, ex: Exception) {
                // Handle errors during the upload
            }
        })
    }

    private fun dialogMedia() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.choose_media_popup)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val img = dialog.findViewById<ImageView>(R.id.ivimg)
        val videoImg = dialog.findViewById<ImageView>(R.id.ivVideosP)
        img.setOnClickListener {
            dialog.dismiss()
            showImagePickDialog()
        }
        videoImg.setOnClickListener {
            dialog.dismiss()
            pickVideoFromGallery()
            *//* if (checkPermission(STORAGE_PERMISSION)) {
                 pickVideoFromGallery()
             }*//*

        }


        dialog.show()

    }*/
}