package com.sb.savings4u.domain.usecase

import com.sb.savings4u.domain.repository.AccountsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@ExperimentalCoroutinesApi
class GetAccountHolderNameUseCaseTest {

    private val accountsRepository: AccountsRepository = mockk()
    private val useCase = GetAccountHolderNameUseCase(accountsRepository)

    @Test
    fun `GIVEN successful response WHEN execute is called THEN returns account holder name`() =
        runTest {
            // Given
            val expectedName = "Monnie McSaver"
            coEvery { accountsRepository.getAccountHolderName() } returns Result.success(
                expectedName
            )

            // When
            val result = useCase.execute()

            // Then
            assertEquals(expectedName, result)
        }

    @Test
    fun `GIVEN failure response WHEN execute is called THEN throws exception`() = runTest {
        // Given
        val expectedException = IllegalStateException("Failed to get account holder name")
        coEvery { accountsRepository.getAccountHolderName() } returns Result.failure(
            expectedException
        )

        // When & Then
        val exception = assertThrows<IllegalStateException> {
            useCase.execute()
        }
        assertEquals(expectedException.message, exception.message)
    }
}
