package com.localCachingWithJetpackCompose.data.remote

import com.localCachingWithJetpackCompose.BuildConfig
import com.localCachingWithJetpackCompose.data.remote.dto.CompanyInfoDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getStockListings(
        @Query("apikey") apiKey: String = BuildConfig.STOCK_API_KEY,
    ): ResponseBody

    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getCompanyIntraday(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = BuildConfig.STOCK_API_KEY,
    ): ResponseBody

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyOverview(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = BuildConfig.STOCK_API_KEY,
    ): CompanyInfoDto
}