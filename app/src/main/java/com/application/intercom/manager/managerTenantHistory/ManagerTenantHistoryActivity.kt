package com.application.intercom.manager.managerTenantHistory

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerHomeViewModel.ManagerHomeViewModel
import com.application.intercom.data.model.factory.managerFactory.managerHome.ManagerHomeFactory
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerPropertyListRes
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerTenantHistoryList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerHomeRepo.ManagerHomeRepo
import com.application.intercom.databinding.ActivityManagerTenantHistoryBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.utils.*

class ManagerTenantHistoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityManagerTenantHistoryBinding
    private lateinit var getList: ManagerPropertyListRes.Data
    private var from: String = ""
    private var flatId: String = ""
    private var address: String = ""
    private lateinit var viewModel: ManagerHomeViewModel
    private var tenant_history_list = ArrayList<ManagerTenantHistoryList.Data.Result>()
    private var adptr: ManagerTenantHistoryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerTenantHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        initView()
        lstnr()
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = "Property History"
        if (from == "manager_property") {
            getList = intent.getSerializableExtra("list") as ManagerPropertyListRes.Data
            flatId = getList._id
            binding.imageView63.loadImagesWithGlideExt(getList.buildingInfo.get(0).photos.get(0))
            binding.textView116.text = getList.buildingInfo.get(0).buildingName
            binding.tvFlat.text = getList.name
            binding.textView117.text = getList.buildingInfo.get(0).address
            address = getList.buildingInfo.get(0).address

            //owner data
            binding.ivExpandProfile.loadImagesWithGlideExt(getList.ownerInfo[0].profilePic)
            binding.tvExpandProfileName.text = getList.ownerInfo[0].fullName
            binding.tvExpandAddress.text = getList.ownerInfo[0].address
            binding.ivExpandProfile.loadImagesWithGlideExt(getList.ownerInfo[0].profilePic)
            val valid = setFormatDate(getList.buildingInfo.get(0).validFrom)
            binding.tvDate.text = valid
            binding.tvStatus.text = "Till Now"
            binding.ivCalling.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${getList.ownerInfo[0].phoneNumber}")
                startActivity(intent)
            }
            binding.tvUserType.text = getList.ownerInfo[0].role
        }
        initialize()
        observer()

    }

    private fun initialize() {
        val repo = ManagerHomeRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this, ManagerHomeFactory(repo)
        )[ManagerHomeViewModel::class.java]


    }

    private fun getTenantHistory() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        viewModel.managerTenantHistoryList(token, flatId)

    }

    private fun observer() {
        viewModel.managerTenantHistoryListLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            tenant_history_list.clear()
                            tenant_history_list.addAll(it.data.result)
                            binding.rcy.layoutManager = LinearLayoutManager(this)
                            adptr = ManagerTenantHistoryAdapter(this, tenant_history_list, address)
                            binding.rcy.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_FAILURE) {

                            this.longToast(it.message)
                        } else {
                            this.longToast(it.message)
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
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        getTenantHistory()
    }
}