package com.vovasoft.unilot.repository.models.pure

import com.google.gson.annotations.SerializedName
import com.vovasoft.unilot.repository.models.GsonModel

/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
data class Player(@SerializedName("address")
                  var wallet: String? = null) : GsonModel()