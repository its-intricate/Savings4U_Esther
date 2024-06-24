package com.sb.savings4u.domain.usecase

import com.sb.savings4u.domain.model.Amount
import com.sb.savings4u.domain.model.TransferRequest
import com.sb.savings4u.domain.repository.AccountsRepository
import com.sb.savings4u.domain.usecase.GetRoundUpTotalUseCase.Companion.MINOR_TO_MAJOR_UNIT_QUALIFIER
import java.util.UUID
import javax.inject.Inject

class TransferToSavingsGoalUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val getAccountsUseCase: GetAccountUseCase,
    private val getRoundUpTotalUseCase: GetRoundUpTotalUseCase,
    private val getSavingsGoalUseCase: GetSavingsGoalUseCase
) {
    suspend fun execute() {
        val accountUid = getAccountsUseCase.execute().accountUid
        val roundUpTotalMajorUnits = getRoundUpTotalUseCase.execute()
        val roundUpTotalMinorUnits = (roundUpTotalMajorUnits * MINOR_TO_MAJOR_UNIT_QUALIFIER).toInt()
        val savingsGoalUid = getSavingsGoalUseCase.execute().savingsGoalUid
        val transferUid = UUID.randomUUID().toString()
        val transferRequest = TransferRequest(
            Amount(minorUnits = roundUpTotalMinorUnits)
        )

        accountsRepository.transferToSavingsGoal(
            accountUid = accountUid,
            savingsGoalUid = savingsGoalUid,
            transferUid = transferUid,
            transferRequest = transferRequest
        )
    }
}