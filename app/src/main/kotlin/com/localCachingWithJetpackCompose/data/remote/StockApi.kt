package com.localCachingWithJetpackCompose.data.remote

import com.localCachingWithJetpackCompose.BuildConfig
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getStockListings(
        @Query("apikey") apiKey: String = BuildConfig.STOCK_API_KEY,
    ): ResponseBody

}