package com.localCachingWithJetpackCompose.presentation.companyListingScreen

import com.localCachingWithJetpackCompose.domain.models.CompanyListing

data class CompanyListingState(
    val companies: List<CompanyListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val errorMessage: String? = null
)