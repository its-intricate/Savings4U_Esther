package com.sb.savings4u.domain.usecase

import com.sb.savings4u.domain.model.SavingsGoal
import com.sb.savings4u.domain.repository.AccountsRepository
import javax.inject.Inject

class GetSavingsGoalUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val getAccountsUseCase: GetAccountUseCase,
) {
    suspend fun execute(): SavingsGoal {
        val accountUid = getAccountsUseCase.execute().accountUid
        return accountsRepository.getCurrentSavingsGoal(accountUid = accountUid).getOrThrow()
            .first()

    }
}