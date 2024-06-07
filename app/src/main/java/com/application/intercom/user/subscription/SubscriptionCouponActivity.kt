package com.application.intercom.user.subscription

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.UserPlanViewModel
import com.application.intercom.data.model.factory.UserPLanFactory
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.data.repository.UserPlanRespo.UserPlanRepo
import com.application.intercom.databinding.ActivitySubscriptionCouponBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.user.dialog.SuccessDialog
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.utils.*

class SubscriptionCouponActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubscriptionCouponBinding
    private var id: String = ""
    private var from: String = ""
    private lateinit var viewModel: UserHomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionCouponBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getStringExtra("id").toString()
        from = intent.getStringExtra("from").toString()
        println("----from${from}")

        println("----id${id}")
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.btnActivate.tv.text = "Activate"
        binding.btnActivate.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            }
            usercode()
            /*  SuccessDialog.newInstance(
                  getString(R.string.tv_register_member),
                  getString(R.string.app_name), from,
                  id
              )
                  .show(supportFragmentManager, SuccessDialog.TAG)*/

        }
        initialize()
        observer()
    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]


    }

    private fun usercode() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation!!.latitude.toString()
        )
        viewModel.userCode(token, binding.edtCoupon.text.trim().toString())

    }

    private fun observer() {
        viewModel.userCodeLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(
                                    this,
                                    GetIntercomPremiumActivity::class.java
                                ).putExtra("from", from).putExtra("id", id)
                                    .putExtra("discount", it.data.discount.toString())
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                            finish()
                            println("----getDiscount${it.data.discount.toString()}")

                        } else if (it.status == AppConstants.STATUS_401) {
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

    private fun validationData(): Boolean {
        if (TextUtils.isEmpty(binding.edtCoupon.text.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Coupon Code!!", Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true

    }
}