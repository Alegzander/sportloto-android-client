package com.vovasoft.sportloto.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
object RuntimePermissionUtil {

    fun onRequestPermissionsResult(grantResults: IntArray, permissionsResultCallback: PermissionsResultCallback) {
        if (grantResults.isNotEmpty()) {
            for (grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    permissionsResultCallback.onPermissionGranted()
                } else {
                    permissionsResultCallback.onPermissionDenied()
                }
            }
        }
    }

    fun requestPermission(activity: Activity, permissions: Array<String>, REQUEST_CODE: Int) {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE)
    }

    fun requestPermission(activity: Activity, permission: String,
                          REQUEST_CODE: Int) {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(activity, arrayOf(permission), REQUEST_CODE)
    }

    fun checkPermissonGranted(context: Context, permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}
