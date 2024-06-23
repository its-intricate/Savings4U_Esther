package com.sb.savings4u.domain.model

data class Amount(
    val currency: String = "GBP",
    val minorUnits: Int
)