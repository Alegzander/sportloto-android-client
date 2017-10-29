package com.vovasoft.unilot.repository

import com.vovasoft.unilot.App
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult
import com.vovasoft.unilot.repository.models.entities.Wallet
import com.vovasoft.unilot.repository.models.pure.Winner
import com.vovasoft.unilot.repository.retrofit.WebClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/***************************************************************************
 * Created by arseniy on 14/09/2017.
 ****************************************************************************/
class AppRepository {

    private val webClient = WebClient()

    fun getRemoteGames(callback: RepositoryCallback<List<Game>?>) {
        webClient.webservice.games().enqueue(object : Callback<List<Game>> {
            override fun onResponse(call: Call<List<Game>>?, response: Response<List<Game>>?) {
                doAsync {
                    response?.body()?.let { games ->
                        App.database.gamesDao().insertAll(games)
                    }
                }
                callback.done(response?.body())
            }

            override fun onFailure(call: Call<List<Game>>?, t: Throwable?) {
                callback.done(null)
            }
        })
    }


    fun getRemoteWinners(id: Int, callback: RepositoryCallback<List<Winner>?>) {
        webClient.webservice.winners(id).enqueue(object : Callback<List<Winner>> {
            override fun onResponse(call: Call<List<Winner>>?, response: Response<List<Winner>>?) {
                callback.done(response?.body())
            }

            override fun onFailure(call: Call<List<Winner>>?, t: Throwable?) {
                callback.done(null)
            }
        })
    }


    fun getGamesHistory(callback: RepositoryCallback<List<Game>?>) {
        doAsync {
            val games = App.database.gamesDao().getGames()
            games.sortedBy { it.endTime() }
            uiThread {
                callback.done(games)
            }
        }
    }


    fun getGameById(id: Int, callback: RepositoryCallback<Game?>) {
        doAsync {
            val game = App.database.gamesDao().getGameById(id)
            uiThread {
                callback.done(game)
            }
        }
    }


    fun getWallets(callback: RepositoryCallback<List<Wallet>?>) {
        doAsync {
            val wallets = App.database.walletsDao().getWallets()
            uiThread {
                callback.done(wallets)
            }
        }
    }


    fun getWalletsNumbers(callback: RepositoryCallback<List<String>?>) {
        doAsync {
            val numbers = App.database.walletsDao().getWalletsNumbers()
            uiThread {
                callback.done(numbers)
            }
        }
    }


    fun getResults(callback: RepositoryCallback<List<GameResult>>) {
        doAsync {
            val results = App.database.gameResultsDao().getGameResults()
            uiThread {
                callback.done(results)
            }
        }
    }
}