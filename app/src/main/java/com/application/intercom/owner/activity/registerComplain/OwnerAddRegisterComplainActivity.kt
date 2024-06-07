package com.application.intercom.owner.activity.registerComplain

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
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
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.local.owner.registerComplain.OwnerRegisterComplainPostModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ServiceRepository
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.databinding.ActivityOwnerAddRegisterComplainBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.owner.activity.OwnerParking.OwnerParkingActivity
import com.application.intercom.owner.activity.helpSupport.OwnerHelpSupportActivity
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.activity.ownerProperty.OwnerPropertyActivity
import com.application.intercom.owner.activity.ownerVisitor.OwnerVisitorActivity
import com.application.intercom.owner.activity.ownerbilling.BillingAccountOwnerActivity
import com.application.intercom.owner.activity.ownerbilling.OwnerBillingActivity
import com.application.intercom.owner.activity.properties.OwnerPropertiesActivity
import com.application.intercom.owner.adapter.PhotoUploadAdapter
import com.application.intercom.owner.adapter.SpinnerAdapterBrands
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.MyCommunity.TenantMyCommunityActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticBoardActivity
import com.application.intercom.tenant.activity.registerComplain.TenantRegisterComplainActivity
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.user.newflow.fav.UserFavActivity
import com.application.intercom.user.service.ServiceFactory
import com.application.intercom.user.service.ServiceViewModel
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner
import java.io.File


