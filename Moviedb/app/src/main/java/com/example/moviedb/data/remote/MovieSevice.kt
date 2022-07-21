package com.example.moviedb.data.remote

import com.example.moviedb.data.entities.MovieDetails
import com.example.moviedb.data.entities.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieSevice {

    @GET("3/movie/now_playing")
    suspend fun getPopularMovie(@Query("page") page: Int): Response<MovieResponse>

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") id: Int): Response<MovieDetails>
}
