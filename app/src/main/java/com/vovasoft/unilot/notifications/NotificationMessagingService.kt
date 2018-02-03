package com.vovasoft.unilot.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.vovasoft.unilot.App
import com.vovasoft.unilot.R
import com.vovasoft.unilot.components.Preferences
import com.vovasoft.unilot.repository.AppRepository
import com.vovasoft.unilot.repository.Reactive
import com.vovasoft.unilot.repository.models.GsonModel
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult
import com.vovasoft.unilot.repository.models.pure.Result
import com.vovasoft.unilot.repository.models.pure.Winner
import com.vovasoft.unilot.ui.activities.MainActivity
import java.io.Serializable
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap


/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
class NotificationMessagingService : FirebaseMessagingService() {

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
        try {
            remoteMessage?.also {
                if (remoteMessage.data.isNotEmpty()) {
                    Log.e("Notification", remoteMessage.data.toString())
                    val action = remoteMessage.data["action"]?.let { Action.from(it) }
                    when (action) {
                        Action.GAME_STARTED -> gameStartedAction(remoteMessage.data)
                        Action.GAME_UPDATED -> gameUpdatedAction(remoteMessage.data)
                        Action.GAME_FINISHED -> gameFinishedAction(remoteMessage.data)
                        Action.GAME_UNPUBLISHED -> gameUnpublishedAction(remoteMessage.data)
                    }
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun gameStartedAction(data: Map<String, String>) {
        val jsonData = data["data"]
        val messageJson = data["message"]

        jsonData?.let {
            val game = GsonModel.fromJson(jsonData, Game::class.java)
            sendBroadcastIntent(game.type, Action.GAME_UPDATED, jsonData)
            showNotification(messageJson, game.type, true)
        }
    }


    private fun gameUpdatedAction(data: Map<String, String>) {
        val jsonData = data["data"]
        val messageJson = data["message"]

        jsonData?.let {
            val game = GsonModel.fromJson(jsonData, Game::class.java)
            sendBroadcastIntent(game.type, Action.GAME_UPDATED, jsonData)
            showNotification(messageJson, game.type, true)
        }
    }


    private fun gameUnpublishedAction(data: Map<String, String>) {
        val jsonData = data["data"]
        val messageJson = data["message"]

        jsonData?.let {
            val game = GsonModel.fromJson(jsonData, Game::class.java)
            sendBroadcastIntent(game.type, Action.GAME_UPDATED, jsonData)
            showNotification(messageJson, game.type, true)
        }
    }


    private fun gameFinishedAction(data: Map<String, String>) {
        val jsonData = data["data"]
        val messageJson = data["message"]

        jsonData?.let {
            val result = GsonModel.fromJson(jsonData, Result::class.java)
            result.gameId?.let { gameId ->

                appRepo.getRemoteGameById(gameId, object : Reactive<Game?> {
                    override fun done(game: Game?) {
                        game?.let {
                            game.saveAsync()
                            appRepo.getWalletsNumbers(object : Reactive<List<String>?> {
                                override fun done(wallets: List<String>?) {
                                    if (wallets.orEmpty().isNotEmpty()) {
                                        var isWinner = false
                                        wallets?.forEach {
                                            if (result.winners.contains(it.toLowerCase())) {
                                                isWinner = true
                                            }
                                        }

                                        if (isWinner) {
                                            appRepo.getRemoteWinners(gameId, object : Reactive<List<Winner>?> {
                                                override fun done(winnersData: List<Winner>?) {
                                                    winnersData?.let { winners ->
                                                        wallets?.forEach { wallet ->
                                                            winners.forEach { winner ->
                                                                if (wallet.toLowerCase() == winner.wallet?.toLowerCase()) {
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

                                                        sendBroadcastIntent(game.type, Action.GAME_FINISHED, null)
                                                        sendNewsCountBroadcastIntent()
                                                        showNotification(messageJson, game.type, true)
                                                    }
                                                }
                                            })
                                        }
                                        else {
                                            val gameResult = GameResult()
                                            gameResult.gameId = gameId
                                            gameResult.position = 0
                                            gameResult.saveAsync()

                                            sendBroadcastIntent(game.type, Action.GAME_FINISHED, null)
                                            sendNewsCountBroadcastIntent()
                                            showNotification(messageJson, game.type, true)
                                        }

                                    }
                                    else {
                                        val gameResult = GameResult()
                                        gameResult.gameId = gameId
                                        gameResult.position = -1
                                        gameResult.saveAsync()

                                        sendBroadcastIntent(game.type, Action.GAME_FINISHED, null)
                                        sendNewsCountBroadcastIntent()
                                        showNotification(messageJson, game.type, true)
                                    }
                                }
                            })
                        }
                    }
                })
            }
        }
    }


    private fun sendBroadcastIntent(type: Int?, action: Action, data: Serializable?) {
        val broadcastIntent: Intent? = when (type) {
            Game.Type.DAILY.value -> Intent("daily")
            Game.Type.WEEKLY.value -> Intent("weekly")
            Game.Type.MONTHLY.value -> Intent("monthly")
            Game.Type.TOKEN.value -> Intent("token")
            else -> null
        }

        broadcastIntent?.let {
            broadcastIntent.putExtra("action", action)
            data?.let {
                broadcastIntent.putExtra("data", it)
            }
            broadcaster.sendBroadcast(broadcastIntent)
        }
    }


    private fun sendNewsCountBroadcastIntent() {
        val broadcastIntent = Intent("news")
        broadcaster.sendBroadcast(broadcastIntent)
    }


    private fun showNotification(messageBody: String?, type: Int? = null, isJson: Boolean = false, extraData: HashMap<String, Any> = HashMap()) {

        if (!App.isBackground) {
            return
        }

        val contentText = (if (isJson) {
            messageBody?.let {
                if (type == Game.Type.DAILY.value && Preferences.instance.dayLotteryNotify) {
                    Gson().fromJson(messageBody, JsonObject::class.java)?.get(Preferences.instance.language)?.asString
                }
                else if (type == Game.Type.WEEKLY.value && Preferences.instance.weekLotteryNotify) {
                    Gson().fromJson(messageBody, JsonObject::class.java)?.get(Preferences.instance.language)?.asString
                }
                else if (type == Game.Type.MONTHLY.value && Preferences.instance.monthLotteryNotify) {
                    Gson().fromJson(messageBody, JsonObject::class.java)?.get(Preferences.instance.language)?.asString
                }
                else if (type == Game.Type.TOKEN.value && Preferences.instance.tokenLotteryNotify) {
                    Gson().fromJson(messageBody, JsonObject::class.java)?.get(Preferences.instance.language)?.asString
                }
                else {
                    null
                }
            }
        }
        else {
            messageBody
        }) ?: return

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

        val data = HashMap<String, Any>(extraData)
        type?.let {
            data.put("type", it)
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("data", data)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(contentText)
                .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
                .setAutoCancel(true)
                .setChannelId(channelId)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)


        notificationManager.notify(Random().nextInt(), notificationBuilder.build())
    }

}