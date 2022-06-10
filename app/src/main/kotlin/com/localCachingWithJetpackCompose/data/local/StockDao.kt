package com.localCachingWithJetpackCompose.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.localCachingWithJetpackCompose.data.DataLayerConstants

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListings(listings: List<CompanyListingEntity>)

    @Query("DELETE FROM ${DataLayerConstants.STOCK_LISTINGS_TABLE_NAME}")
    suspend fun clearAllListings()

    @Query(
        """
        SELECT *
        FROM ${DataLayerConstants.STOCK_LISTINGS_TABLE_NAME}
        WHERE LOWER(${DataLayerConstants.STOCK_LISTINGS_NAME}) LIKE  '%' || LOWER(:query) || '%' 
            OR
            UPPER(:query) == ${DataLayerConstants.STOCK_LISTING_SYMBOL}
    """
    )
    suspend fun searchCompanyListings(query: String): List<CompanyListingEntity>

}