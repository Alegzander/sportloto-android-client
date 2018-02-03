package com.vovasoft.unilot.components

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


    val isRegistered: Boolean
        get() = this.manager != null


    fun registerFragmentManager(manager: FragmentManager) {
        this.manager = manager
    }


    fun openFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        manager?.let { manager ->
            if (addToBackStack && currentFragment != null) fragmentStack.push(currentFragment)
            val transaction = manager.beginTransaction().add(R.id.contentFrame, fragment)
            transaction.commit()
            currentFragment = fragment
        }
    }


    fun openFragmentWithAnimation(fragment: Fragment, anim: Int, addToBackStack: Boolean = false) {
        manager?.let { manager ->
            if (addToBackStack && currentFragment != null) fragmentStack.push(currentFragment)
            val transaction = manager.beginTransaction().setCustomAnimations(anim, anim).add(R.id.contentFrame, fragment)
            transaction.commit()
            currentFragment = fragment
        }
    }


    fun popBackStack() {
        manager?.let { manager ->
            if (fragmentStack.isNotEmpty()) {
                val fragment = fragmentStack.pop()
                fragment?.let {
                    val transaction = manager.beginTransaction().replace(R.id.contentFrame, fragment)
                    currentFragment?.let { transaction.remove(currentFragment) }
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