package com.application.intercom.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.application.intercom.fireBaseNotification.MyFireBaseMessagingServices

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val serviceIntent = Intent(context, MyFireBaseMessagingServices::class.java)
            context?.startService(serviceIntent)
        }
    }
}