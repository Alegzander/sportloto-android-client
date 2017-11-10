package com.vovasoft.unilot.ui

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.crashlytics.android.Crashlytics
import com.google.firebase.iid.FirebaseInstanceId
import com.vovasoft.unilot.App
import com.vovasoft.unilot.R
import com.vovasoft.unilot.components.NetworkStateReceiver
import com.vovasoft.unilot.components.Preferences
import com.vovasoft.unilot.repository.RepositoryCallback
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult
import com.vovasoft.unilot.ui.fragments.BaseFragment
import com.vovasoft.unilot.ui.fragments.HistoryFragment
import com.vovasoft.unilot.ui.fragments.MainPagerFragment
import com.vovasoft.unilot.ui.fragments.SettingsFragment
import com.vovasoft.unilot.view_models.AppVM
import com.vovasoft.unilot.view_models.GamesVM
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_drawer_layout.*
import java.util.*


class MainActivity : AppCompatActivity(), NetworkStateReceiver.ReceiverCallback {

    private lateinit var appVM: AppVM

    private lateinit var gamesVM: GamesVM

    private lateinit var networkStateReceiver: NetworkStateReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        Preferences.updateLanguage()
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)

        appVM = ViewModelProviders.of(this).get(AppVM::class.java)

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


    private val newsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateMarkers()
        }
    }


    private val updatesReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val updateIntent = Intent(this@MainActivity, UpdateRequireActivity::class.java)
            startActivity(updateIntent)
            finish()
        }
    }


    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(newsReceiver, IntentFilter("news"))
        LocalBroadcastManager.getInstance(this).registerReceiver(updatesReceiver, IntentFilter("update"))
    }


    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(newsReceiver)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updatesReceiver)
    }


    private fun updateMarkers() {
        gamesVM.getAllResults(object: RepositoryCallback<List<GameResult>> {
            override fun done(data: List<GameResult>?) {
                data?.let { list ->
                    if (list.isNotEmpty()) {
                        drawerMarkerLabel.text = list.size.toString()
                        drawerMarkerLabel.visibility = View.VISIBLE
                    }
                    else {
                        drawerMarkerLabel.visibility = View.INVISIBLE
                    }
                }
            }
        })
    }


    override fun onResume() {
        super.onResume()
        App.isBackground = false
        gamesVM.updateGamesList()
        updateMarkers()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
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

        val intentData = intent.getSerializableExtra("data") as HashMap<*, *>?
        intentData?.get("type")?.let {
            when (Game.Type.from(it as Int)) {
                Game.Type.DAILY -> appVM.selectedPage.value = MainPagerFragment.Page.DAY.value
                Game.Type.WEEKLY -> appVM.selectedPage.value = MainPagerFragment.Page.WEEK.value
                Game.Type.MONTHLY -> appVM.selectedPage.value = MainPagerFragment.Page.MONTH.value
            }
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
                    .setTitle(R.string.how_does_it_work)
                    .setMessage(R.string.how_does_it_work_text)
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
