package com.localCachingWithJetpackCompose.presentation.companyInfoScreen

import com.localCachingWithJetpackCompose.domain.models.CompanyInfo
import com.localCachingWithJetpackCompose.domain.models.IntraDayInfo

data class CompanyInfoState(
    val stockInfos: List<IntraDayInfo> = emptyList(),
    val companyInfo: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
