package com.localCachingWithJetpackCompose.di

import android.app.Application
import androidx.room.Room
import com.localCachingWithJetpackCompose.data.DataLayerConstants
import com.localCachingWithJetpackCompose.data.local.StockDatabase
import com.localCachingWithJetpackCompose.data.remote.StockApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStockApi(): StockApi = Retrofit
        .Builder()
        .baseUrl(DataLayerConstants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(
            OkHttpClient
                .Builder()
                .connectTimeout(30,TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        )
        .build()
        .create(StockApi::class.java)

    @Provides
    @Singleton
    fun provideStockDatabase(app: Application): StockDatabase = Room.databaseBuilder(
        app.applicationContext,
        StockDatabase::class.java,
        DataLayerConstants.STOCK_DATABASE_NAME
    ).build()
}