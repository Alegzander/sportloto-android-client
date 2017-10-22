package com.vovasoft.unilot.repository.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.vovasoft.unilot.repository.models.Game
import com.vovasoft.unilot.repository.models.Wallet
import com.vovasoft.unilot.repository.room.dao.GamesDao
import com.vovasoft.unilot.repository.room.dao.WalletsDao

/***************************************************************************
 * Created by arseniy on 14/09/2017.
 ****************************************************************************/
@Database(entities = arrayOf(Game::class, Wallet::class),
        version = 1,
        exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gamesDao(): GamesDao
    abstract fun walletsDao(): WalletsDao
}
