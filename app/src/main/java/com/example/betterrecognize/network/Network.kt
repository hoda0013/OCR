package com.example.betterrecognize.network

class Network constructor(private val networkApi: NetworkApi) {
    suspend fun uploadData() = networkApi.postData()
}