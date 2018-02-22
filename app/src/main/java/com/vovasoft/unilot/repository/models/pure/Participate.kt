package com.vovasoft.unilot.repository.models.pure

import com.google.gson.annotations.SerializedName
import com.vovasoft.unilot.repository.models.GsonModel

/***************************************************************************
 * Created by arseniy on 13/02/2018.
 ****************************************************************************/
data class Participate(@SerializedName("games")
                       var gamesIds: Set<Long> = mutableSetOf(),

                       @SerializedName("wallet")
                       var wallet: String? = null,

                       var types: MutableSet<Int> = mutableSetOf()) : GsonModel()