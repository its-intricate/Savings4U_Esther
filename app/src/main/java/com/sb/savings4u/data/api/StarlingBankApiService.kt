package com.sb.savings4u.data.api

import com.sb.savings4u.data.api.model.Accounts
import com.sb.savings4u.data.api.model.SavingsGoals
import com.sb.savings4u.data.api.model.Transactions
import com.sb.savings4u.domain.model.AccountHolderName
import com.sb.savings4u.domain.model.TransferRequest
import com.sb.savings4u.domain.model.TransferResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface StarlingBankApiService {
    @Headers(
        "Accept: application/json",
        "User-Agent: Esther"
    )
    @GET("api/v2/accounts")
    fun getAccounts(
        @Header("Authorization") authorization: String
    )
            : Call<Accounts>

    @Headers(
        "Accept: application/json",
        "User-Agent: Esther"
    )
    @GET("api/v2/account-holder/name")
    fun getAccountHolderName(
        @Header("Authorization") authorization: String
    )
            : Call<AccountHolderName>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("api/v2/feed/account/{accountUid}/category/{categoryUid}")
    fun getTransactions(
        @Header("Authorization") authorization: String,
        @Path("accountUid") accountUid: String,
        @Path("categoryUid") categoryUid: String,
        @Query("changesSince") changesSince: String
    ): Call<Transactions>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("api/v2/account/{accountUid}/savings-goals")
    fun getSavingsGoals(
        @Header("Authorization") authorization: String,
        @Path("accountUid") accountUid: String
    ): Call<SavingsGoals>


    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @PUT("api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/{transferUid}")
    fun transferToSavingsGoal(
        @Header("Authorization") authorization: String,
        @Path("accountUid") accountUid: String,
        @Path("savingsGoalUid") savingsGoalUid: String,
        @Path("transferUid") transferUid: String,
        @Body transferRequest: TransferRequest
    ): Call<TransferResponse>
}
