package com.localCachingWithJetpackCompose.data.mappers

import com.localCachingWithJetpackCompose.data.remote.dto.IntraDayInfoDto
import com.localCachingWithJetpackCompose.domain.models.IntraDayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun IntraDayInfoDto.toIntraDayInfo(): IntraDayInfo {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val dateTime = LocalDateTime.parse(timestamp, formatter)
    return IntraDayInfo(
        dateTime = dateTime,
        close = close
    )
}