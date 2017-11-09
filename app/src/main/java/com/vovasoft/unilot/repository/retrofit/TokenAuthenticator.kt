package com.vovasoft.unilot.repository.retrofit

import com.vovasoft.unilot.App
import com.vovasoft.unilot.BuildConfig
import com.vovasoft.unilot.R
import com.vovasoft.unilot.components.Preferences
import com.vovasoft.unilot.repository.retrofit.requests.AuthorizationRequest
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
class TokenAuthenticator : Authenticator {
    /**
     * Returns a request that includes a credential to satisfy an authentication challenge in `response`. Returns null if the challenge cannot be satisfied.
     */
    override fun authenticate(route: Route?, response: Response): Request? {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_API)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val webservice = retrofit.create(WebService::class.java)
        val authorizationModel = webservice.authorize(AuthorizationRequest(
                BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRET,
                App.instance.getString(R.string.grant_type))).execute().body()

        authorizationModel?.let {
            Preferences.instance.token = it.token
            return response.request().newBuilder().header("Authorization", "Bearer ${Preferences.instance.token}").build()
        }

        return null
    }
}