package com.example.moviedb.data.remote

import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val movieService: MovieSevice
): BaseDataSource() {

    suspend fun getMovies(page: Int) = getResult { movieService.getPopularMovie(page) }
    suspend fun getMoviesDetail(id: Int) = getResult { movieService.getMovieDetails(id) }
}
