package com.application.intercom.user.newflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.local.newFlow.UserPlanDetailsList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.ActivitySellRentBinding
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.user.newflow.adapter.FeatureAdapter
import com.application.intercom.user.newflow.adapter.InfoAdpter
import com.application.intercom.utils.*

class Sell_RentActivity : AppCompatActivity() {
    lateinit var binding: ActivitySellRentBinding
    private var infoAdpter: InfoAdpter? = null
    private lateinit var viewModel: UserHomeViewModel
    private var list = ArrayList<UserPlanDetailsList.Data>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellRentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        listener()
    }

    private fun initView() {
        initialize()
        observer()



    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]


    }

    private fun userPlanDetails() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.userPlanDetails(token)

    }

    private fun observer() {
        viewModel.userPlanDetailsListLiveData.observe(this, androidx.lifecycle.Observer {
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
                            binding.rcyinfo.layoutManager = LinearLayoutManager(this)
                            infoAdpter = InfoAdpter(this, list)
                            binding.rcyinfo.adapter = infoAdpter
                            infoAdpter!!.notifyDataSetChanged()
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
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvContinue.setOnClickListener {
            startActivity(Intent(this, LetGetReadyActivity::class.java))
        }


    }

    override fun onResume() {
        super.onResume()
        userPlanDetails()
    }
}