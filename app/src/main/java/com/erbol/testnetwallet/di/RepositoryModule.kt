package com.erbol.testnetwallet.di

import com.erbol.testnetwallet.data.repository.AuthRepositoryImpl
import com.erbol.testnetwallet.data.repository.WalletRepositoryImpl
import com.erbol.testnetwallet.domain.repository.AuthRepository
import com.erbol.testnetwallet.domain.repository.WalletRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindWalletRepository(
        impl: WalletRepositoryImpl
    ): WalletRepository
}
