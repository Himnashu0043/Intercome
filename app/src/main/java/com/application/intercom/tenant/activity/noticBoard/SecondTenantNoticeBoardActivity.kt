package com.application.intercom.tenant.activity.noticBoard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.tenantViewModel.TenantSideViewModel
import com.application.intercom.data.model.factory.tenantFactory.TenantSideFactory
import com.application.intercom.data.model.remote.tenant.tenantSide.TenantNoticeListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.tenantRepo.TenantSideRepo
import com.application.intercom.databinding.ActivitySecondTenantNoticeBoardBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.tenant.adapter.noticeBoard.SecondTenantNoticBoardAdapter
import com.application.intercom.tenant.adapter.noticeBoard.TenantNoticeBoardAdapter
import com.application.intercom.utils.*

class SecondTenantNoticeBoardActivity :BaseActivity<ActivitySecondTenantNoticeBoardBinding>(),
    TenantNoticeBoardAdapter.OwnerNoticeClick {

    override fun getLayout(): ActivitySecondTenantNoticeBoardBinding {
        return ActivitySecondTenantNoticeBoardBinding.inflate(layoutInflater)
    }

    private lateinit var tenant_viewModel: TenantSideViewModel

    private var adptr: SecondTenantNoticBoardAdapter? = null
    private var from: String = ""
    private var tenant_noticeList = ArrayList<TenantNoticeListRes.Data>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from").toString()
        initView()
        listener()
    }

    private fun initView() {
        initialize()
        observer()
        binding.noticBoard.tvTittle.text = getString(R.string.notice_board)
    }

    private fun listener() {

    }

    private fun initialize() {
        val tenant_repo = TenantSideRepo(BaseApplication.apiService)
        tenant_viewModel = ViewModelProvider(
            this,
            TenantSideFactory(tenant_repo)
        )[TenantSideViewModel::class.java]


    }

    private fun getOwnerNotices() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        tenant_viewModel.tenantNoticeList(token)


    }

    private fun observer() {
          tenant_viewModel.tenantNoticeLiveData.observe(this, Observer {
              when (it) {
                  is EmpResource.Loading -> {
                      EmpCustomLoader.showLoader(this)
                  }

                  is EmpResource.Success -> {
                      EmpCustomLoader.hideLoader()
                      it.value.let {
                          if (it.status == AppConstants.STATUS_SUCCESS) {
                              if (from.equals("owner")) {
//                            tenant_noticeList.clear()
//                            tenant_noticeList.addAll(it.data)
//                            binding.rcyNotice.layoutManager = LinearLayoutManager(this)
//                            adptr = SecondTenantNoticBoardAdapter(
//                                this,
//                                tenant_noticeList
//
//                            )
                            binding.rcyNotice.adapter = adptr
                            adptr!!.notifyDataSetChanged()

                              } else {

                              }

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
        /*  } else if (from.equals("tenant")) {*/
     /*   tenant_viewModel.tenantNoticeLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    if (it.value.status == AppConstants.STATUS_SUCCESS) {
                        tenant_noticeList.clear()
                        tenant_noticeList.addAll(it.value.data)
                        adptr = SecondTenantNoticBoardAdapter(
                            this,
                            tenant_noticeList

                        )
                        binding.rcyNotice.adapter = adptr
                        adptr!!.notifyDataSetChanged()

                    } else if (it.value.status == AppConstants.STATUS_404) {
                        this.longToast(it.value.message)
                    } else {

                    }
                }
                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })
*/

    }

    override fun onClick(position: Int, id: String) {

    }

    override fun onResume() {
        super.onResume()
        getOwnerNotices()
    }
}