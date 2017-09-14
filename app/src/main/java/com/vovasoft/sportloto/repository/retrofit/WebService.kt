package com.vovasoft.sportloto.repository.retrofit

import com.vovasoft.sportloto.repository.models.AuthorizationModel
import com.vovasoft.sportloto.repository.models.Game
import com.vovasoft.sportloto.repository.retrofit.requests.AuthorizationRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
interface WebService {
    @POST("o2/token/")
    fun authorize(@Body body: AuthorizationRequest): Call<AuthorizationModel>

    @GET("api/v1/games")
    fun games(): Call<List<Game>>
}