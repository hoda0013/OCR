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
            .baseUrl("https://conservis-hackathon-ocr.herokuapp.com/")
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
            .create(NetworkApi::class.java)
    }

    private val gsonConverterFactory: GsonConverterFactory by lazy {
        GsonConverterFactory.create()
    }

    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()

//        builder.authenticator(conservisAuthenticator)

        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.HEADERS
        builder.addInterceptor(logger)
            .build()
    }
}