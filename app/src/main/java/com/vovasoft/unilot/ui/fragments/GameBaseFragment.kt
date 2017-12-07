package com.vovasoft.unilot.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import com.vovasoft.unilot.notifications.NotificationMessagingService
import com.vovasoft.unilot.repository.RepositoryCallback
import com.vovasoft.unilot.repository.models.GsonModel
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult
import com.vovasoft.unilot.ui.AppFragmentManager
import com.vovasoft.unilot.ui.dialogs.LooserDialog
import com.vovasoft.unilot.ui.dialogs.UnknownStatusDialog
import com.vovasoft.unilot.ui.dialogs.WinnerDialog
import com.vovasoft.unilot.view_models.GamesVM
import java.util.*

/***************************************************************************
 * Created by arseniy on 29/10/2017.
 ****************************************************************************/
abstract class GameBaseFragment : BaseFragment() {


    protected lateinit var gamesVM: GamesVM

    protected var game: Game? = null

    protected var countDown: CountDownTimer? = null

    private var allowDialogs: Boolean = true

    private lateinit var broadcaster: LocalBroadcastManager


    protected val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val action = intent.getSerializableExtra("action") as NotificationMessagingService.Action
                when (action) {
                    NotificationMessagingService.Action.GAME_UPDATED -> {
                        game = GsonModel.fromJson(intent.getStringExtra("data"), Game::class.java)
                        countDown?.cancel()
                        setupViews()
                    }

                    NotificationMessagingService.Action.GAME_FINISHED -> {
                        fetchGameResults()
                    }
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { context ->
            broadcaster = LocalBroadcastManager.getInstance(context)
        }
        gamesVM = ViewModelProviders.of(activity).get(GamesVM::class.java)
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        fetchGameResults()
    }


    override fun onPause() {
        super.onPause()
        countDown?.cancel()
    }


    protected abstract fun setupViews()


    private fun fetchGameResults() {
        if (isOnScreen && isCreated && allowDialogs) {
            allowDialogs = false
            gamesVM.getNewResults(object: RepositoryCallback<Queue<GameResult>> {
                override fun done(data: Queue<GameResult>?) {
                    data?.let { queue ->
                        proceedGameResult(queue)
                    }
                }
            })
        }
    }


    private fun proceedGameResult(results: Queue<GameResult>) {
        val result = results.poll()
        if (result == null) {
            allowDialogs = true
        }
        else {
            showResultDialog(result, { next ->
                result.deleteAsync()
                if (next) {
                    proceedGameResult(results)
                }
                else {
                    results.forEach { result ->
                        result.show = false
                        result.saveAsync()
                    }
                    allowDialogs = true
                }
            })
        }
    }


    private fun showResultDialog(result: GameResult, resultCallback: (Boolean) -> Unit) {
        context?.let { context ->
            gamesVM.getGameById(result.gameId!!, object : RepositoryCallback<Game?> {
                override fun done(data: Game?) {
                    data?.let {
                        if (it.type == data.type) {
                            var showNext = true
                            when {
                                result.position!! > 0 -> {
                                    val dialog = WinnerDialog(context, result)
                                    dialog.setOnDismissListener {
                                        sendNewsCountBroadcastIntent()
                                        resultCallback(showNext)
                                    }

                                    dialog.setonHistoryListener {
                                        showNext = false
                                        gamesVM.selectedHistoryGame = data
                                        dialog.dismiss()
                                        AppFragmentManager.instance.openFragment(HistoryGameDetailsFragment(), true)
                                    }
                                    dialog.show()
                                }
                                result.position!! == 0 -> {
                                    val dialog = LooserDialog(context, it)
                                    dialog.setOnDismissListener {
                                        sendNewsCountBroadcastIntent()
                                        resultCallback(showNext)
                                    }

                                    gamesVM.getMonthlyGame().observe(this@GameBaseFragment, Observer { bonusGame ->
                                        bonusGame?.let {
                                            dialog.setDays(it.endTime())
                                        }
                                    })
                                    dialog.setonHistoryListener {
                                        showNext = false
                                        gamesVM.selectedHistoryGame = data
                                        dialog.dismiss()
                                        AppFragmentManager.instance.openFragment(HistoryGameDetailsFragment(), true)
                                    }
                                    dialog.show()
                                }
                                else -> {
                                    val dialog = UnknownStatusDialog(context, it)
                                    dialog.setOnDismissListener {
                                        sendNewsCountBroadcastIntent()
                                        resultCallback(showNext)
                                    }

                                    dialog.setOnHistoryListener {
                                        showNext = false
                                        gamesVM.selectedHistoryGame = data
                                        dialog.dismiss()
                                        AppFragmentManager.instance.openFragment(HistoryGameDetailsFragment(), true)
                                    }
                                    dialog.show()
                                }
                            }
                        }
                    }
                }
            })
        }
    }


    private fun sendNewsCountBroadcastIntent() {
        val broadcastIntent = Intent("news")
        broadcaster.sendBroadcast(broadcastIntent)
    }

}