package com.sb.savings4u.data.repository

import com.sb.savings4u.data.api.StarlingBankApiService
import com.sb.savings4u.domain.model.Account
import com.sb.savings4u.domain.model.SavingsGoal
import com.sb.savings4u.domain.model.Transaction
import com.sb.savings4u.domain.model.TransferRequest
import com.sb.savings4u.domain.repository.AccountsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    private val starlingBankApiService: StarlingBankApiService
) : AccountsRepository {

    override suspend fun getAccounts(): Result<List<Account>> {
        return try {
            withContext(Dispatchers.Main) {
                val response = starlingBankApiService.getAccounts("Bearer $ACCESS_TOKEN")
                    .awaitResponse()
                val accounts = response.body()

                if (accounts != null && accounts.accounts.isNotEmpty()) {
                    Result.success(accounts.accounts)
                } else {
                    Result.failure(IllegalArgumentException())
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAccountHolderName(): Result<String> {
        return try {
            withContext(Dispatchers.Main) {
                val response = starlingBankApiService.getAccountHolderName("Bearer $ACCESS_TOKEN")
                    .awaitResponse()
                val accountHolderName = response.body()

                if (accountHolderName != null && accountHolderName.accountHolderName.isNotBlank()) {
                    Result.success(accountHolderName.accountHolderName)
                } else {
                    Result.failure(IllegalArgumentException())
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTransactions(
        accountUid: String,
        categoryUid: String
    ): Result<List<Transaction>> {
        return try {
            val changesSince = getOneWeekPriorIsoDateTime()

            withContext(Dispatchers.Main) {
                val response = starlingBankApiService.getTransactions(
                    "Bearer $ACCESS_TOKEN",
                    accountUid,
                    categoryUid,
                    changesSince
                )
                    .awaitResponse()
                val transactions = response.body()

                if (transactions != null && transactions.transactions.isNotEmpty()) {
                    Result.success(transactions.transactions)
                } else {
                    Result.failure(IllegalArgumentException())
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentSavingsGoal(accountUid: String): Result<List<SavingsGoal>> {
        return try {
            withContext(Dispatchers.Main) {
                val response = starlingBankApiService.getSavingsGoals(
                    "Bearer $ACCESS_TOKEN",
                    accountUid
                )
                    .awaitResponse()
                val savingsGoals = response.body()

                if (savingsGoals != null && savingsGoals.savingsGoals.isNotEmpty()) {
                    Result.success(savingsGoals.savingsGoals)
                } else {
                    Result.failure(IllegalArgumentException())
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun transferToSavingsGoal(
        accountUid: String,
        savingsGoalUid: String,
        transferUid: String,
        transferRequest: TransferRequest
    ): Result<Boolean> {
        return try {
            withContext(Dispatchers.Main) {
                val response = starlingBankApiService.transferToSavingsGoal(
                    "Bearer $ACCESS_TOKEN",
                    accountUid,
                    savingsGoalUid,
                    transferUid,
                    transferRequest
                )
                    .awaitResponse()
                val transferResponse = response.body()

                if (transferResponse != null && transferResponse.transferUid.isNotEmpty()) {
                    Result.success(transferResponse.success)
                } else {
                    Result.failure(IllegalArgumentException())
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getOneWeekPriorIsoDateTime(): String {
        val currentDateTime = ZonedDateTime.now()
        val oneWeekPriorDateTime = currentDateTime.minusWeeks(1)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return oneWeekPriorDateTime.format(formatter)
    }

    companion object {
        private const val ACCESS_TOKEN =
            "eyJhbGciOiJQUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_21Ty5KjMAz8lSnOoyneBG5z2x_YDxCWSFwDNmWbzE5t7b-vwRAClUuK7pZakqX8jaS1URPhKIF40B_Woemluraovj6EHqL3yE6tj6A4TbqizaDKKYG8LAVgceH5pyZiqigVPpj_jFGTVEmdplmdZ--RRBeIJKuKmUAh9KTcL90Tm9-SVu824xQueVZCXsUMl1QwFG1bJ5zknUD03k5_sQoZWRbncZamUOY4Z5QIdZUQtGl7Kds25SSufIYf61MItnavU3N2gSquK8jjooBaFImHIiPkuiuqeh5Y6JHnRwmdwm1pFRQO3BhGejsJ7mc8CZJYOdlJNke-l9YdmBUQGd9kwyTdAwTFORS3gR-RO_420vEbTu6mjbR-ZSAVybukCfsQ3GKPSqytCTQEQitndB8KzcyqadVJM6CTWoHuoJsUrQ2IyTo9bHPwgHLNHlAROm6Ie_Z9bHAJG9ihR9gID2dxw0vmiD_MmxTAahLAHgRywOvqGbT9E5xBZVHMPT9o6LXw0-_egQA9P8OZXbOM7mS_lQq1D9QSZViwHN0B2KMU9mHx7ldh4ar3Pg7cOuqBW3yemTBc55_9hcUuvvDaxWAqbkxTzwR-7P2MLDvnB5zGFY64nYn___sr8sekDT2VP7Jb3SP7Ih_0t3rwjucGQNj7mRqpC9TzTpdVnJcc_fsPk49eQLIEAAA.BesiCHq89FnA5KIortUdOARGRRPnjFGb6A4tKC59qDprmlmu0idRDbVgxyqBNj_spJq-wHx2cnJz_FBgw0x-CuCXpugl7TKaOcboEfXh5VRsbOZ5Mbrvlv4ha8M-KH_N_mpjflQXXWMNnUxe9HPrGevA_1bjnDYOLjv09ZIqyd8K9N54dzCXs7Nw2ztjhze4y3ffOMm2_Jsp3WuznTXDCYY8U7AD0wC1ZkYJ7-2WcJ3DC-tWPFyd6I9H35Z-bGS5LFdcs1YG9LN4jKcNWSoXjy1RWCHJabtssKMjOH3Uj0xTFIzMUnsVbj-hON__yqTELwUX36Ux_X2oalxf0GOYma_UozB0n105Fr5CQQtgHjtMa88VHoUAVex9p-okkqESOl-IjfzK-ewnr1ODe1QxjDfRp8VBrhLWs2rZAHzloFo8kX6rYjWjfSeFx6CTD-Cn632_o14LWnT5YolNxHdaMzwxa6BM4z5VaNDXmjMoZ_hZklpu6cZxih6sAEgBlw_YH4S_NY4_XKEOverJ3op1H7Nxigt1Oc0O-q8NWKc6--t6JHGM94hpShQTuMZdB3ym7eoBpFWQZODe-1kRmKbBN3TNjRgscJWF_hsGNOphLE9LDIeWXj2YIIUmlKggDTUAnkVDzYspwojrysHWYAtiTzRwpB-54OKRXnpjjRuzIys"
    }
}
