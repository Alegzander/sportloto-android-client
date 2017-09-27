package com.vovasoft.sportloto.view_models

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.vovasoft.sportloto.repository.AppRepository
import com.vovasoft.sportloto.repository.RepositoryCallback
import com.vovasoft.sportloto.repository.models.Game

/***************************************************************************
 * Created by arseniy on 14/09/2017.
 ****************************************************************************/
class GamesVM : ViewModel() {

    private val appRepo = AppRepository()

    private var gamesLiveData = MutableLiveData<List<Game>>()

    private var dailyGameLiveData = MutableLiveData<Game>()

    private var weeklyGameLiveData = MutableLiveData<Game>()

    private var monthlyGameLiveData = MutableLiveData<Game>()

    private var isUpdating = false


    fun getGamesList() : LiveData<List<Game>> {
        if (gamesLiveData.value == null) {
            updateGamesList()
        }
        return gamesLiveData
    }


    fun updateGamesList() {
        if (!isUpdating) {
            isUpdating = true
            appRepo.getRemoteGames(object : RepositoryCallback<List<Game>?> {
                override fun dataUpdated(data: List<Game>?) {

                    try {
                        dailyGameLiveData.value = data?.first { game ->
                            Game.Status.from(game.status) == Game.Status.PUBLISHED && Game.Type.from(game.type) == Game.Type.DAILY
                        }
                    }
                    catch (e: NoSuchElementException) {
                        e.printStackTrace()
                    }

                    try {
                        weeklyGameLiveData.value = data?.first { game ->
                            Game.Status.from(game.status) == Game.Status.PUBLISHED && Game.Type.from(game.type) == Game.Type.WEEKLY
                        }
                    }
                    catch (e: NoSuchElementException) {
                        e.printStackTrace()
                    }

                    try {
                        monthlyGameLiveData.value = data?.first { game ->
                            Game.Status.from(game.status) == Game.Status.PUBLISHED && Game.Type.from(game.type) == Game.Type.MONTHLY
                        }
                    }
                    catch (e: NoSuchElementException) {
                        e.printStackTrace()
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