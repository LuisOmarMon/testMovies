package com.example.moviedb.ui.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.moviedb.BuildConfig
import com.example.moviedb.data.entities.MovieDetails
import com.example.moviedb.databinding.MovieDetailFragmentBinding
import com.example.moviedb.utils.Resource
import com.example.moviedb.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var binding: MovieDetailFragmentBinding by autoCleared()
    private val viewModel: MovieDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MovieDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt("id")?.let { viewModel.start(it) }
        setupObservers()
    }

    private fun setupObservers() {

        viewModel.movie.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    bindMovie(it.data!!)
                    binding.progressBar.visibility = View.GONE
                }

                Resource.Status.ERROR ->
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()

                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun bindMovie(movieDetails: MovieDetails) {
        binding.movieTitle.text = movieDetails.title
        binding.movieTagline.text = movieDetails.tagline
        binding.movieRuntime.text = movieDetails.runtime.toString()
        binding.movieReleaseDate.text = movieDetails.releaseDate
        binding.movieRating.text = movieDetails.rating.toString()
        binding.movieOverview.text = movieDetails.overview
        Glide.with(binding.root)
            .load(BuildConfig.IMAGE_BASE_URL.plus(movieDetails.posterPath))
            .into(binding.ivMoviePoster)
    }
}
