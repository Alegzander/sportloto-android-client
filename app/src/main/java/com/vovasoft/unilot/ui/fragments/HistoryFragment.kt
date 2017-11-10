package com.vovasoft.unilot.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
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
                adapter.dataSet = games.toMutableList()
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
    }


    override fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }


    override fun onBackPressed() {
        lockDrawerMode(false)
        AppFragmentManager.instance.popBackStack()
    }

}
