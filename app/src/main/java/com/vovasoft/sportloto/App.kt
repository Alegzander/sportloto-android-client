package com.vovasoft.sportloto

import android.app.Application
import android.arch.persistence.room.Room
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.vovasoft.sportloto.repository.retrofit.WebClient
import com.vovasoft.sportloto.repository.retrofit.requests.TokenRegistrationRequest
import com.vovasoft.sportloto.repository.room.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
class App : Application() {

    companion object {
        lateinit var instance: App
            private set

        lateinit var database: AppDatabase
            private set

        fun updateNotificationToken() {
            val webClient = WebClient()
            val request = TokenRegistrationRequest(FirebaseInstanceId.getInstance().token)
            webClient.webservice.registerToken(request).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>?, response: Response<Any>?) { }
                override fun onFailure(call: Call<Any>?, t: Throwable?) { }
            })
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        App.database =  Room.databaseBuilder(this, AppDatabase::class.java, "local_storage").build()
    }
}
