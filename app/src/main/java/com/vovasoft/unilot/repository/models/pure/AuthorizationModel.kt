package com.vovasoft.unilot.repository.models.pure

import com.google.gson.annotations.SerializedName
import com.vovasoft.unilot.repository.models.GsonModel

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
data class AuthorizationModel(@SerializedName("access_token") val token: String,
                              @SerializedName("token_type") val type: String,
                              @SerializedName("expires_in") val expires: Int,
                              @SerializedName("scope") val scope: String) : GsonModel()