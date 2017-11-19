package com.vovasoft.unilot.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.RepositoryCallback
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult
import com.vovasoft.unilot.ui.AppFragmentManager
import com.vovasoft.unilot.ui.recycler_adapters.HistoryRecyclerAdapter
import com.vovasoft.unilot.view_models.AppVM
import com.vovasoft.unilot.view_models.GamesVM
import kotlinx.android.synthetic.main.fragment_history.*

/***************************************************************************
 * Created by arseniy on 15/10/2017.
 ****************************************************************************/
class HistoryFragment : BaseFragment() {

    private lateinit var appVM: AppVM

    private lateinit var gamesVM: GamesVM

    private val adapter = HistoryRecyclerAdapter()

    private val history = mutableListOf<Game>()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_history, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appVM = ViewModelProviders.of(activity).get(AppVM::class.java)
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

        gamesVM.getAllResults(object: RepositoryCallback<List<GameResult>> {
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
                appVM.selectedHistoryFilter.value?.let {
                    filterData(it)
                }
            }
        })

        appVM.selectedHistoryFilter.observe(this, Observer {
            it?.let {
                filterData(it)
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
                    Game.Type.DAILY.value -> appVM.selectedPage.value = MainPagerFragment.Page.DAY.value
                    Game.Type.WEEKLY.value -> appVM.selectedPage.value = MainPagerFragment.Page.WEEK.value
                    Game.Type.MONTHLY.value -> appVM.selectedPage.value = MainPagerFragment.Page.MONTH.value
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
                appVM.selectedHistoryFilter.value = null
                appVM.selectedHistoryFilter.value = tab?.position
            }
        })

        appVM.selectedHistoryFilter.value?.let {
            filterTabs.getTabAt(it)?.select()
        }

    }


    private fun filterData(pos: Int) {
        for (i in 0..filterTabs.tabCount) {
            val tab = filterTabs.getTabAt(i)
            tab?.icon?.setColorFilter(ContextCompat.getColor(context, R.color.colorLightGray), PorterDuff.Mode.SRC_IN)
            if (i == pos) {
                tab?.icon?.setColorFilter(ContextCompat.getColor(context, R.color.colorBlack), PorterDuff.Mode.SRC_IN)
                when (pos) {
                    0 -> adapter.dataSet = history
                    1 -> adapter.dataSet = history.filter { it.type == Game.Type.DAILY.value }.toMutableList()
                    2 -> adapter.dataSet = history.filter { it.type == Game.Type.WEEKLY.value }.toMutableList()
                    3 -> adapter.dataSet = history.filter { it.type == Game.Type.MONTHLY.value }.toMutableList()
                }
            }
        }
    }


    override fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }


    override fun onBackPressed() {
        lockDrawerMode(false)
        AppFragmentManager.instance.popBackStack()
    }

}
