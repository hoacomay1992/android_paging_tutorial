package cmc.ati.pagingtutorial.screens.nowplaying

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import cmc.ati.pagingtutorial.data.model.Genre
import cmc.ati.pagingtutorial.data.model.GenreId
import cmc.ati.pagingtutorial.data.repository.MovieRepository
import cmc.ati.pagingtutorial.utils.AppConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

/**
 * Bước 2, bạn cần một stream của paged data từ quá trình triển khai PagingSource. Thiết lập data stream này trong ViewModel.
 * Lớp Pager cung cấp các phương thức hiển thị luồng phản ứng của các đối tượng PagingData từ PagingSource.
 * Thư viện Paging hỗ trợ sử dụng một số loại stream, bao gồm Flow, LiveData cũng như các loại Flowable và Observable từ RxJava.
 *
 * Khi tạo Pager instance để thiết lập luồng phản ứng (reactive stream), bạn phải cung cấp cho instance đó một đối tượng cấu hình PagingConfig
 * và một hàm cho Pager biết cách lấy một phiên bản triển khai PagingSource của bạn:
 *
 * Toán tử cachedIn() giúp luồng dữ liệu có thể chia sẻ được và lưu vào bộ nhớ đệm dữ liệu đã tải bằng CoroutineScope được cung cấp.
 * Ví dụ này sử dụng viewModelScope được cung cấp bởi cấu phần phần mềm lifecycle-viewmodel-ktx
 *
 * Đối tượng Pager gọi phương thức Load() từ đối tượng PagingSource, cung cấp cho nó đối tượng LoadParams và nhận lại đối tượng LoadResult.
 */
@HiltViewModel
class NowPlayingViewModel @Inject constructor(private val repo: MovieRepository) : ViewModel() {

    var selectedGenre = mutableStateOf(Genre(null, AppConstant.DEFAULT_GENRE_ITEM))
    val filterData = MutableStateFlow<GenreId?>(null)

    val nowPlayingData = filterData.flatMapLatest { genreId ->
        repo.nowPlayingPagingDataSource(genreId?.genreId)
    }.cachedIn(viewModelScope)
}