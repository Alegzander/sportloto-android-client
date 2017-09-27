package com.vovasoft.sportloto.repository.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
)