package cmc.ati.pagingtutorial.di

import cmc.ati.pagingtutorial.data.datasource.remote.ApiService
import cmc.ati.pagingtutorial.data.datasource.remote.ApiURL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /**
     * Provides baseUrl as String
     */
    @Singleton
    @Provides
    fun provideBaseURL(): String {
        return ApiURL.BASE_URL
    }

    /**
     * Provides converter factory for retrofit
     */
    @Singleton
    @Provides
    fun providesConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    /**
     * Provides LoggingInterceptor for api information
     */
    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    /**
     * Provies custom OkHttp
     */

    @Singleton
    @Provides
    fun providesOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
        okHttpClient.callTimeout(40, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(40, TimeUnit.SECONDS)
        okHttpClient.readTimeout(40, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(40, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(loggingInterceptor)
        okHttpClient.build()
        return okHttpClient.build()
    }

    /**
     * Provides Api Service using retrofit
     */
    @Singleton
    @Provides
    fun provideRetrofitClient(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        convertFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(convertFactory)
            .build()
    }

    /**
     * Provides Api Service using retrofit
     */
    @Singleton
    @Provides
    fun providesRestApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}