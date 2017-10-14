package com.vovasoft.sportloto.repository.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
data class TopPlace(@SerializedName("id")
                    @PrimaryKey @ColumnInfo(name = "id")
                    var id: Int? = null,

                    @SerializedName("place")
                    @ColumnInfo(name = "place")
                    var place: String? = null,

                    @SerializedName("prize")
                    @ColumnInfo(name = "prize")
                    var prize: Float? = null,

                    @SerializedName("prize_fiat")
                    @ColumnInfo(name = "prize_fiat")
                    var prizeFiat: Float? = null)