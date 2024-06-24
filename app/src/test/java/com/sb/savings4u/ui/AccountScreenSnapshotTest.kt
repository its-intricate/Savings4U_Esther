package com.sb.savings4u.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.paparazzi.Paparazzi
import com.sb.savings4u.domain.model.Amount
import com.sb.savings4u.domain.model.SavingsGoal
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountScreenSnapshotTest {

    @get:Rule
    val paparazzi = Paparazzi()

    @get:Rule
    val composeTestRule = createComposeRule()

    private val savingsGoal = SavingsGoal(
        savingsGoalUid = "1",
        totalSaved = Amount("GBP", 10000),
        name = "Test Goal",
        target = Amount("GBP", 20000)
    )

    @Before
    fun setUp() {
        // Disable animations
        composeTestRule.mainClock.autoAdvance = false
    }

    @Test
    fun accountScreenLoadingState() {
        val viewModel: AccountViewModel = mockk {
            every { uiState } returns mutableStateOf(
                AccountContract.AccountUIState(
                    isLoading = true,
                    savingsGoalInfo = savingsGoal,
                    isError = false,
                    accountHolderName = null,
                    roundUpTotal = null,
                    totalSaved = null,
                    targetSavings = null,
                    percentageSaved = 0f
                )
            )
        }

        composeTestRule.setContent {
            AccountScreen(viewModel = viewModel)
        }

        // Advance the clock to ensure all animations are complete
        composeTestRule.mainClock.advanceTimeBy(10000L) // Adjust time as needed

        // Take a snapshot
        paparazzi.snapshot {
            AccountScreen(viewModel = viewModel)
        }
    }

    @Test
    fun accountScreenErrorState() {
        val viewModel: AccountViewModel = mockk {
            every { uiState } returns mutableStateOf(
                AccountContract.AccountUIState(
                    isError = true,
                    savingsGoalInfo = savingsGoal,
                    isLoading = false,
                    accountHolderName = null,
                    roundUpTotal = null,
                    totalSaved = null,
                    targetSavings = null,
                    percentageSaved = 0f
                )
            )
        }

        composeTestRule.setContent {
            AccountScreen(viewModel = viewModel)
        }

        // Advance the clock to ensure all animations are complete
        composeTestRule.mainClock.advanceTimeBy(10000L) // Adjust time as needed

        // Take a snapshot
        paparazzi.snapshot {
            AccountScreen(viewModel = viewModel)
        }
    }

    @Test
    fun accountScreenContentState() {
        val viewModel: AccountViewModel = mockk {
            every { uiState } returns mutableStateOf(
                AccountContract.AccountUIState(
                    accountHolderName = "Monnie McSaver",
                    roundUpTotal = "£100.00",
                    totalSaved = "£100.00",
                    targetSavings = "£200.00",
                    savingsGoalInfo = savingsGoal,
                    percentageSaved = 50f,
                    isLoading = false,
                    isError = false
                )
            )
        }

        composeTestRule.setContent {
            AccountScreen(viewModel = viewModel)
        }

        // Advance the clock to ensure all animations are complete
        composeTestRule.mainClock.advanceTimeBy(10000L) // Adjust time as needed

        // Take a snapshot
        paparazzi.snapshot {
            AccountScreen(viewModel = viewModel)
        }
    }
}
