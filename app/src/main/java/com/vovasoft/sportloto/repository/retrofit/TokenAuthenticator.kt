package com.vovasoft.sportloto.repository.retrofit

import com.vovasoft.sportloto.App
import com.vovasoft.sportloto.BuildConfig
import com.vovasoft.sportloto.Preferences
import com.vovasoft.sportloto.R
import com.vovasoft.sportloto.repository.retrofit.requests.AuthorizationRequest
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
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

        val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build()

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
                App.instance.getString(R.string.client_id),
                App.instance.getString(R.string.client_secret),
                App.instance.getString(R.string.grant_type))).execute().body()

        authorizationModel?.let {
            Preferences.instance.token = it.token
            return response.request().newBuilder().header("Authorization", "Bearer ${Preferences.instance.token}").build()
        }

        return null
    }
}