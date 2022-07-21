package com.example.moviedb.di

import android.content.Context
import com.example.moviedb.BuildConfig.API_KEY
import com.example.moviedb.BuildConfig.BASE_URL
import com.example.moviedb.data.local.AppDatabase
import com.example.moviedb.data.local.MovieDAO
import com.example.moviedb.data.remote.MovieRemoteDataSource
import com.example.moviedb.data.remote.MovieSevice
import com.example.moviedb.data.repository.MovieRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    val requestInterceptor = Interceptor { chain ->
        val url = chain.request()
            .url()
            .newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .addQueryParameter("language", "es")
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        return@Interceptor chain.proceed(request)
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(requestInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieSevice = retrofit.create(MovieSevice::class.java)

    @Singleton
    @Provides
    fun provideMovieRemoteDataSource(movieService: MovieSevice) = MovieRemoteDataSource(movieService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideMovieDao(db: AppDatabase) = db.movieDao()


    @Singleton
    @Provides
    fun provideMovieRepository(remoteDataSource: MovieRemoteDataSource,
                          localDataSource: MovieDAO) =
        MovieRepository(remoteDataSource, localDataSource)
}
