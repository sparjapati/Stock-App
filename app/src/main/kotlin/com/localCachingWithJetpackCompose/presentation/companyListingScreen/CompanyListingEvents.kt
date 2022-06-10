package com.localCachingWithJetpackCompose.presentation.companyListingScreen

sealed class CompanyListingEvents {
    object Refresh : CompanyListingEvents()
    data class OnSearchQueryChange(val query: String) : CompanyListingEvents()
}
