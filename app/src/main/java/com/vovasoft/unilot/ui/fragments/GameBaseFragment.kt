package com.vovasoft.unilot.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.vovasoft.unilot.notifications.NotificationMessagingService
import com.vovasoft.unilot.repository.RepositoryCallback
import com.vovasoft.unilot.repository.models.GsonModel
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult
import com.vovasoft.unilot.ui.AppFragmentManager
import com.vovasoft.unilot.ui.dialogs.LooserDialog
import com.vovasoft.unilot.ui.dialogs.WinnerDialog
import com.vovasoft.unilot.view_models.GamesVM

/***************************************************************************
 * Created by arseniy on 29/10/2017.
 ****************************************************************************/
abstract class GameBaseFragment : BaseFragment() {


    protected lateinit var gamesVM: GamesVM

    protected var game: Game? = null

    protected var countDown: CountDownTimer? = null


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
                        showResultDialog()
                    }
                }
            }
        }
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gamesVM = ViewModelProviders.of(activity).get(GamesVM::class.java)
    }


    override fun onResume() {
        super.onResume()
        showResultDialog()
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        showResultDialog()
    }


    override fun onPause() {
        super.onPause()
        countDown?.cancel()
    }

    protected abstract fun setupViews()


    protected fun showResultDialog() {
        if (isOnScreen && isCreated) {
            gamesVM.getResults(object: RepositoryCallback<List<GameResult>> {
                override fun done(results: List<GameResult>?) {
                    results?.forEach { result ->
                        gamesVM.getGameById(result.gameId!!, object : RepositoryCallback<Game?> {
                            override fun done(data: Game?) {
                                data?.let {
                                    if (it.type == game?.type) {
                                        if (result.position!! > 0) {
                                            val dialog = WinnerDialog(context, result)
                                            dialog.setonHistoryListener {
                                                gamesVM.selectedHistoryGame = data
                                                AppFragmentManager.instance.openFragment(HistoryGameDetailsFragment(), true)
                                                dialog.dismiss()
                                            }
                                            dialog.show()
                                        }
                                        else {
                                            val dialog = LooserDialog(context, it, result)
                                            gamesVM.getMonthlyGame().observe(this@GameBaseFragment, Observer { bonusGame ->
                                                bonusGame?.let {
                                                    dialog.setDays(it.endTime())
                                                }
                                            })
                                            dialog.setonHistoryListener {
                                                gamesVM.selectedHistoryGame = data
                                                AppFragmentManager.instance.openFragment(HistoryGameDetailsFragment(), true)
                                                dialog.dismiss()
                                            }
                                            dialog.show()
                                        }
                                    }
                                }
                            }
                        })
                    }
                }
            })
        }
    }

}