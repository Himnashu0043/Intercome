package com.application.intercom.fireBaseNotification

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import android.util.Log
import com.application.intercom.R

class RingtoneService : IntentService("RingtoneService") {
    private val TAG = "RingtoneService"
    private var mediaPlayer: MediaPlayer? = null

    @Deprecated("Deprecated in Java")
    override fun onHandleIntent(p0: Intent?) {
        Log.d("RingtoneService", "Service is running")
        mediaPlayer = MediaPlayer.create(this, R.raw.telephone_ringtone_new)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            stopSelf()
        }
    }
}