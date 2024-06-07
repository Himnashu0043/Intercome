package com.application.intercom.fireBaseNotification

import android.annotation.SuppressLint
import android.app.*
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.gatekepper.Main.MainGateKepperActivity
import com.application.intercom.gatekepper.activity.newFlow.ownerTenantRegularEntryHistory.OwnerTenantRegularEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.ownerTenantSingleEntryHistory.OwnerTenantSingleEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.regularEntryHistory.RegularEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntryHistory.SingleEntryHistoryActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.manager.bills.ApprovalBillingManagerActivity
import com.application.intercom.manager.bills.UnPaidBillingManagerActivity
import com.application.intercom.manager.complaint.RegisterComplaintsActivity
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.manager.notice.NoticeBoardActivity
import com.application.intercom.manager.visitorAndGatePass.ManagerGatePassActivity
import com.application.intercom.owner.activity.gatepass.OwnerGatePassActivity
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.activity.ownerbilling.OwnerBillingActivity
import com.application.intercom.tenant.activity.MyCommunity.TenantMyCommunityActivity
import com.application.intercom.tenant.activity.billing.TenantBillingsActivity
import com.application.intercom.tenant.activity.chat.ChatDetailsActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticBoardActivity
import com.application.intercom.tenant.activity.registerComplain.TenantRegisterComplainActivity
import com.application.intercom.utils.PushKeys
import com.application.intercom.utils.SessionConstants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import java.util.*


class MyFireBaseMessagingServices : FirebaseMessagingService() {
    var notitype: String = ""
    var titletype: String = ""
    var tvGetName: String = ""
    var tvTitleName: String = ""
    var tvBodyName: String = ""
    var tvGetFlat: String = ""
    var tvGetDelivery: String = ""
    var tvGetAddresss: String = ""
    var tvGetNoti: String = ""
    var tvGetMobile: String = ""
    var role: String = ""
    var getVisitorId: String = ""
    var reciverRole: String = ""
    var complainId: String = ""
    var img: String = ""
    var reciverProfilePic: String = ""
    var recieverId: String = ""
    var senderId: String = ""
    var roomNotiId: String = ""
    var senderRole: String = ""
    var senderProfileName: String = ""
    var senderProfilePic: String = ""
    var senderPhoneNo: String = ""

