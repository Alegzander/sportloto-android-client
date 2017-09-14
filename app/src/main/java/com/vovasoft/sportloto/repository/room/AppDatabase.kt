package com.vovasoft.sportloto.repository.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.vovasoft.sportloto.repository.models.Game
import com.vovasoft.sportloto.repository.room.dao.GamesDao

/***************************************************************************
 * Created by arseniy on 14/09/2017.
 ****************************************************************************/
@Database(entities = arrayOf(Game::class),
        version = 1,
        exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gamesDao(): GamesDao
}
