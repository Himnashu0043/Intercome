package com.application.intercom.utils

import android.Manifest.permission.*
import android.app.Activity
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.location.LocationManager
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.application.intercom.R
import com.application.intercom.baseActivity.prefs
import com.application.intercom.helper.NoInternetConnectionDailog
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.annotations.Nullable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object CommonUtil {
    const val DELAY_MS: Long = 500
    const val PERIOD_MS: Long = 3000
    var noInternetDailog: NoInternetConnectionDailog? = null


    fun Bundle.printBundle() {
        for (key in keySet()) {
            Log.d("PRINT_BUNDLE", "key: $key value: ${get(key)}")
        }
    }

    fun themeSet(context: Context, window: Window) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(
                context as Activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true
            )
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(
                context as Activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false
            )
            window.statusBarColor = Color.TRANSPARENT
           //window.navigationBarColor  = Color.BLACK
        }


    }

    fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win: Window = activity.window
        val winParams: WindowManager.LayoutParams = win.getAttributes()
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.setAttributes(winParams)
    }

    fun indefiniteSnack(context: Context, msg: String): Snackbar {
        val snackbar = Snackbar.make(
            (context as Activity).findViewById(android.R.id.content),
            msg,
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.show()

        return snackbar
    }

    fun showSnackBar(context: Context?, msg: String?) {
        var snackbar: Snackbar? = null
        snackbar = if (context is com.application.intercom.MainActivity) Snackbar.make(
            (context as Activity).findViewById(
                R.id.content
            ), msg!!, Snackbar.LENGTH_LONG
        )
        else Snackbar.make(
            (context as Activity).findViewById(android.R.id.content), msg!!, Snackbar.LENGTH_LONG
        )
        val snackBarView = snackbar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                context, android.R.color.holo_red_dark
            )
        )
        val tv = snackBarView.findViewById<View>(R.id.snackbar_text) as TextView
        snackBarView.minimumHeight = 20
        tv.textSize = 14f
        tv.setTextColor(ContextCompat.getColor(context, R.color.white))
        snackbar.show()
    }

    fun getAge(date: String?): Int {
        var age = 0
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date1 = formatter.parse(date)
            val now = Calendar.getInstance()
            val dob = Calendar.getInstance()
            dob.time = date1
            require(!dob.after(now)) { "Can't be born in the future" }
            val year1 = now[Calendar.YEAR]
            val year2 = dob[Calendar.YEAR]
            age = year1 - year2
            val month1 = now[Calendar.MONTH]
            val month2 = dob[Calendar.MONTH]
            if (month2 > month1) {
                age--
            } else if (month1 == month2) {
                val day1 = now[Calendar.DAY_OF_MONTH]
                val day2 = dob[Calendar.DAY_OF_MONTH]
                if (day2 > day1) {
                    age--
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        Log.d("TAG", "getAge: AGE=> $age")
        return age
    }

    fun checkGPS(context: Context?): Boolean {
        return try {
            if (context != null) {
                val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                return !(!gps_enabled && !network_enabled)
            } else {
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    fun showHidePassword(context: Context, editText: EditText, iv: ImageView) {
        if (editText.transformationMethod == PasswordTransformationMethod.getInstance()) {
            iv.setImageResource(R.drawable.ic_eye_visible)
            //Show Password
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            iv.setImageResource(R.drawable.ic_eye_hide)
            //Hide Password
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
        }

    }

    fun blankValidation(str: String?): Boolean {
        return str != null && str.trim().isNotEmpty() && str.trim().length > 9
    }

    fun isValidMobile(phone: String): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            phone.length in 9..15
        } else false
    }

    fun getProperText(textView: TextView): String? {
        return textView.text.toString().trim { it <= ' ' }
    }

    fun Context.shortToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    fun startDownload(url: String, con: Context, fileName: String, tittle: String) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setTitle(tittle)
        request.setMimeType("*/*")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        /*enque manager*/
        val manager = con.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    fun downloadVideo(context: Context, url: String, title: String) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setTitle(title)
        request.setDescription("Downloading video")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }


   /* fun generateThumbnail(context: Context,videoUrl: String) {
        val outputPath = "/path/to/your/output/thumbnail.jpg"

        // FFmpeg command to generate the thumbnail
        val command = arrayOf("-i", videoUrl, "-ss", "00:00:01.000", "-vframes", "1", outputPath)

        // Execute FFmpeg command
        FFmpeg.getInstance(context).execute(command, object : FFmpegExecuteResponseHandler {
            override fun onSuccess(message: String?) {
                // Thumbnail generation successful
                // The thumbnail will be saved at the specified 'outputPath'
            }

            override fun onFailure(message: String?) {
                // Thumbnail generation failed
                Log.e("FFmpeg", "Failed to generate thumbnail: $message")
            }

            override fun onProgress(message: String?) {}
            override fun onStart() {}
            override fun onFinish() {}
        })
    }
    fun retrieveVideoFrameFromVideo(videoPath: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(videoPath, HashMap<String, String>())
            bitmap = mediaMetadataRetriever.frameAtTime
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
    }*/
    /** check permission for lower version of TIRAMISU*/
    fun checkCameraPermission(context: Context): Boolean {
        val camera =
            ContextCompat.checkSelfPermission(context, CAMERA)
        val storage =
            ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE)
        return if (storage != PackageManager.PERMISSION_GRANTED) {
            false
        } else camera == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkCameraPermission2(context: Context): Boolean {
        val camera =
            ContextCompat.checkSelfPermission(context, CAMERA)
        val image =
            ContextCompat.checkSelfPermission(context, READ_MEDIA_IMAGES)

        return if (image != PackageManager.PERMISSION_GRANTED) {
            false
        } else camera == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkVideoFilePermission(context: Context): Boolean {
        val camera =
            ContextCompat.checkSelfPermission(context, CAMERA)
        val video =
            ContextCompat.checkSelfPermission(context, READ_MEDIA_VIDEO)
        return if (video != PackageManager.PERMISSION_GRANTED) {
            false
        } else camera == PackageManager.PERMISSION_GRANTED
    }


   fun requestCamFilePermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
               CAMERA,
               READ_EXTERNAL_STORAGE
            ),
            AppConstants.CAMERA_REQUEST_CODE
        )
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestCamFilePermission2(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                CAMERA,
               READ_MEDIA_IMAGES
            ),
            AppConstants.CAMERA_REQUEST_CODE
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestVideoPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(READ_MEDIA_VIDEO, CAMERA),
            AppConstants.VIDEO_REQ_CODE
        )
    }
    fun setLightStatusBar(activity: Activity) {
        var flags = activity.window.decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        activity.window.decorView.systemUiVisibility = flags
    }

    fun clearLightStatusBar(activity: Activity) {
        var flags = activity.window.decorView.systemUiVisibility
        flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        activity.window.decorView.systemUiVisibility = flags
    }
    /*fun setLanguageForApp(context: Context, languageType: String) {
        Log.e("TAG", "setLanguageForApp${languageType}")
        prefs.put(SessionConstants.LANG,languageType)
        val locale = Locale(languageType)
        Locale.setDefault(locale)
        val res: Resources = context.resources
        val config = Configuration(res.configuration)
        config.locale = locale
        res.updateConfiguration(config, res.displayMetrics)
    }*/

