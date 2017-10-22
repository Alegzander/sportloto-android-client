package com.vovasoft.unilot.repository.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
data class Winner(@SerializedName("id")
                  @PrimaryKey @ColumnInfo(name = "id")
                  var id: Int? = null,

                  @SerializedName("position")
                  @ColumnInfo(name = "position")
                  var position: Int? = null,

                  @SerializedName("address")
                  @ColumnInfo(name = "address")
                  var address: String? = null,

                  @SerializedName("prize_amount")
                  @ColumnInfo(name = "prize_amount")
                  var prize: Float? = null,

                  @SerializedName("prize_amount_fiat")
                  @ColumnInfo(name = "prize_amount_fiat")
                  var prizeFiat: Float? = null)