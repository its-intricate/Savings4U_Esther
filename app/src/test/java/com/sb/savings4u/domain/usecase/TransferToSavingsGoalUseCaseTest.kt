package com.sb.savings4u.domain.usecase

import com.sb.savings4u.domain.model.Account
import com.sb.savings4u.domain.model.Amount
import com.sb.savings4u.domain.model.SavingsGoal
import com.sb.savings4u.domain.model.TransferRequest
import com.sb.savings4u.domain.repository.AccountsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@ExperimentalCoroutinesApi
class TransferToSavingsGoalUseCaseTest {

    private val accountsRepository: AccountsRepository = mockk(relaxed = true)
    private val getAccountsUseCase: GetAccountUseCase = mockk()
    private val getRoundUpTotalUseCase: GetRoundUpTotalUseCase = mockk()
    private val getSavingsGoalUseCase: GetSavingsGoalUseCase = mockk()
    private val useCase = TransferToSavingsGoalUseCase(
        accountsRepository, getAccountsUseCase, getRoundUpTotalUseCase, getSavingsGoalUseCase
    )

    @Test
    fun `GIVEN successful responses WHEN execute is called THEN performs transfer to savings goal`() =
        runTest {
            // Given
            val accountUid = "123"
            val savingsGoalUid = "goal123"
            val roundUpTotalMajorUnits = 5.0
            val account = Account(accountUid, "defaultCategory", "GBP")
            val savingsGoal =
                SavingsGoal(savingsGoalUid, Amount("GBP", 1000), "Test Goal", Amount("GBP", 2000))

            coEvery { getAccountsUseCase.execute() } returns account
            coEvery { getRoundUpTotalUseCase.execute() } returns roundUpTotalMajorUnits
            coEvery { getSavingsGoalUseCase.execute() } returns savingsGoal

            // When
            useCase.execute()

            // Then
            val transferRequest =
                TransferRequest(Amount("GBP", (roundUpTotalMajorUnits * 100).toInt()))
            coVerify {
                accountsRepository.transferToSavingsGoal(
                    accountUid = accountUid,
                    savingsGoalUid = savingsGoalUid,
                    transferUid = any(),
                    transferRequest = transferRequest
                )
            }
        }

    @Test
    fun `GIVEN failure in getAccountsUseCase WHEN execute is called THEN throws exception`() =
        runTest {
            // Given
            val expectedException = IllegalStateException("Failed to get accounts")
            coEvery { getAccountsUseCase.execute() } throws expectedException

            // When & Then
            val exception = assertThrows<IllegalStateException> {
                useCase.execute()
            }
            assertEquals(expectedException.message, exception.message)
        }

    @Test
    fun `GIVEN failure in getRoundUpTotalUseCase WHEN execute is called THEN throws exception`() =
        runTest {
            // Given
            val accountUid = "123"
            val account = Account(accountUid, "defaultCategory", "GBP")
            val expectedException = IllegalStateException("Failed to get round up total")

            coEvery { getAccountsUseCase.execute() } returns account
            coEvery { getRoundUpTotalUseCase.execute() } throws expectedException

            // When & Then
            val exception = assertThrows<IllegalStateException> {
                useCase.execute()
            }
            assertEquals(expectedException.message, exception.message)
        }

    @Test
    fun `GIVEN failure in getSavingsGoalUseCase WHEN execute is called THEN throws exception`() =
        runTest {
            // Given
            val accountUid = "123"
            val roundUpTotalMajorUnits = 5.0
            val account = Account(accountUid, "defaultCategory", "GBP")
            val expectedException = IllegalStateException("Failed to get savings goal")

            coEvery { getAccountsUseCase.execute() } returns account
            coEvery { getRoundUpTotalUseCase.execute() } returns roundUpTotalMajorUnits
            coEvery { getSavingsGoalUseCase.execute() } throws expectedException

            // When & Then
            val exception = assertThrows<IllegalStateException> {
                useCase.execute()
            }
            assertEquals(expectedException.message, exception.message)
        }

    @Test
    fun `GIVEN failure in transferToSavingsGoal WHEN execute is called THEN throws exception`() =
        runTest {
            // Given
            val accountUid = "123"
            val savingsGoalUid = "goal123"
            val roundUpTotalMajorUnits = 5.0
            val account = Account(accountUid, "defaultCategory", "GBP")
            val savingsGoal =
                SavingsGoal(savingsGoalUid, Amount("GBP", 1000), "Test Goal", Amount("GBP", 2000))
            val expectedException = IllegalStateException("Transfer failed")

            coEvery { getAccountsUseCase.execute() } returns account
            coEvery { getRoundUpTotalUseCase.execute() } returns roundUpTotalMajorUnits
            coEvery { getSavingsGoalUseCase.execute() } returns savingsGoal
            coEvery {
                accountsRepository.transferToSavingsGoal(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } throws expectedException

            // When & Then
            val exception = assertThrows<IllegalStateException> {
                useCase.execute()
            }
            assertEquals(expectedException.message, exception.message)
        }
}
