package com.vovasoft.sportloto.repository.retrofit

import com.vovasoft.sportloto.App
import com.vovasoft.sportloto.BuildConfig
import com.vovasoft.sportloto.Preferences
import okhttp3.CipherSuite
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256
import okhttp3.ConnectionSpec
import okhttp3.TlsVersion
import java.util.*


/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
class WebClient(api: API = WebClient.API.BASE) {

    enum class API { BASE }

    val webservice: WebService

    init {

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
                .addInterceptor { chain ->
                    val original = chain.request()

                    val request = original.newBuilder()
                            .header("Content-Type", "context/json")
                            .header("Authorization", "Bearer ${Preferences.instance.token}")
                            .method(original.method(), original.body())
                            .build()

                    chain.proceed(request)
                }
                .authenticator(TokenAuthenticator(App.instance!!))
                .connectionSpecs(Collections.singletonList(spec))
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()

        val baseUrl = when (api) {
            API.BASE ->  BuildConfig.BASE_API
        }

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        webservice = retrofit.create(WebService::class.java)
    }

}