    override fun onNewToken(p0: String) {
        Log.e("Device Token  ==>>", p0)
        prefs.put(SessionConstants.DEVICETOKEN, p0)

    }

    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = am.runningAppProcesses
        for (processInfo in runningProcesses) {
            if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (activeProcess in processInfo.pkgList) {
                    if (activeProcess == context.getPackageName()) {
                        isInBackground = false
                    }
                }
            }
        }
        return isInBackground
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(
            TAG,
            "onMessageReceived: notification object ${Gson().toJson(remoteMessage.notification)}"
        )
        var intent = Intent()
        notitype = remoteMessage.data.get(PushKeys.type.name).toString()

        prefs.put(SessionConstants.NOTYTYPE, remoteMessage.data.get(PushKeys.type.name).toString())
        prefs.put(
            SessionConstants.TITTLETYPE,
            remoteMessage.data.get(PushKeys.title.name).toString()
        )
        role = prefs.getString(SessionConstants.ROLE, GPSService.mLastLocation?.latitude.toString())
        for (key in remoteMessage.data.keys) {
            println("-----reee key : $key || value : ${remoteMessage.data[key]}")
        }
        tvTitleName = remoteMessage.data[PushKeys.title.name] ?: ""
        tvBodyName = remoteMessage.data[PushKeys.body.name] ?: ""
        tvGetName = remoteMessage.data[PushKeys.visitorName.name] ?: ""
        tvGetAddresss = remoteMessage.data[PushKeys.address.name] ?: ""
        tvGetFlat = remoteMessage.data[PushKeys.flatName.name] ?: ""
        tvGetNoti = remoteMessage.data[PushKeys.note.name] ?: ""
        img = remoteMessage.data[PushKeys.photo.name] ?: ""
        getVisitorId = remoteMessage.data[PushKeys.visitorId.name] ?: ""
        tvGetMobile = remoteMessage.data[PushKeys.mobileNumber.name] ?: ""
        tvGetDelivery = remoteMessage.data[PushKeys.visitCategoryName.name] ?: ""
        reciverRole = remoteMessage.data[PushKeys.reciverRole.name] ?: ""


        reciverProfilePic = remoteMessage.data[PushKeys.reciverProfilePic.name] ?: ""
        recieverId = remoteMessage.data[PushKeys.reciever.name] ?: ""
        senderId = remoteMessage.data[PushKeys.sender.name] ?: ""
        roomNotiId = remoteMessage.data[PushKeys.roomId.name] ?: ""
        senderRole = remoteMessage.data[PushKeys.senderRole.name] ?: ""
        senderProfileName = remoteMessage.data[PushKeys.senderProfileName.name] ?: ""
        senderProfilePic = remoteMessage.data[PushKeys.senderProfilePic.name] ?: ""
        senderPhoneNo = remoteMessage.data[PushKeys.senderPhoneNo.name] ?: ""
        prefs.put(SessionConstants.RECIVERPROFILE, reciverProfilePic)
        prefs.put(SessionConstants.RECIEVER, recieverId)
        prefs.put(SessionConstants.SENDER, senderId)
        prefs.put(SessionConstants.ROOMID, roomNotiId)
        prefs.put(SessionConstants.SENDER_ROLE, senderRole)
        prefs.put(SessionConstants.SENDER_PROFILE_PIC, senderProfilePic)
        prefs.put(SessionConstants.SENDER_PROFILE_NAME, senderProfileName)
        prefs.put(SessionConstants.SENDER_PHONE_NO, senderPhoneNo)
        //complainId = remoteMessage.data[PushKeys.COMPLAIN_DENY.name] ?: ""
        println("==========$reciverRole")

        createNotificationChannel(this)
        when (notitype) {
            PushKeys.CHECKED_IN.toString() -> {
                sendBroadcast(Intent("PushBroadCast").apply {
                    //send data
                    putExtra(PushKeys.visitorName.name, tvGetName)
                    putExtra(PushKeys.address.name, tvGetAddresss)
                    putExtra(PushKeys.flatName.name, tvGetName)
                    putExtra(PushKeys.note.name, tvGetNoti)
                    putExtra(PushKeys.mobileNumber.name, tvGetMobile)
                    putExtra(PushKeys.visitCategoryName.name, tvGetDelivery)
                    putExtra(PushKeys.photo.name, img)
                    putExtra(PushKeys.visitorId.name, getVisitorId)
                    // TODO add sound to base activity
                })

            }
            PushKeys.CHECKED_OUT.toString() -> {
                if (role == "owner") {
                    intent = Intent(
                        this,
                        OwnerTenantSingleEntryHistoryActivity::class.java
                    ).putExtra("from", role)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java).addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotification(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(
                        this,
                        OwnerTenantSingleEntryHistoryActivity::class.java
                    ).putExtra("from", role)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotification(paddingIntentRedirect, tvTitleName, tvBodyName)
                }
            }
            PushKeys.New_Chat_Mesg.toString() -> {
                if (BaseApplication.mRoomId.isEmpty() || BaseApplication.mRoomId != roomNotiId) {
                    if (role.equals("owner")) {
                        intent = Intent(this, ChatDetailsActivity::class.java).putExtra(
                            "key", "from_noti_side"
                        ).putExtra("reciverProfilePic", reciverProfilePic)
                            .putExtra("reciverProfilePic", reciverProfilePic)
                            .putExtra("recieverId", recieverId)
                            .putExtra("senderId", senderId)
                            .putExtra("roomNotiId", roomNotiId)
                            .putExtra("senderRole", senderRole)
                            .putExtra("senderProfileName", senderProfileName)
                            .putExtra("senderProfilePic", senderProfilePic)
                            .putExtra("senderPhoneNo", senderPhoneNo)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val paddingIntentRedirect =
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                                PendingIntent.getActivity(
                                    this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                                )
                            } else {
                                val stackBuilder = TaskStackBuilder.create(this)
                                    .addParentStack(OwnerMainActivity::class.java)
                                    .addNextIntent(intent)
                                stackBuilder.getPendingIntent(
                                    getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                                )
                            }
                        showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                    } else {
                        intent = Intent(this, ChatDetailsActivity::class.java).putExtra(
                            "key", "from_noti_side"
                        ).putExtra("reciverProfilePic", reciverProfilePic)
                            .putExtra("reciverProfilePic", reciverProfilePic)
                            .putExtra("recieverId", recieverId)
                            .putExtra("senderId", senderId)
                            .putExtra("roomNotiId", roomNotiId)
                            .putExtra("senderRole", senderRole)
                            .putExtra("senderProfileName", senderProfileName)
                            .putExtra("senderProfilePic", senderProfilePic)
                            .putExtra("senderPhoneNo", senderPhoneNo)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val paddingIntentRedirect =
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                                PendingIntent.getActivity(
                                    this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                                )
                            } else {
                                val stackBuilder = TaskStackBuilder.create(this)
                                    .addParentStack(TenantMainActivity::class.java)
                                    .addNextIntent(intent)
                                stackBuilder.getPendingIntent(
                                    getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                                )
                            }
                        showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                    }
                }
            }
            PushKeys.New_Bill_Msg.toString() -> {
                if (role == "owner") {
                    intent = Intent(this, OwnerBillingActivity::class.java).putExtra(
                        "from", "owner"
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java).addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else if (role == "tenant") {
                    intent = Intent(this, TenantBillingsActivity::class.java).putExtra(
                        "from", "tenant"
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    if (tvBodyName == "Approval is Pending") {
                        intent = Intent(this, ApprovalBillingManagerActivity::class.java).putExtra("notyFlow","notyType")
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        val paddingIntentRedirect =
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                                PendingIntent.getActivity(
                                    this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                                )
                            } else {
                                val stackBuilder = TaskStackBuilder.create(this)
                                    .addParentStack(ManagerMainActivity::class.java)
                                    .addNextIntent(intent)
                                stackBuilder.getPendingIntent(
                                    getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                                )
                            }
                        showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                    } else if (tvBodyName == "Service Approval is Pending") {
                        intent = Intent(
                            this,
                            ApprovalBillingManagerActivity::class.java
                        ).putExtra("notyFlow", "notyType")
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        val paddingIntentRedirect =
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                                PendingIntent.getActivity(
                                    this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                                )
                            } else {
                                val stackBuilder = TaskStackBuilder.create(this)
                                    .addParentStack(ManagerMainActivity::class.java)
                                    .addNextIntent(intent)
                                stackBuilder.getPendingIntent(
                                    getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                                )
                            }
                        showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                    } else {
                        intent = Intent(this, UnPaidBillingManagerActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        val paddingIntentRedirect =
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                                PendingIntent.getActivity(
                                    this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                                )
                            } else {
                                val stackBuilder = TaskStackBuilder.create(this)
                                    .addParentStack(ManagerMainActivity::class.java)
                                    .addNextIntent(intent)
                                stackBuilder.getPendingIntent(
                                    getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                                )
                            }
                        showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                    }

                }
            }
            PushKeys.NEW_SERVICE_Bill.toString() -> {
                if (role == "owner") {
                    intent = Intent(this, OwnerBillingActivity::class.java).putExtra(
                        "from", "owner"
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java).addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else if (role == "tenant") {
                    intent = Intent(this, TenantBillingsActivity::class.java).putExtra(
                        "from", "tenant"
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    if (tvBodyName == "Approval is Pending") {
                        intent = Intent(
                            this,
                            ApprovalBillingManagerActivity::class.java
                        ).putExtra("notyFlow", "notyType")
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        val paddingIntentRedirect =
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                                PendingIntent.getActivity(
                                    this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                                )
                            } else {
                                val stackBuilder = TaskStackBuilder.create(this)
                                    .addParentStack(ManagerMainActivity::class.java)
                                    .addNextIntent(intent)
                                stackBuilder.getPendingIntent(
                                    getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                                )
                            }
                        showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                    } else {
                        intent = Intent(this, UnPaidBillingManagerActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        val paddingIntentRedirect =
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                                PendingIntent.getActivity(
                                    this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                                )
                            } else {
                                val stackBuilder = TaskStackBuilder.create(this)
                                    .addParentStack(ManagerMainActivity::class.java)
                                    .addNextIntent(intent)
                                stackBuilder.getPendingIntent(
                                    getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                                )
                            }
                        showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                    }

                }
            }
            PushKeys.NEW_BILL_NOTIFY.toString() -> {
                if (role == "owner") {
                    intent = Intent(this, OwnerBillingActivity::class.java).putExtra(
                        "from", "owner"
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java).addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, TenantBillingsActivity::class.java).putExtra(
                        "from", "tenant"
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } /*else {
                    if (tvBodyName == "Approval is Pending") {
                        intent = Intent(
                            this,
                            ApprovalBillingManagerActivity::class.java
                        ).putExtra("notyFlow", "notyType")
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        val paddingIntentRedirect =
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                                PendingIntent.getActivity(
                                    this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                                )
                            } else {
                                val stackBuilder = TaskStackBuilder.create(this)
                                    .addParentStack(ManagerMainActivity::class.java)
                                    .addNextIntent(intent)
                                stackBuilder.getPendingIntent(
                                    getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                                )
                            }
                        showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                    } else {
                        intent = Intent(this, UnPaidBillingManagerActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        val paddingIntentRedirect =
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                                PendingIntent.getActivity(
                                    this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                                )
                            } else {
                                val stackBuilder = TaskStackBuilder.create(this)
                                    .addParentStack(ManagerMainActivity::class.java)
                                    .addNextIntent(intent)
                                stackBuilder.getPendingIntent(
                                    getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                                )
                            }
                        showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                    }

                }*/
            }
            PushKeys.NOTICE.toString() -> {
                if (role == "owner") {
                    intent = Intent(this, TenantNoticBoardActivity::class.java).putExtra(
                        "from", "owner"
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java).addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else if (role == "tenant") {
                    intent = Intent(this, TenantNoticBoardActivity::class.java).putExtra(
                        "from", "tenant"
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, NoticeBoardActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(ManagerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                }
            }
            PushKeys.NEW_COMPLAIN_RESOLVE.toString() -> {
                if (role == "owner") {
                    intent = Intent(this, TenantRegisterComplainActivity::class.java)
                    intent.putExtra("from", "owner")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java).addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else if (role == "tenant") {
                    intent = Intent(this, TenantRegisterComplainActivity::class.java)
                    intent.putExtra("from", "tenant")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, RegisterComplaintsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(ManagerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                }
            }
            PushKeys.COMPLAIN_DENY.toString() -> {
                intent = Intent(this, RegisterComplaintsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val paddingIntentRedirect =
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                        PendingIntent.getActivity(
                            this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                        )
                    } else {
                        val stackBuilder = TaskStackBuilder.create(this)
                            .addParentStack(ManagerMainActivity::class.java)
                            .addNextIntent(intent)
                        stackBuilder.getPendingIntent(
                            getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                        )
                    }

                showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
            }
            PushKeys.COMPLAIN_CONFIRM.toString() -> {
                intent = Intent(this, RegisterComplaintsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val paddingIntentRedirect =
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                        PendingIntent.getActivity(
                            this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                        )
                    } else {
                        val stackBuilder = TaskStackBuilder.create(this)
                            .addParentStack(ManagerMainActivity::class.java)
                            .addNextIntent(intent)
                        stackBuilder.getPendingIntent(
                            getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                        )
                    }

                showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
            }
            PushKeys.VISITOR_ADDED.toString() -> {
                if (role == "gatekeeper") {
                    intent = Intent(this, RegularEntryHistoryActivity::class.java).putExtra(
                        "from",
                        "gatekeeper"
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(MainGateKepperActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotification(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, RegularEntryHistoryActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(ManagerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotification(paddingIntentRedirect, tvTitleName, tvBodyName)
                }

            }
            PushKeys.NEW_COMPLAIN.toString() -> {
                intent = Intent(this, RegisterComplaintsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val paddingIntentRedirect =
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                        PendingIntent.getActivity(
                            this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                        )
                    } else {
                        val stackBuilder = TaskStackBuilder.create(this)
                            .addParentStack(ManagerMainActivity::class.java)
                            .addNextIntent(intent)
                        stackBuilder.getPendingIntent(
                            getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                        )
                    }

                showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
            }
            PushKeys.BUILDING_APPROVE.toString() -> {
                if (role == "owner") {
                    intent = Intent(this, OwnerMainActivity::class.java)
                    intent.putExtra("from", "from_myList")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java).addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else if (role == "tenant") {
                    intent = Intent(this, TenantMainActivity::class.java)
                    intent.putExtra("from", "from_myList")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("from", "from_myList")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(MainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                }
            }
            PushKeys.BUILDING_DENY.toString() -> {
                if (role == "owner") {
                    intent = Intent(this, OwnerMainActivity::class.java)
                    intent.putExtra("from", "from_myList")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java).addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else if (role == "tenant") {
                    intent = Intent(this, TenantMainActivity::class.java)
                    intent.putExtra("from", "from_myList")
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("from", "from_myList")
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(ManagerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                }
            }
            PushKeys.VISITOR_ACCEPT_REJECT.toString() -> {
                if (role == "gatekeeper") {
                    intent = Intent(this, SingleEntryHistoryActivity::class.java)
                    intent.putExtra("from", "gatekeeper")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(MainGateKepperActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotification(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, SingleEntryHistoryActivity::class.java)
                    intent.putExtra("from", "manager")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(ManagerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotification(paddingIntentRedirect, tvTitleName, tvBodyName)
                }

            }
            PushKeys.REGULAR_CHECKED_IN.toString() -> {
                if (role == "tenant") {
                    intent = Intent(this, OwnerTenantRegularEntryHistoryActivity::class.java)
                    intent.putExtra("from", "tenant")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java).addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotification(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, OwnerTenantRegularEntryHistoryActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotification(paddingIntentRedirect, tvTitleName, tvBodyName)
                }
            }
            PushKeys.REGULAR_CHECKED_OUT.toString() -> {
                if (role == "tenant") {
                    intent = Intent(this, OwnerTenantRegularEntryHistoryActivity::class.java)
                    intent.putExtra("from", "tenant")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java).addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotification(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, OwnerTenantRegularEntryHistoryActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotification(paddingIntentRedirect, tvTitleName, tvBodyName)
                }
            }
            PushKeys.GATEPASS_CREATE.toString() -> {
                if (role == "gatekeeper") {
                    intent = Intent(this, MainGateKepperActivity::class.java).putExtra(
                        "from",
                        "from_gate_create_pass"
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(MainGateKepperActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, ManagerGatePassActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(ManagerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                }

            }
            PushKeys.BILL_APPROVED.toString() -> {
                if (role == "owner") {
                    intent = Intent(this, OwnerBillingActivity::class.java)
                    intent.putExtra("key", "paid")
                    intent.putExtra("from", role)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, TenantBillingsActivity::class.java)
                    intent.putExtra("key", "paid")
                    intent.putExtra("from", role)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                }

            }
            PushKeys.NEW_RENT_Bill.toString() -> {
                if (role == "owner") {
                    intent = Intent(this, OwnerBillingActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, TenantBillingsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                }

            }
            PushKeys.New_Rent_Bill_Msg.toString() -> {
                if (role == "tenant") {
                    intent = Intent(this, TenantBillingsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantBillingsActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } /*else {
                    intent = Intent(this, TenantBillingsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                }*/

            }

            PushKeys.NEW_BILL_PAID.toString() -> {
                if (role == "owner") {
                    intent =
                        Intent(this, OwnerBillingActivity::class.java).putExtra("from", "owner")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else if (role == "tenant") {
                    intent =
                        Intent(this, TenantBillingsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantBillingsActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, ApprovalBillingManagerActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(ManagerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                }

            }
            PushKeys.NEW_BILL_PAID_APPROVED.toString() -> {
                intent = Intent(this, TenantBillingsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val paddingIntentRedirect =
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                        PendingIntent.getActivity(
                            this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                        )
                    } else {
                        val stackBuilder = TaskStackBuilder.create(this)
                            .addParentStack(ManagerMainActivity::class.java)
                            .addNextIntent(intent)
                        stackBuilder.getPendingIntent(
                            getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                        )
                    }

                showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)

            }
            PushKeys.GATEPASS_COMPLETE.toString() -> {
                if (role == "owner") {
                    intent = Intent(this, OwnerGatePassActivity::class.java)
                    intent.putExtra("from",role)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, OwnerGatePassActivity::class.java)
                    intent.putExtra("from",role)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                }

            }
            PushKeys.LIKED_POST.toString() -> {
                if (role == "owner") {
                    intent = Intent(this, TenantMyCommunityActivity::class.java)
                    intent.putExtra("from", role)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, TenantMyCommunityActivity::class.java)
                    intent.putExtra("from", role)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                }

            }
            PushKeys.COMMENT_POST.toString() -> {
                if (role == "owner") {
                    intent = Intent(this, TenantMyCommunityActivity::class.java)
                    intent.putExtra("from", role)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, TenantMyCommunityActivity::class.java)
                    intent.putExtra("from", role)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                }

            }
            PushKeys.POST.toString() -> {
                if (role == "owner") {
                    intent = Intent(this, TenantMyCommunityActivity::class.java)
                    intent.putExtra("from", role)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, TenantMyCommunityActivity::class.java)
                    intent.putExtra("from", role)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                }

            }
            PushKeys.NEW_BUILDING.toString() -> {
                if (role == "owner") {
                    intent = Intent(this, OwnerMainActivity::class.java)
                    intent.putExtra("from", "from_myList")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, TenantMainActivity::class.java)
                    intent.putExtra("from", "from_myList")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                }

            }
            PushKeys.BILL_REJECT.toString() -> {
                if (role == "owner") {
                    intent = Intent(this, OwnerBillingActivity::class.java)
                    intent.putExtra("from", "owner")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, TenantBillingsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                    showNotificationDefault(paddingIntentRedirect, tvTitleName, tvBodyName)
                }

            }
            else -> {
                if (role == "owner") {
                    intent = Intent(this, OwnerMainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(OwnerMainActivity::class.java).addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotification(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else if (role == "tenant") {
                    intent = Intent(this, TenantMainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(TenantMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotification(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else if (role == "manager") {
                    intent = Intent(this, ManagerMainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(ManagerMainActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotification(paddingIntentRedirect, tvTitleName, tvBodyName)
                } else {
                    intent = Intent(this, MainGateKepperActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    val paddingIntentRedirect =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            PendingIntent.getActivity(
                                this, getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            val stackBuilder = TaskStackBuilder.create(this)
                                .addParentStack(MainGateKepperActivity::class.java)
                                .addNextIntent(intent)
                            stackBuilder.getPendingIntent(
                                getUniqueId(), PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    showNotification(paddingIntentRedirect, tvTitleName, tvBodyName)
                }
//
            }

        }


    }

    /* private fun showNotification(intent: Intent, title: String, message: String) {
         val uniqueId = getUniqueId()
         val rawUri =
             Uri.parse("android.resource://" + packageName + "/" + R.raw.telephone_ringtone_new)
         try {
             val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
             val paddingIntentRedirect = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                 PendingIntent.getActivity(
                     this, uniqueId, intent, PendingIntent.FLAG_IMMUTABLE
                 )
             } else {
                 val stackBuilder = TaskStackBuilder.create(this)
                     .addParentStack(MainActivity::class.java)
                     .addNextIntent(intent)
                 stackBuilder
                     .getPendingIntent(getUniqueId(), PendingIntent.FLAG_IMMUTABLE)
             }
             val bigText = NotificationCompat.BigTextStyle()
             bigText.bigText(title)
             bigText.setBigContentTitle(title)
             val mBuilder = NotificationCompat.Builder(
                 this,
                 getString(R.string.default_notification_channel_id)
             )
             mBuilder.setLargeIcon(
                 BitmapFactory.decodeResource(
                     resources, R.drawable.app_sq_logo
                 )
             ).setSmallIcon(R.drawable.app_sq_logo).setContentIntent(paddingIntentRedirect)
                 .setContentTitle(title).setContentText(message).setAutoCancel(true).setSilent(true)
                 .setCategory(NotificationCompat.CATEGORY_CALL).setSound(rawUri).setDefaults(0)
                 .setPriority(NotificationCompat.PRIORITY_MAX)
                 .build()

             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 val channel = NotificationChannel(
                     getString(R.string.default_notification_channel_id),
                     getString(R.string.himanshu_gmail_com),
                     NotificationManager.IMPORTANCE_HIGH
                 )
                 val attributes =
                     AudioAttributes.Builder()
                         .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                         .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                         .build()
                 channel.setSound(
                     rawUri, attributes
                 )
 //                channel.setSound(, attributes)
                 mNotificationManager.createNotificationChannel(channel)
             }
             mNotificationManager.notify(uniqueId, mBuilder.build())

         } catch (e: Exception) {
             e.printStackTrace()
         }

     }*/


    @SuppressLint("MissingPermission", "DiscouragedApi")
    private fun showNotification(
        paddingIntentRedirect: PendingIntent, title: String, text: String
    ) {
        val soundResourceId = resources.getIdentifier("telephone_ringtone_new", "raw", packageName)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder =
                Notification.Builder(this, getString(R.string.default_notification_channel_id))
                    .setSmallIcon(R.drawable.app_logo).setLargeIcon(
                        BitmapFactory.decodeResource(
                            resources, R.drawable.app_logo
                        )
                    ).setContentTitle(title).setContentText(text)
                    .setPriority(Notification.PRIORITY_MAX).setCategory(Notification.CATEGORY_CALL)
                    .setContentIntent(paddingIntentRedirect).setDefaults(0)
                    .setAutoCancel(true) // Dismiss the notification when clicked
                    .setSound(Uri.parse("android.resource://$packageName/$soundResourceId"))
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }
        } else {
            val builder = NotificationCompat.Builder(
                this, getString(R.string.default_notification_channel_id)
            ).setSmallIcon(R.drawable.app_logo).setLargeIcon(
                BitmapFactory.decodeResource(
                    resources, R.drawable.app_logo
                )
            ).setContentTitle(title).setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_MAX).setDefaults(0)
                .setContentIntent(paddingIntentRedirect)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setAutoCancel(true) // Dismiss the notification when clicked
                .setSound(Uri.parse("android.resource://$packageName/$soundResourceId"))
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }
        }

    }

    @SuppressLint("MissingPermission", "DiscouragedApi")
    private fun showNotificationDefault(
        paddingIntentRedirect: PendingIntent, title: String, text: String
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder =
                Notification.Builder(
                    this,
                    getString(R.string.default_notification_channel_id_without_sound)
                )
                    .setSmallIcon(R.drawable.app_logo).setLargeIcon(
                        BitmapFactory.decodeResource(
                            resources, R.drawable.app_logo
                        )
                    ).setContentTitle(title).setContentText(text)
                    .setPriority(Notification.PRIORITY_MAX).setCategory(Notification.CATEGORY_CALL)
                    .setContentIntent(paddingIntentRedirect).setDefaults(0)
                    .setAutoCancel(true) // Dismiss the notification when clicked
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }
        } else {
            val builder = NotificationCompat.Builder(
                this, getString(R.string.default_notification_channel_id_without_sound)
            ).setSmallIcon(R.drawable.app_logo).setLargeIcon(
                BitmapFactory.decodeResource(
                    resources, R.drawable.app_logo
                )
            ).setContentTitle(title).setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_MAX).setDefaults(0)
                .setContentIntent(paddingIntentRedirect)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setAutoCancel(true) // Dismiss the notification when clicked
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }
        }

    }

    companion object {

        private fun getUniqueId() = ((System.currentTimeMillis() % 10000).toInt())
        private val TAG = MyFireBaseMessagingServices::class.java.simpleName

        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val name = "Remote Notification Channel"
                val descriptionText = "Channel for Remote Notifications"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val soundUri =
                    Uri.parse("android.resource://${context.packageName}/${R.raw.telephone_ringtone_new}")

                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
                val channel = NotificationChannel(
                    context.getString(R.string.default_notification_channel_id), name, importance
                ).apply {
                    description = descriptionText
                    setSound(soundUri, audioAttributes)
                }
                notificationManager.createNotificationChannel(channel)
            }
        }

        fun createNotificationChannelDefault(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val name = "All Notification Channel"
                val descriptionText = "Channel for All Notifications"
                val importance = NotificationManager.IMPORTANCE_HIGH
//                val soundUri =
//                    Uri.parse("android.resource://${context.packageName}/${R.raw.telephone_ringtone_new}")

                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
                val channel = NotificationChannel(
                    context.getString(R.string.default_notification_channel_id_without_sound),
                    name,
                    importance
                ).apply {
                    description = descriptionText
//                    setSound(soundUri, audioAttributes)
                }
                notificationManager.createNotificationChannel(channel)
            }
        }


        private val notificationId get() = Random().nextInt()
    }
}


