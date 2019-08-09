package com.example.betterrecognize.network

class Network constructor(private val networkApi: NetworkApi) {
    suspend fun uploadData(token: String, body: TicketBody) = networkApi.postData(body)
    suspend fun authenticate() = networkApi.authenticate()
}