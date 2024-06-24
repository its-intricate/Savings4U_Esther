package com.sb.savings4u.domain.usecase

import com.sb.savings4u.domain.model.Account
import com.sb.savings4u.domain.repository.AccountsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@ExperimentalCoroutinesApi
class GetAccountUseCaseTest {

    private val accountsRepository: AccountsRepository = mockk()
    private val useCase = GetAccountUseCase(accountsRepository)

    @Test
    fun `GIVEN successful response WHEN execute is called THEN returns first account`() = runTest {
        // Given
        val expectedAccount = Account("123", "defaultCategory", "GBP")
        coEvery { accountsRepository.getAccounts() } returns Result.success(listOf(expectedAccount))

        // When
        val result = useCase.execute()

        // Then
        assertEquals(expectedAccount, result)
    }

    @Test
    fun `GIVEN failure response WHEN execute is called THEN throws exception`() = runTest {
        // Given
        val expectedException = IllegalStateException("Failed to get accounts")
        coEvery { accountsRepository.getAccounts() } returns Result.failure(expectedException)

        // When & Then
        val exception = assertThrows<IllegalStateException> {
            useCase.execute()
        }
        assertEquals(expectedException.message, exception.message)
    }
}