//    fun Context.setAppLocale(language: String): Context {
//        prefs.put(SessionConstants.LANG,language)
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//        val config = resources.configuration
//        config.setLocale(locale)
//        config.setLayoutDirection(locale)
//        return createConfigurationContext(config)
//    }


    /*fun callLocalization(context: Context,amharichLang: String) {
        val locale = Locale(amharichLang)
        Locale.setDefault(locale)

        val res: Resources = context.resources
        val config = Configuration(res.configuration)
        config.locale = locale
        res.updateConfiguration(config, res.displayMetrics)

    }*/
}


/* fun showNoInternetDialog(activity: Activity?) {
     if (noInternetDailog == null) noInternetDailog = NoInternetConnectionDailog.show(
         activity!!,
         true
     )
     try {
         noInternetDailog!!.setCancelable(false)
         noInternetDailog!!.show()
     } catch (e: Exception) {
         e.printStackTrace()
     }
 }
 fun networkConnectionCheck(context: Context): Boolean {
     val isConnected = isOnline(context)
     if (!isConnected) {
         showNoInternetDialog(context as Activity)
     }
     return isConnected
 }*/

//    fun isOnline(context: Context): Boolean {
//        return try {
//            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            val mobile_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
//            val wifi_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//            if (mobile_info != null) {
//                if (mobile_info.isConnectedOrConnecting || wifi_info!!.isConnectedOrConnecting) true
//                else false
//            } else {
//                if (wifi_info!!.isConnectedOrConnecting) true
//                else false
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            println("" + e)
//            false
//        }
//    }


