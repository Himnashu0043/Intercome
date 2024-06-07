package com.application.intercom.tenant.activity.notification

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.NotificationList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.ActivityTenantNotificationBinding
import com.application.intercom.tenant.adapter.noti.TenantNotificationAdapter
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.utils.*

class TenantNotificationActivity : BaseActivity<ActivityTenantNotificationBinding>() {
    private lateinit var viewModel: UserHomeViewModel
    private var list = ArrayList<NotificationList.Data>()
    override fun getLayout(): ActivityTenantNotificationBinding {
        return ActivityTenantNotificationBinding.inflate(layoutInflater)
    }

    private var adptr: TenantNotificationAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        lstnr()

    }

    private fun initView() {
        binding.notiToolbar.tvTittle.text = "Notifications"
        binding.rcyNoti.layoutManager = LinearLayoutManager(this)

        initialize()
        observer()

    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]

    }

    private fun getUserNotification() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.getUserNotificationList(token)
    }

    private fun observer() {
        viewModel.userNotificationLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            list.clear()
                            list.addAll(it.data)
                            binding.rcyNoti.layoutManager = LinearLayoutManager(this)
                            adptr = TenantNotificationAdapter(this, list)
                            binding.rcyNoti.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message ?: "")
                        } else if (it.status == AppConstants.STATUS_FAILURE) {

                        } else if (it.status == AppConstants.STATUS_500) {

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

    private fun lstnr() {
        binding.notiToolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        getUserNotification()
    }
}