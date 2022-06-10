package com.localCachingWithJetpackCompose.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.localCachingWithJetpackCompose.data.DataLayerConstants

@Entity(tableName = DataLayerConstants.STOCK_LISTINGS_TABLE_NAME)
data class CompanyListingEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DataLayerConstants.STOCK_LISTING_ID)
    val id: Int? = null,
    @ColumnInfo(name = DataLayerConstants.STOCK_LISTINGS_NAME)
    val name: String,
    @ColumnInfo(name = DataLayerConstants.STOCK_LISTING_SYMBOL)
    val symbol: String,
    @ColumnInfo(name = DataLayerConstants.STOCK_LISTING_EXCHANGE)
    val exchange: String
)
