package com.vovasoft.unilot.repository

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.vovasoft.unilot.App
import com.vovasoft.unilot.components.Preferences
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult
import com.vovasoft.unilot.repository.models.entities.Wallet
import com.vovasoft.unilot.repository.models.pure.Participate
import com.vovasoft.unilot.repository.models.pure.Player
import com.vovasoft.unilot.repository.models.pure.Winner
import com.vovasoft.unilot.repository.retrofit.WebClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/***************************************************************************
 * Created by arseniy on 14/09/2017.
 ****************************************************************************/
class AppRepository {

    private val webClient = WebClient()

    private var broadcaster: LocalBroadcastManager = LocalBroadcastManager.getInstance(App.instance)


    private fun showUpdateScreen() {
        val broadcastIntent = Intent("update")
        broadcaster.sendBroadcast(broadcastIntent)
    }


    fun getRemoteGames(callback: Reactive<List<Game>?>) {
        webClient.webservice.games().enqueue(object : Callback<List<Game>> {
            override fun onResponse(call: Call<List<Game>>?, response: Response<List<Game>>?) {
                response?.let {
                    when {
                        response.isSuccessful -> {
                            doAsync {
                                response.body()?.let { games ->
                                    App.database.gamesDao().insertAll(games)
                                }
                            }
                            callback.done(response.body())
                        }
                        response.code() == 417 -> {
                            callback.done(null)
                            showUpdateScreen()
                        }
                        else -> getLocalActiveGames(callback)
                    }
                }
            }

            override fun onFailure(call: Call<List<Game>>?, t: Throwable?) {
                t?.printStackTrace()
                getLocalActiveGames(callback)
            }
        })
    }


    fun getLocalActiveGames(callback: Reactive<List<Game>?>) {
        doAsync {
            val games = App.database.gamesDao().getActiveGames()
            games.sortedBy { it.endTime() }
            uiThread {
                callback.done(games)
            }
        }
    }


    fun getRemoteParticipate(wallets: List<String?>, callback: Reactive<List<Participate>?>) {
        webClient.webservice.participate(wallets).enqueue(object : Callback<List<Participate>> {
            override fun onResponse(call: Call<List<Participate>>?, response: Response<List<Participate>>?) {
                response?.let {
                    when {
                        response.isSuccessful -> {
                            callback.done(response.body())
                            Preferences.instance.participate = response.body() ?: listOf()
                        }
                        response.code() == 417 -> {
                            callback.done(null)
                            showUpdateScreen()
                        }
                        else -> callback.done(Preferences.instance.participate)
                    }
                }
            }

            override fun onFailure(call: Call<List<Participate>>?, t: Throwable?) {
                t?.printStackTrace()
                callback.done(Preferences.instance.participate)
            }
        })
    }


    fun getRemoteWinners(id: Long, callback: Reactive<List<Winner>?>) {
        webClient.webservice.winners(id).enqueue(object : Callback<List<Winner>> {
            override fun onResponse(call: Call<List<Winner>>?, response: Response<List<Winner>>?) {
                if (response?.code() == 417) {
                    callback.done(null)
                    showUpdateScreen()
                }
                else {
                    callback.done(response?.body())
                }
            }

            override fun onFailure(call: Call<List<Winner>>?, t: Throwable?) {
                callback.done(null)
            }
        })
    }


    fun getRemotePlayers(id: Long, callback: Reactive<List<Player>?>) {
        webClient.webservice.players(id).enqueue(object : Callback<List<Player>> {
            override fun onResponse(call: Call<List<Player>>?, response: Response<List<Player>>?) {
                if (response?.code() == 417) {
                    callback.done(null)
                    showUpdateScreen()
                }
                else {
                    callback.done(response?.body())
                }
            }

            override fun onFailure(call: Call<List<Player>>?, t: Throwable?) {
                callback.done(null)
            }
        })
    }


    fun getRemoteGamesHistory(callback: Reactive<List<Game>?>) {
        webClient.webservice.gamesArchived().enqueue(object : Callback<List<Game>> {
            override fun onResponse(call: Call<List<Game>>?, response: Response<List<Game>>?) {
                if (response?.code() == 417) {
                    callback.done(null)
                    showUpdateScreen()
                }
                else {
                    doAsync {
                        response?.body()?.let { games ->
                            App.database.gamesDao().insertAll(games)
                        }
                    }
                    callback.done(response?.body())
                }
            }

            override fun onFailure(call: Call<List<Game>>?, t: Throwable?) {
                callback.done(null)
            }
        })
    }


    fun getRemoteGameById(id: Long, callback: Reactive<Game?>) {
        webClient.webservice.gameById(id).enqueue(object : Callback<Game> {
            override fun onResponse(call: Call<Game>?, response: Response<Game>?) {
                if (response?.code() == 417) {
                    callback.done(null)
                    showUpdateScreen()
                }
                else {
                    callback.done(response?.body())
                    response?.body()?.saveAsync()
                }
            }

            override fun onFailure(call: Call<Game>?, t: Throwable?) {
                callback.done(null)
            }
        })
    }


    fun getLocalGameById(id: Long, callback: Reactive<Game?>) {
        doAsync {
            val game = App.database.gamesDao().getGameById(id)
            uiThread {
                callback.done(game)
            }
        }
    }


    fun getWallets(callback: Reactive<List<Wallet>>) {
        doAsync {
            val wallets = App.database.walletsDao().getWallets()
            uiThread {
                callback.done(wallets)
            }
        }
    }


    fun getWalletsNumbers(callback: Reactive<List<String>?>) {
        doAsync {
            val numbers = App.database.walletsDao().getWalletsNumbers()
            uiThread {
                callback.done(numbers)
            }
        }
    }


    fun getNewResults(callback: Reactive<Queue<GameResult>>) {
        doAsync {
            val results = App.database.gameResultsDao().getGameResults(true)
            uiThread {
                callback.done(LinkedList<GameResult>(results))
            }
        }
    }


    fun getAllResults(callback: Reactive<List<GameResult>>) {
        doAsync {
            val results = App.database.gameResultsDao().getAllGameResults()
            uiThread {
                callback.done(results)
            }
        }
    }
}