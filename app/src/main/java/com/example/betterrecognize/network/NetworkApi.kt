package com.example.betterrecognize.network

import retrofit2.http.POST


interface NetworkApi {
    @POST("ticketdata")
    suspend fun postData(): TicketResponse
}

data class TicketResponse(
    val msg: String
)