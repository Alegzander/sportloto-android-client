package com.vovasoft.unilot.repository.room.dao

import android.arch.persistence.room.*
import com.vovasoft.unilot.repository.models.entities.GameResult
import java.util.*

/***************************************************************************
 * Created by arseniy on 28/10/2017.
 ****************************************************************************/
@Dao
interface GameResultsDao {
    @Query("SELECT * FROM game_results WHERE show = :arg0")
    fun getGameResults(show: Boolean = true): List<GameResult>

    @Query("SELECT * FROM game_results WHERE game_id = :arg0 LIMIT 1")
    fun getGameResultByGameId(id: Int): GameResult

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insert(wallet: GameResult)

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insertAll(wallets: List<GameResult>)

    @Delete
    fun delete(wallet: GameResult)

    @Query("DELETE FROM game_results")
    fun deleteAll()
}