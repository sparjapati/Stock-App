package com.localCachingWithJetpackCompose.domain.repository

import com.localCachingWithJetpackCompose.domain.models.CompanyListing
import com.localCachingWithJetpackCompose.utils.Result
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Result<List<CompanyListing>>>
}