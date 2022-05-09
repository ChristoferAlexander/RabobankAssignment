package com.example.rabobankassignment.di

import com.example.rabobankassignment.api.NetworkResultCallAdapterFactory
import com.example.rabobankassignment.api.CsvApi
import com.example.rabobankassignment.api.CsvApi.Companion.API_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class ApiModule {

    protected open fun baseUrl() = API_URL.toHttpUrl()

    @Provides
    @Singleton
    fun provideAuthInterceptorOkHttpClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .build()


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
            .baseUrl(baseUrl())
            .build()

    @Provides
    @Singleton
    fun provideCsvApiService(retrofit: Retrofit): CsvApi.Service =
        retrofit.create(CsvApi.Service::class.java)
}