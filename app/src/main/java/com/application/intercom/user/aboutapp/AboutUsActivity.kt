package com.application.intercom.user.aboutapp

import android.os.Bundle
import android.text.Html
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.repository.AboutRepository
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.databinding.ActivityAboutUsBinding
import com.application.intercom.utils.*

class AboutUsActivity : BaseActivity<ActivityAboutUsBinding>() {
    private lateinit var viewModel: AboutViewModel
    override fun getLayout(): ActivityAboutUsBinding {
        return ActivityAboutUsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        listener()

    }

    private fun initView() {
        binding.toolbar.tvTittle.text = getString(R.string.about)
        init()
        observer()
        getAboutUs()
    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun init() {
        val repo = AboutRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, AboutUsFactory(repo))[AboutViewModel::class.java]
    }

    private fun getAboutUs() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        viewModel.userGetAboutUs(token)
    }

    private fun observer() {
        viewModel.aboutUsLiveData.observe(this, Observer {
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