package com.localCachingWithJetpackCompose.data.repository

import com.localCachingWithJetpackCompose.data.csv.CSVParser
import com.localCachingWithJetpackCompose.data.local.StockDatabase
import com.localCachingWithJetpackCompose.data.mappers.toCompanyInfo
import com.localCachingWithJetpackCompose.data.mappers.toCompanyListing
import com.localCachingWithJetpackCompose.data.mappers.toCompanyListingEntity
import com.localCachingWithJetpackCompose.data.remote.StockApi
import com.localCachingWithJetpackCompose.domain.models.CompanyInfo
import com.localCachingWithJetpackCompose.domain.models.CompanyListing
import com.localCachingWithJetpackCompose.domain.models.IntraDayInfo
import com.localCachingWithJetpackCompose.domain.repository.StockRepository
import com.localCachingWithJetpackCompose.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    db: StockDatabase,
    private val api: StockApi,
    private val companyListingParser: CSVParser<CompanyListing>,
    private val intradayParser: CSVParser<IntraDayInfo>
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(fetchFromRemote: Boolean, query: String): Flow<Result<List<CompanyListing>>> = withContext(Dispatchers.IO) {
        flow {
            emit(Result.Loading(true))
            val localListings = dao.searchCompanyListings(query)
            emit(Result.Success(
                data = localListings.map {
                    it.toCompanyListing()
                }
            ))

            val isDbEmptyForBlankQuery = localListings.isEmpty() && query.isBlank()
            val shouldLoadFromRemote = fetchFromRemote || isDbEmptyForBlankQuery
            if (!shouldLoadFromRemote) {
                emit(Result.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val response = api.getStockListings()
                companyListingParser.parse(response.byteStream())
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                emit(Result.Error("No Internet!!"))
                null
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error("Developer error while parsing CSV!!"))
                null
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error("Something went wrong!!"))
                null
            }
            remoteListings?.let { listings ->
                dao.clearAllListings()
                dao.insertListings(listings.map { it.toCompanyListingEntity() })
                emit(
                    Result.Success(
                        data = dao.searchCompanyListings("")
                            .map {
                                it.toCompanyListing()
                            })
                )
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Result<CompanyInfo> = withContext(Dispatchers.IO) {
        try {
            val response = api.getCompanyOverview(symbol)
            return@withContext Result.Success(response.toCompanyInfo())
        } catch (e: IOException) {
            return@withContext Result.Error("Some error while parsing CSV!!")
        } catch (e: UnknownHostException) {
            return@withContext Result.Error("No Internet")
        } catch (e: Exception) {
            return@withContext Result.Error(e.message.toString())
        }
    }

    override suspend fun getCompanyIntraday(symbol: String): Result<List<IntraDayInfo>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getCompanyIntraday(symbol)
            return@withContext Result.Success(intradayParser.parse(response.byteStream()))
        } catch (e: IOException) {
            return@withContext Result.Error("Some error while parsing CSV!!")
        } catch (e: UnknownHostException) {
            return@withContext Result.Error("No Internet")
        } catch (e: Exception) {
            return@withContext Result.Error(e.message.toString())
        }
    }
}