package com.vovasoft.unilot.repository.retrofit.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
data class AuthorizationRequest(@SerializedName("client_id") val clientId: String?,
                                @SerializedName("client_secret") val clientSecret: String?,
                                @SerializedName("grant_type") val grantType: String?)