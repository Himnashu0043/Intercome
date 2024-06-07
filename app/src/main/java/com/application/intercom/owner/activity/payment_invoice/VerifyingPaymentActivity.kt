package com.application.intercom.owner.activity.payment_invoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityVerifyingPaymentBinding
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.owner.activity.ownerbilling.OwnerBillingActivity
import com.application.intercom.tenant.activity.billing.TenantBillingsActivity
import com.application.intercom.tenant.activity.profile.ProfileActivity
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.roundToInt


class VerifyingPaymentActivity : BaseActivity<ActivityVerifyingPaymentBinding>() {

    override fun getLayout(): ActivityVerifyingPaymentBinding {
        return ActivityVerifyingPaymentBinding.inflate(layoutInflater)
    }

    private var from: String = ""
    private var accountNumber: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from").toString()
        accountNumber = intent.getStringExtra("account").toString()
        println("----from${from}")
        println("----accountNumber${accountNumber}")
        initView()
        listener()
        startTimer()
    }

    private fun initView() {
        binding.textView190.text = accountNumber
        binding.commonBtn.tv.text = getString(R.string.okay)

    }


    private fun startTimer() {
        val time = 1800000L
        object : CountDownTimer(time, 1000) {
            override fun onTick(p0: Long) {
                val progress = ((1-(p0.toFloat()/time.toFloat()))*100).roundToInt()
                binding.progressBar.setProgress(progress, true)
                val f: NumberFormat = DecimalFormat("00")
                val min = p0 / 60000 % 60
                val sec = p0 / 1000 % 60
                binding.tvTimerText.text = binding.tvTimerText.context.getString(
                    R.string.min_sec,
                    f.format(min),
                    f.format(sec)
                )
            }

            override fun onFinish() {
                binding.progressBar.setProgress(100, true)
                binding.tvTimerText.text = "00:00"
            }
        }.start()
    }

    private fun listener() {
        binding.commonBtn.tv.setOnClickListener {
            if (from.equals("owner")) {
                startActivity(
                    Intent(this, OwnerBillingActivity::class.java).putExtra("from", from).putExtra("key","bank_pay")
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                finish()
            } else if (from == "manager") {
                startActivity(
                    Intent(this, ManagerMainActivity::class.java).putExtra("from", from)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                finish()
            } else {
                startActivity(
                    Intent(this, TenantBillingsActivity::class.java).putExtra("from", from).putExtra("key","bank_pay")
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                finish()
            }
        }

    }
}