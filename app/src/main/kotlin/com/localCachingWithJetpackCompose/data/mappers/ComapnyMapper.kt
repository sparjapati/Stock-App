package com.localCachingWithJetpackCompose.data.mappers

import com.localCachingWithJetpackCompose.data.local.CompanyListingEntity
import com.localCachingWithJetpackCompose.domain.models.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing = CompanyListing(
    name = name,
    symbol = symbol,
    exchange = exchange
)

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity = CompanyListingEntity(
    name = name,
    symbol = symbol,
    exchange = exchange
)