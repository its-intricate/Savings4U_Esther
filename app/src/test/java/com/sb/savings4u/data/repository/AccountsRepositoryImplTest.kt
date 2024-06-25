package com.sb.savings4u.data.repository

import com.sb.savings4u.data.api.StarlingBankApiService
import com.sb.savings4u.data.api.model.Accounts
import com.sb.savings4u.data.api.model.SavingsGoals
import com.sb.savings4u.data.api.model.Transactions
import com.sb.savings4u.domain.model.Account
import com.sb.savings4u.domain.model.AccountHolderName
import com.sb.savings4u.domain.model.Amount
import com.sb.savings4u.domain.model.SavingsGoal
import com.sb.savings4u.domain.model.Transaction
import com.sb.savings4u.domain.model.TransferRequest
import com.sb.savings4u.domain.model.TransferResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@ExperimentalCoroutinesApi
class AccountsRepositoryImplTest {

    private lateinit var repository: AccountsRepositoryImpl
    private val apiService: StarlingBankApiService = mockk()
    private val accessToken = "test_access_token"

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeEach
    fun setUp() {
        repository = AccountsRepositoryImpl(apiService, accessToken)
    }

    @Test
    fun `GIVEN valid response WHEN getAccounts is called THEN returns accounts`() =
        testScope.runTest {
            // Given
            val accounts = listOf(Account("123", "defaultCategory", "GBP"))
            coEvery { apiService.getAccounts("Bearer $accessToken") } returns Response.success(
                Accounts(accounts)
            )

            // When
            val result = repository.getAccounts()

            // Then
            assert(result.isSuccess)
            assert(result.getOrNull() == accounts)
        }

    @Test
    fun `GIVEN error response WHEN getAccounts is called THEN returns failure`() =
        testScope.runTest {
            // Given
            coEvery { apiService.getAccounts("Bearer $accessToken") } returns Response.error(
                400,
                "Error".toResponseBody("application/json".toMediaType())
            )

            // When
            val result = repository.getAccounts()

            // Then
            assert(result.isFailure)
        }

    @Test
    fun `GIVEN valid response WHEN getAccountHolderName is called THEN returns name`() =
        testScope.runTest {
            // Given
            val accountHolderName = AccountHolderName("Monnie McSaver")
            coEvery { apiService.getAccountHolderName("Bearer $accessToken") } returns Response.success(
                accountHolderName
            )

            // When
            val result = repository.getAccountHolderName()

            // Then
            assert(result.isSuccess)
            assert(result.getOrNull() == "Monnie McSaver")
        }

    @Test
    fun `GIVEN error response WHEN getAccountHolderName is called THEN returns failure`() =
        testScope.runTest {
            // Given
            coEvery { apiService.getAccountHolderName("Bearer $accessToken") } returns Response.error(
                400,
                "Error".toResponseBody("application/json".toMediaType())
            )

            // When
            val result = repository.getAccountHolderName()

            // Then
            assert(result.isFailure)
        }

    @Test
    fun `GIVEN valid response WHEN getTransactions is called THEN returns transactions`() =
        testScope.runTest {
            // Given
            val transactions = listOf(Transaction("1", "456", Amount("GBP", 1000)))
            coEvery {
                apiService.getTransactions(
                    "Bearer $accessToken",
                    "123",
                    "456",
                    any()
                )
            } returns Response.success(
                Transactions(transactions)
            )

            // When
            val result = repository.getTransactions("123", "456")

            // Then
            assert(result.isSuccess)
            assert(result.getOrNull() == transactions)
        }

    @Test
    fun `GIVEN error response WHEN getTransactions is called THEN returns failure`() =
        testScope.runTest {
            // Given
            coEvery {
                apiService.getTransactions(
                    "Bearer $accessToken",
                    "123",
                    "456",
                    any()
                )
            } returns Response.error(400, "Error".toResponseBody("application/json".toMediaType()))

            // When
            val result = repository.getTransactions("123", "456")

            // Then
            assert(result.isFailure)
        }

    @Test
    fun `GIVEN valid response WHEN getCurrentSavingsGoal is called THEN returns savings goals`() =
        testScope.runTest {
            // Given
            val savingsGoals =
                listOf(SavingsGoal("1", Amount("GBP", 1000), "Test Goal", Amount("GBP", 2000)))
            coEvery {
                apiService.getSavingsGoals(
                    "Bearer $accessToken",
                    "123"
                )
            } returns Response.success(
                SavingsGoals(savingsGoals)
            )

            // When
            val result = repository.getCurrentSavingsGoal("123")

            // Then
            assert(result.isSuccess)
            assert(result.getOrNull() == savingsGoals)
        }

    @Test
    fun `GIVEN error response WHEN getCurrentSavingsGoal is called THEN returns failure`() =
        testScope.runTest {
            // Given
            coEvery {
                apiService.getSavingsGoals(
                    "Bearer $accessToken",
                    "123"
                )
            } returns Response.error(400, "Error".toResponseBody("application/json".toMediaType()))

            // When
            val result = repository.getCurrentSavingsGoal("123")

            // Then
            assert(result.isFailure)
        }

    @Test
    fun `GIVEN valid response WHEN transferToSavingsGoal is called THEN returns success`() =
        testScope.runTest {
            // Given
            val transferRequest = TransferRequest(Amount("GBP", 1000))
            val transferResponse = TransferResponse("1", true)
            coEvery {
                apiService.transferToSavingsGoal(
                    "Bearer $accessToken",
                    "123",
                    "456",
                    "789",
                    transferRequest
                )
            } returns Response.success(transferResponse)

            // When
            val result = repository.transferToSavingsGoal("123", "456", "789", transferRequest)

            // Then
            assert(result.isSuccess)
            assert(result.getOrNull() == true)
        }

    @Test
    fun `GIVEN error response WHEN transferToSavingsGoal is called THEN returns failure`() =
        testScope.runTest {
            // Given
            val transferRequest = TransferRequest(Amount("GBP", 1000))
            coEvery {
                apiService.transferToSavingsGoal(
                    "Bearer $accessToken",
                    "123",
                    "456",
                    "789",
                    transferRequest
                )
            } returns Response.error(400, "Error".toResponseBody("application/json".toMediaType()))

            // When
            val result = repository.transferToSavingsGoal("123", "456", "789", transferRequest)

            // Then
            assert(result.isFailure)
        }

    @Test
    fun `GIVEN current date WHEN getOneWeekPriorIsoDateTime is called THEN returns correct format`() {
        // Given
        val expectedFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val expectedDate = ZonedDateTime.now().minusWeeks(1).format(expectedFormat)

        // When
        val result = repository.getOneWeekPriorIsoDateTime()

        // Then
        assertEquals(expectedDate, result)
    }
}
