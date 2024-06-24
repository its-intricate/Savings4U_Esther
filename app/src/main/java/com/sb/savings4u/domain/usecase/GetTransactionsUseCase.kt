package com.sb.savings4u.domain.usecase

import com.sb.savings4u.domain.model.Transaction
import com.sb.savings4u.domain.repository.AccountsRepository
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val getAccountsUseCase: GetAccountUseCase,
    private val accountsRepository: AccountsRepository
) {
    suspend fun execute(): List<Transaction> {
        val account = getAccountsUseCase.execute()
        return accountsRepository.getTransactions(account.accountUid, account.defaultCategory)
            .getOrThrow()
    }
}