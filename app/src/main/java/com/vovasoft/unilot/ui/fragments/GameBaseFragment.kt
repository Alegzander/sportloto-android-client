package com.vovasoft.unilot.ui.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import com.vovasoft.unilot.notifications.MyFirebaseMessagingService
import com.vovasoft.unilot.repository.RepositoryCallback
import com.vovasoft.unilot.repository.models.GsonModel
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult
import com.vovasoft.unilot.ui.dialogs.LooserDialog
import com.vovasoft.unilot.ui.dialogs.WinnerDialog
import com.vovasoft.unilot.view_models.GamesVM

/***************************************************************************
 * Created by arseniy on 29/10/2017.
 ****************************************************************************/
abstract class GameBaseFragment : BaseFragment() {


    protected val gamesVM: GamesVM
        get() = ViewModelProviders.of(activity).get(GamesVM::class.java)

    protected var game: Game? = null

    protected var countDown: CountDownTimer? = null


    protected val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val action = intent.getSerializableExtra("action") as MyFirebaseMessagingService.Action
                when (action) {
                    MyFirebaseMessagingService.Action.GAME_UPDATED -> {
                        game = GsonModel.fromJson(intent.getStringExtra("game_updated"), Game::class.java)
                        game?.saveAsync()
                        countDown?.cancel()
                        setupViews()
                    }

                    MyFirebaseMessagingService.Action.GAME_FINISHED -> {
                        showResultDialog()
                    }
                }
            }
        }
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
                                            WinnerDialog(context, result).show()
                                        }
                                        else {
                                            LooserDialog(context, it, result).show()
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


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        showResultDialog()
    }


    override fun onPause() {
        super.onPause()
        countDown?.cancel()
    }

}