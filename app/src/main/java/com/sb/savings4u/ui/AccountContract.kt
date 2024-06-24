package com.sb.savings4u.ui

import com.sb.savings4u.domain.model.SavingsGoal

interface AccountContract {

    data class AccountUIState(
        val isLoading: Boolean,
        val isError: Boolean,
        val accountHolderName: String?,
        val roundUpTotal: String?,
        val totalSaved: String?,
        val targetSavings: String?,
        val savingsGoalInfo: SavingsGoal,
        val percentageSaved: Float
    )

    sealed class AccountUIEvent {
        object RefreshScreen : AccountUIEvent()
        object TransferToSavings : AccountUIEvent()
    }
}