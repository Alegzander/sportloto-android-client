package com.vovasoft.unilot.repository.room.dao

import android.arch.persistence.room.*
import com.vovasoft.unilot.repository.models.Game
import com.vovasoft.unilot.repository.models.Wallet


/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
@Dao
interface WalletsDao {
    @Query("SELECT * FROM wallets")
    fun getWallets(): List<Wallet>

    @Query("SELECT * FROM wallets WHERE number = :arg0 LIMIT 1")
    fun getGameByNumber(number: String): Game

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insert(wallet: Wallet)

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insertAll(wallets: List<Wallet>)

    @Delete
    fun delete(wallet: Wallet)

    @Query("DELETE FROM wallets")
    fun deleteAll()
}