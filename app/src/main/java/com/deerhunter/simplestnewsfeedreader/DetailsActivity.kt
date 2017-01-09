package com.deerhunter.simplestnewsfeedreader

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val url = intent?.getStringExtra(URL)

        webView.settings.apply {
            domStorageEnabled = true
            builtInZoomControls = false
            displayZoomControls = false
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = false
        }

        progressView.max = 100

        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressView.visibility = View.VISIBLE
                progressView.progress = 0
            }

            override fun onPageFinished(view: WebView, url: String?) {
                super.onPageFinished(view, url)
                progressView.visibility = View.GONE
            }
        })

        webView.setWebChromeClient(object:WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressView.progress = newProgress
            }
        })

        webView.loadUrl(url)
    }

    companion object {
        val URL = "URL_KEY"
    }
}
