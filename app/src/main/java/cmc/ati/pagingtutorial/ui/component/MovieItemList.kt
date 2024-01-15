package cmc.ati.pagingtutorial.ui.component

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import cmc.ati.pagingtutorial.data.datasource.remote.ApiURL
import cmc.ati.pagingtutorial.data.model.Genre
import cmc.ati.pagingtutorial.data.model.MovieItem
import cmc.ati.pagingtutorial.ui.theme.DefaultBackgroundColor
import cmc.ati.pagingtutorial.ui.theme.Purple40
import cmc.ati.pagingtutorial.ui.theme.Purple80
import cmc.ati.pagingtutorial.ui.theme.SecondaryFontColor
import cmc.ati.pagingtutorial.utils.conditional
import cmc.ati.pagingtutorial.utils.items
import cmc.ati.pagingtutorial.utils.pagingLoadingState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import kotlinx.coroutines.flow.Flow

@Composable
fun MovieItemList(
    movies: Flow<PagingData<MovieItem>>,
    genres: ArrayList<Genre>? = null,
    selectedName: Genre?,
    onclick: (genreId: Genre?) -> Unit
) {
    val activity = (LocalContext.current as? Activity)
    val progressBar = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }
    val moviesItems: LazyPagingItems<MovieItem> = movies.collectAsLazyPagingItems()

//    BackHandler(enabled = (currentRoute(navController) == Screen.Home.route)) {
//        openDialog.value = true
//    }
    Column(modifier = Modifier.background(DefaultBackgroundColor)) {
        genres?.let {
            LazyRow(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp, start = 9.dp, end = 9.dp)
                    .fillMaxWidth()
            ) {
                items(genres) { item ->
                    SelectableGenreChip(
                        selected = item.name === selectedName?.name,
                        genre = item.name,
                        onclick = {
                            onclick(item)
                        }
                    )
                }
            }
        }
        CircularIndeterminateProgressBar(isDisplayed = progressBar.value, 0.4f)
        LazyVerticalGrid(columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp)
                .conditional(genres == null) {
                    padding(top = 8.dp)
                },
            content = {
                items(moviesItems) { item ->
                    item?.let {
                        MovieItemView(item)
                    }
                }
            })
    }
//    if (openDialog.value) {
//        ExitAlertDialog(navController, {
//            openDialog.value = it
//        }, {
//            activity?.finish()
//        })
//
//    }
    moviesItems.pagingLoadingState {
        progressBar.value = it
    }
}


@Composable
fun MovieItemView(item: MovieItem) {
    Column(modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 0.dp, bottom = 10.dp)) {
        CoilImage(
            modifier = Modifier
                .size(250.dp),
            //.cornerRadius(10),
//                .clickable {
//                    navController.navigate(Screen.MovieDetail.route.plus("/${item.id}"))
//                },
            imageModel = { ApiURL.IMAGE_URL.plus(item.posterPath) },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                contentDescription = "Movie item",
                colorFilter = null,
            ),
            component = rememberImageComponent {
                +CircularRevealPlugin(
                    duration = 800
                )
                +ShimmerPlugin(
                    baseColor = SecondaryFontColor,
                    highlightColor = DefaultBackgroundColor
                )
            },
        )
    }
}

@Composable
fun SelectableGenreChip(
    selected: Boolean,
    genre: String,
    onclick: () -> Unit
) {

    val animateChipBackgroundColor by animateColorAsState(
        targetValue = if (selected) Purple80 else Purple40,
        animationSpec = tween(
            durationMillis = 50,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = ""
    )
    Box(
        modifier = Modifier
            .padding(end = 8.dp)
            // .cornerRadius(16)
            .background(
                color = animateChipBackgroundColor
            )
            .height(32.dp)
            .widthIn(min = 80.dp)
            .padding(horizontal = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onclick()
            }
    ) {
        Text(
            text = genre,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
            color = Color.White.copy(alpha = 0.80F)
        )
    }
}



