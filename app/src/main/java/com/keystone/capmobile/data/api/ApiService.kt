package com.keystone.capmobile.data.api

import com.keystone.capmobile.data.model.*
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

    @POST("/Operations/OpenAccount")
   suspend fun openAccount(
        @Body accountOpeningCredentials: OpenAccountRequestBody
    ): OpenAccountResponse

    @POST("/Operations/reactivateAccount")
   suspend fun reactivateAccount(
        @Body reactivateAccountRequestBody: ReactivateAccountRequestBody
    ): ReactivateAccountResponseBody // Will know what the response body looks like after i am able to test it on post man

    @GET("query/message")
   suspend fun fetchMessage(
        @HeaderMap headers: Map<String, String>,
        @QueryMap levelsAndStatus: Map<String, String>
    ): GeneralMessageResponseBody

    @POST("query/sendmessage")
    suspend fun sendMessage(
        @HeaderMap headers: Map<String, String>,
        @Body messageRequestModel: SendMessageRequestBody
    ): GeneralMessageResponseBody

    @POST("Operations/BvnValidation")
    suspend fun validateBvn(@Body bvnRequestBody: BvnValidationRequestModel): BvnValidationResponse

    @GET("Branch/getlist")
    suspend fun getListOfBranch(): List<GetBankBranchResponse>

    @POST("query/mycabal")
    suspend fun getHardCore(
        @HeaderMap headers: Map<String, String>,
        @Body hardCoreRequest: GetHardCoreRequestBody
    ): GetHardCoreResponseBody

    @GET("query/accountsummary?status=ALL")
    suspend fun getAccountSummary(
        @HeaderMap headers: Map<String, String>
    ): AccountSummary
}