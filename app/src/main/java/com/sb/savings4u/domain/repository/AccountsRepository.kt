package com.sb.savings4u.domain.repository

import com.sb.savings4u.domain.model.Account
import com.sb.savings4u.domain.model.SavingsGoal
import com.sb.savings4u.domain.model.Transaction
import com.sb.savings4u.domain.model.TransferRequest

interface AccountsRepository {
    suspend fun getAccounts(): Result<List<Account>>
    suspend fun getAccountHolderName(): Result<String>
    suspend fun getTransactions(accountUid: String, categoryUid: String): Result<List<Transaction>>
    suspend fun getCurrentSavingsGoal(accountUid: String): Result<List<SavingsGoal>>
    suspend fun transferToSavingsGoal(
        accountUid: String,
        savingsGoalUid: String,
        transferUid: String,
        transferRequest: TransferRequest
    ): Result<Boolean>
}
