package com.application.intercom.user.aboutapp

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.repository.AboutRepository
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.databinding.ActivityPrivacyPolicyBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.utils.*



class PrivacyPolicyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyPolicyBinding
    private lateinit var viewModel: AboutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.tvTittle.text = getString(R.string.privacy_policy)
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
      //  ZohoSalesIQ.showLauncher(true)
       /* val initConfig = InitConfig()
        initConfig.setFont(Fonts.REGULAR, "fonts/Lato-Regular.ttf")
        SalesIQListeners.initialise()
        ZohoSalesIQ.init(
            application,
            "M6Bt%2F%2FcDiGZKhi%2FyrzNyD%2BQRxyEtVbT23LhK9Y3EW%2BYEQm9SCokWvikYw7WWabus_in",
            "n%2FGi9svd4XBPxSpD%2FKoYtjOgbf%2F3A4hmaAhch0tPErehJXCB35l0p7VISPn0RMJE0hSMF44QhFrBCd3OfE5EXLMHnlDNB6abnP5z14d41HLNv8ux8BacND3c6H8FieTB",
            initConfig,
            object : InitListener {
                override fun onInitSuccess() {
                    ZohoSalesIQ.showLauncher(true) //by default launcher will be hidden and it can be enabled with this line.
                }

                override fun onInitError(errorCode: Int, errorMessage: String) {
                    //your code
                }
            })

*/

      /*  try {
            ZohoSalesIQ.showLauncher(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
        init()
        observer()
        getPrivacyPolicy()

    }

    private fun init() {
        val repo = AboutRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, AboutUsFactory(repo))[AboutViewModel::class.java]
    }

    private fun getPrivacyPolicy() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
           ""
        )
        viewModel.userGetPrivacyPolicy(token)
    }

    private fun observer() {
        viewModel.privacyPolicyLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            val htmlAsString = it.data.data
                            val htmlAsSpanned = Html.fromHtml(htmlAsString)
                            binding.tvPrivacyPolicy.text = htmlAsSpanned
                            binding.textView26.text = it.data.title
                        } else if (it.status == AppConstants.STATUS_404) {
                            longToast(it.message)
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
}