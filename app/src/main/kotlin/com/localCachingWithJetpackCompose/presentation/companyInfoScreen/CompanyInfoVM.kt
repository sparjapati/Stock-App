package com.localCachingWithJetpackCompose.presentation.companyInfoScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.localCachingWithJetpackCompose.domain.repository.StockRepository
import com.localCachingWithJetpackCompose.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CompanyInfoVM @Inject constructor(
    // directly passes the navigation arguments to viewModel
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
) : ViewModel() {
    var state by mutableStateOf(CompanyInfoState())
        private set

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            val intradayResult = async { repository.getCompanyIntraday(symbol) }
            val companyInfo = async { repository.getCompanyInfo(symbol) }

            when (val result = companyInfo.await()) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    withContext(Dispatchers.Main) {
                        state = state.copy(companyInfo = result.data)
                    }
                }
                is Result.Error -> {
                    state = state.copy(errorMessage = result.message, companyInfo = null)
                }
            }
            when (val result = intradayResult.await()) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    withContext(Dispatchers.Main) {
                        state = state.copy(stockInfos = result.data!!)
                    }
                }
                is Result.Error -> {
                    state = state.copy(errorMessage = result.message, stockInfos = emptyList())
                }
            }
            state = state.copy(isLoading = false)
        }
    }
}