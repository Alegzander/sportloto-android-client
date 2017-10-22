package com.vovasoft.unilot.notifications

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    private val TAG = "InstanceIDService"

    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.w(TAG, "Refreshed token: " + refreshedToken!!)
    }

}