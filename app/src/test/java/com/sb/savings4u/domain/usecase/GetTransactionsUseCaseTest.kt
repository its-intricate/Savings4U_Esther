package com.sb.savings4u.domain.usecase

import com.sb.savings4u.domain.model.Account
import com.sb.savings4u.domain.model.Amount
import com.sb.savings4u.domain.model.Transaction
import com.sb.savings4u.domain.repository.AccountsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@ExperimentalCoroutinesApi
class GetTransactionsUseCaseTest {

    private val getAccountUseCase: GetAccountUseCase = mockk()
    private val accountsRepository: AccountsRepository = mockk()
    private val useCase = GetTransactionsUseCase(getAccountUseCase, accountsRepository)

    @Test
    fun `GIVEN successful response WHEN execute is called THEN returns transactions`() = runTest {
        // Given
        val account = Account("123", "defaultCategory", "GBP")
        val transactions = listOf(
            Transaction("1", "category1", Amount("GBP", 1000)),
            Transaction("2", "category1", Amount("GBP", 2000))
        )
        coEvery { getAccountUseCase.execute() } returns account
        coEvery {
            accountsRepository.getTransactions(
                account.accountUid,
                account.defaultCategory
            )
        } returns Result.success(transactions)

        // When
        val result = useCase.execute()

        // Then
        assertEquals(transactions, result)
    }

    @Test
    fun `GIVEN failure response WHEN execute is called THEN throws exception`() = runTest {
        // Given
        val account = Account("123", "defaultCategory", "GBP")
        val expectedException = IllegalStateException("Failed to get transactions")
        coEvery { getAccountUseCase.execute() } returns account
        coEvery {
            accountsRepository.getTransactions(
                account.accountUid,
                account.defaultCategory
            )
        } returns Result.failure(expectedException)

        // When & Then
        val exception = assertThrows<IllegalStateException> {
            useCase.execute()
        }
        assertEquals(expectedException.message, exception.message)
    }
}
