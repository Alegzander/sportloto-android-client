package com.vovasoft.unilot.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.view.View
import com.vovasoft.unilot.ui.activities.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
abstract class BaseFragment : Fragment() {

    protected var isCreated = false

    protected var isOnScreen = false

    open fun onBackPressed() { }

    open fun showLoading(show: Boolean) { }

    open fun onNetworkStateChanged(isOnline: Boolean) { }


    fun lockDrawerMode(lock: Boolean) {
        val drawer = activity.drawerLayout
        if (lock) drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        else drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }


    lateinit var activity : MainActivity


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCreated = true
        activity = super.getActivity() as MainActivity
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isOnScreen = isVisibleToUser
    }

}