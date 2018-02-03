package com.vovasoft.unilot.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.vovasoft.unilot.R
import com.vovasoft.unilot.components.AppFragmentManager
import com.vovasoft.unilot.components.Preferences
import com.vovasoft.unilot.repository.Reactive
import com.vovasoft.unilot.repository.models.entities.GameResult
import com.vovasoft.unilot.ui.pager_adapters.MainPagerAdapter
import com.vovasoft.unilot.view_models.AppSettings
import com.vovasoft.unilot.view_models.GamesVM
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_pager.*


/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
class MainPagerFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    enum class Page(val value: Int) {
        DAY(0), WEEK(1), MONTH(2), TOKEN(3);

        companion object {
            fun from(findValue: Int) : Page {
                return when (findValue) {
                    0 -> DAY
                    1 -> WEEK
                    2 -> MONTH
                    else -> TOKEN
                }
            }

            fun iconNormal(findValue: Int) : Int {
                return when (findValue) {
                    0 -> R.drawable.ic_day_gray
                    1 -> R.drawable.ic_week_gray
                    2 -> R.drawable.ic_month_gray
                    else -> R.drawable.ic_token_gray
                }
            }

            fun iconSelected(findValue: Int) : Int {
                return when (findValue) {
                    0 -> R.drawable.ic_day_orange
                    1 -> R.drawable.ic_week_orange
                    2 -> R.drawable.ic_month_orange
                    else -> R.drawable.ic_token_orange
                }
            }
        }
    }

    private lateinit var appSettings: AppSettings

    private lateinit var gamesVM: GamesVM


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_pager, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appSettings = ViewModelProviders.of(activity).get(AppSettings::class.java)
        gamesVM = ViewModelProviders.of(activity).get(GamesVM::class.java)
        drawerBtn.setOnClickListener { activity.drawerLayout.openDrawer(GravityCompat.START) }

        setupPager()
    }


    private val newsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateMarkers()
        }
    }


    override fun onStart() {
        super.onStart()
        context?.let { context ->
            LocalBroadcastManager.getInstance(context).registerReceiver(newsReceiver, IntentFilter("news"))
        }
    }


    override fun onStop() {
        super.onStop()
        context?.let { context ->
            LocalBroadcastManager.getInstance(context).unregisterReceiver(newsReceiver)
        }
    }


    private fun updateMarkers() {
        gamesVM.getAllResults(object: Reactive<List<GameResult>> {
            override fun done(data: List<GameResult>?) {
                data?.let { list ->
                    context?.let {
                        if (list.isNotEmpty()) {
                            markerLabel.text = list.size.toString()
                            markerLabel.visibility = View.VISIBLE
                        }
                        else {
                            markerLabel.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        })
    }


    override fun onResume() {
        super.onResume()

        refreshLayout.setOnRefreshListener(this)
        refreshLayout.isRefreshing = true

        observeData()
        updateMarkers()
        updateTabs()
        refreshPager()
    }


    private fun observeData() {
        gamesVM.getGamesList().observe(this, Observer {
            refreshLayout.isRefreshing = false
        })

        appSettings.gamePageChanged.observe(this, Observer { changed ->
            changed?.let {
                if (it) {
                    updateTabs()
                    refreshPager()
                }
            }
        })
    }


    private fun setupPager() {
        refreshLayout.setColorSchemeResources(R.color.colorAccent)

        profileBtn.setOnClickListener {
            AppFragmentManager.instance.openFragment(ProfileFragment(), true)
            Answers.getInstance().logCustom(CustomEvent("EVENT_PROFILE")
                    .putCustomAttribute("language", Preferences.instance.language))
        }

        val pagerAdapter = MainPagerAdapter(childFragmentManager)

        pager.adapter = pagerAdapter

        tabs.setupWithViewPager(pager)
        tabs.getTabAt(Page.DAY.value)?.setText(R.string.daily)?.setIcon(R.drawable.ic_day_gray)
        tabs.getTabAt(Page.WEEK.value)?.setText(R.string.weekly)?.setIcon(R.drawable.ic_week_gray)
        tabs.getTabAt(Page.MONTH.value)?.setText(R.string.monthly_bonus)?.setIcon(R.drawable.ic_month_gray)
        tabs.getTabAt(Page.TOKEN.value)?.setText(R.string.token)?.setIcon(R.drawable.ic_token_gray)

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val x = ((pager.width * (pager.adapter!!.count - 1 - position) - positionOffsetPixels) * computeFactor()).toInt()
                scrollView.scrollTo(x, 0)
            }

            override fun onPageSelected(position: Int) {
                activity.hideKeyboard()
                AppSettings.selectedPage = position
                updateTabs()
            }

            override fun onPageScrollStateChanged(state: Int) {
                enableDisableSwipeRefresh( state == ViewPager.SCROLL_STATE_IDLE )
            }

            private fun computeFactor(): Float {
                return (backgroundImg.measuredWidth - pager.width) / (pager.width * (pager.adapter!!.count - 1)).toFloat()
            }

            private fun enableDisableSwipeRefresh(enable: Boolean) {
                refreshLayout?.isEnabled = enable
            }
        })

        updateTabs()
    }


    private fun updateTabs() {
        context?.let { _ ->
            for (i in 0..tabs.tabCount) {
                val tab = tabs.getTabAt(i)
                if (i == AppSettings.selectedPage) {
                    tab?.setIcon(Page.iconSelected(i))
                }
                else {
                    tab?.setIcon(Page.iconNormal(i))
                }
            }
        }
    }


    private fun refreshPager() {
        Handler().post {
            view?.let {
                pager?.setCurrentItem(AppSettings.selectedPage, false)
            }
        }
    }


    override fun onRefresh() {
        gamesVM.updateGamesList()
    }

}
