package com.sb.savings4u.domain.usecase

import com.sb.savings4u.domain.model.Account
import com.sb.savings4u.domain.repository.AccountsRepository
import javax.inject.Inject

class GetAccountUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository
) {
    suspend fun execute(): Account {
        return accountsRepository.getAccounts().getOrThrow().first()
    }
}