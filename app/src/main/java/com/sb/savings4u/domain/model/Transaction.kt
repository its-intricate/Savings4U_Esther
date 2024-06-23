package com.sb.savings4u.domain.model

data class Transaction(
    val feedItemUid: String,
    val categoryUid: String,
    val amount: Amount
)
