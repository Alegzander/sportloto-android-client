package com.vovasoft.sportloto.ui.fragments

import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import com.vovasoft.sportloto.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
abstract class BaseFragment : Fragment() {

    open fun onBackPressed() { }

    open fun showLoading(show: Boolean) { }

    open fun onNetworkStateChanged(isOnline: Boolean) { }

    fun lockDrawerMode(lock: Boolean) {
        val drawer = activity.drawerLayout
        if (lock) drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        else drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    val activity : MainActivity
        get() = super.getActivity() as MainActivity
}