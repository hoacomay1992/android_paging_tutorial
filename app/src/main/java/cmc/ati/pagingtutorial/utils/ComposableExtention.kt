package cmc.ati.pagingtutorial.utils

import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import cmc.ati.pagingtutorial.utils.network.DataState

fun <T : Any> LazyGridScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    itemContent: @Composable LazyGridItemScope.(value: T?) -> Unit
) {
    items(lazyPagingItems.itemCount) { index ->
        itemContent(lazyPagingItems[index])

    }
}

@Composable
fun <T : Any> LazyPagingItems<T>.pagingLoadingState(isLoading: (pagingState: Boolean) -> Unit) {
    this.apply {
        when {
            // dữ liệu được tải lần đầu tiên
            loadState.refresh is LoadState.Loading -> {
                isLoading(true)
            }
            // dữ liệu đang được tải lần thứ hai hoặc phân trang
            loadState.append is LoadState.Loading -> {
                isLoading(true)
            }

            loadState.refresh is LoadState.NotLoading -> {
                isLoading(false)
            }

            loadState.append is LoadState.NotLoading -> {
                isLoading(false)
            }
        }
    }
}

fun <T : Any> MutableState<DataState<T>?>.pagingLoadingState(isLoading: (pagingState: Boolean) -> Unit) {
    when (this.value) {
        is DataState.Success<T> -> {
            isLoading(false)
        }

        is DataState.Loading -> {
            isLoading(true)
        }

        is DataState.Error -> {
            isLoading(false)
        }

        else -> {}
    }
}

fun Modifier.conditional(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}