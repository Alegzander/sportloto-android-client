package com.vovasoft.unilot.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log

/***************************************************************************
 * Created by arseniy on 30/10/2017.
 ****************************************************************************/

class NetworkStateReceiver(private val callback: ReceiverCallback) : BroadcastReceiver() {

    interface ReceiverCallback {
        fun networkStateChanged(isOnline: Boolean)
    }


    companion object {
        fun isOnline(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }
    }


    override fun onReceive(context: Context, intent: Intent) {
        Log.e("app", "Network connectivity change")
        if (intent.extras != null) {
            callback.networkStateChanged(isOnline(context))
        }
    }

}
