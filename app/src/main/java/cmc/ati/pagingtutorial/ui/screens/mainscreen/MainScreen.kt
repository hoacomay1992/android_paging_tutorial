package cmc.ati.pagingtutorial.ui.screens.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import cmc.ati.pagingtutorial.data.model.Genre
import cmc.ati.pagingtutorial.data.model.Genres
import cmc.ati.pagingtutorial.ui.component.CircularIndeterminateProgressBar
import cmc.ati.pagingtutorial.ui.screens.nowplaying.NowPlaying
import cmc.ati.pagingtutorial.utils.AppConstant
import cmc.ati.pagingtutorial.utils.network.DataState
import cmc.ati.pagingtutorial.utils.pagingLoadingState

@Composable
fun MainScreen() {
    val mainViewMode = hiltViewModel<MainScreenViewModel>()
    val genreList = remember { mutableStateOf(arrayListOf<Genre>()) }
    val isProgressBar = remember { mutableStateOf(false) }

    //genre api call first time
    LaunchedEffect(key1 = 0) {
        mainViewMode.genreList()
    }
    if (mainViewMode.genres.value is DataState.Success<Genres>) {
        genreList.value =
            (mainViewMode.genres.value as DataState.Success<Genres>).data.genres as ArrayList
        if (genreList.value.first().name != AppConstant.DEFAULT_GENRE_ITEM)
            genreList.value.add(0, Genre(null, AppConstant.DEFAULT_GENRE_ITEM))
    }
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularIndeterminateProgressBar(isDisplayed = isProgressBar.value, 0.1f)
            NowPlaying(
                isDisplayed = genreList.value.isNotEmpty(),
                genres = genreList.value
            )
        }

    }
    mainViewMode.genres.pagingLoadingState { isProgressBar.value = it }
}