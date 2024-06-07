package com.application.intercom.user.paymentGateway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.intercom.databinding.ActivityPaymentGatewayBinding
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener

class PaymentGatewayActivity : AppCompatActivity(), SSLCTransactionResponseListener {
    lateinit var binding: ActivityPaymentGatewayBinding
    private var cost: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentGatewayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cost = intent.getStringExtra("cost").toString()
        initView()
        listener()
    }

    private fun initView() {


    }

    private fun listener() {

    }

    override fun transactionSuccess(p0: SSLCTransactionInfoModel?) {

    }

    override fun transactionFail(p0: String?) {

    }

    override fun merchantValidationError(p0: String?) {

    }
}