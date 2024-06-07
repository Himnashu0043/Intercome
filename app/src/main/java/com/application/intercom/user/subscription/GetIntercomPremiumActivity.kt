package com.application.intercom.user.subscription

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.UserPlanViewModel
import com.application.intercom.data.model.factory.UserPLanFactory
import com.application.intercom.data.model.local.BuySubscribe.BuySubscribePost
import com.application.intercom.data.model.local.UserBuySubscriprtionPostModel
import com.application.intercom.data.model.remote.UserPlanList.UserPlanListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserPlanRespo.UserPlanRepo
import com.application.intercom.databinding.ActivityGetIntercomPremiumBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.user.dialog.SuccessDialog
import com.application.intercom.user.paymentGateway.PaymentGatewayActivity
import com.application.intercom.utils.*
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener


class GetIntercomPremiumActivity : AppCompatActivity(), UserPlanAdapter.Click,
    SSLCTransactionResponseListener {
    private lateinit var binding: ActivityGetIntercomPremiumBinding
    private lateinit var viewModel: UserPlanViewModel
    private var adptr: UserPlanAdapter? = null
    private var userPlanList = ArrayList<UserPlanListRes.Data>()
    private var planId: String = ""
    private var cost: String = ""
    private var contacts: Int = 0
    private var duration: Int = 0
    private var from: String = ""
    private var id: String = ""
    private var getDiscount: String? = null
    private var transaction_id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetIntercomPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        id = intent.getStringExtra("id").toString()
        getDiscount = intent.getStringExtra("discount").toString()
        println("----from${from}")
        println("----getDiscount${getDiscount}")
        println("----id${id}")
        init()
        /* binding.tvEnterPromocode.setOnClickListener {
             startActivity(Intent(this, SubscriptionCouponActivity::class.java))
         }
         binding.toolbar.ivBack.setOnClickListener {
             finish()
         }*/
        binding.btnSubmit.setOnClickListener {
            // buySub()
            val ssl = SSLCommerzInitialization(
                "incub612884b366bf8",
                "incub612884b366bf8@ssl",
                cost.toDouble(),
                SSLCCurrencyType.BDT,
                "123",
                "shopping",
                SSLCSdkType.TESTBOX
            )
            IntegrateSSLCommerz.getInstance(this).addSSLCommerzInitialization(ssl)
                .buildApiCall(this)
//            val ssl = SSLCommerzInitialization(
//                "doctorliveapplive",
//                "61AEF3BF29DE999631",
//                cost.toDouble(),
//                SSLCCurrencyType.BDT,
//                "id",
//                "CATEGORY",
//                SSLCSdkType.LIVE
//            )
//            IntegrateSSLCommerz.getInstance(this).addSSLCommerzInitialization(ssl).buildApiCall(this)
            //startActivity(Intent(this, PaymentGatewayActivity::class.java).putExtra("cost", cost))
        }
        binding.tvPromoCode.setOnClickListener {
            startActivity(
                Intent(this, SubscriptionCouponActivity::class.java).putExtra("id", id)
                    .putExtra("from", from)
            )
        }

        /* binding.layoutSilver.setOnClickListener {
             binding.tvSubscriptionName.setTextColor(ContextCompat.getColor(this, R.color.black))
             binding.tvSubscriptionDuration.setTextColor(ContextCompat.getColor(this, R.color.black))
             binding.tvSubscriptionDetails.setTextColor(ContextCompat.getColor(this, R.color.black))
             binding.tvSubscriptionPrice.setTextColor(ContextCompat.getColor(this, R.color.black))
             binding.tvGoldSubscriptionName.setTextColor(ContextCompat.getColor(this, R.color.white))
             binding.tvGoldSubscriptionDuration.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.white
                 )
             )
             binding.tvGoldSubscriptionDetails.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.white
                 )
             )
             binding.tvGoldSubscriptionPrice.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.white
                 )
             )
             binding.tvPlatinumSubscriptionName.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.white
                 )
             )
             binding.tvPlatinumSubscriptionDuration.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.white
                 )
             )
             binding.tvPlatinumSubscriptionDetails.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.white
                 )
             )
             binding.tvPlatinumSubscriptionPrice.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.white
                 )
             )

             binding.layoutSilver.background =
                 ContextCompat.getDrawable(this, R.drawable.bg_subscription_item)
             binding.layoutGold.background =
                 ContextCompat.getDrawable(this, R.drawable.bg_subscription_stroke_item)
             binding.layoutPlatinum.background =
                 ContextCompat.getDrawable(this, R.drawable.bg_subscription_stroke_item)
         }
         binding.layoutGold.setOnClickListener {

             binding.tvSubscriptionName.setTextColor(ContextCompat.getColor(this, R.color.white))
             binding.tvSubscriptionDuration.setTextColor(ContextCompat.getColor(this, R.color.white))
             binding.tvSubscriptionDetails.setTextColor(ContextCompat.getColor(this, R.color.white))
             binding.tvSubscriptionPrice.setTextColor(ContextCompat.getColor(this, R.color.white))
             binding.tvGoldSubscriptionName.setTextColor(ContextCompat.getColor(this, R.color.black))
             binding.tvGoldSubscriptionDuration.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.black
                 )
             )
             binding.tvGoldSubscriptionDetails.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.black
                 )
             )
             binding.tvGoldSubscriptionPrice.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.black
                 )
             )
             binding.tvPlatinumSubscriptionName.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.white
                 )
             )
             binding.tvPlatinumSubscriptionDuration.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.white
                 )
             )
             binding.tvPlatinumSubscriptionDetails.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.white
                 )
             )
             binding.tvPlatinumSubscriptionPrice.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.white
                 )
             )
             binding.layoutGold.background =
                 ContextCompat.getDrawable(this, R.drawable.bg_subscription_item)
             binding.layoutPlatinum.background =
                 ContextCompat.getDrawable(this, R.drawable.bg_subscription_stroke_item)
             binding.layoutSilver.background =
                 ContextCompat.getDrawable(this, R.drawable.bg_subscription_stroke_item)
         }
         binding.layoutPlatinum.setOnClickListener {
             binding.tvSubscriptionName.setTextColor(ContextCompat.getColor(this, R.color.white))
             binding.tvSubscriptionDuration.setTextColor(ContextCompat.getColor(this, R.color.white))
             binding.tvSubscriptionDetails.setTextColor(ContextCompat.getColor(this, R.color.white))
             binding.tvSubscriptionPrice.setTextColor(ContextCompat.getColor(this, R.color.white))
             binding.tvGoldSubscriptionName.setTextColor(ContextCompat.getColor(this, R.color.white))
             binding.tvGoldSubscriptionDuration.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.white
                 )
             )
             binding.tvGoldSubscriptionDetails.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.white
                 )
             )
             binding.tvGoldSubscriptionPrice.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.white
                 )
             )
             binding.tvPlatinumSubscriptionName.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.black
                 )
             )
             binding.tvPlatinumSubscriptionDuration.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.black
                 )
             )
             binding.tvPlatinumSubscriptionDetails.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.black
                 )
             )
             binding.tvPlatinumSubscriptionPrice.setTextColor(
                 ContextCompat.getColor(
                     this,
                     R.color.black
                 )
             )
             binding.layoutPlatinum.background =
                 ContextCompat.getDrawable(this, R.drawable.bg_subscription_item)
             binding.layoutGold.background =
                 ContextCompat.getDrawable(this, R.drawable.bg_subscription_stroke_item)
             binding.layoutSilver.background =
                 ContextCompat.getDrawable(this, R.drawable.bg_subscription_stroke_item)
         }*/
    }

    private fun init() {
        initialize()
        getUserPlanList()
        observer()
    }

    private fun initialize() {
        val repo = UserPlanRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserPLanFactory(repo))[UserPlanViewModel::class.java]


    }

    private fun getUserPlanList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
           ""
        )
        viewModel.userPlan(token)

    }

    private fun buySub() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation!!.latitude.toString()
        )
        val model = BuySubscribePost(contacts, cost, duration, planId)
        viewModel.buySubscription(token, model)

    }

    private fun userSubscription() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation!!.latitude.toString()
        )
        val model = UserBuySubscriprtionPostModel(contacts, cost, duration, planId, transaction_id,"")
        viewModel.userBuySubscription(token, model)

    }

    /* private fun activeActive(
         activeLayout: ConstraintLayout,
         inActiveLayoutOne: ConstraintLayout,
         inActiveLayoutTwo: ConstraintLayout
     ) {

         activeLayout.background = ContextCompat.getDrawable(this, R.drawable.bg_subscription_item)
 //        activeLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.orange))
         inActiveLayoutOne.background =
             ContextCompat.getDrawable(this, R.drawable.bg_subscription_stroke_item)
         inActiveLayoutTwo.background =
             ContextCompat.getDrawable(this, R.drawable.bg_subscription_stroke_item)
     }*/

    private fun observer() {
        viewModel.userPlanLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            userPlanList.clear()
                            userPlanList.addAll(it.data)
                            binding.rvPlan.layoutManager = LinearLayoutManager(this)
                            adptr = UserPlanAdapter(this, userPlanList, this, getDiscount!!)
                            userPlanList.get(0).isSelected = true
                            planId = userPlanList.get(0)._id
                            cost = userPlanList.get(0).price.toString()
                            contacts = userPlanList.get(0).contacts.toInt()
                            duration = userPlanList.get(0).validFor.toInt()
                            binding.rvPlan.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                            println("---test${planId},${cost},${contacts},${duration}")

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
        viewModel.userBuySubscriptionLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            buySub()

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
        viewModel.buySubscriptionLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            if (from.equals("user_parking_details")) {
                                SuccessDialog.newInstance(
                                    "  Submitted",
                                    " Our executive will connect with you.",
                                    from,
                                    id
                                )
                                    .show(supportFragmentManager, SuccessDialog.TAG)
                            } else if (from.equals("tenant_parking_details")) {
                                SuccessDialog.newInstance(
                                    "  Submitted",
                                    " Our executive will connect with you.",
                                    from,
                                    id
                                )
                                    .show(supportFragmentManager, SuccessDialog.TAG)
                            } else if (from.equals("ownerSide_property")) {
                                SuccessDialog.newInstance(
                                    "  Submitted",
                                    " Our executive will connect with you.",
                                    from,
                                    id
                                )
                                    .show(supportFragmentManager, SuccessDialog.TAG)
                            } else {
                                SuccessDialog.newInstance(
                                    "  Submitted",
                                    " Our executive will connect with you.",
                                    from,
                                    id
                                )
                                    .show(supportFragmentManager, SuccessDialog.TAG)
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

    }

    override fun onClick(msg: UserPlanListRes.Data) {
        planId = msg._id
        contacts = msg.contacts.toInt()
        cost = msg.price.toString()
        duration = msg.validFor.toInt()
        println("---test1${planId},${cost},${contacts},${duration}")

    }

    override fun transactionSuccess(p0: SSLCTransactionInfoModel?) {
        transaction_id = p0!!.tranId
        userSubscription()
    }

    override fun transactionFail(p0: String?) {

    }

    override fun merchantValidationError(p0: String?) {

    }


}