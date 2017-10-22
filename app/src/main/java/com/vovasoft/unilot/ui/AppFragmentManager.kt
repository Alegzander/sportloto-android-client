package com.vovasoft.unilot.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.vovasoft.unilot.R
import java.util.*

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
class AppFragmentManager private constructor() {


    private object Holder { val INSTANCE = AppFragmentManager() }


    companion object {
        val instance: AppFragmentManager by lazy { Holder.INSTANCE }
    }


    private var manager: FragmentManager? = null


    private var fragmentStack: Stack<Fragment> = Stack()


    var currentFragment: Fragment? = null
        private set(value) {
            field = value
        }
        get() = field


    val isRegistered: Boolean
        get() = this.manager != null


    fun registerFragmentManager(manager: FragmentManager) {
        this.manager = manager
    }


    fun openFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        manager?.also { manager ->
            if (addToBackStack && currentFragment != null) fragmentStack.push(currentFragment)
            val transaction = manager.beginTransaction().replace(R.id.contentFrame, fragment)
            currentFragment?.also { transaction.remove(currentFragment) }
            transaction.commit()
            currentFragment = fragment
        }
    }


    fun popBackStack() {
        manager?.also { manager ->
            if (fragmentStack.isNotEmpty()) {
                val fragment = fragmentStack.pop()
                fragment?.also {
                    val transaction = manager.beginTransaction().replace(R.id.contentFrame, fragment)
                    currentFragment?.also { transaction.remove(currentFragment) }
                    transaction.commit()
                    currentFragment = fragment
                }
            }
        }
    }


    fun backStackSize() : Int {
        return fragmentStack.size
    }


    fun clearBackStack() {
        fragmentStack.clear()
    }

}