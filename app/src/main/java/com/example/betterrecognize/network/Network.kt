package com.example.betterrecognize.network

class Network constructor(private val networkApi: NetworkApi) {
    val BEARER_PREFIX = "Bearer"

    suspend fun uploadData(token: String, body: TicketBody) = networkApi.postData(makeBearerToken(token), "application/json", "application/json", body)
    suspend fun authenticate() = networkApi.authenticate()

    private fun makeBearerToken(accessToken: String): String {
        return "$BEARER_PREFIX $accessToken"
    }
}