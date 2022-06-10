package com.localCachingWithJetpackCompose.presentation.companyListingScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.localCachingWithJetpackCompose.domain.repository.StockRepository
import com.localCachingWithJetpackCompose.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CompanyListingVM @Inject constructor(private val repository: StockRepository) : ViewModel() {

    var state by mutableStateOf(CompanyListingState())
    var searchJob: Job? = null

    init {
        getCompanyListings("", true)
    }

    fun onEvent(events: CompanyListingEvents) {
        when (events) {
            is CompanyListingEvents.Refresh -> {
                getCompanyListings("", true)
            }
            is CompanyListingEvents.OnSearchQueryChange -> {
                state = state.copy(searchQuery = events.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500)
                    getCompanyListings(searchQuery = events.query, false)
                }
            }
        }
    }

    private fun getCompanyListings(
        searchQuery: String = state.searchQuery.lowercase(Locale.ROOT),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCompanyListings(fetchFromRemote, searchQuery)
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            withContext(Dispatchers.Main) {
                                state = state.copy(isLoading = result.isLoading)
                            }
                        }
                        is Result.Success -> {
                            result.data?.let { listings ->
                                withContext(Dispatchers.Main) {
                                    state = state.copy(companies = listings)
                                }
                            }
                        }
                        is Result.Error -> {
                            withContext(Dispatchers.Main) {
                                state = state.copy(errorMessage = result.message!!)
                            }
                        }
                    }
                }

        }
    }

}