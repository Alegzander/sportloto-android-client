package com.vovasoft.unilot.repository.models.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.vovasoft.unilot.App
import com.vovasoft.unilot.repository.models.GsonModel
import org.jetbrains.anko.doAsync

/***************************************************************************
 * Created by arseniy on 28/10/2017.
 ****************************************************************************/
@Entity(tableName = "game_results")
data class GameResult(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
                      var id: Int? = null,

                      @SerializedName("id")
                      @ColumnInfo(name = "game_id")
                      var gameId: Long? = null,

                      @SerializedName("position")
                      @ColumnInfo(name = "position")
                      var position: Long? = null,

                      @SerializedName("wallet")
                      @ColumnInfo(name = "wallet")
                      var address: String? = null,

                      @SerializedName("prize_amount")
                      @Embedded(prefix = "prize_")
                      var prize: Game.Prize? = null,

                      @SerializedName("prize_amount_fiat")
                      @ColumnInfo(name = "prize_amount_fiat")
                      var prizeFiat: Float? = null,

                      @SerializedName("show")
                      @ColumnInfo(name = "show")
                      var show: Boolean? = true
) : GsonModel() {


    fun save() {
        App.database.gameResultsDao().insert(this@GameResult)
    }


    fun saveAsync() {
        doAsync {
            App.database.gameResultsDao().insert(this@GameResult)
        }
    }


    fun delete() {
        App.database.gameResultsDao().delete(this@GameResult)
    }


    fun deleteAsync() {
        doAsync {
            App.database.gameResultsDao().delete(this@GameResult)
        }
    }

}