package com.example.betterrecognize

import android.app.Application
import com.example.betterrecognize.network.NetworkApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BetterRecognizeApplication : Application() {

    val networkApi: NetworkApi by lazy {
        Retrofit
            .Builder()
            .baseUrl("http://demo4037212.mockable.io/")
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
            .create(NetworkApi::class.java)
    }

    private val gsonConverterFactory: GsonConverterFactory by lazy {
        GsonConverterFactory.create()
    }

    private val  okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()

//        builder.authenticator(conservisAuthenticator)

        if (BuildConfig.DEBUG) {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.HEADERS
            builder.addInterceptor(logger)
                .build()
        } else {
            builder.build()
        }
    }
}