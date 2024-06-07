package com.application.intercom.manager.service_charge

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerAddServiceChargePostModel
import com.application.intercom.data.model.remote.manager.managerSide.serviceCharege.ManagerServiceChargeList
import com.application.intercom.data.model.remote.manager.managerSide.serviceCharege.ManagerViewServiceChargeRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityServiceChargesDetailsBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.setFormatDate
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.tenant.activity.profile.ProfileActivity
import com.application.intercom.utils.*


class ServiceChargesDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServiceChargesDetailsBinding
    private lateinit var viewModel: ManagerSideViewModel
    private var charegeId: String = ""
    private var list = ArrayList<ManagerServiceChargeList.Data>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceChargesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        initView()
        listener()


    }

    private fun initView() {
        initialize()
        observer()
        viewServiceCharge()
        binding.btnLogin.tv.text = "Edit"
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(this, R.color.orange))
    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

    private fun viewServiceCharge() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )

        viewModel.viewServiceChargeManager(token)
    }

    private fun observer() {
        viewModel.viewServiceChargeManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            if (it.data == null) {
                                charegeId = ""
                            } else {
                                charegeId = it.data[0]._id?:""
                            }
                            list.addAll(it.data)


                           /* binding.tvMonthly.text = it.data[0].serviceChargeType*/
                            val date = setFormatDate(it.data[0].date?:"")
                            binding.tvEveryMonth.text = date
                            binding.tvTotalAmount.text = it.data[0].amount.toString()
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

    private fun listener() {
        binding.btnLogin.tv.setOnClickListener {
            startActivity(
                Intent(this, EditServiceChargeActivity::class.java).putExtra(
                    "from",
                    list
                )
            )
        }
        binding.imageView4.setOnClickListener {
            /*startActivity(
                Intent(this, ProfileActivity::class.java).putExtra("from", "manager")
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )*/
            startActivity(
                Intent(this, ManagerMainActivity::class.java).putExtra("from", "from_side_home")
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(
            Intent(this, ProfileActivity::class.java).putExtra("from", "manager")
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        finish()
    }
}