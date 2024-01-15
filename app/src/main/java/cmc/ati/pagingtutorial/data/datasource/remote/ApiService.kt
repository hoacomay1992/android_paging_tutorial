package cmc.ati.pagingtutorial.data.datasource.remote

import cmc.ati.pagingtutorial.data.model.BaseModel
import cmc.ati.pagingtutorial.data.model.Genres
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/now_playing")
    suspend fun nowPlayingMovieList(
        @Query("page") page: Int,
        @Query("with_genres") genreId: String?,
        @Query("api_key") api_key: String = ApiURL.API_KEY
    ): BaseModel

    @GET("genre/movie/list")
    suspend fun genreList(@Query("api_key") api_key: String = ApiURL.API_KEY): Genres
}