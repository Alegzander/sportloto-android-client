package com.vovasoft.sportloto.repository.retrofit.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/***************************************************************************
 * Created by arseniy on 27/09/2017.
 ****************************************************************************/
data class TokenRegistrationRequest(@SerializedName("token") val token: String?,
                                    @SerializedName("os") val os: Int = 20)