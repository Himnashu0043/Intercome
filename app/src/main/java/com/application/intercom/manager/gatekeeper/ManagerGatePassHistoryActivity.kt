package com.application.intercom.manager.gatekeeper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.remote.manager.managerSide.gatepass.ManagerGatePassHistoryListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityManagerGatePassHistoryBinding
import com.application.intercom.gatekepper.activity.gatePass.SecondGatePassActivity
import com.application.intercom.gatekepper.gatekeeperAdapter.gatePassHistory.GatePassHistoryAdapter
import com.application.intercom.helper.GPSService
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.utils.*

class ManagerGatePassHistoryActivity : AppCompatActivity(), GatePassHistoryAdapter.Click,
    CommunityImgAdapter.ClickImg {
    lateinit var binding: ActivityManagerGatePassHistoryBinding
    private var adptr: GatePassHistoryAdapter? = null
    private lateinit var viewModel: ManagerSideViewModel
    private var gatePassHistroyList = ArrayList<ManagerGatePassHistoryListRes.Data.Result>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerGatePassHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        lstnr()
    }

    private fun initView() {
        initialize()
        observer()
        getGatePassList()
        binding.gatepassHistoryToolbar.tvTittle.text = "Gatepass History"


    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

    private fun getGatePassList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation!!.latitude.toString()
        )
        viewModel.gatePassHistoryList(token)
    }

    private fun observer() {
        viewModel.gatePassHistoryListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            gatePassHistroyList.clear()
                            gatePassHistroyList.addAll(it.data.result)
                            binding.rcyGatePassHistory.layoutManager = LinearLayoutManager(this)
                            adptr = GatePassHistoryAdapter(this, gatePassHistroyList, this, this)
                            binding.rcyGatePassHistory.adapter = adptr
                            adptr!!.notifyDataSetChanged()

                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
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
    }

    private fun lstnr() {
        binding.gatepassHistoryToolbar.ivBack.setOnClickListener {
            finish()
        }

    }


    override fun onclick(position: Int) {
        startActivity(
            Intent(
                this,
                ManagerSecondGatePassActivity::class.java
            ).putExtra("gatePass_list", gatePassHistroyList[position])
        )
    }

    override fun showImg(img: String) {
        TODO("Not yet implemented")
    }
}