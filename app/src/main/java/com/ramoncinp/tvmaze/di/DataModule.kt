package com.ramoncinp.tvmaze.di

import com.ramoncinp.tvmaze.data.provider.DateProviderImpl
import com.ramoncinp.tvmaze.domain.providers.DateProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun provideDateProvider(
        dateProvider: DateProviderImpl
    ): DateProvider
}
