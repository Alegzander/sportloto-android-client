package com.vovasoft.unilot.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.google.firebase.iid.FirebaseInstanceId
import com.vovasoft.unilot.App
import com.vovasoft.unilot.R
import com.vovasoft.unilot.components.NetworkStateReceiver
import com.vovasoft.unilot.components.Preferences
import com.vovasoft.unilot.components.daysPlural
import com.vovasoft.unilot.ui.fragments.BaseFragment
import com.vovasoft.unilot.ui.fragments.HistoryFragment
import com.vovasoft.unilot.ui.fragments.MainPagerFragment
import com.vovasoft.unilot.ui.fragments.SettingsFragment
import com.vovasoft.unilot.view_models.GamesVM
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_drawer_layout.*



class MainActivity : AppCompatActivity(), NetworkStateReceiver.ReceiverCallback {

    private lateinit var gamesVM: GamesVM

    private lateinit var networkStateReceiver: NetworkStateReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        Preferences.updateLanguage()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gamesVM = ViewModelProviders.of(this).get(GamesVM::class.java)

        networkStateReceiver = NetworkStateReceiver(this)
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkStateReceiver, filter)

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
    }


    override fun onResume() {
        super.onResume()
        App.isBackground = false
        gamesVM.updateGamesList()
    }


    override fun onPause() {
        super.onPause()
        App.isBackground = true
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (Preferences.isLanguageChanged) {
            super.recreate()
        }
    }


    override fun networkStateChanged(isOnline: Boolean) {
        if (isOnline) {
            gamesVM.updateGamesList()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setupDrawer() {
        navigationView?.refreshDrawableState()

        historyBtn.setOnClickListener {
            drawerLayout.closeDrawers()
            AppFragmentManager.instance.openFragment(HistoryFragment(), true)
        }

        statisticBtn.setOnClickListener {
            drawerLayout.closeDrawers()
        }

        infoBtn.setOnClickListener {
            AlertDialog.Builder(this)
                    .setTitle(R.string.how_does_it_works)
                    .setMessage(R.string.how_does_it_works_text)
                    .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
                    .create().show()
            drawerLayout.closeDrawers()
        }

        whitepaperBtn.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.presentation_url)))
            startActivity(browserIntent)
            drawerLayout.closeDrawers()
        }

        settingsBtn.setOnClickListener {
            drawerLayout.closeDrawers()
            AppFragmentManager.instance.openFragment(SettingsFragment(), true)
        }

        soonBtn.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://unilot.io/ru/"))
            startActivity(browserIntent)
        }

        facebookBtn.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/unilot.io/"))
            startActivity(browserIntent)
        }

        telecgramBtn.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/unilot_channel/"))
            startActivity(browserIntent)
        }

        redditBtn.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.reddit.com/user/unilot_lottery/"))
            startActivity(browserIntent)
        }

        twitterBtn.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/unilot_lottery/"))
            startActivity(browserIntent)
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


    override fun onDestroy() {
        unregisterReceiver(networkStateReceiver)
        super.onDestroy()
    }

}
