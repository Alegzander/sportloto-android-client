package com.vovasoft.unilot.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.unilot.R
import com.vovasoft.unilot.components.AppFragmentManager
import com.vovasoft.unilot.repository.Reactive
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult
import com.vovasoft.unilot.ui.recycler_adapters.HistoryRecyclerAdapter
import com.vovasoft.unilot.view_models.AppSettings
import com.vovasoft.unilot.view_models.GamesVM
import kotlinx.android.synthetic.main.fragment_history.*

/***************************************************************************
 * Created by arseniy on 15/10/2017.
 ****************************************************************************/
class HistoryFragment : BaseFragment() {

    enum class Page(val value: Int) {
        ALL(0), DAY(1), WEEK(2), MONTH(3), TOKEN(4);

        companion object {
            fun from(findValue: Int) : Page {
                return when (findValue) {
                    1 -> DAY
                    2 -> WEEK
                    3 -> MONTH
                    4 -> TOKEN
                    else -> ALL
                }
            }

            fun iconNormal(findValue: Int) : Int {
                return when (findValue) {
                    1 -> R.drawable.ic_day_gray
                    2 -> R.drawable.ic_week_gray
                    3 -> R.drawable.ic_month_gray
                    4 -> R.drawable.ic_token_gray
                    else -> R.drawable.ic_all_days_gray
                }
            }

            fun iconSelected(findValue: Int) : Int {
                return when (findValue) {
                    1 -> R.drawable.ic_day_black
                    2 -> R.drawable.ic_week_black
                    3 -> R.drawable.ic_month_black
                    4 -> R.drawable.ic_token_black
                    else -> R.drawable.ic_all_days_black
                }
            }
        }
    }


    private lateinit var appSettings: AppSettings

    private lateinit var gamesVM: GamesVM

    private val adapter = HistoryRecyclerAdapter()

    private val history = mutableListOf<Game>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appSettings = ViewModelProviders.of(activity).get(AppSettings::class.java)
        gamesVM = ViewModelProviders.of(activity).get(GamesVM::class.java)
        lockDrawerMode(true)
        setupViews()
        observeData()
    }


    override fun onResume() {
        super.onResume()
        recyclerView?.adapter?.notifyDataSetChanged()
    }


    private fun observeData() {
        showLoading(true)

        gamesVM.getAllResults(object: Reactive<List<GameResult>> {
            override fun done(data: List<GameResult>?) {
                data?.let { list ->
                    adapter.results = list.toMutableList()
                }
            }
        })

        gamesVM.getGamesHistory().observe(this, Observer { games ->
            showLoading(false)
            games?.let {
                history.clear()
                history.addAll(games)
                filterData(AppSettings.selectedHistoryFilter)
            }
        })

    }


    private fun setupViews() {
        backBtn.setOnClickListener {
            onBackPressed()
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { game ->
            if (game.status == Game.Status.PUBLISHED.value) {
                when (game.type) {
                    Game.Type.DAILY.value -> AppSettings.selectedPage = MainPagerFragment.Page.DAY.value
                    Game.Type.WEEKLY.value -> AppSettings.selectedPage = MainPagerFragment.Page.WEEK.value
                    Game.Type.MONTHLY.value -> AppSettings.selectedPage = MainPagerFragment.Page.MONTH.value
                    Game.Type.TOKEN.value -> AppSettings.selectedPage = MainPagerFragment.Page.TOKEN.value
                }
                onBackPressed()
            }
            else {
                gamesVM.selectedHistoryGame = game
                AppFragmentManager.instance.openFragment(HistoryGameDetailsFragment(), true)
            }
        }

        filterTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let {
                    AppSettings.selectedHistoryFilter = it
                    filterData(it)
                }
            }
        })

        filterTabs.getTabAt(AppSettings.selectedHistoryFilter)?.select()
    }


    private fun filterData(pos: Int) {
        context?.let { _ ->
            for (i in 0..filterTabs.tabCount) {
                val tab = filterTabs.getTabAt(i)
                if (i == pos) {
                    tab?.setIcon(Page.iconSelected(i))
                    when (pos) {
                        Page.ALL.value -> adapter.dataSet = history
                        Page.DAY.value -> adapter.dataSet = history.filter { it.type == Game.Type.DAILY.value }.toMutableList()
                        Page.WEEK.value -> adapter.dataSet = history.filter { it.type == Game.Type.WEEKLY.value }.toMutableList()
                        Page.MONTH.value -> adapter.dataSet = history.filter { it.type == Game.Type.MONTHLY.value }.toMutableList()
                        Page.TOKEN.value -> adapter.dataSet = history.filter { it.type == Game.Type.TOKEN.value }.toMutableList()
                    }
                }
                else {
                    tab?.setIcon(Page.iconNormal(i))
                }
            }
        }
    }


    override fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }


    override fun onBackPressed() {
        lockDrawerMode(false)
        appSettings.gamePageChanged.value = true
        AppFragmentManager.instance.popBackStack()
    }

}
