package cmc.ati.pagingtutorial.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import cmc.ati.pagingtutorial.data.datasource.remote.ApiService
import cmc.ati.pagingtutorial.data.datasource.remote.paging.NowPlayingPagingDataSource
import cmc.ati.pagingtutorial.data.model.MovieItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class MovieRepository @Inject constructor(private val apiService: ApiService) :
    MovieRepositoryInterface {
    override fun nowPlayingPagingDataSource(genreId: String?): Flow<PagingData<MovieItem>> =
        Pager(
            pagingSourceFactory = { NowPlayingPagingDataSource(apiService, genreId) },
            config = PagingConfig(pageSize = 1)//Định cấu hình cách tải dữ liệu bằng cách chuyển các thuộc tính bổ sung tới PagingConfig, chẳng hạn như prefetchDistance.
        ).flow
}