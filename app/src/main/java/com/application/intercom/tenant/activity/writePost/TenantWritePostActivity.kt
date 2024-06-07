package com.application.intercom.tenant.activity.writePost

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import com.application.intercom.BuildConfig
import com.application.intercom.R
import com.application.intercom.aws.AWSConstants
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.local.owner.createPost.OwnerCreatePostModel
import com.application.intercom.data.model.local.owner.createPost.OwnerEditMyCommunityPostModel
import com.application.intercom.data.model.remote.owner.community.OwnerMyCommunityListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.databinding.ActivityTenantWritePostBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.helper.getCurrentDateNew
import com.application.intercom.owner.adapter.PhotoUploadAdapter
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner
import java.io.File


class TenantWritePostActivity : BaseActivity<ActivityTenantWritePostBinding>(), AWSListner {

    override fun getLayout(): ActivityTenantWritePostBinding {
        return ActivityTenantWritePostBinding.inflate(layoutInflater)
    }

    private lateinit var owner_viewModel: OwnerHomeViewModel
    private lateinit var side_owner_viewModel: OwnerSideViewModel
    private var projectId: String = ""
    private var buildingId: String = ""
    private var flatId: String = ""
    private var flat: String = ""
    private var buildingName: String = ""
    private var imagePath: String = ""
    private var photoAdapter: PhotoUploadAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    private val PICK_VIDEO_REQUESTT = 10199188
    private var from: String = ""
    private var key: String = ""
    private var postId: String? = null
    var editList: OwnerMyCommunityListRes.Data? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        from = intent.getStringExtra("from").toString()
        key = intent.getStringExtra("key").toString()
        println("----key$key")
        initView()
        lstnr()
    }

    private fun initView() {
        initialize()
        observer()
        getOwnerDetails()
        if (key == "edit") {
            editList =
                intent.getSerializableExtra("edit_list") as OwnerMyCommunityListRes.Data
            println("-----edit_LIst$editList")
            postId = editList!!._id
            binding.edWriteDescription.setText(editList!!.description)
            println("-----LIst${editList!!.file}")
            editList!!.file.forEach {
                photo_upload_list.add(it)
            }
            binding.rcyPhoto.visibility = View.VISIBLE
            binding.rcyPhoto.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            photoAdapter = PhotoUploadAdapter(this, photo_upload_list)
            binding.rcyPhoto.adapter = photoAdapter
            photoAdapter!!.notifyDataSetChanged()
        }

        binding.writetoolbar.tvTittle.text = getString(R.string.my_post)
        binding.writetoolbar.tvText.visibility = View.VISIBLE
        binding.writetoolbar.tvText.text = getString(R.string.post)

    }

    private fun initialize() {
        val ownerModel = OwnerHomeRepo(BaseApplication.apiService)
        owner_viewModel = ViewModelProvider(
            this, OwnerHomeFactory(ownerModel)
        )[OwnerHomeViewModel::class.java]

        val sideownerModel = OwnerSideRepo(BaseApplication.apiService)
        side_owner_viewModel = ViewModelProvider(
            this, OwnerSideFactory(sideownerModel)
        )[OwnerSideViewModel::class.java]
    }

    private fun getOwnerDetails() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        owner_viewModel.ownerDetails(token)

    }

    private fun createPost() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        val model = OwnerCreatePostModel(
            buildingName,
            buildingId,
            binding.edWriteDescription.text.trim().toString(),
            photo_upload_list,
            flat,
            flatId,
            projectId
        )
        side_owner_viewModel.createPostOwner(token, model)
    }

    private fun editPost() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        val model = OwnerEditMyCommunityPostModel(
            buildingName,
            buildingId,
            binding.edWriteDescription.text.trim().toString(),
            photo_upload_list,
            flat,
            flatId,
            postId!!,
            projectId
        )
        side_owner_viewModel.editMyCommunityPostOwner(token, model)
    }

    private fun observer() {
        owner_viewModel.ownerDetailsLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            binding.textView72.text = it.data.userDetails.fullName
                            /* val date = setNewFormatDate(it.data.userDetails.createdAt)*/
                            val date = getCurrentDateNew()
                            binding.textView73.text = date
                            binding.imageView37.loadImagesWithGlideExt(it.data.userDetails.profilePic)
                            /*if (!it.data.userData.isNullOrEmpty()) {
                                projectId = it.data.userData[0].buildingId.projectId._id
                                buildingId = it.data.userData[0].buildingId._id
                                buildingName = it.data.userData[0].buildingId.buildingName
                                flat = it.data.userData[0].name ?: ""
                                flatId = it.data.userData[0]._id
                            }*/
                            projectId = it.data.userData[0].buildingId.projectId._id
                            buildingId = it.data.userData[0].buildingId._id
                            buildingName = it.data.userData[0].buildingId.buildingName
                            flat = it.data.userData[0].name ?: ""
                            flatId = it.data.userData[0]._id

                            println("-------projectId$projectId")
                            println("-------buildingId$buildingId")
                            println("-------buildingName$buildingName")
                            println("-------flat$flat")
                            println("-------flatId$flatId")

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
        side_owner_viewModel.createPostOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            if (from.equals("ownerHome")) {
                                finish()
                            } else if (from.equals("tenantHome")) {
                                finish()
                            } else if (from.equals("tenant")) {
//                                startActivity(
//                                    Intent(this, ProfileActivity::class.java).putExtra(
//                                        "from", from
//                                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                )
                                finish()
                            } else {
//                                startActivity(
//                                    Intent(this, ProfileActivity::class.java).putExtra(
//                                        "from", from
//                                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                )
                                finish()
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
        side_owner_viewModel.editMyCommunityPostOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            if (from.equals("ownerHome")) {
                                finish()
                            } else if (from.equals("tenantHome")) {
                                finish()
                            } else if (from.equals("tenant")) {
//                                startActivity(
//                                    Intent(this, ProfileActivity::class.java).putExtra(
//                                        "from", from
//                                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                )
                                finish()
                            } else {
//                                startActivity(
//                                    Intent(this, ProfileActivity::class.java).putExtra(
//                                        "from", from
//                                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                )
                                finish()
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

    private fun pickVideoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUESTT)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val path = data?.getStringExtra("filePath")
        if (requestCode == TakeImageWithCrop.CAMERA_REQUEST) {
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
        } else if (requestCode == PICK_VIDEO_REQUESTT) {
            binding.pro.visibility = View.VISIBLE
            val selectedVideoUri: Uri? = data?.data
            if (selectedVideoUri != null) {
                val videoPath: String = getRealPathFromURI(selectedVideoUri).toString().trim()
              //  uploadVideoToS3(videoPath)
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        val path = cursor?.getString(columnIndex ?: -1)
        cursor?.close()
        return path
    }

 /*   private fun uploadVideoToS3(videoPath: String) {
       *//* val credentialsProvider = CognitoCachingCredentialsProvider(
            applicationContext, AWSConstants.IDENTITY_POOL_ID, AWSConstants.REGION
        )*//*
        val credentialsProvider = BasicAWSCredentials(BuildConfig.ACCESS_KEY, BuildConfig.SECRET_KEY)
        val s3Client = AmazonS3Client(credentialsProvider)
        val file = File(videoPath)
        val fileName = file.name.toString().trim()

        val transferUtility =
            TransferUtility.builder().context(applicationContext).s3Client(s3Client).build()


        Log.e("TAG", "laxman--: ${file}   ${fileName}")

        val uploadObserver = transferUtility.upload(BuildConfig.BUCKET_NAME, fileName, file)

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    when (state) {
                        TransferState.COMPLETED -> {
                            onAWSSuccess(BuildConfig.IMAGE_URL.plus(fileName))
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
    }*/

    private fun lstnr() {
        binding.writetoolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.imageView38.setOnClickListener {
            showImagePickDialog()
        }
        binding.ivViedosPost.setOnClickListener {
            // pickVideoFromGallery()
        }
        binding.writetoolbar.tvText.setOnClickListener {
            if (key == "edit") {
                editPost()
            } else {
                createPost()
            }

        }
    }

    override fun onAWSLoader(isLoader: Boolean) {
        EmpCustomLoader.showLoader(this)
        binding.rcyPhoto.visibility = View.VISIBLE
    }

    override fun onAWSSuccess(url: String?) {
        if (url != null) {
            imagePath = url
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
        Log.e("error", error ?: "")
    }

    override fun onAWSProgress(progress: Int?) {
        EmpCustomLoader.showLoader(this)
        binding.rcyPhoto.visibility = View.VISIBLE
        Log.e("progress", progress!!.toString())
    }
}