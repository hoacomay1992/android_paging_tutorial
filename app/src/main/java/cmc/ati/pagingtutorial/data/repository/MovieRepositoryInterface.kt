package cmc.ati.pagingtutorial.data.repository

import androidx.paging.PagingData
import cmc.ati.pagingtutorial.data.model.Genre
import cmc.ati.pagingtutorial.data.model.Genres
import cmc.ati.pagingtutorial.data.model.MovieItem
import cmc.ati.pagingtutorial.utils.network.DataState
import kotlinx.coroutines.flow.Flow

interface MovieRepositoryInterface {
    fun nowPlayingPagingDataSource(genreId: String?): Flow<PagingData<MovieItem>>
    suspend fun genreList(): Flow<DataState<Genres>>
}