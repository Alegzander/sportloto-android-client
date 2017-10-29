package com.vovasoft.unilot.repository.models.pure

import com.google.gson.annotations.SerializedName
import com.vovasoft.unilot.repository.models.GsonModel

/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
data class Winner(@SerializedName("position")
                  var position: Int? = null,

                  @SerializedName("address")
                  var wallet: String? = null,

                  @SerializedName("prize_amount")
                  var prize: Float? = null,

                  @SerializedName("prize_amount_fiat")
                  var prizeFiat: Float? = null) : GsonModel()