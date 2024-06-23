package com.sb.savings4u.domain.model

data class SavingsGoal(
    val savingsGoalUid: String,
    val totalSaved: Amount,
    val name: String,
    val target: Amount
)
