package com.vovasoft.sportloto.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.google.firebase.iid.FirebaseInstanceId
import com.vovasoft.sportloto.App
import com.vovasoft.sportloto.R
import com.vovasoft.sportloto.components.Preferences
import com.vovasoft.sportloto.ui.fragments.BaseFragment
import com.vovasoft.sportloto.ui.fragments.MainPagerFragment
import com.vovasoft.sportloto.ui.fragments.SettingsFragment
import com.vovasoft.sportloto.view_models.AppVM
import com.vovasoft.sportloto.view_models.GamesVM
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_drawer_layout.*



class MainActivity : AppCompatActivity() {

    private val gamesVM: GamesVM
        get() = ViewModelProviders.of(this).get(GamesVM::class.java)

    private val appVM: AppVM
        get() = ViewModelProviders.of(this).get(AppVM::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        Preferences.updateLanguage()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppFragmentManager.instance.registerFragmentManager(supportFragmentManager)

        setupDrawer()

        Log.w("FirebaseToken", FirebaseInstanceId.getInstance().token ?: "No token")
        App.updateNotificationToken()

        AppFragmentManager.instance.clearBackStack()
        if (Preferences.isLanguageChanged) {
            AppFragmentManager.instance.openFragment(MainPagerFragment())
            AppFragmentManager.instance.openFragment(SettingsFragment(), true)
            Preferences.isLanguageChanged = false
        }
        else {
            AppFragmentManager.instance.openFragment(MainPagerFragment())
        }

        gamesVM.updateGamesList()

//        Log.e("Width", .toString())
//        Log.e("Height", height.toString())

    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Preferences.isLanguageChanged = true
        super.recreate()
    }


    @SuppressLint("SetTextI18n")
    private fun setupDrawer() {
        navigationView?.refreshDrawableState()

        historyBtn.setOnClickListener {
            drawerLayout.closeDrawers()
        }

        statisticBtn.setOnClickListener {
            drawerLayout.closeDrawers()
        }

        infoBtn.setOnClickListener {
            drawerLayout.closeDrawers()
        }

        whitepaperBtn.setOnClickListener {
            drawerLayout.closeDrawers()
        }

        settingsBtn.setOnClickListener {
            drawerLayout.closeDrawers()
            AppFragmentManager.instance.openFragment(SettingsFragment(), true)
        }

    }


    fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    override fun onBackPressed() {
        if (AppFragmentManager.instance.backStackSize() == 0) {
            super.onBackPressed()
        } else {
            val fragment = AppFragmentManager.instance.currentFragment as BaseFragment
            fragment.onBackPressed()
        }
    }

}
