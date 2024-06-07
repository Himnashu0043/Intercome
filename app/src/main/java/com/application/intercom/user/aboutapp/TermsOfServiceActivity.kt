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
import com.application.intercom.databinding.ActivityTermsOfServiceBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.utils.*

class TermsOfServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermsOfServiceBinding
    private lateinit var viewModel: AboutViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsOfServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.tvTittle.text = getString(R.string.terms_of_service)
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        init()
        observer()
        getTermsOfServices()

    }
    private fun init() {
        val repo = AboutRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, AboutUsFactory(repo))[AboutViewModel::class.java]
    }

    private fun getTermsOfServices() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
          ""
        )
        viewModel.userGetTermsOfServices(token)
    }

    private fun observer() {
        viewModel.termsOfServicesLiveData.observe(this, Observer {
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
                            binding.tvTermsOfServices.text = htmlAsSpanned
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