class OwnerAddRegisterComplainActivity : BaseActivity<ActivityOwnerAddRegisterComplainBinding>(),
    AWSListner /*ProfileAdapter.ProfileClick*/ {
    override fun getLayout(): ActivityOwnerAddRegisterComplainBinding {
        return ActivityOwnerAddRegisterComplainBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: ServiceViewModel
    private lateinit var regi: OwnerSideViewModel
    private var serviceCategoryList = ArrayList<String>()
    private var serviceCategoryHashMapID: HashMap<String, String> = HashMap()
    private var serviceCategoryId: String = ""
    private var imagePath: String = ""
    private var photoAdapter: PhotoUploadAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    private var buildingId: String = ""
    private val PICK_VIDEO_REQUESTT = 2
    private val STORAGE_PERMISSION = 2233
    private lateinit var dialog: Dialog

   /* /////side menu
    private var profile_list = java.util.ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null

    ///side mneu*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildingId = prefs.getString(
            SessionConstants.BUILDINGID,
            GPSService.mLastLocation?.latitude.toString()
        )
        /*//side mneu
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
            binding.nav.imageView96.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.pro_bangla_img
                )
            )
        } else {
            binding.nav.tvBl.visibility = View.INVISIBLE
            binding.nav.tvEnglish.visibility = View.VISIBLE
            binding.nav.imageView96.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.pro_img
                )
            )
        }
        //side menu*/
        println("---bulid$buildingId")
        initView()
        listener()
        /*///side menu
        rcyItem()
        //sidemenu*/
    }

  /*  private fun rcyItem() {
        profile_list.add(ProfileModal(R.drawable.home_icon, getString(R.string.home)))
        profile_list.add(ProfileModal(R.drawable.property_icon, getString(R.string.my_properties)))
        profile_list.add(ProfileModal(R.drawable.parking_icon, getString(R.string.property)))
        profile_list.add(ProfileModal(R.drawable.service_icon, getString(R.string.parking)))
//        profile_list.add(ProfileModal(R.drawable.property_icon, "My Property"))
//        profile_list.add(ProfileModal(R.drawable.parking_icon, "My Parking"))
        profile_list.add(ProfileModal(R.drawable.fav_icon, getString(R.string.favorites)))
        profile_list.add(ProfileModal(R.drawable.community_icon, getString(R.string.my_community)))
        profile_list.add(ProfileModal(R.drawable.billing_icon, getString(R.string.complain)))
        profile_list.add(ProfileModal(R.drawable.community_icon, getString(R.string.my_billings)))
        profile_list.add(
            ProfileModal(
                R.drawable.visitor_icon,
                getString(R.string.visitors_gatepass)
            )
        )
        profile_list.add(
            ProfileModal(
                R.drawable.term_icon,
                "Billing Account"
            )
        )
        profile_list.add(ProfileModal(R.drawable.notics_icon, getString(R.string.notice_board)))
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
        profile_list.add(
            ProfileModal(
                R.drawable.term_icon,
                getString(R.string.about)
            )
        )
        binding.nav.rcyProfile.layoutManager = LinearLayoutManager(this)
        profileAdapter = ProfileAdapter(this, profile_list, "owner", this)
        binding.nav.rcyProfile.adapter = profileAdapter
        profileAdapter!!.notifyDataSetChanged()
    }*/

    private fun initView() {
        serviceCategoryList.add(0, getString(R.string.select_service_category))
        init()
        observer()
        getServicesList()
        binding.complainToolbar.tvTittle.text = getString(R.string.register_complain)
        binding.commonBtn.tv.text = getString(R.string.register_complain)
        binding.complainToolbar.ivBack.visibility = View.VISIBLE
       /* binding.complainToolbar.ivmneu.visibility = View.VISIBLE*/

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

        val register = OwnerSideRepo(BaseApplication.apiService)
        regi = ViewModelProvider(
            this,
            OwnerSideFactory(register)
        )[OwnerSideViewModel::class.java]
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
        regi.registerComplainOwner(token, model)
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
        regi.registerComplainOwnerLiveData.observe(this, Observer {
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

    private fun listener() {
        binding.edService.setOnClickListener {
            binding.spinner.performClick()
        }
        binding.textView96.setOnClickListener {
            startActivity(
                Intent(this, TenantRegisterComplainActivity::class.java).putExtra(
                    "from",
                    "owner"
                )
            )
        }
        binding.commonBtn.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            }
            registerComplain()
        }
        binding.complainToolbar.ivBack.setOnClickListener {
            finish()
        }
       /* binding.complainToolbar.ivmneu.setOnClickListener {
            binding.ownerComplainDrw.openDrawer(GravityCompat.START)
        }*/
        binding.edUpload.setOnClickListener {
            //dialogMedia()
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
                applicationContext,
                getString(R.string.please_enter_description),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } /*else if (binding.spinner.selectedItemPosition.equals(0)) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_select_service_category),
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
        } else if (requestCode == PICK_VIDEO_REQUESTT) {
            val selectedVideoUri: Uri? = data?.data
            if (selectedVideoUri != null) {
                val videoPath: String = getRealPathFromURI(selectedVideoUri).toString().trim()
                //uploadVideoToS3(videoPath)
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

    private fun pickVideoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUESTT)
    }

   /* private fun uploadVideoToS3(videoPath: String) {
       *//* val credentialsProvider = CognitoCachingCredentialsProvider(
            applicationContext,
            AWSConstants.IDENTITY_POOL_ID,
            AWSConstants.REGION
        )*//*
        val credentialsProvider = BasicAWSCredentials(BuildConfig.ACCESS_KEY, BuildConfig.SECRET_KEY)
        val s3Client = AmazonS3Client(credentialsProvider)

        val file = File(videoPath)
        val fileName = file.name.toString().trim()

        val transferUtility = TransferUtility.builder()
            .context(applicationContext)
            .s3Client(s3Client)
            .build()

        val uploadObserver = transferUtility.upload(BuildConfig.BUCKET_NAME, fileName, file)
        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    when (state) {
                        TransferState.COMPLETED -> {
                            onAWSSuccess(BuildConfig.IMAGE_URL.plus(fileName))
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
    }*/

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
            /* if (checkPermission(STORAGE_PERMISSION)) {
                 pickVideoFromGallery()
             }*/

        }


        dialog.show()

    }

    /*  private fun setVideoPickData(data: Uri) {
          val videoPath: String? = GetPath.getPath(applicationContext, data)
          Log.d("TAG", "setVideoPickData: VIDEO PATH = $videoPath")

          strVideoPath.add(videoPath!!)
  //            setVideoThumbnail(videoPath)

          *//* if (checkVideoLength(data)) {

            if (isUpdatePost) {
                mImageVideoList.add(videoPath);
                strVideoPath = mImageVideoList;
                setUpdateBottomAdapter();

            } else {
                strVideoPath.add(videoPath);
                setVideoThumbnail(videoPath);
            }

        } else {
            CommonUtil.showSnackBar(CreatePostActivity.this, "Video must be less than 16 seconds");
        }*//*
    }
*/
    override fun onAWSLoader(isLoader: Boolean) {
        EmpCustomLoader.showLoader(this)
    }

    override fun onAWSSuccess(url: String?) {
        if (url != null) {
            imagePath = url
            println("---url$url")
            binding.rcyPhoto.visibility = View.VISIBLE
            EmpCustomLoader.hideLoader()
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
        EmpCustomLoader.hideLoader()
        binding.rcyPhoto.visibility = View.INVISIBLE
        Log.e("error", error ?: "")
    }

    override fun onAWSProgress(progress: Int?) {
        EmpCustomLoader.showLoader(this)
        binding.rcyPhoto.visibility = View.VISIBLE
        Log.e("progress", progress!!.toString())
    }

    /* private fun pickVideo() {
         if (checkPermission()) {
             val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
             startActivityForResult(Intent.createChooser(intent, "Select Video"), VIDEO_CODE)
         } else requestPermision()
     }

     //permission
     fun requestPermision() {
         ActivityCompat.requestPermissions(
             this,
             arrayOf<String>(
                 WRITE_EXTERNAL_STORAGE,
                 READ_EXTERNAL_STORAGE
             ),
             101
         )
     }

     fun checkPermission(): Boolean {
         val READ_EXTERNAL_STORAGE =
             ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
         val WRITE_EXTERNAL_STORAGE =
             ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
         return if (WRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
             false
         } else if (READ_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
             false
         } else {
             true
         }
     }

     fun getPathcontent(context: Context?, uri: Uri): String {
         // DocumentProvider
         if (DocumentsContract.isDocumentUri(context, uri)) {
             // ExternalStorageProvider
             if (isExternalStorageDocument(uri)) {
                 val docId = DocumentsContract.getDocumentId(uri)
                 val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                     .toTypedArray()
                 val type = split[0]
                 if ("primary".equals(type, ignoreCase = true)) {
                     return Environment.getExternalStorageDirectory() + "/" + split[1]
                 }

                 // TODO handle non-primary volumes
             } else if (isDownloadsDocument(uri)) {
                 val id = DocumentsContract.getDocumentId(uri)
                 val contentUri = ContentUris.withAppendedId(
                     Uri.parse("content://downloads/public_downloads"), id.toLong()
                 )
                 return getDataColumn(context!!, contentUri, null, null)
             } else if (isMediaDocument(uri)) {
                 val docId = DocumentsContract.getDocumentId(uri)
                 val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                     .toTypedArray()
                 val type = split[0]
                 var contentUri: Uri? = null
                 if ("image" == type) {
                     contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                 } else if ("video" == type) {
                     contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                 } else if ("audio" == type) {
                     contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                 }
                 val selection = "_id=?"
                 val selectionArgs = arrayOf(
                     split[1]
                 )
                 return getDataColumn(context!!, contentUri, selection, selectionArgs)
             }
         } else if ("content".equals(uri.scheme, ignoreCase = true)) {
             return getDataColumn(context!!, uri, null, null)
         } else if ("file".equals(uri.scheme, ignoreCase = true)) {
             return uri.path
         }
         return null
     }

     fun getDataColumn(
         context: Context, uri: Uri?, selection: String?,
         selectionArgs: Array<String>?
     ): String {
         var cursor: Cursor? = null
         val column = "_data"
         val projection = arrayOf(
             column
         )
         try {
             cursor = context.contentResolver.query(
                 uri!!, projection, selection, selectionArgs,
                 null
             )
             if (cursor != null && cursor.moveToFirst()) {
                 val column_index: Int = cursor.getColumnIndexOrThrow(column)
                 return cursor.getString(column_index)
             }
         } finally {
             if (cursor != null) cursor.close()
         }
         return null
     }*/
    fun checkPermission(requestCode: Int): Boolean {
        var ret = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    this,
                    WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ret = false
                requestPermissions(
                    arrayOf(
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE
                    ), requestCode
                )
            }
        }
        return ret
    }

    /*override fun onClick(position: Int) {
        when (position) {
            0 -> {
                startActivity(
                    Intent(this, OwnerMainActivity::class.java).putExtra(
                        "from", "from_side_home"
                    )
                )
            }
            1 -> {
                startActivity(
                    Intent(this, OwnerPropertiesActivity::class.java)
                )
            }
            2 -> {
                startActivity(
                    Intent(this, OwnerPropertyActivity::class.java)
                )
            }
            3 -> {
                startActivity(
                    Intent(this, OwnerParkingActivity::class.java)
                )
            }
            4 -> {
                startActivity(Intent(this, UserFavActivity::class.java))
            }
            5 -> {
                startActivity(
                    Intent(
                        this, TenantMyCommunityActivity::class.java
                    ).putExtra("from", "owner")*//*.putExtra("projectId", projectId)*//*
                )
            }
            6 -> {
                startActivity(Intent(this, OwnerAddRegisterComplainActivity::class.java))
            }
            7 -> {
                startActivity(
                    Intent(this, OwnerBillingActivity::class.java).putExtra(
                        "from", "owner"
                    )
                )

            }
            8 -> {
                startActivity(
                    Intent(this, OwnerVisitorActivity::class.java).putExtra(
                        "from",
                        "owner"
                    )
                )
            }
            9 -> {
//                startActivity(
//                    Intent(
//                        this, TenantNoticBoardActivity::class.java
//                    ).putExtra("from", "owner")
//                )
                startActivity(
                    Intent(
                        this, BillingAccountOwnerActivity::class.java
                    )
                )
            }
            10 -> {

                startActivity(
                    Intent(
                        this, TenantNoticBoardActivity::class.java
                    ).putExtra("from", "owner")
                )
            }
            11 -> {
                startActivity(Intent(this, OwnerHelpSupportActivity::class.java))

            }
            12 -> {
                startActivity(Intent(this, TenantSettingActivity::class.java))
            }
            13 -> {
                shareIntent(this, "https://intercomapp.page.link/Go1D")

            }
            14 -> {
                startActivity(Intent(this, PrivacyPolicyActivity::class.java))

            }
            15 -> {
                startActivity(Intent(this, TermsOfServiceActivity::class.java))

            }
            16 -> {
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
        if (binding.ownerComplainDrw.isDrawerOpen(GravityCompat.START)) {
            binding.ownerComplainDrw.closeDrawer(GravityCompat.START)
        }
    }

    override fun onResume() {
        super.onResume()
        closeDrawer()
    }*/
}