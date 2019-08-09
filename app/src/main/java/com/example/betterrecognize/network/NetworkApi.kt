package com.example.betterrecognize.network

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface NetworkApi {
    @POST("api/delivery-receipts")
    suspend fun postData(
        @Header("Authorization") token: String,
        @Header("Content-Type") content: String = "application/json",
        @Header("Accept") accept: String = "application/json",
        @Body body: TicketBody): TicketResponse

    @POST("api/authenticate")
    suspend fun authenticate(@Body body: AuthBody = AuthBody("admin", "admin", true)): TicketResponse
}

data class TicketResponse(
    val id_token: String
)

data class AuthBody(
    val username: String,
    val password: String,
    val rememberMe: Boolean
)

data class TicketBody(
    val crop: String,
    val elevatorLocation: String,
    val grossBushel: Float?,
    val grossWeight: Float?,
    val grower: String,
    val moisture: Float?,
    val netBushel: Float?,
    val netWeight: Float?,
    val tareWeight: Float?,
    val ticketNo: Long,
    val uploadedBy: String,
    val uploadedDate: String?,
    val vehicleNo: String
)