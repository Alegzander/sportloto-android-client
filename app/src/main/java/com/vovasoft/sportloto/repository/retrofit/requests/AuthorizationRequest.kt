package com.vovasoft.sportloto.repository.retrofit.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
data class AuthorizationRequest(@Expose @SerializedName("client_id") val clientId: String?,
                                @Expose @SerializedName("client_secret") val clientSecret: String?,
                                @Expose @SerializedName("grant_type") val grantType: String?)