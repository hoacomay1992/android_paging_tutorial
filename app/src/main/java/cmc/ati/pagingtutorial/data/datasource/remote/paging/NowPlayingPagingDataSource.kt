package cmc.ati.pagingtutorial.data.datasource.remote.paging

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import cmc.ati.pagingtutorial.data.datasource.remote.ApiService
import cmc.ati.pagingtutorial.data.model.BaseModel
import cmc.ati.pagingtutorial.data.model.MovieItem
import timber.log.Timber

/**
 * Load và hiển thị paged data
 *
 * Thư viện phân trang cung cấp các khả năng mạnh mẽ để tải và hiển thị dữ liệu phân trang từ larger
 * dataset. Hướng dẫn này trình bày cách sử dụng thư viện Phân trang để thiết lập luồng dữ liệu phân
 * trang từ nguồn dữ liệu mạng và hiển thị nó trong RecyclerView.
 *
 * ## Định nghĩa một data source
 *
 * Bước đầu tiên là xác định cách triển khai PagingSource để xác định nguồn dữ liệu. Lớp API
 * PagingSource bao gồm phương thức Load() mà bạn ghi đè để chỉ ra cách truy xuất dữ liệu đã phân trang
 * từ nguồn dữ liệu tương ứng.
 *
 * Chọn key và value type
 *
 * Việc triển khai PagingSource điển hình sẽ chuyển các tham số được cung cấp trong hàm khởi tạo của nó tới phương thức Load()
 * để tải dữ liệu thích hợp cho truy vấn. Trong ví dụ trên, các tham số đó là:
 *      - apiService: một instance của backend service cung cấp dữ liệu
 *      - genreId: truy vấn tìm kiếm để gửi đến dịch vụ được chỉ định bởi backend
 * LoadParams object chứa thông tin về thao tác load sẽ được thực hiện. Điều này bao gồm key cần tải và số lượng mục (item) cần tải.
 * Đối tượng LoadResult chứa kết quả của thao tác tải. LoadResult là một lớp kín có một trong hai dạng, tùy thuộc vào lệnh gọi Load() có thành công hay không:
 *    -   If the load is successful, return a LoadResult.Page object.
 *    -   If the load is not successful, return a LoadResult.Error object.
 *
 * Việc triển khai PagingSource cũng phải triển khai phương thức getRefreshKey() lấy đối tượng PagingState làm tham số.
 * Nó trả về khóa để chuyển vào phương thức Load() khi dữ liệu được làm mới hoặc vô hiệu sau lần tải đầu tiên.
 * Thư viện phân trang sẽ tự động gọi phương thức này vào những lần làm mới dữ liệu tiếp theo.
 *
 * Handle errors
 * Yêu cầu tải dữ liệu có thể không thành công vì một số lý do, đặc biệt là khi tải qua mạng.
 * Báo cáo lỗi gặp phải trong quá trình tải bằng cách trả về đối tượng LoadResult.Error từ phương thức Load().
 * Ví dụ: bạn có thể phát hiện và báo cáo lỗi tải trong examplePagingSource từ ví dụ trước bằng cách thêm phần sau vào phương thức Load():
 */
class NowPlayingPagingDataSource(val apiService: ApiService, val genreId: String?) :
    PagingSource<Int, MovieItem>() {
    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        //Cố gắng tìm page key của trang gần nhất với vị trí neo từ prevKey hoặc nextKey; bạn cần xử lý nullability ở đây
        //prevKey == null -> anchorPage là trang đầu tiên
        //nextKey == null -> anchorPage là trang cuối cùng
        //cả prevKey và nextKey đều null -> anchorPage là trang đầu tiên initial page, nên trả về null

        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        try {
            //start refresh at page 1 if undefined
            val nextPageNumber = params.key ?: 1
            val movieList: BaseModel = apiService.nowPlayingMovieList(nextPageNumber, genreId)
            return LoadResult.Page(
                data = movieList.results,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1, //nếu prevKey =null tức là chỉ phân trang về phía trước.
                nextKey = if (movieList.results.isNotEmpty()) movieList.page + 1 else null
            )
        } catch (e: Exception) {
            Timber.e("Exception: ${e.message}")
            return LoadResult.Error(e)
        } catch (httpException: HttpException) {
            Timber.e("HttpException: ${httpException.message}")
            return LoadResult.Error(httpException)
        }
    }

}