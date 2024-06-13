package com.sb.savings4u.data.api

import com.sb.savings4u.data.api.model.Accounts
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface StarlingBankApiService {
    @Headers(
        "Accept: application/json",
        "User-Agent: Esther"
    )
    @GET("api/v2/accounts")
    fun getAccounts(@Header("Authorization") authorization: String): Call<Accounts>
}
