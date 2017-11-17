package com.vovasoft.unilot.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.unilot.R
import com.vovasoft.unilot.components.AppWebViewClients
import com.vovasoft.unilot.ui.AppFragmentManager
import kotlinx.android.synthetic.main.fragment_faq.*


/***************************************************************************
 * Created by arseniy on 17/11/2017.
 ****************************************************************************/
class FAQFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_faq, container, false)
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lockDrawerMode(true)

        backBtn.setOnClickListener {
            onBackPressed()
        }

        webView.settings.javaScriptEnabled = true

        webView.webViewClient = AppWebViewClients(context, progressBar)

        webView.loadUrl(getString(R.string.faq_url))
    }


    override fun onBackPressed() {
        lockDrawerMode(false)
        AppFragmentManager.instance.popBackStack()
    }

}