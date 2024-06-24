package com.sb.savings4u.domain.usecase

import javax.inject.Inject
import kotlin.math.ceil

class GetRoundUpTotalUseCase @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) {
    suspend fun execute(): Double {
        var roundUpTotal = 0.0
        getTransactionsUseCase.execute().forEach {
            val majorUnits = it.amount.minorUnits / 100.0
            val roundUpAmount = calculateRoundUp(majorUnits)
            roundUpTotal += roundUpAmount
        }

        return roundUpTotal
    }

    private fun calculateRoundUp(transactionAmount: Double): Double {
        return ceil(transactionAmount) - transactionAmount
    }
}