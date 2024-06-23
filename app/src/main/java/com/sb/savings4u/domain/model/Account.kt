package com.sb.savings4u.domain.model

data class Account(
    val accountUid: String,
    val accountType: String,
    val defaultCategory: String,
    val currency: String,
    val createdAt: String,
    val name: String
)
