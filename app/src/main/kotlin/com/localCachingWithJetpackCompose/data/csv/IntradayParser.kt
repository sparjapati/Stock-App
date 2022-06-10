package com.localCachingWithJetpackCompose.data.csv

import com.localCachingWithJetpackCompose.data.mappers.toIntraDayInfo
import com.localCachingWithJetpackCompose.data.remote.dto.IntraDayInfoDto
import com.localCachingWithJetpackCompose.domain.models.IntraDayInfo
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject

class IntradayParser @Inject constructor() : CSVParser<IntraDayInfo> {
    override suspend fun parse(stream: InputStream): List<IntraDayInfo> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val timeStamp = line.getOrNull(0) ?: return@mapNotNull null
                    val close = line.getOrNull(4) ?: return@mapNotNull null
                    IntraDayInfoDto(timeStamp, close.toDouble()).toIntraDayInfo()
                }
                .filter { info ->
                    info.dateTime.dayOfMonth == LocalDateTime.now().minusDays(1).dayOfMonth
                }
                .sortedBy { info ->
                    info.dateTime.hour
                }
                .also {
                    csvReader.close()
                }
        }
    }
}