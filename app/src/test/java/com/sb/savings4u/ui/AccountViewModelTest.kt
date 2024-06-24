package com.sb.savings4u.ui

import com.sb.savings4u.domain.model.Amount
import com.sb.savings4u.domain.model.SavingsGoal
import com.sb.savings4u.domain.usecase.GetAccountHolderNameUseCase
import com.sb.savings4u.domain.usecase.GetRoundUpTotalUseCase
import com.sb.savings4u.domain.usecase.GetSavingsGoalUseCase
import com.sb.savings4u.domain.usecase.TransferToSavingsGoalUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class AccountViewModelTest {

    private lateinit var viewModel: AccountViewModel

    @RelaxedMockK
    private lateinit var getAccountHolderNameUseCase: GetAccountHolderNameUseCase

    @RelaxedMockK
    private lateinit var getRoundUpTotalUseCase: GetRoundUpTotalUseCase

    @RelaxedMockK
    private lateinit var getSavingsGoalUseCase: GetSavingsGoalUseCase

    @RelaxedMockK
    private lateinit var transferToSavingsGoalUseCase: TransferToSavingsGoalUseCase

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        viewModel = AccountViewModel(
            getAccountHolderNameUseCase,
            getRoundUpTotalUseCase,
            transferToSavingsGoalUseCase,
            getSavingsGoalUseCase
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN initial state WHEN screen is loaded THEN isLoading is true`() {
        // Given
        coEvery { getAccountHolderNameUseCase.execute() } returns "Monnie McSaver"
        coEvery { getRoundUpTotalUseCase.execute() } returns 100.0
        coEvery { getSavingsGoalUseCase.execute() } returns SavingsGoal(
            savingsGoalUid = "1",
            totalSaved = Amount("GBP", 1000),
            name = "Holiday",
            target = Amount("GBP", 2000)
        )

        // When
        viewModel.handleEvent(AccountContract.AccountUIEvent.RefreshScreen)

        // Then
        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN loading screen WHEN data is fetched THEN isLoading is false`() = testScope.runTest {
        // Given
        coEvery { getAccountHolderNameUseCase.execute() } returns "Monnie McSaver"
        coEvery { getRoundUpTotalUseCase.execute() } returns 100.0
        coEvery { getSavingsGoalUseCase.execute() } returns SavingsGoal(
            savingsGoalUid = "1",
            totalSaved = Amount("GBP", 1000),
            name = "Holiday",
            target = Amount("GBP", 2000)
        )

        // When
        viewModel.handleEvent(AccountContract.AccountUIEvent.RefreshScreen)
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN account holder name WHEN fetched THEN accountHolderName is set`() =
        testScope.runTest {
            // Given
            val accountHolderName = "Monnie McSaver"
            coEvery { getAccountHolderNameUseCase.execute() } returns accountHolderName

            // When
            viewModel.handleEvent(AccountContract.AccountUIEvent.RefreshScreen)
            advanceUntilIdle()

            // Then
            assertEquals(accountHolderName, viewModel.uiState.value.accountHolderName)
        }

    @Test
    fun `GIVEN round up total WHEN fetched THEN roundUpTotal is set`() = testScope.runTest {
        // Given
        val roundUpTotal = 100.0
        coEvery { getRoundUpTotalUseCase.execute() } returns roundUpTotal

        // When
        viewModel.handleEvent(AccountContract.AccountUIEvent.RefreshScreen)
        advanceUntilIdle()

        // Then
        assertEquals("£100.00", viewModel.uiState.value.roundUpTotal)
    }

    @Test
    fun `GIVEN savings goal WHEN fetched THEN totalSaved and targetSavings are set`() =
        testScope.runTest {
            // Given
            val savingsGoal = SavingsGoal(
                savingsGoalUid = "1",
                totalSaved = Amount("GBP", 1000),
                name = "Holiday",
                target = Amount("GBP", 2000)
            )
            coEvery { getSavingsGoalUseCase.execute() } returns savingsGoal

            // When
            viewModel.handleEvent(AccountContract.AccountUIEvent.RefreshScreen)
            advanceUntilIdle()

            // Then
            assertEquals("£10.00", viewModel.uiState.value.totalSaved)
            assertEquals("£20.00", viewModel.uiState.value.targetSavings)
        }

    @Test
    fun `GIVEN transfer to savings WHEN executed THEN loadScreen is triggered`() =
        testScope.runTest {
            // Given
            coEvery { transferToSavingsGoalUseCase.execute() } just runs
            coEvery { getAccountHolderNameUseCase.execute() } returns "Monnie McSaver"
            coEvery { getRoundUpTotalUseCase.execute() } returns 100.0
            coEvery { getSavingsGoalUseCase.execute() } returns SavingsGoal(
                savingsGoalUid = "1",
                totalSaved = Amount("GBP", 1000),
                name = "Holiday",
                target = Amount("GBP", 2000)
            )

            // When
            viewModel.handleEvent(AccountContract.AccountUIEvent.TransferToSavings)

            assertTrue(viewModel.uiState.value.isLoading)

            advanceUntilIdle()

            // Then
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `GIVEN API error WHEN screen is loaded THEN isError is true`() = testScope.runTest {
        // Given
        coEvery { getAccountHolderNameUseCase.execute() } returns "Monnie McSaver"
        coEvery { getRoundUpTotalUseCase.execute() } returns 100.0
        coEvery { getSavingsGoalUseCase.execute() } throws Exception("API error")
        // When
        viewModel.handleEvent(AccountContract.AccountUIEvent.RefreshScreen)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value.isError)
    }
}
