package com.vovasoft.unilot.ui.fragments

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.unilot.App
import com.vovasoft.unilot.R
import com.vovasoft.unilot.components.AppFragmentManager
import com.vovasoft.unilot.components.toHumanDate
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.pure.Winner
import com.vovasoft.unilot.ui.recycler_adapters.DetailsHistoryRecyclerAdapter
import com.vovasoft.unilot.ui.widgets.ZxingReader
import com.vovasoft.unilot.view_models.GamesVM
import kotlinx.android.synthetic.main.fragment_history_game_details.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.regex.Pattern

/***************************************************************************
 * Created by arseniy on 22/10/2017.
 ****************************************************************************/
class HistoryGameDetailsFragment : BaseFragment(), SearchView.OnQueryTextListener {

    private lateinit var gamesVM: GamesVM

    private var hasCameraPermission = false

    private var game: Game? = null

    private val adapter = DetailsHistoryRecyclerAdapter()

    private val winners = mutableListOf<Winner>()

    private var searchValue: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history_game_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gamesVM = ViewModelProviders.of(activity).get(GamesVM::class.java)

        lockDrawerMode(true)
        game = gamesVM.selectedHistoryGame
        setupViews()
        observeData()
    }


    private fun observeData() {
        showLoading(true)
        gamesVM.getWallets().observe(this, Observer { wallets ->
            wallets?.let {
                adapter.wallets = it.toMutableList()
            }
        })

        game?.let {
            it.id?.let { id ->
                gamesVM.getWinners(id).observe(this, Observer { winnersData ->
                    showLoading(false)
                    winnersData?.let {
                        this.winners.clear()
                        this.winners.addAll(it)
                        winnersTv.text = it.size.toString()

                        doAsync {
                            App.database.gameResultsDao().deleteGameResultByGameId(id)
                            uiThread {
                                context?.let { context ->
                                    val broadcaster = LocalBroadcastManager.getInstance(context)
                                    val broadcastIntent = Intent("news")
                                    broadcaster.sendBroadcast(broadcastIntent)
                                }
                            }
                        }
                    }
                    refreshSearch()
                })
            }
        }
    }


    private fun setupViews() {
        backBtn.setOnClickListener {
            onBackPressed()
        }

        when (game?.type) {
            Game.Type.DAILY.value -> titleIcon.setImageResource(R.drawable.ic_day_orange)
            Game.Type.WEEKLY.value -> titleIcon.setImageResource(R.drawable.ic_week_orange)
            Game.Type.MONTHLY.value -> titleIcon.setImageResource(R.drawable.ic_month_orange)
        }

        dateTv.text = game?.endTime().toHumanDate()
        prizeTv.text = "%.3f".format(game?.prize?.amount)
        peopleTv.text = game?.playersNum?.toString()

        searchBox.setOnQueryTextListener(this)

        scanBtn.setOnClickListener {
            if (hasCameraPermission) {
                runQReader()
            } else {
                requestPermissions(listOf(ZxingReader.CAMERA_PERMISSION).toTypedArray(), ZxingReader.CAMERA_PERMISSION_CODE)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }


    private fun refreshSearch() {
        adapter.dataSet = winners.filter { winner ->
            winner.wallet?.toLowerCase()?.contains(Regex(Pattern.quote(searchValue.toLowerCase()))) == true
        }.toMutableList()
    }


    override fun onQueryTextSubmit(text: String?): Boolean {
        return true
    }


    override fun onQueryTextChange(text: String?): Boolean {
        searchValue = text ?: ""
        refreshSearch()
        return true
    }


    private fun runQReader() {
        val intent = Intent(context, ZxingReader::class.java)
        startActivityForResult(intent, ZxingReader.RESULT_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ZxingReader.RESULT_CODE -> {
                    var result = data?.getStringExtra("result")
                    val p = Pattern.compile("(0x)?[0-9a-f]{40}")
                    val m = p.matcher(result?.toLowerCase())
                    if (m.find()) {
                        result = m.group()
                    }

                    searchBox.setQuery(result, false)
                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        context?.let { context ->
            when (requestCode) {
                ZxingReader.CAMERA_PERMISSION_CODE -> {
                    hasCameraPermission = ContextCompat.checkSelfPermission(context, ZxingReader.CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED
                    if (hasCameraPermission) {
                        runQReader()
                    }
                }
            }
        }
    }


    override fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }


    override fun onBackPressed() {
        lockDrawerMode(false)
        gamesVM.selectedHistoryGame = null
        AppFragmentManager.instance.popBackStack()
    }

}
