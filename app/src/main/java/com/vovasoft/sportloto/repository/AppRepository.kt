package com.vovasoft.sportloto.repository

import com.vovasoft.sportloto.App
import com.vovasoft.sportloto.repository.models.Game
import com.vovasoft.sportloto.repository.retrofit.WebClient
import org.jetbrains.anko.doAsync
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
}