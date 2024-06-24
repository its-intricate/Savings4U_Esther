package com.sb.savings4u.ui

import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.sb.savings4u.domain.model.Amount
import com.sb.savings4u.domain.model.SavingsGoal
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class AccountScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel: AccountViewModel = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val defaultUiState = AccountContract.AccountUIState(
        isLoading = false,
        isError = false,
        accountHolderName = "Monnie McSaver",
        roundUpTotal = "£100.00",
        totalSaved = "£10.00",
        targetSavings = "£20.00",
        savingsGoalInfo = SavingsGoal(
            savingsGoalUid = "1",
            totalSaved = Amount("GBP", 1000),
            name = "Holiday",
            target = Amount("GBP", 2000)
        ),
        percentageSaved = 50.0f
    )

    private fun givenUiStateReturns(uiState: AccountContract.AccountUIState = defaultUiState) {
        val state = mutableStateOf(uiState) as State<AccountContract.AccountUIState>
        every { mockViewModel.uiState } returns state
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN AccountScreen, WHEN Refresh button is clicked, THEN onUiEvent RefreshScreen is triggered`() =
        testScope.runTest {
            // Given
            givenUiStateReturns()

            // When
            composeTestRule.setContent {
                AccountScreen(viewModel = mockViewModel)
            }

            // Trigger UI interaction
            composeTestRule.onNodeWithText("Refresh").performClick()

            // Then
            coVerify { mockViewModel.handleEvent(AccountContract.AccountUIEvent.RefreshScreen) }
        }

    @Test
    fun `GIVEN AccountScreen, WHEN Transfer button is clicked, THEN onUiEvent TransferToSavings is triggered`() =
        testScope.runTest {
            // Given
            givenUiStateReturns()

            // When
            composeTestRule.setContent {
                AccountScreen(viewModel = mockViewModel)
            }

            // Trigger UI interaction
            composeTestRule.onNodeWithText("Transfer").performClick()

            // Then
            coVerify { mockViewModel.handleEvent(AccountContract.AccountUIEvent.TransferToSavings) }
        }

    @Test
    fun `GIVEN error state WHEN Retry button is clicked THEN onUiEvent RefreshScreen is triggered`() =
        testScope.runTest {
            // Given
            val errorUiState = defaultUiState.copy(isError = true)
            givenUiStateReturns(uiState = errorUiState)

            // When
            composeTestRule.setContent {
                AccountScreen(viewModel = mockViewModel)
            }

            // Trigger UI interaction
            composeTestRule.onNodeWithText("Retry").performClick()

            // Then
            coVerify { mockViewModel.handleEvent(AccountContract.AccountUIEvent.RefreshScreen) }
        }
}
