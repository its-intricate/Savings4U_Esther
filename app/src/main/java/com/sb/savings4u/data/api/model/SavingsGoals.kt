package com.sb.savings4u.data.api.model

import com.google.gson.annotations.SerializedName
import com.sb.savings4u.domain.model.SavingsGoal
import com.sb.savings4u.domain.model.Transaction

data class SavingsGoals(
    @SerializedName("savingsGoalList")
    val savingsGoals: List<SavingsGoal>
)
