package cmc.ati.pagingtutorial.ui.screens.mainscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmc.ati.pagingtutorial.data.model.Genres
import cmc.ati.pagingtutorial.data.repository.MovieRepository
import cmc.ati.pagingtutorial.utils.network.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val repo: MovieRepository) : ViewModel() {
    val genres: MutableState<DataState<Genres>?> = mutableStateOf(null)
    fun genreList() {
        viewModelScope.launch {
            repo.genreList().onEach {
                genres.value = it
            }.launchIn(viewModelScope)
        }
    }
}