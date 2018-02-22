package com.vovasoft.unilot.components

import android.content.Context
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast

/***************************************************************************
 * Created by arseniy on 17/11/2017.
 ****************************************************************************/
class AppWebViewClients(private val context: Context, private val progressBar: ProgressBar) : WebViewClient() {

    init {
        progressBar.visibility = View.VISIBLE
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        progressBar.visibility = View.GONE
    }

    override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
        Toast.makeText(context, description, Toast.LENGTH_SHORT).show()
    }

}
