package com.application.intercom.baseActivity

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.application.intercom.R
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.gatekepper.activity.newFlow.ownerTenantSingleEntryHistory.OwnerTenantSingleEntryHistoryActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.utils.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.runBlocking

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: B
    protected var snackbar: Snackbar? = null
    private lateinit var viewModel: OwnerSideViewModel
    private var role: String = ""

    //////
    var notitype: String = ""
    var tvGetName: String = ""
    var tvTitleName: String = ""
    var tvBodyName: String = ""
    var tvGetFlat: String = ""
    var tvGetDelivery: String = ""
    var tvGetAddresss: String = ""
    var tvGetNoti: String = ""
    var tvGetMobile: String = ""
    var getVisitorId: String = ""
    var img: String = ""
    abstract fun getLayout(): B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getLayout()
        role = prefs.getString(SessionConstants.ROLE, GPSService.mLastLocation?.latitude.toString())
        println("---baseRole$role")
        setContentView(binding.root)
        initialize()
        registerReceiver(pushRecevier, IntentFilter("PushBroadCast"))

        var lang =
            prefs.getString(SessionConstants.LANG, GPSService.mLastLocation?.latitude.toString())
        if (lang.isEmpty()) {
            lang = Language.BANGLA.languageCode
            setLocale(lang)
        }
        setLocale(lang)

        if (intent.hasExtra("type"))
            if (intent.extras?.getString("type") == "CHECKED_IN") {
                tvGetName = intent?.getStringExtra(PushKeys.visitorName.name).toString()
                tvGetAddresss = intent?.getStringExtra(PushKeys.address.name).toString()
                tvGetFlat = intent?.getStringExtra(PushKeys.flatName.name).toString()
                tvGetDelivery = intent?.getStringExtra(PushKeys.visitCategoryName.name).toString()
                tvGetMobile = intent?.getStringExtra(PushKeys.mobileNumber.name).toString()
                tvGetNoti = intent?.getStringExtra(PushKeys.note.name).toString()
                img = intent?.getStringExtra(PushKeys.photo.name).toString()
                getVisitorId = intent?.getStringExtra(PushKeys.visitorId.name).toString()
                println("======get$getVisitorId")
                println("======tvGetName$tvGetName")
                notiPopup()
            }

    }

    private fun initialize() {
        val repo = OwnerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, OwnerSideFactory(repo))[OwnerSideViewModel::class.java]


    }

    private fun observer() {

        viewModel.visitorActionTenantLiveData.observe(
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
                                this.longToast(getString(R.string.accept_successfully))
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
        viewModel.visitorActionOwnerLiveData.observe(
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
                                this.longToast(getString(R.string.accept_successfully))

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

        viewModel.rejectvisitorActionOwnerLiveData.observe(
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
                                this.longToast(getString(R.string.reject_successfully))

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
        viewModel.rejectvisitorActionTenantLiveData.observe(
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
                                this.longToast("Reject Successfully!!")

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

    private val pushRecevier = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            // get data here
            val soundResourceId = resources.getIdentifier("telephone_ringtone_new", "raw", packageName)
            val ringtone = RingtoneManager.getRingtone(this@BaseActivity, Uri.parse("android.resource://$packageName/$soundResourceId"))
            ringtone.play()
            tvGetName = intent?.getStringExtra(PushKeys.visitorName.name).toString()
            tvGetAddresss = intent?.getStringExtra(PushKeys.address.name).toString()
            tvGetFlat = intent?.getStringExtra(PushKeys.flatName.name).toString()
            tvGetDelivery = intent?.getStringExtra(PushKeys.visitCategoryName.name).toString()
            tvGetMobile = intent?.getStringExtra(PushKeys.mobileNumber.name).toString()
            tvGetNoti = intent?.getStringExtra(PushKeys.note.name).toString()
            img = intent?.getStringExtra(PushKeys.photo.name).toString()
            getVisitorId = intent?.getStringExtra(PushKeys.visitorId.name).toString()
            println("=====tvGetNoti$tvGetNoti")
            println("=====tgetVisitorId$getVisitorId")
//            when (tvGetNoti) {
//                PushKeys.CHECKED_IN.toString() -> {
//                    notiPopup()
//                }
//            }

            notiPopup()
        }

    }

    private fun notiPopup() {
        runBlocking {
            val dialog = Dialog(this@BaseActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.notification_single_entry_popup)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
            var tvName = dialog.findViewById<TextView>(R.id.textView48)
            var tvflat = dialog.findViewById<TextView>(R.id.textView49)
            var tvDelivery = dialog.findViewById<TextView>(R.id.textView82)
            var tvphone = dialog.findViewById<TextView>(R.id.textView83)
//        val tvOwnerphone = dialog.findViewById<TextView>(R.id.textView1722)
            var tvAddress = dialog.findViewById<TextView>(R.id.textView831)
            var tvNote = dialog.findViewById<TextView>(R.id.textView8311)
            var tvAccept = dialog.findViewById<TextView>(R.id.textView85)
            var tvReject = dialog.findViewById<TextView>(R.id.textView851)
            var tvimg = dialog.findViewById<ImageView>(R.id.imageView20)
            var ivCrossImg = dialog.findViewById<ImageView>(R.id.ivCrossImg)


            tvName.text = tvGetName
            tvAddress.text = tvGetAddresss
            tvflat.text = tvGetFlat
            tvDelivery.text = "${tvGetDelivery} | Single Entry"
            tvphone.text = tvGetMobile
            tvNote.text = tvGetNoti
            tvimg.loadImagesWithGlideExt(img)
            tvAccept.setOnClickListener {
                dialog.dismiss()
                if (role.equals("tenant")) {
                    val token = prefs.getString(
                        SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
                    )
                    viewModel.visitorActionTenant(token, getVisitorId, "Accept")
                } else {
                    val token = prefs.getString(
                        SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
                    )
                    viewModel.visitorActionOwner(token, getVisitorId, "Accept")
                }
            }
            ivCrossImg.setOnClickListener {
                if (role.equals("tenant")) {
                    dialog.dismiss()
                    startActivity(
                        Intent(
                            this@BaseActivity,
                            OwnerTenantSingleEntryHistoryActivity::class.java
                        ).putExtra(
                            "from",
                            "tenant"
                        )
                    )
                } else {
                    dialog.dismiss()
                    startActivity(
                        Intent(
                            this@BaseActivity,
                            OwnerTenantSingleEntryHistoryActivity::class.java
                        )
                    )
                }
            }
            tvReject.setOnClickListener {
                if (role.equals("tenant")) {
                    dialog.dismiss()
                    val token = prefs.getString(
                        SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
                    )
                    viewModel.rejectvisitorActionTenant(token, getVisitorId, "Rejected")
                } else {
                    dialog.dismiss()
                    val token = prefs.getString(
                        SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
                    )
                    viewModel.rejectvisitorActionOwner(token, getVisitorId, "Rejected")
                }
            }
            val window = dialog.window
            if (window != null) {
                window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
                )
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(pushRecevier)
        super.onDestroy()
    }
    //for localization

//     private fun callLocalization(amharichLang: String) {
//         val locale = Locale(amharichLang)
//         Locale.setDefault(locale)
//
//         val res: Resources = getResources()
//         val config = Configuration(res.getConfiguration())
//         config.locale = locale
//         res.updateConfiguration(config, res.getDisplayMetrics())
//
//     }
}