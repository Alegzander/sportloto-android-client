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
import com.vovasoft.sportloto.Preferences
import com.vovasoft.sportloto.R
import com.vovasoft.sportloto.ui.fragments.MainPagerFragment
import com.vovasoft.sportloto.view_models.GamesVM
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : LifecycleActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val gamesVM: GamesVM
        get() = ViewModelProviders.of(this).get(GamesVM::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppFragmentManager.instance.registerFragmentManager(supportFragmentManager)

        setupDrawer()

        Log.w("FirebaseToken", FirebaseInstanceId.getInstance().token ?: "No token")

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
        navigationView?.setNavigationItemSelectedListener(this)
        navigationView?.refreshDrawableState()
//        onNavigationItemSelected(navigationView.menu.findItem(R.id.nav_my_orders))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {

        }

        drawerLayout.closeDrawers()

        return false
    }

}
