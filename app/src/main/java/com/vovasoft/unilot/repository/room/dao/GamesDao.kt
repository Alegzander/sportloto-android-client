package com.vovasoft.unilot.repository.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.vovasoft.unilot.repository.models.entities.Game

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
@Dao
interface GamesDao {
    @Query("SELECT * FROM games")
    fun getAllGames(): List<Game>

    @Query("SELECT * FROM games WHERE status = 10")
    fun getActiveGames(): List<Game>

    @Query("SELECT * FROM games WHERE id = :arg0 LIMIT 1")
    fun getGameById(id: Int): Game?

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insert(game: Game)

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insertAll(games: List<Game>)

    @Query("DELETE FROM games")
    fun deleteAll()
}