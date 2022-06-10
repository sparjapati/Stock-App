package com.localCachingWithJetpackCompose.presentation.companyListingScreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(start = true)
fun CompanyListingScreen(
    navigator: DestinationsNavigator,
    vm: CompanyListingVM = hiltViewModel()
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = vm.state.isRefreshing)
    val state = vm.state
    state.errorMessage?.let {
        Log.d("abcd", "CompanyListingScreen: $it")
    }
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = vm.state.searchQuery,
            onValueChange = {
                vm.onEvent(CompanyListingEvents.OnSearchQueryChange(it))
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Search...")
            },
            maxLines = 1,
            singleLine = true
        )

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                vm.onEvent(CompanyListingEvents.Refresh)
            }) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.companies.size) { i ->
                    val item = state.companies[i]
                    CompanyItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                Log.d("abcd", "CompanyListingScreen: $item")
                            }
                            .padding(16.dp),
                        data = item
                    )
                    if (i < state.companies.size) {
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}