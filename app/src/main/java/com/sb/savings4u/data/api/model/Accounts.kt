package com.sb.savings4u.data.api.model

import com.google.gson.annotations.SerializedName
import com.sb.savings4u.domain.model.Account

data class Accounts(
    @SerializedName("accounts")
    val accounts: List<Account>
)
