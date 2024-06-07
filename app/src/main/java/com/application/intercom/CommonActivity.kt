package com.application.intercom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.application.intercom.databinding.ActivityCommonBinding

class CommonActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommonBinding
    private lateinit var webView: WebView
    private var from: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        initView()
    }

    private fun initView() {
        webView = binding.webview
        webView.webViewClient = WebViewClient()
        if (from.equals("terms")) {
            binding.toolbar.tvTittle.text = "Terms & Conditions"
            webView.loadUrl("http://3.130.15.227/intercom/static/terms&Conditions.html")
        } else {
            binding.toolbar.tvTittle.text = "Privacy Policy"
            webView.loadUrl("http://3.130.15.227/intercom/static/privacyPolicy.html")
        }


    }

    inner class WebViewClient : android.webkit.WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            binding.progressBar.visibility = View.GONE
        }
    }
}