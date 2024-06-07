package com.application.intercom.user.newflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.UserPlanViewModel
import com.application.intercom.data.model.ViewModel.getUserDetailsViewModel.GetUserDetailsViewModel
import com.application.intercom.data.model.factory.UserPLanFactory
import com.application.intercom.data.model.factory.getUserDetailsFactory.GetUserDetailsFactory
import com.application.intercom.data.model.local.BuySubscribe.BuySubscribePost
import com.application.intercom.data.model.local.UserBuySubscriprtionPostModel
import com.application.intercom.data.model.remote.UserFAQList
import com.application.intercom.data.model.remote.UserPlanList.UserPlanListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ServiceRepository
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.data.repository.UserPlanRespo.UserPlanRepo
import com.application.intercom.data.repository.getUserDetailsRepo.GetUserDetailsRepo
import com.application.intercom.databinding.ActivityNewSubUserBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.user.dialog.SuccessDialog
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.user.home.UserTopAdvitAdapter
import com.application.intercom.user.login.LoginUsingOtpActivity
import com.application.intercom.user.newflow.adapter.FAQAdapter
import com.application.intercom.user.newflow.adapter.HowsAdapter
import com.application.intercom.user.service.ServiceFactory
import com.application.intercom.user.service.ServiceViewModel
import com.application.intercom.user.subscription.UserPlanAdapter
import com.application.intercom.utils.*
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener
import java.util.*
import kotlin.collections.ArrayList

