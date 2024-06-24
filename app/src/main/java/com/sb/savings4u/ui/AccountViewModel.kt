package com.sb.savings4u.ui

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sb.savings4u.domain.model.Amount
import com.sb.savings4u.domain.model.SavingsGoal
import com.sb.savings4u.domain.usecase.GetAccountHolderNameUseCase
import com.sb.savings4u.domain.usecase.GetRoundUpTotalUseCase
import com.sb.savings4u.domain.usecase.GetRoundUpTotalUseCase.Companion.MINOR_TO_MAJOR_UNIT_QUALIFIER
import com.sb.savings4u.domain.usecase.GetSavingsGoalUseCase
import com.sb.savings4u.domain.usecase.TransferToSavingsGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getAccountHolderNameUseCase: GetAccountHolderNameUseCase,
    private val getRoundUpTotalUseCase: GetRoundUpTotalUseCase,
    private val transferToSavingsGoalUseCase: TransferToSavingsGoalUseCase,
    private val getSavingsGoalUseCase: GetSavingsGoalUseCase
) : ViewModel() {

    private val _uiState = mutableStateOf(initialUiState())
    val uiState: State<AccountContract.AccountUIState> = _uiState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("AccountViewModel", "Error: ${throwable.localizedMessage}")
        _uiState.value = uiState.value.copy(isLoading = false, isError = true)
    }

    init {
        loadScreen()
    }

    fun handleEvent(event: AccountContract.AccountUIEvent) {
        when (event) {
            AccountContract.AccountUIEvent.TransferToSavings -> transferRoundUpToSavings()
            AccountContract.AccountUIEvent.RefreshScreen -> {
                loadScreen()
            }
        }
    }

    private fun loadScreen() {
        viewModelScope.launch(exceptionHandler) {
            _uiState.value = uiState.value.copy(isLoading = true)
            val results = listOf(
                async { runCatching { fetchSavingsGoal() } },
                async { runCatching { fetchRoundUpTotal() } },
                async { runCatching { fetchAccountHolder() } }
            ).awaitAll()

            val isSuccess = results.all { it.isSuccess }
            _uiState.value = uiState.value.copy(isLoading = false, isError = !isSuccess)
        }
    }

    private suspend fun fetchAccountHolder() {
        val name = getAccountHolderNameUseCase.execute()
        _uiState.value = uiState.value.copy(accountHolderName = name)
    }

    private suspend fun fetchSavingsGoal() {
        val savingsGoal = getSavingsGoalUseCase.execute()
        val totalSavingsMajorUnits =
            savingsGoal.totalSaved.minorUnits / MINOR_TO_MAJOR_UNIT_QUALIFIER
        val targetSavingsMajorUnits = savingsGoal.target.minorUnits / MINOR_TO_MAJOR_UNIT_QUALIFIER
        _uiState.value = uiState.value.copy(
            totalSaved = formatCurrency(totalSavingsMajorUnits, savingsGoal.totalSaved.currency),
            targetSavings = formatCurrency(targetSavingsMajorUnits, savingsGoal.target.currency),
            percentageSaved = (totalSavingsMajorUnits / targetSavingsMajorUnits).toFloat() * MINOR_TO_MAJOR_UNIT_QUALIFIER.toFloat(),
            savingsGoalInfo = savingsGoal
        )
    }

    private suspend fun fetchRoundUpTotal() {
        val roundUpTotal = getRoundUpTotalUseCase.execute()
        _uiState.value = uiState.value.copy(roundUpTotal = formatCurrency(roundUpTotal, "GBP"))
    }

    private fun transferRoundUpToSavings() = viewModelScope.launch(exceptionHandler) {
        _uiState.value = uiState.value.copy(isLoading = true)
        transferToSavingsGoalUseCase.execute()
        loadScreen()
    }

    private fun formatCurrency(amount: Double, currencyCode: String): String {
        val format = NumberFormat.getCurrencyInstance(Locale.UK)
        format.currency = Currency.getInstance(currencyCode)
        return format.format(amount)
    }

    companion object {
        fun initialUiState() = AccountContract.AccountUIState(
            isLoading = true,
            isError = false,
            accountHolderName = null,
            roundUpTotal = null,
            totalSaved = null,
            targetSavings = null,
            savingsGoalInfo = SavingsGoal(
                savingsGoalUid = "",
                totalSaved = Amount(minorUnits = 0),
                name = "",
                target = Amount(minorUnits = 0)
            ),
            percentageSaved = 0f
        )
    }
}
