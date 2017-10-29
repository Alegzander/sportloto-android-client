package com.vovasoft.unilot.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vovasoft.unilot.R
import android.support.v4.content.LocalBroadcastManager
import android.content.Intent
import com.vovasoft.unilot.repository.AppRepository
import com.vovasoft.unilot.repository.RepositoryCallback
import com.vovasoft.unilot.repository.models.GsonModel
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult
import com.vovasoft.unilot.repository.models.pure.Result
import com.vovasoft.unilot.repository.models.pure.Winner
import java.io.Serializable


/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
class MyFirebaseMessagingService : FirebaseMessagingService() {

    enum class Action(val value: String) : Serializable {
        GAME_STARTED("game_started"),
        GAME_UPDATED("game_updated"),
        GAME_UNPUBLISHED("game_unpublished"),
        GAME_FINISHED("game_finished"),
        UNKNOWN("unknown");

        companion object {
            fun from(findValue: String) : Action {
                return when (findValue) {
                    "game_started" -> GAME_STARTED
                    "game_updated" -> GAME_UPDATED
                    "game_unpublished" -> GAME_UNPUBLISHED
                    "game_finished" -> GAME_FINISHED
                    else -> UNKNOWN
                }
            }
        }
    }

    private lateinit var broadcaster: LocalBroadcastManager
    private lateinit var appRepo: AppRepository

    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
        appRepo = AppRepository()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        remoteMessage?.also {
            if (remoteMessage.data.isNotEmpty()) {
                Log.e("Notification", remoteMessage.data.toString())
                val action = remoteMessage.data["action"]?.let { Action.from(it) }
                when (action) {
                    Action.GAME_UPDATED -> gameUpdatedAction(remoteMessage.data["data"])
                    Action.GAME_FINISHED -> gameFinishedAction(remoteMessage.data["data"])
                }
            }
        }
    }


    private fun gameUpdatedAction(jsonData: String?) {
        jsonData?.let {
            val game = GsonModel.fromJson(jsonData, Game::class.java)
            val intent: Intent? = when (game.type) {
                Game.Type.DAILY.value -> Intent("daily")
                Game.Type.WEEKLY.value -> Intent("weekly")
                Game.Type.MONTHLY.value -> Intent("monthly")
                else -> null
            }

            intent?.let {
                intent.putExtra("action", Action.GAME_UPDATED)
                intent.putExtra("game_updated", jsonData)
                broadcaster.sendBroadcast(intent)
            }
        }
    }


    private fun gameFinishedAction(jsonData: String?) {
        jsonData?.let {
            val result = GsonModel.fromJson(jsonData, Result::class.java)
            result.gameId?.let { gameId ->

                appRepo.getGameById(gameId, object : RepositoryCallback<Game?> {
                    override fun done(game: Game?) {
                        game?.let {
                            appRepo.getWalletsNumbers(object : RepositoryCallback<List<String>?> {
                                override fun done(wallets: List<String>?) {
                                    var isWinner = false
                                    wallets?.forEach {
                                        if (result.winners.contains(it)) {
                                            isWinner = true
                                        }
                                    }

                                    if (isWinner) {
                                        appRepo.getRemoteWinners(gameId, object : RepositoryCallback<List<Winner>?> {
                                            override fun done(data: List<Winner>?) {
                                                data?.let { winners ->
                                                    wallets?.forEach { wallet ->
                                                        winners.forEach { winner ->
                                                            if (wallet == winner.wallet) {
                                                                val gameResult = GameResult()
                                                                gameResult.gameId = gameId
                                                                gameResult.address = winner.wallet
                                                                gameResult.position = winner.position
                                                                gameResult.prize = winner.prize
                                                                gameResult.prizeFiat = winner.prizeFiat
                                                                gameResult.saveAsync()
                                                            }
                                                        }
                                                    }

                                                    val intent: Intent? = when (game.type) {
                                                        Game.Type.DAILY.value -> Intent("daily")
                                                        Game.Type.WEEKLY.value -> Intent("weekly")
                                                        Game.Type.MONTHLY.value -> Intent("monthly")
                                                        else -> null
                                                    }

                                                    intent?.let {
                                                        intent.putExtra("action", Action.GAME_FINISHED)
                                                        intent.putExtra("winner", true)
                                                        broadcaster.sendBroadcast(intent)
                                                    }
                                                }
                                            }
                                        })
                                    }
                                    else {
                                        val gameResult = GameResult()
                                        gameResult.gameId = gameId
                                        gameResult.address = null
                                        gameResult.position = -1
                                        gameResult.saveAsync()

                                        val intent: Intent? = when (game.type) {
                                            Game.Type.DAILY.value -> Intent("daily")
                                            Game.Type.WEEKLY.value -> Intent("weekly")
                                            Game.Type.MONTHLY.value -> Intent("monthly")
                                            else -> null
                                        }

                                        intent?.let {
                                            intent.putExtra("action", Action.GAME_FINISHED)
                                            intent.putExtra("winner", true)
                                            broadcaster.sendBroadcast(intent)
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


    private fun notificationBuilder(messageBody: String): NotificationCompat.Builder {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "${getString(R.string.app_name)}_chanel_id"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "${getString(R.string.app_name)}_chanel_name"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        return NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setChannelId(channelId)
                .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)
    }

}