package com.vovasoft.unilot.repository.retrofit

import com.vovasoft.unilot.BuildConfig
import com.vovasoft.unilot.components.Preferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
class WebClient(api: API = WebClient.API.BASE) {

    enum class API { BASE }

    val webservice: WebService

    init {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor { chain ->
                    val original = chain.request()

                    val request = original.newBuilder()
                            .header("Content-Type", "context/json")
                            .header("Api-Version", BuildConfig.API_VERSION)
                            .header("Authorization", "Bearer ${Preferences.instance.token}")
                            .method(original.method(), original.body())
                            .build()

                    chain.proceed(request)
                }
                .authenticator(TokenAuthenticator())
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