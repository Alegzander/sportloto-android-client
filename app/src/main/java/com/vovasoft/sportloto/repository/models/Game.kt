package com.vovasoft.sportloto.repository.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vovasoft.sportloto.App
import com.vovasoft.sportloto.repository.RepositoryCallback
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
@Entity(tableName = "games")
data class Game(@Expose @SerializedName("id")
                @PrimaryKey @ColumnInfo(name = "id")
                var id: Int? = null,

                @Expose @SerializedName("status")
                @ColumnInfo(name = "status")
                var status: Int? = null,

                @Expose @SerializedName("type")
                @ColumnInfo(name = "type")
                var type: Int? = null,

                @Expose @SerializedName("prize_amount")
                @ColumnInfo(name = "prize_amount")
                var prizeAmount: Float? = null,

                @Expose @SerializedName("prize_amount_fiat")
                @ColumnInfo(name = "prize_amount_fiat")
                var prizeAmountFiat: Float? = null,

                @Expose @SerializedName("num_players")
                @ColumnInfo(name = "num_players")
                var playersNum: Int? = null,

                @Expose @SerializedName("smart_contract_id")
                @ColumnInfo(name = "smart_contract_id")
                var smartContractId: String? = null,

                @Expose @SerializedName("started_at")
                @ColumnInfo(name = "started_at")
                var startedAt: String? = null,

                @Expose @SerializedName("ending_at")
                @ColumnInfo(name = "ending_at")
                var endingAt: String? = null
) {

    enum class Type {
        DAILY,
        WEEKLY,
        MONTHLY,
        UNKNOWN;

        companion object {
            fun from(findValue: Int?) : Type {
                return when(findValue) {
                    10 -> return DAILY
                    30 -> return WEEKLY
                    50 -> return MONTHLY
                    else -> UNKNOWN
                }
            }
        }

        val value: Int
            get() {
                return when(this) {
                    DAILY -> 10
                    WEEKLY -> 30
                    MONTHLY -> 50
                    else -> -1
                }
            }
    }


    enum class Status {
        PUBLISHED,
        CANCELLED,
        FINISHED,
        UNKNOWN;

        companion object {
            fun from(findValue: Int?) : Status {
                return when(findValue) {
                    10 -> return PUBLISHED
                    20 -> return CANCELLED
                    30 -> return FINISHED
                    else -> UNKNOWN
                }
            }
        }

        val value: Int
            get() {
                return when(this) {
                    PUBLISHED -> 10
                    CANCELLED -> 20
                    FINISHED -> 30
                    else -> -1
                }
            }
    }


    fun startTime() : Long {
        startedAt?.let {
            val calendar = Calendar.getInstance().clone() as Calendar
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            calendar.time = formatter.parse(it)
            return calendar.timeInMillis
        }
        return Calendar.getInstance().timeInMillis
    }


    fun endTime() : Long {
        endingAt?.let {
            val calendar = Calendar.getInstance().clone() as Calendar
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            calendar.time = formatter.parse(it)
            return calendar.timeInMillis
        }
        return Calendar.getInstance().timeInMillis
    }


    fun save() {
        App.database.gamesDao().insert(this@Game)
    }


    fun saveAsync(callback: RepositoryCallback<Unit>) {
        doAsync {
            App.database.gamesDao().insert(this@Game)
            uiThread {
                callback.done()
            }
        }
    }

}