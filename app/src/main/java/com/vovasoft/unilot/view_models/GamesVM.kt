package com.vovasoft.unilot.view_models

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.vovasoft.unilot.repository.AppRepository
import com.vovasoft.unilot.repository.Reactive
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult
import com.vovasoft.unilot.repository.models.entities.Wallet
import com.vovasoft.unilot.repository.models.pure.Player
import com.vovasoft.unilot.repository.models.pure.Winner
import java.util.*

/***************************************************************************
 * Created by arseniy on 14/09/2017.
 ****************************************************************************/
class GamesVM : ViewModel() {

    private val appRepo = AppRepository()

    private var walletsLiveData = MutableLiveData<List<Wallet>>()

    private var gamesLiveData = MutableLiveData<List<Game>>()

    private var gamesHistoryLiveData = MutableLiveData<List<Game>>()

    private var dailyGameLiveData = MutableLiveData<Game>()

    private var weeklyGameLiveData = MutableLiveData<Game>()

    private var monthlyGameLiveData = MutableLiveData<Game>()

    private var tokenGameLiveData = MutableLiveData<Game>()

    private var isUpdating = false


    var selectedHistoryGame: Game? = null


    fun getWallets() : LiveData<List<Wallet>> {
        appRepo.getWallets(object : Reactive<List<Wallet>?> {
            override fun done(data: List<Wallet>?) {
                walletsLiveData.value = data
            }
        })
        return walletsLiveData
    }


    fun getWallets(callback: Reactive<List<Wallet>?>) {
        appRepo.getWallets(callback)
    }


    fun getGamesHistory() : LiveData<List<Game>> {
        appRepo.getRemoteGamesHistory(object : Reactive<List<Game>?> {
            override fun done(data: List<Game>?) {
                gamesHistoryLiveData.value = data
            }
        })
        return gamesHistoryLiveData
    }


    fun getGamesList() : LiveData<List<Game>> {
        if (gamesLiveData.value == null) {
            updateGamesList()
        }
        return gamesLiveData
    }


    fun getWinners(id: Long) : LiveData<List<Winner>> {
        val winners = MutableLiveData<List<Winner>>()
        appRepo.getRemoteWinners(id, object : Reactive<List<Winner>?> {
            override fun done(data: List<Winner>?) {
                winners.value = data
            }
        })
        return winners
    }


    fun getPlayers(id: Long) : LiveData<List<Player>> {
        val players = MutableLiveData<List<Player>>()
        appRepo.getRemotePlayers(id, object : Reactive<List<Player>?> {
            override fun done(data: List<Player>?) {
                players.value = data
            }
        })
        return players
    }


    fun updateGamesList() {
        if (!isUpdating) {
            isUpdating = true
            appRepo.getRemoteGames(object : Reactive<List<Game>?> {
                override fun done(data: List<Game>?) {

                    gamesLiveData.value = data

                    dailyGameLiveData.value = data?.firstOrNull { game ->
                        (Game.Status.from(game.status) == Game.Status.PUBLISHED
                                || Game.Status.from(game.status) == Game.Status.FINISHING)
                                && Game.Type.from(game.type) == Game.Type.DAILY
                    }

                    weeklyGameLiveData.value = data?.firstOrNull { game ->
                        (Game.Status.from(game.status) == Game.Status.PUBLISHED
                                || Game.Status.from(game.status) == Game.Status.FINISHING)
                                && Game.Type.from(game.type) == Game.Type.WEEKLY
                    }

                    monthlyGameLiveData.value = data?.firstOrNull { game ->
                        (Game.Status.from(game.status) == Game.Status.PUBLISHED
                                || Game.Status.from(game.status) == Game.Status.FINISHING)
                                && Game.Type.from(game.type) == Game.Type.MONTHLY
                    }

                    tokenGameLiveData.value = data?.firstOrNull { game ->
                        (Game.Status.from(game.status) == Game.Status.PUBLISHED
                                || Game.Status.from(game.status) == Game.Status.FINISHING)
                                && Game.Type.from(game.type) == Game.Type.TOKEN
                    }

                    isUpdating = false
                }
            })
        }
    }


    fun getDailyGame() : LiveData<Game> {
        return dailyGameLiveData
    }


    fun getWeeklyGame() : LiveData<Game> {
        return weeklyGameLiveData
    }


    fun getMonthlyGame() : LiveData<Game> {
        return monthlyGameLiveData
    }


    fun getTokenGame() : LiveData<Game> {
        return tokenGameLiveData
    }


    fun getNewResults(callback: Reactive<Queue<GameResult>>) {
        appRepo.getNewResults(object : Reactive<Queue<GameResult>> {
            override fun done(data: Queue<GameResult>?) {
                callback.done(data)
            }
        })
    }


    fun getAllResults(callback: Reactive<List<GameResult>>) {
        appRepo.getAllResults(object : Reactive<List<GameResult>> {
            override fun done(data: List<GameResult>?) {
                callback.done(data)
            }
        })
    }


    fun getGameById(id: Long, callback: Reactive<Game?>) {
        appRepo.getLocalGameById(id, object : Reactive<Game?> {
            override fun done(data: Game?) {
                callback.done(data)
            }
        })
    }

}