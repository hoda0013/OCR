package com.example.betterrecognize

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BetterRecognizeApplication : Application() {

    val retrofit: Retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl("")
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
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

    override fun onCreate() {
        super.onCreate()
    }
}