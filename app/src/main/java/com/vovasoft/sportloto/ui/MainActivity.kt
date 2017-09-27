package com.vovasoft.sportloto.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import com.google.firebase.iid.FirebaseInstanceId
import com.vovasoft.sportloto.App
import com.vovasoft.sportloto.Preferences
import com.vovasoft.sportloto.R
import com.vovasoft.sportloto.ui.fragments.MainPagerFragment
import com.vovasoft.sportloto.view_models.GamesVM
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_drawer_layout.*

class MainActivity : LifecycleActivity() {

    private val gamesVM: GamesVM
        get() = ViewModelProviders.of(this).get(GamesVM::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppFragmentManager.instance.registerFragmentManager(supportFragmentManager)

        setupDrawer()

        Log.w("FirebaseToken", FirebaseInstanceId.getInstance().token ?: "No token")
        App.updateNotificationToken()

        gamesVM.getGamesList().observe(this, Observer { games ->
            games?.let {
                println(games)
            }
        })

        AppFragmentManager.instance.clearBackStack()
        AppFragmentManager.instance.openFragment(MainPagerFragment())
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
        }


    }

}
