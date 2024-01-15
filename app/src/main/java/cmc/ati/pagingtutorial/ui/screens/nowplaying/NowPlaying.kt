package cmc.ati.pagingtutorial.ui.screens.nowplaying

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import cmc.ati.pagingtutorial.data.model.Genre
import cmc.ati.pagingtutorial.data.model.GenreId
import cmc.ati.pagingtutorial.ui.component.MovieItemList

@Composable
fun NowPlaying(
    isDisplayed: Boolean = false, genres: ArrayList<Genre>? = null
) {
    if (isDisplayed) {
        val nowPlayingViewModel = hiltViewModel<NowPlayingViewModel>()
//        Column {
//          //  Text(text = if (genres?.isNotEmpty() == true) genres.first().name else "Text")
//
//        }
//        LazyColumn {
//            genres?.let {
//                items(genres) { genre ->
//                    AssistChip(
//                        onClick = { },
//                        label = { Text(text = "${genre.name}") },
//                        leadingIcon = {
//                            Icon(
//                                Icons.Filled.Done,
//                                modifier = Modifier.size(AssistChipDefaults.IconSize),
//                                contentDescription = "Localized description"
//                            )
//                        })
//                }
//            }
//
//        }

        val nowPlayViewModel = hiltViewModel<NowPlayingViewModel>()
        MovieItemList(
            movies = nowPlayViewModel.nowPlayingMovies,
            genres = genres,
            selectedName = nowPlayViewModel.selectedGenre.value
        ) {
            nowPlayViewModel.filterData.value = GenreId(it?.id.toString())
            it?.let {
                nowPlayViewModel.selectedGenre.value = it
            }
        }
    }

}