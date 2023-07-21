package com.ilhomsoliev.rikandmortytest.di

import com.ilhomsoliev.rikandmortytest.data.network.BASE_URL
import com.ilhomsoliev.rikandmortytest.data.network.ServerApi
import com.ilhomsoliev.rikandmortytest.data.repository.Repository
import com.ilhomsoliev.rikandmortytest.domain.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
/*
    @Binds
    fun bindRepository(repositoryImpl: RepositoryImpl): Repository*/

    companion object {

        @Singleton
        @Provides
        fun providesRepository(api: ServerApi):Repository{
            return RepositoryImpl(api)
        }

        @Singleton
        @Provides
        fun provideServerApi(): ServerApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .readTimeout(120, TimeUnit.SECONDS)
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .build()
                )
                .build()
                .create(ServerApi::class.java)
        }
    }
}