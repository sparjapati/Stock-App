package com.localCachingWithJetpackCompose.domain.models

import java.time.LocalDateTime

data class IntraDayInfo(
    val dateTime: LocalDateTime,
    val close: Double
)
