package com.sb.savings4u.domain.usecase

import com.sb.savings4u.domain.model.Amount
import com.sb.savings4u.domain.model.Transaction
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@ExperimentalCoroutinesApi
class GetRoundUpTotalUseCaseTest {

    private val getTransactionsUseCase: GetTransactionsUseCase = mockk()
    private val useCase = GetRoundUpTotalUseCase(getTransactionsUseCase)

    @Test
    fun `GIVEN transactions WHEN execute is called THEN returns correct round-up total`() =
        runTest {
            // Given
            val transactions = listOf(
                Transaction("1", "category1", Amount("GBP", 950)),
                Transaction("2", "category2", Amount("GBP", 1900)),
                Transaction("3", "category3", Amount("GBP", 2850))
            )
            coEvery { getTransactionsUseCase.execute() } returns transactions

            // When
            val result = useCase.execute()

            // Then
            assertEquals(1.0, result)
        }

    @Test
    fun `GIVEN no transactions WHEN execute is called THEN returns zero`() = runTest {
        // Given
        coEvery { getTransactionsUseCase.execute() } returns emptyList()

        // When
        val result = useCase.execute()

        // Then
        assertEquals(0.0, result)
    }

    @Test
    fun `GIVEN failure in getTransactionsUseCase WHEN execute is called THEN throws exception`() =
        runTest {
            // Given
            val expectedException = IllegalStateException("Failed to get transactions")
            coEvery { getTransactionsUseCase.execute() } throws expectedException

            // When & Then
            val exception = assertThrows<IllegalStateException> {
                useCase.execute()
            }
            assertEquals(expectedException.message, exception.message)
        }
}
