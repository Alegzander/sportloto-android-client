package com.vovasoft.unilot.repository.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult
import com.vovasoft.unilot.repository.models.entities.Wallet
import com.vovasoft.unilot.repository.room.dao.GameResultsDao
import com.vovasoft.unilot.repository.room.dao.GamesDao
import com.vovasoft.unilot.repository.room.dao.WalletsDao

/***************************************************************************
 * Created by arseniy on 14/09/2017.
 ****************************************************************************/
@Database(entities = arrayOf(Game::class, Wallet::class, GameResult::class),
        version = 2,
        exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gamesDao(): GamesDao
    abstract fun walletsDao(): WalletsDao
    abstract fun gameResultsDao(): GameResultsDao
}
