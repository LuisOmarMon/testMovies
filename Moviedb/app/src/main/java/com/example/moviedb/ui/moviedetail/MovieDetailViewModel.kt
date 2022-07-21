package com.example.moviedb.ui.moviedetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.moviedb.data.entities.MovieDetails
import com.example.moviedb.data.repository.MovieRepository
import com.example.moviedb.utils.Resource

class MovieDetailViewModel @ViewModelInject constructor(
    private val moviesRepository: MovieRepository
) : ViewModel() {
    private val _id = MutableLiveData<Int>()
    private val _movie = _id.switchMap { id ->
        moviesRepository.getSingleMovie(id)
    }

    val movie: LiveData<Resource<MovieDetails>> = _movie

    fun start(id: Int) {
        _id.value = id
    }
}
