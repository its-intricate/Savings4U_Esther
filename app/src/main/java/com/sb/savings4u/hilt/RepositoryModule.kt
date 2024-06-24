package com.sb.savings4u.hilt

import com.sb.savings4u.data.repository.AccountsRepositoryImpl
import com.sb.savings4u.domain.repository.AccountsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideAccountsRepository(
        accountsRepository: AccountsRepositoryImpl
    ): AccountsRepository {
        return accountsRepository
    }
}