package com.example.jetpackapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import com.example.jetpackapp.domain.model.Character
import com.example.jetpackapp.ui.vm.CharacterViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CharactersScreen(
    characterViewModel: CharacterViewModel = hiltViewModel()
) {
    val lazyCharacterItems: LazyPagingItems<Character> =
        characterViewModel.charactersFlow.collectAsLazyPagingItems()

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = lazyCharacterItems.loadState.refresh is LoadState.Loading
    )

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            lazyCharacterItems.refresh()
        }) {

        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            items(lazyCharacterItems) { character ->
                character?.let {
                    CharacterItem(character = character)
                }
            }

            lazyCharacterItems.apply {
                when {
                    loadState.append is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    loadState.refresh is LoadState.Error -> {
                        val e = lazyCharacterItems.loadState.refresh as LoadState.Error
                        if (lazyCharacterItems.itemCount <= 0) {
                            item {
                                ErrorItem(
                                    message = e.error.localizedMessage!!,
                                    modifier = Modifier.fillParentMaxSize(),
                                    onClickRetry = { retry() }
                                )
                            }
                        }
                        else {
                            item {
                                ErrorItem(
                                    message = e.error.localizedMessage!!,
                                    onClickRetry = { retry() }
                                )
                            }
                        }
                    }
                    loadState.append is LoadState.Error -> {
                        val e = lazyCharacterItems.loadState.append as LoadState.Error
                        item {
                            ErrorItem(
                                message = e.error.localizedMessage!!,
                                onClickRetry = { retry() }
                            )
                        }
                    }
                    loadState.refresh is LoadState.Loading -> {
                        if (lazyCharacterItems.itemCount == 0) {
                            item { LoadingItem() }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(character: Character) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { },
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = character.name, modifier = Modifier.fillMaxHeight())
            Image(
                painter = rememberAsyncImagePainter(character.image), contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

@Composable
fun LoadingItem() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(onClick = onClickRetry) {
            Text(text = "Something went wrong, try again!")
        }
    }
}

@Preview
@Composable
fun PreviewCharactersScreen() {
    CharactersScreen()
}