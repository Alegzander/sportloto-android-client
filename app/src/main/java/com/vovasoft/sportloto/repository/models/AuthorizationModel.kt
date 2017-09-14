package com.vovasoft.sportloto.repository.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
data class AuthorizationModel(@Expose @SerializedName("access_token") val token: String,
                              @Expose @SerializedName("token_type") val type: String,
                              @Expose @SerializedName("expires_in") val expires: Int,
                              @Expose @SerializedName("scope") val scope: String)