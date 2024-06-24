package com.sb.savings4u.data.repository

import com.sb.savings4u.data.api.StarlingBankApiService
import com.sb.savings4u.domain.model.Account
import com.sb.savings4u.domain.model.SavingsGoal
import com.sb.savings4u.domain.model.Transaction
import com.sb.savings4u.domain.model.TransferRequest
import com.sb.savings4u.domain.repository.AccountsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    private val starlingBankApiService: StarlingBankApiService,
    private val accessToken: String
) : AccountsRepository {

    override suspend fun getAccounts(): Result<List<Account>> = safeApiCall {
        val response = starlingBankApiService.getAccounts("Bearer $accessToken")

        if (response.isSuccessful && response.body()?.accounts?.isNotEmpty() == true) {
            response.body()?.accounts ?: throw IllegalStateException("No accounts found")
        } else {
            throw Exception("Failed to load accounts: ${response.message()}")
        }
    }

    override suspend fun getAccountHolderName(): Result<String> = safeApiCall {
        val response = starlingBankApiService.getAccountHolderName("Bearer $accessToken")

        if (response.isSuccessful && !response.body()?.accountHolderName.isNullOrBlank()) {
            response.body()?.accountHolderName
                ?: throw IllegalStateException("Account holder name not found")
        } else {
            throw Exception("Failed to load account holder name: ${response.message()}")
        }
    }

    override suspend fun getTransactions(
        accountUid: String,
        categoryUid: String
    ): Result<List<Transaction>> {
        val changesSince = getOneWeekPriorIsoDateTime()
        return safeApiCall {
            val response = starlingBankApiService.getTransactions(
                "Bearer $accessToken", accountUid, categoryUid, changesSince
            )

            if (response.isSuccessful && response.body()?.transactions?.isNotEmpty() == true) {
                response.body()?.transactions
                    ?: throw IllegalStateException("No transactions found")
            } else {
                throw Exception("Failed to load transactions: ${response.message()}")
            }
        }
    }

    override suspend fun getCurrentSavingsGoal(accountUid: String): Result<List<SavingsGoal>> =
        safeApiCall {
            val response = starlingBankApiService.getSavingsGoals("Bearer $accessToken", accountUid)

            if (response.isSuccessful && response.body()?.savingsGoals?.isNotEmpty() == true) {
                response.body()?.savingsGoals
                    ?: throw IllegalStateException("No savings goals found")
            } else {
                throw Exception("Failed to load savings goals: ${response.message()}")
            }
        }

    override suspend fun transferToSavingsGoal(
        accountUid: String,
        savingsGoalUid: String,
        transferUid: String,
        transferRequest: TransferRequest
    ): Result<Boolean> = safeApiCall {
        val response = starlingBankApiService.transferToSavingsGoal(
            "Bearer $accessToken", accountUid, savingsGoalUid, transferUid, transferRequest
        )
        if (response.isSuccessful) {
            response.body()?.success ?: throw IllegalStateException("Transfer failed")
        } else {
            throw Exception("Failed to transfer to savings goal: ${response.message()}")
        }
    }

    private fun getOneWeekPriorIsoDateTime(): String {
        val oneWeekPriorDateTime = ZonedDateTime.now().minusWeeks(1)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return oneWeekPriorDateTime.format(formatter)
    }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            withContext(Dispatchers.IO) {
                Result.success(apiCall())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
