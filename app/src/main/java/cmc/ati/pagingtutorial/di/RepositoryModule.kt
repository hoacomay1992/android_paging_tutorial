package cmc.ati.pagingtutorial.di

import cmc.ati.pagingtutorial.data.datasource.remote.ApiService
import cmc.ati.pagingtutorial.data.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    /**
     * Provides RemoteDataRepository for access api service method.
     */
    @Singleton
    @Provides
    fun provideMovieRepository(apiService: ApiService): MovieRepository {
        return MovieRepository(apiService)
    }
}