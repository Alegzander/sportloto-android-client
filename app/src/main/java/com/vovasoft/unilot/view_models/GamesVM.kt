package com.vovasoft.unilot.view_models

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.vovasoft.unilot.repository.AppRepository
import com.vovasoft.unilot.repository.RepositoryCallback
import com.vovasoft.unilot.repository.models.Game
import com.vovasoft.unilot.repository.models.Wallet
import com.vovasoft.unilot.repository.models.Winner

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
        appRepo.getGamesHistory(object : RepositoryCallback<List<Game>?> {
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

                    dailyGameLiveData.value = data?.firstOrNull { game ->
                        Game.Status.from(game.status) == Game.Status.PUBLISHED && Game.Type.from(game.type) == Game.Type.DAILY
                    }

                    weeklyGameLiveData.value = data?.firstOrNull { game ->
                        Game.Status.from(game.status) == Game.Status.PUBLISHED && Game.Type.from(game.type) == Game.Type.WEEKLY
                    }

                    monthlyGameLiveData.value = data?.firstOrNull { game ->
                        Game.Status.from(game.status) == Game.Status.PUBLISHED && Game.Type.from(game.type) == Game.Type.MONTHLY
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

}