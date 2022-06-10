package com.localCachingWithJetpackCompose.data.repository

import com.localCachingWithJetpackCompose.data.csv.CSVParser
import com.localCachingWithJetpackCompose.data.local.StockDatabase
import com.localCachingWithJetpackCompose.data.mappers.toCompanyListing
import com.localCachingWithJetpackCompose.data.mappers.toCompanyListingEntity
import com.localCachingWithJetpackCompose.data.remote.StockApi
import com.localCachingWithJetpackCompose.domain.models.CompanyListing
import com.localCachingWithJetpackCompose.domain.repository.StockRepository
import com.localCachingWithJetpackCompose.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    db: StockDatabase,
    private val api: StockApi,
    private val csvParser: CSVParser<CompanyListing>
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(fetchFromRemote: Boolean, query: String): Flow<Result<List<CompanyListing>>> = flow {
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
            csvParser.parse(response.byteStream())
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