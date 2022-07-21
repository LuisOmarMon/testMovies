package com.example.moviedb.ui.movies

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.moviedb.data.repository.MovieRepository
import com.example.moviedb.constants.Constants.PAGE

class MoviesViewModel @ViewModelInject constructor(
    moviesRepository: MovieRepository
) : ViewModel() {

    val movies = moviesRepository.getMovies(PAGE)
}
