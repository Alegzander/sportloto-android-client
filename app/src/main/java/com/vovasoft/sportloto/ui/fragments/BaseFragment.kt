package com.vovasoft.sportloto.ui.fragments

import android.arch.lifecycle.LifecycleFragment
import android.support.v4.widget.DrawerLayout
import com.vovasoft.sportloto.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
abstract class BaseFragment : LifecycleFragment() {

    open fun onBackPressed() { }

    open fun onNetworkStateChanged(isOnline: Boolean) { }

    fun lockDrawerMode(lock: Boolean) {
        val drawer = activity.drawerLayout
        if (lock) drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        else drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    val activity : MainActivity
        get() = super.getActivity() as MainActivity
}