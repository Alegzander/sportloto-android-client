package com.vovasoft.unilot.repository.retrofit

import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.pure.AuthorizationModel
import com.vovasoft.unilot.repository.models.pure.Player
import com.vovasoft.unilot.repository.models.pure.Winner
import com.vovasoft.unilot.repository.retrofit.requests.AuthorizationRequest
import com.vovasoft.unilot.repository.retrofit.requests.TokenRegistrationRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
interface WebService {
    @POST("o2/token/")
    fun authorize(@Body body: AuthorizationRequest): Call<AuthorizationModel>

    @POST("api/v1/device/")
    fun registerToken(@Body body: TokenRegistrationRequest): Call<Any>

    @GET("api/v1/games")
    fun games(): Call<List<Game>>

    @GET("api/v1/games/archived")
    fun gamesArchived(): Call<List<Game>>

    @GET("api/v1/games/{id}")
    fun gameById(@Path("id") id: Long): Call<Game>

    @GET("api/v1/games/{id}/winners")
    fun winners(@Path("id") id: Long): Call<List<Winner>>

    @GET("api/v1/games/{id}/players")
    fun players(@Path("id") id: Long): Call<List<Player>>
}