# Tổng quan về Paging library

- Paging library giúp bạn tải và hiển thị các trang dữ liệu từ larger dataset lấy từ local storage
  hoặc network. Cách tiếp cận này cho phép ứng dụng của bạn sử dụng cả băng thông mạng và tài nguyên
  hệ thống hiệu quả hơn. Các thành phần của thư viện Paging được thiết kế để phù hợp với
  [Android app architeture](https://developer.android.com/topic/architecture) được đề xuất, tích hợp
  hoàn hảo với các thành phần Jetpack khác và cung cấp khả năng hỗ trợ Kotlin hạng nhất
  (first-class).

## Lợi ích của việc sử dụng Paging library

Paging library bao gồm các tính năng sau:

- lưu trong bộ nhớ đêm cho dữ liệu phân trang của bạn. Điều này giúp đảm bảo rằng ứng dụng của bạn
  sử dụng tài nguyên hệ thống một cách hiệu quả khi làm việc với dữ liệu được phân trang.
- Tính năng chống trùng lặp yêu cầu được tích hợp sẵn, giúp đảm bảo rằng ứng dụng của bạn sử dụng
  băng thông mạng và tài nguyên hệ thống một cách hiệu quả.
- Configurable RecyclerView adapter có thể định cấu hình tự động yêu cầu dữ liệu khi người dùng cuộn
  về cuối dữ liệu đã tải
- First-class support cho các coroutine và flows Kotlin cũng như LiveData và RxJava.
- Hỗ trợ tích hợp để xử lý lỗi, bao gồm khả năng làm mới (refresh) và thử lại (retry capabilities).

## Setup

Để import paging component vào ứng dụng Android, hãy thêm các phần phụ thuộc sau vào tệp
build.gradle của ứng dụng:

```
dependencies {
val paging_version = "3.2.1"

implementation("androidx.paging:paging-runtime:$paging_version")

// alternatively - without Android dependencies for tests
testImplementation("androidx.paging:paging-common:$paging_version")

// optional - RxJava2 support
implementation("androidx.paging:paging-rxjava2:$paging_version")

// optional - RxJava3 support
implementation("androidx.paging:paging-rxjava3:$paging_version")

// optional - Guava ListenableFuture support
implementation("androidx.paging:paging-guava:$paging_version")

// optional - Jetpack Compose integration
implementation("androidx.paging:paging-compose:3.3.0-alpha02")
}
```

## Library architecture

Các thành phần của thư viện Paging hoạt động trong ba tầng ứng dụng của bạn:

- The repository layer
- The ViewModel layer
- The UI layer

![](images/paging_architecture.png)
Phần này mô tả các thành phần thư viện phân trang hoạt động ở mỗi lớp và cách chúng phối hợp với
nhau để tải và hiển thị dữ liệu phân trang.

### Repository layer

Các paging library component chính trong repository layer
là [PagingSource](https://developer.android.com/reference/kotlin/androidx/paging/PagingSource). Mỗi
object PagingSource xác định một nguồn dữ liệu và cách truy xuất dữ liệu từ nguồn đó. Đối tượng
PagingSource có thể tải dữ liệu từ bất kỳ nguồn nào, bao gồm cả nguồn từ network và local database.

Một paging library component khác mà bạn có thể sử dụng là RemoteMediator. Đối tượng
RemoteMediator xử lý phân trang từ layer data source, chẳng hạn như nguồn dữ liệu network có bộ
nhớ đệm cơ sở dữ liệu cục bộ (local database cache)..

### ViewModel layer

Thành phần [Pager](https://developer.android.com/reference/kotlin/androidx/paging/Pager) cung cấp
public API để xây dựng các instance PagingData được exposed trong các luồng phản ứng (reactive
streans), dựa trên đối tượng PagingSource và đối tượng cấu
hình [PagingConfig](https://developer.android.com/reference/kotlin/androidx/paging/PagingConfig).
Thành phần kết nối lớp ViewModel với giao diện người dùng là PagingData. Đối
tượng [PagingData](https://developer.android.com/reference/kotlin/androidx/paging/PagingData) là nơi
chứa snapshot dữ liệu được phân trang. Nó truy vấn một đối tượng PagingSource và lưu trữ kết quả.

### UI layer

Paging library component chính trong UI layer
là [PagingDataAdapter](https://developer.android.com/reference/kotlin/androidx/paging/PagingDataAdapter),
một [RecyclerView](https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView)
adapter xử lý dữ liệu được phân trang.
Ngoài ra, bạn có thể sử dụng thành
phần [AsyncPagingDataDiffer](https://developer.android.com/reference/kotlin/androidx/paging/AsyncPagingDataDiffer)
đi kèm để xây dựng bộ điều hợp tùy chỉnh của riêng mình.

# Load và hiển thị paged data

Thư viện phân trang cung cấp các khả năng mạnh mẽ để tải và hiển thị dữ liệu phân trang từ larger
dataset. Hướng dẫn này trình bày cách sử dụng thư viện Phân trang để thiết lập luồng dữ liệu phân
trang từ nguồn dữ liệu mạng và hiển thị nó trong RecyclerView.

## Định nghĩa một data source

Bước đầu tiên là xác định cách triển khai PagingSource để xác định nguồn dữ liệu. Lớp API
PagingSource bao gồm phương thức Load() mà bạn ghi đè để chỉ ra cách truy xuất dữ liệu đã phân trang
từ nguồn dữ liệu tương ứng.