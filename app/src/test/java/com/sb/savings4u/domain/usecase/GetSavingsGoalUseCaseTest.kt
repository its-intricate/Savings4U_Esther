package com.sb.savings4u.domain.usecase

import com.sb.savings4u.domain.model.Account
import com.sb.savings4u.domain.model.Amount
import com.sb.savings4u.domain.model.SavingsGoal
import com.sb.savings4u.domain.repository.AccountsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@ExperimentalCoroutinesApi
class GetSavingsGoalUseCaseTest {

    private val accountsRepository: AccountsRepository = mockk()
    private val getAccountUseCase: GetAccountUseCase = mockk()
    private val useCase = GetSavingsGoalUseCase(accountsRepository, getAccountUseCase)

    @Test
    fun `GIVEN successful response WHEN execute is called THEN returns first savings goal`() =
        runTest {
            // Given
            val account = Account("123", "defaultCategory", "GBP")
            val savingsGoals =
                listOf(SavingsGoal("1", Amount("GBP", 1000), "Test Goal", Amount("GBP", 2000)))
            coEvery { getAccountUseCase.execute() } returns account
            coEvery { accountsRepository.getCurrentSavingsGoal(account.accountUid) } returns Result.success(
                savingsGoals
            )

            // When
            val result = useCase.execute()

            // Then
            assertEquals(savingsGoals.first(), result)
        }

    @Test
    fun `GIVEN failure response WHEN execute is called THEN throws exception`() = runTest {
        // Given
        val account = Account("123", "defaultCategory", "GBP")
        val expectedException = IllegalStateException("Failed to get savings goals")
        coEvery { getAccountUseCase.execute() } returns account
        coEvery { accountsRepository.getCurrentSavingsGoal(account.accountUid) } returns Result.failure(
            expectedException
        )

        // When & Then
        val exception = assertThrows<IllegalStateException> {
            useCase.execute()
        }
        assertEquals(expectedException.message, exception.message)
    }
}
