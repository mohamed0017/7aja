package com.haja.hja.View.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_webview.*
import android.graphics.Bitmap
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import com.haja.hja.BaseActivity
import com.haja.hja.R
import im.delight.android.webview.AdvancedWebView


class WebviewActivity : BaseActivity() , AdvancedWebView.Listener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        webview.setListener(this, this)
        webview.loadUrl(intent.getStringExtra("url"))

        
    }


    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        webview.onResume()
        // ...
    }

    @SuppressLint("NewApi")
    override fun onPause() {
        webview.onPause()
        // ...
        super.onPause()
    }

    override fun onDestroy() {
        webview.onDestroy()
        // ...
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        webview.onActivityResult(requestCode, resultCode, intent)
        // ...
    }

    override fun onBackPressed() {
        if (!webview.onBackPressed()) {
            return
        }
        // ...
        super.onBackPressed()
    }

    override fun onPageStarted(url: String, favicon: Bitmap?) {
        Log.e("onPageStarted", url +"..")
    }

    override fun onPageFinished(url: String) {}

    override fun onPageError(errorCode: Int, description: String, failingUrl: String) {
        Log.e("onPageError", description + "..")

    }

    override fun onDownloadRequested(
        url: String,
        suggestedFilename: String,
        mimeType: String,
        contentLength: Long,
        contentDisposition: String,
        userAgent: String
    ) {
    }

    override fun onExternalPageRequest(url: String) {}


}
