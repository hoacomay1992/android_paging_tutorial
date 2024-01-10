package cmc.ati.pagingtutorial.data.repository

import androidx.paging.PagingData
import cmc.ati.pagingtutorial.data.model.MovieItem
import kotlinx.coroutines.flow.Flow

interface MovieRepositoryInterface {
    fun nowPlayingPagingDataSource(genreId: String?): Flow<PagingData<MovieItem>>
}