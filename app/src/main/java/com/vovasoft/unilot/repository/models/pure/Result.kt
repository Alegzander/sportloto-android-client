package com.vovasoft.unilot.repository.models.pure

import com.google.gson.annotations.SerializedName
import com.vovasoft.unilot.repository.models.GsonModel

/***************************************************************************
 * Created by arseniy on 29/10/2017.
 ****************************************************************************/
data class Result(@SerializedName("id")
                  var gameId: Long? = null,

                  @SerializedName("winners")
                  var winners: List<String> = emptyList()) : GsonModel()