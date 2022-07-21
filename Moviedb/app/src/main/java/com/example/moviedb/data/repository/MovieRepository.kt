package com.example.moviedb.data.repository

import com.example.moviedb.data.local.MovieDAO
import com.example.moviedb.data.remote.MovieRemoteDataSource
import com.example.moviedb.utils.performGetOperation
import com.example.moviedb.utils.performGetOperationWDB
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource,
    private val localDataSource: MovieDAO
) {

    fun getMovies(page: Int) = performGetOperation(
        databaseQuery = { localDataSource.getAllMovies() },
        networkCall = { remoteDataSource.getMovies(page) },
        saveCallResult = { localDataSource.insertAll(it.movieList) }
    )

    fun getSingleMovie(id: Int) = performGetOperationWDB(
        networkCall = { remoteDataSource.getMoviesDetail(id) }
    )
}
