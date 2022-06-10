package com.localCachingWithJetpackCompose.di

import com.localCachingWithJetpackCompose.data.csv.CSVParser
import com.localCachingWithJetpackCompose.data.csv.CompanyListingParser
import com.localCachingWithJetpackCompose.data.csv.IntradayParser
import com.localCachingWithJetpackCompose.data.repository.StockRepositoryImpl
import com.localCachingWithJetpackCompose.domain.models.CompanyListing
import com.localCachingWithJetpackCompose.domain.models.IntraDayInfo
import com.localCachingWithJetpackCompose.domain.repository.StockRepository
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
    abstract fun bindCompanyListingParser(companyListingParser: CompanyListingParser): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindCompanyInfoParser(intradayParser: IntradayParser): CSVParser<IntraDayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(stockRepositoryImpl: StockRepositoryImpl): StockRepository
}