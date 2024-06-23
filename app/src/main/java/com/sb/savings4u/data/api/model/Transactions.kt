package com.sb.savings4u.data.api.model

import com.google.gson.annotations.SerializedName
import com.sb.savings4u.domain.model.Transaction

data class Transactions(
    @SerializedName("feedItems")
    val transactions: List<Transaction>
)
