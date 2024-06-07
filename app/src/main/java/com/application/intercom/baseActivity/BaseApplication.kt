package  com.application.intercom.baseActivity

import android.app.Application
import com.application.intercom.data.api.ApiService
import com.application.intercom.data.api.RetrofitHelper
import com.application.intercom.db.AppDatabase
import com.application.intercom.fireBaseNotification.MyFireBaseMessagingServices
import com.application.intercom.utils.Prefs

val prefs: Prefs by lazy {
    BaseApplication.prefs!!
}

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
        instance = this@BaseApplication
        apiService = RetrofitHelper.getInstance().create(ApiService::class.java)

        MyFireBaseMessagingServices.createNotificationChannel(this)
        MyFireBaseMessagingServices.createNotificationChannelDefault(this)
    }


    /*
 ---------companiopn object used is for static keyword like method ,variable-------------
 ----------lateinit before using we will initialize ------------
  */
    companion object {
        var prefs: Prefs? = null
        lateinit var instance: BaseApplication
            private set
        lateinit var apiService: ApiService
        var mRoomId: String = ""

        val appDatabase by lazy { AppDatabase.getInstance(instance) }
    }

}