class NewSubUserActivity : AppCompatActivity(), SSLCTransactionResponseListener {
    lateinit var binding: ActivityNewSubUserBinding
    private var howsAdapter: HowsAdapter? = null
    private var faqAdapter: FAQAdapter? = null
    private lateinit var viewModel: UserHomeViewModel
    private lateinit var viewModel1: UserPlanViewModel
    private var userPlanList = ArrayList<UserPlanListRes.Data>()
    private var from: String = ""
    private var id: String = ""
    private var planId: String = ""
    private var cost: String = ""
    private var contacts: Int = 0
    private var duration: Int = 0
    private var transaction_id: String = ""
    private var subscribeTittle: String = ""
    private var faqList = java.util.ArrayList<UserFAQList.Data>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewSubUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        from = intent.getStringExtra("from").toString()
        id = intent.getStringExtra("id").toString()
        println("----from$from")
        println("----id$id")
        initView()
        listener()
    }

    private fun initView() {
        binding.checkBox.isChecked = true
        binding.btmfree.background = ContextCompat.getDrawable(this, R.drawable.black_stroke_3_bg)
        initialize()
        observer()
        getUserFaq()
        getUserPlanList()
        binding.rcyWorks.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        howsAdapter = HowsAdapter(this)
        binding.rcyWorks.adapter = howsAdapter
        howsAdapter!!.notifyDataSetChanged()


    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]
        val repo2 = UserPlanRepo(BaseApplication.apiService)
        viewModel1 = ViewModelProvider(this, UserPLanFactory(repo2))[UserPlanViewModel::class.java]

    }

    private fun getUserFaq() {
        val token = prefs.getString(
            SessionConstants.TOKEN, /*GPSService.mLastLocation?.latitude.toString()*/""
        )
        viewModel.userFaq(token)

    }

    private fun getUserPlanList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            /*GPSService.mLastLocation!!.latitude.toString()*/""
        )
        viewModel1.userPlan(token)

    }

    private fun buySub() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        val model = BuySubscribePost(contacts, cost, duration, planId)
        viewModel1.buySubscription(token, model)

    }

    private fun userSubscription() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        val model =
            UserBuySubscriprtionPostModel(
                contacts,
                cost,
                duration,
                planId,
                transaction_id,
                subscribeTittle
            )
        viewModel1.userBuySubscription(token, model)

    }

    private fun observer() {
        viewModel.userFaqLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            faqList.clear()
                            faqList.addAll(it.data)
                            binding.rcyFAQ.layoutManager = LinearLayoutManager(this)
                            faqAdapter = FAQAdapter(this, faqList)
                            binding.rcyFAQ.adapter = faqAdapter
                            faqAdapter!!.notifyDataSetChanged()
                            if (it.data.isEmpty()) {
                                binding.faq.visibility = View.GONE
                            } else {
                                binding.faq.visibility = View.VISIBLE
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

        viewModel1.userPlanLiveData.observe(this, androidx.lifecycle.Observer {
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
                            binding.textView205112.text = it.data.get(0).title
                            binding.textView209.text = it.data.get(0).title
                            binding.textView205123.text = "৳${it.data.get(0).price}"
                            binding.tvFreeBtm.text = "৳${it.data.get(0).price}"
                            binding.tvfree.text = "Max${it.data.get(0).contacts}"
                            binding.tvValFree.text = it.data.get(0).validFor
                            cost = it.data.get(0).price.toString()
                            duration = userPlanList.get(0).validFor.toInt()
                            planId = userPlanList.get(0)._id
                            contacts = userPlanList.get(0).contacts.toInt()
                            subscribeTittle = userPlanList.get(0).title.toString()
                            println("---test1s${planId},${cost},${contacts},${duration} ${subscribeTittle}")
                            if (!it.data.get(1).title.isNullOrEmpty()) {
                                binding.textView20511.text = it.data.get(1).title
                                binding.textView2091.text = it.data.get(1).title
                                binding.textView20512.text = "৳${it.data.get(1).price}"
                                binding.tvBtmBasic.text = "৳${it.data.get(1).price}"
                                binding.tvBasic.text = "Max${it.data.get(1).contacts}"
                                binding.tvValBasic.text = it.data.get(1).validFor
                            }
                            if (!it.data.get(2).title.isNullOrEmpty()) {
                                binding.textView205.text = it.data.get(2).title
                                binding.textView20911.text = it.data.get(2).title
                                binding.textView2051.text = "৳${it.data.get(2).price}"
                                binding.tvbtmPro.text = "৳${it.data.get(2).price}"
                                binding.textView2061.text = "Max${it.data.get(2).contacts}"
                                binding.tvValpro.text = it.data.get(2).validFor
                            }

                            /*   binding.textView20511.text = it.data.get(1).title
                               binding.textView20512.text = "৳${it.data.get(1).price}"
                               binding.tvBtmBasic.text = "৳${it.data.get(1).price}"
                               binding.tvBasic.text = "Max${it.data.get(1).contacts}"
                               binding.tvValBasic.text = it.data.get(1).validFor

                               binding.textView205.text = it.data.get(1).title
                               binding.textView2051.text = "৳${it.data.get(1).price}"
                               binding.tvbtmPro.text = "৳${it.data.get(1).price}"
                               binding.textView2061.text = "Max${it.data.get(1).contacts}"
                               binding.tvValpro.text = it.data.get(1).validFor*/

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
        viewModel1.userBuySubscriptionLiveData.observe(this, androidx.lifecycle.Observer {
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
        viewModel1.buySubscriptionLiveData.observe(this, androidx.lifecycle.Observer {
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
                            } else if (from.equals("user_side_menu")) {
                                SuccessDialog.newInstance(
                                    "  Submitted",
                                    " Our executive will connect with you.",
                                    from,
                                    ""
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

    private fun listener() {
        binding.imageView115.setOnClickListener {
            finish()
        }
        binding.constraintLayout15.setOnClickListener {
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
        }
        binding.checkBox1.setOnClickListener {
            cost = userPlanList.get(1).price.toString()
            duration = userPlanList.get(1).validFor.toInt()
            planId = userPlanList.get(1)._id
            contacts = userPlanList.get(1).contacts.toInt()
            subscribeTittle = userPlanList.get(1).title.toString()

            println("---test1s${planId},${cost},${contacts},${duration} ${subscribeTittle}")
            binding.checkBox.isChecked = false
            binding.checkBox1.isChecked = true
            binding.checkBox11.isChecked = false
            binding.imageView127.visibility = View.VISIBLE
            binding.imageView128.visibility = View.GONE
            binding.btmBasic.background =
                ContextCompat.getDrawable(this, R.drawable.light_green_stroke_bg)
            binding.btmfree.background = ContextCompat.getDrawable(this, R.drawable.black_stroke_bg)
            binding.btmPro.background = ContextCompat.getDrawable(this, R.drawable.black_stroke_bg)

        }
        binding.checkBox11.setOnClickListener {
            cost = userPlanList.get(2).price.toString()
            duration = userPlanList.get(2).validFor.toInt()
            planId = userPlanList.get(2)._id
            contacts = userPlanList.get(2).contacts.toInt()
            subscribeTittle = userPlanList.get(2).title.toString()

            println("---test1s${planId},${cost},${contacts},${duration} ${subscribeTittle}")
            binding.checkBox.isChecked = false
            binding.checkBox1.isChecked = false
            binding.checkBox11.isChecked = true
            binding.imageView128.visibility = View.VISIBLE
            binding.imageView127.visibility = View.GONE
            binding.btmBasic.background =
                ContextCompat.getDrawable(this, R.drawable.black_stroke_bg)
            binding.btmfree.background = ContextCompat.getDrawable(this, R.drawable.black_stroke_bg)
            binding.btmPro.background =
                ContextCompat.getDrawable(this, R.drawable.light_yellow_stroke_bg)
        }
        binding.checkBox.setOnClickListener {
            cost = userPlanList.get(0).price.toString()
            duration = userPlanList.get(0).validFor.toInt()
            planId = userPlanList.get(0)._id
            contacts = userPlanList.get(0).contacts.toInt()
            subscribeTittle = userPlanList.get(0).title.toString()
            println("---test1s${planId},${cost},${contacts},${duration} ${subscribeTittle}")
            binding.checkBox.isChecked = true
            binding.checkBox1.isChecked = false
            binding.checkBox11.isChecked = false
            binding.imageView128.visibility = View.GONE
            binding.imageView127.visibility = View.GONE
            binding.btmBasic.background =
                ContextCompat.getDrawable(this, R.drawable.black_stroke_bg)
            binding.btmfree.background =
                ContextCompat.getDrawable(this, R.drawable.black_stroke_3_bg)
            binding.btmPro.background = ContextCompat.getDrawable(this, R.drawable.black_stroke_bg)

        }

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