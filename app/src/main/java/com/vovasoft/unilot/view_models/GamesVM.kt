package com.vovasoft.unilot.view_models

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.vovasoft.unilot.repository.AppRepository
import com.vovasoft.unilot.repository.RepositoryCallback
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult
import com.vovasoft.unilot.repository.models.entities.Wallet
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

    private var isUpdating = false


    var selectedHistoryGame: Game? = null


    fun getWallets() : LiveData<List<Wallet>> {
        appRepo.getWallets(object : RepositoryCallback<List<Wallet>?> {
            override fun done(data: List<Wallet>?) {
                walletsLiveData.value = data
            }
        })
        return walletsLiveData
    }


    fun getGamesHistory() : LiveData<List<Game>> {
        appRepo.getRemoteGamesHistory(object : RepositoryCallback<List<Game>?> {
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


    fun getWinners(id: Int) : LiveData<List<Winner>> {
        val winners = MutableLiveData<List<Winner>>()
        appRepo.getRemoteWinners(id, object : RepositoryCallback<List<Winner>?> {
            override fun done(data: List<Winner>?) {
                winners.value = data
            }
        })
        return winners
    }


    fun updateGamesList() {
        if (!isUpdating) {
            isUpdating = true
            appRepo.getRemoteGames(object : RepositoryCallback<List<Game>?> {
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


    fun getNewResults(callback: RepositoryCallback<Queue<GameResult>>) {
        appRepo.getNewResults(object : RepositoryCallback<Queue<GameResult>> {
            override fun done(data: Queue<GameResult>?) {
                callback.done(data)
            }
        })
    }


    fun getAllResults(callback: RepositoryCallback<List<GameResult>>) {
        appRepo.getAllResults(object : RepositoryCallback<List<GameResult>> {
            override fun done(data: List<GameResult>?) {
                callback.done(data)
            }
        })
    }


    fun getGameById(id: Int, callback: RepositoryCallback<Game?>) {
        appRepo.getGameById(id, object : RepositoryCallback<Game?> {
            override fun done(data: Game?) {
                callback.done(data)
            }
        })
    }

}