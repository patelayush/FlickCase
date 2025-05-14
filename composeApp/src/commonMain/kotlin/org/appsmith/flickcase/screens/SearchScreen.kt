package org.appsmith.flickcase.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.appsmith.flickcase.bottomContentPadding
import org.appsmith.flickcase.components.SearchBar
import org.appsmith.flickcase.components.MovieCard
import org.appsmith.flickcase.network.MovieApiClient
import org.appsmith.flickcase.viewmodel.HomeViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var onTyping by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var showSpinner by rememberSaveable { mutableStateOf(false) }

    if(!scrollState.canScrollForward && !homeViewModel.searchedContent.isEmpty() && !homeViewModel.reachedEndOfSearchedContent.value){
        showSpinner = true
    } else {
        showSpinner = false
    }

    LaunchedEffect(showSpinner){
        if(showSpinner){
            homeViewModel.loadMoreSearchedContent(searchQuery)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(top = 30.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { newQuery ->
                    searchQuery = newQuery
                    onTyping = true
                },
                onSearch = { query ->
                    homeViewModel.resetSearchContentState()
                    homeViewModel.searchContent(query)
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0, tween(100))
                    }
                    onTyping = false
                }
            )
            if (homeViewModel.searchedContent.isNotEmpty() && searchQuery.isNotBlank() && !onTyping) {
                Column(
                    Modifier.padding(top = 20.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                append("Results for ")
                            }
                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.tertiary,
                                )
                            ) {
                                append(searchQuery)
                            }
                        },
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Column(Modifier.padding(top = 20.dp).fillMaxWidth().verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            homeViewModel.searchedContent.forEach {
                                MovieCard(
                                    modifier = Modifier
                                        .width(180.dp)
                                        .height(200.dp),
                                    movie = it,
                                    configuration = homeViewModel.configuration.value,
                                    showImageLoader = homeViewModel.isContentDetailsLoading.value,
                                    onCardClick = {
                                        homeViewModel.getContent(it)
                                    }
                                )
                            }
                        }
                        if(showSpinner){
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.tertiary,
                                strokeWidth = 5.dp,
                                modifier = Modifier.padding(top = 15.dp).size(25.dp)
                            )
                        }
                        Spacer(Modifier.height(bottomContentPadding))
                    }
                }
            } else if (homeViewModel.searchedContent.isEmpty() && searchQuery.isNotBlank() && !homeViewModel.isLoading.value) {
                Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 20.dp)
                ) {
                    if (!onTyping) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                ) {
                                    append("No results found for ")
                                }
                                withStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.tertiary,
                                    )
                                ) {
                                    append(searchQuery)
                                }
                            },
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
        if (homeViewModel.isLoading.value && homeViewModel.searchedContent.isEmpty()) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.tertiary,
                strokeWidth = 5.dp,
                modifier = Modifier.align(Alignment.Center).size(50.dp)
            )
        }
    }
}

