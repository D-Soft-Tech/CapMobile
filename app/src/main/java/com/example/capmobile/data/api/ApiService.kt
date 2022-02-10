package com.example.capmobile.data.api

import com.example.capmobile.data.model.GetAccountsResponse
import com.example.capmobile.data.model.GetLevelsAndDescription
import com.example.capmobile.data.model.LoginCredentials
import com.example.capmobile.data.model.LoginResponse
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(
        @Body credentials: LoginCredentials
    ): LoginResponse

    @GET("query/officeraccounts")
   suspend fun getAccounts(
        @HeaderMap headers: Map<String, String>,
        @Query("status") status: String
    ): GetAccountsResponse

    @GET("query/leveldescriptions")
   suspend fun getLevelsAndDescription(
        @HeaderMap headers: Map<String, String>,
        @Query("type") type: String
    ): GetLevelsAndDescription
}