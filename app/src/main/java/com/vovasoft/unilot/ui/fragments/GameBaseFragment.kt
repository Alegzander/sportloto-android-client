package com.vovasoft.unilot.ui.fragments

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import com.vovasoft.unilot.components.AppFragmentManager
import com.vovasoft.unilot.notifications.NotificationMessagingService
import com.vovasoft.unilot.repository.Reactive
import com.vovasoft.unilot.repository.models.GsonModel
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult
import com.vovasoft.unilot.ui.dialogs.LooserDialog
import com.vovasoft.unilot.ui.dialogs.UnknownStatusDialog
import com.vovasoft.unilot.ui.dialogs.WinnerDialog
import com.vovasoft.unilot.ui.widgets.ZxingReader
import com.vovasoft.unilot.view_models.GamesVM
import java.util.*
import java.util.regex.Pattern

/***************************************************************************
 * Created by arseniy on 29/10/2017.
 ****************************************************************************/
abstract class GameBaseFragment : BaseFragment() {

    protected var hasCameraPermission = false

    protected var onScannerResult: ((String?) -> Unit)? = null

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


    override fun onResume() {
        super.onResume()
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
            gamesVM.getNewResults(object: Reactive<Queue<GameResult>> {
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
            gamesVM.getGameById(result.gameId!!, object : Reactive<Game?> {
                override fun done(data: Game?) {
                    data?.let { resultGame ->
                        showResultDialog(result, resultGame, { next ->
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
            })
        }
    }


    private fun showResultDialog(result: GameResult, resultGame: Game, resultCallback: (Boolean) -> Unit) {
        context?.let { context ->
            if (game?.type == resultGame.type) {
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
                            gamesVM.selectedHistoryGame = resultGame
                            dialog.dismiss()
                            AppFragmentManager.instance.openFragment(HistoryGameDetailsFragment(), true)
                        }
                        dialog.show()
                    }
                    result.position == 0L -> {
                        val dialog = LooserDialog(context, resultGame)
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
                            gamesVM.selectedHistoryGame = resultGame
                            dialog.dismiss()
                            AppFragmentManager.instance.openFragment(HistoryGameDetailsFragment(), true)
                        }
                        dialog.show()
                    }
                    else -> {
                        val dialog = UnknownStatusDialog(context, resultGame)
                        dialog.setOnDismissListener {
                            sendNewsCountBroadcastIntent()
                            resultCallback(showNext)
                        }

                        dialog.setOnHistoryListener {
                            showNext = false
                            gamesVM.selectedHistoryGame = resultGame
                            dialog.dismiss()
                            AppFragmentManager.instance.openFragment(HistoryGameDetailsFragment(), true)
                        }
                        dialog.show()
                    }
                }
            }
        }
    }


    private fun sendNewsCountBroadcastIntent() {
        val broadcastIntent = Intent("news")
        broadcaster.sendBroadcast(broadcastIntent)
    }


    protected fun runQReader() {
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

                    onScannerResult?.invoke(result)
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

}