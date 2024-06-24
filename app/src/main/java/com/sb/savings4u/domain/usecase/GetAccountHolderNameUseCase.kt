package com.sb.savings4u.domain.usecase

import com.sb.savings4u.domain.repository.AccountsRepository
import javax.inject.Inject

class GetAccountHolderNameUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository
) {
    suspend fun execute(): String {
        return accountsRepository.getAccountHolderName().getOrThrow()
    }
}