package com.ramoncinp.tvmaze.di

import com.ramoncinp.tvmaze.domain.repository.TvMazeRepository
import com.ramoncinp.tvmaze.data.repository.TvMazeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindTvMazeRepository(
        repository: TvMazeRepositoryImpl
    ): TvMazeRepository
}
