package com.ramoncinp.tvmaze.di

import com.ramoncinp.tvmaze.data.service.TV_MAZE_BASE_URL
import com.ramoncinp.tvmaze.data.service.TvMazeService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideKotlinJsonAdapterFactory(): KotlinJsonAdapterFactory = KotlinJsonAdapterFactory()

    @Provides
    @Singleton
    fun provideMoshi(kotlinJsonAdapterFactory: KotlinJsonAdapterFactory): Moshi = Moshi.Builder()
        .add(kotlinJsonAdapterFactory)
        .build()

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)

    @Provides
    fun provideRetrofit(converterFactory: MoshiConverterFactory): Retrofit = Retrofit.Builder()
        .baseUrl(TV_MAZE_BASE_URL)
        .addConverterFactory(converterFactory)
        .build()

    @Provides
    fun provideTVMazeService(retrofit: Retrofit): TvMazeService = retrofit.create(TvMazeService::class.java)
}
