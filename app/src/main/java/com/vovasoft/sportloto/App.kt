package com.vovasoft.sportloto

import android.app.Application
import android.arch.persistence.room.Room
import android.util.Log
import com.vovasoft.sportloto.repository.room.AppDatabase

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
class App : Application() {

    companion object {
        var instance: App? = null
        var database: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        App.database =  Room.databaseBuilder(this, AppDatabase::class.java, "local_storage").build()
        Log.e("App", "onCreate")
    }
}
