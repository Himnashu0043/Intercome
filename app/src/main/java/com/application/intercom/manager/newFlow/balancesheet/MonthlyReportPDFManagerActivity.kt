package com.application.intercom.manager.newFlow.balancesheet

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityMonthlyReportPdfmanagerBinding
import com.application.intercom.utils.CommonUtil
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import org.chromium.base.task.AsyncTask
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MonthlyReportPDFManagerActivity : BaseActivity<ActivityMonthlyReportPdfmanagerBinding>(),
    OnPageChangeListener, OnLoadCompleteListener {
    private var pdfUrl: String? = ""
    override fun getLayout(): ActivityMonthlyReportPdfmanagerBinding {
        return ActivityMonthlyReportPdfmanagerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        listener()
    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.toolbar.ivNotification.setOnClickListener {
            Toast.makeText(this, "Downloading Start", Toast.LENGTH_SHORT).show()
            CommonUtil.startDownload(pdfUrl ?: "", this, "Pdf", "Pdf")

        }
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = "Monthly Report"
        CommonUtil.themeSet(this, window)
        CommonUtil.clearLightStatusBar(this)
        pdfUrl = intent.getStringExtra("pdfUrl") ?: ""
        //binding.pdfView.fromUri(Uri.parse(pdfUrl.toString())).load()
        LoadPdfFromUrl().execute(pdfUrl)
        binding.toolbar.ivNotification.visibility = View.VISIBLE
        binding.toolbar.ivNotification.setImageResource(R.drawable.pdf_icon)

    }

    inner class LoadPdfFromUrl : android.os.AsyncTask<String, Void, InputStream>() {
        override fun doInBackground(vararg params: String?): InputStream? {
            val urlString = params[0]
            try {
                val url = URL(urlString)
                val urlConnection = url.openConnection() as HttpURLConnection
                return BufferedInputStream(urlConnection.inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(inputStream: InputStream?) {
            if (inputStream != null) {
                binding.pdfView.fromStream(inputStream)
                    .scrollHandle(DefaultScrollHandle(this@MonthlyReportPDFManagerActivity))
                    .spacing(10) // in dp
                    .onPageChange(this@MonthlyReportPDFManagerActivity)
                    .onLoad(this@MonthlyReportPDFManagerActivity)
                    .enableAnnotationRendering(true)
                    .enableAntialiasing(true)
                    .load()
            } else {
                // Handle error
            }
        }
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        // Page changed callback
    }

    override fun loadComplete(nbPages: Int) {
        // Load complete callback
    }
}