package com.marcoscg.movies.ui.details

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ActivityNavigator
import androidx.navigation.navArgs
import com.google.android.material.chip.Chip
import com.marcoscg.movies.R
import com.marcoscg.movies.base.BaseActivity
import com.marcoscg.movies.common.glide.load
import com.marcoscg.movies.common.utils.ColorUtils.darken
import com.marcoscg.movies.common.utils.gone
import com.marcoscg.movies.common.utils.visible
import com.marcoscg.movies.data.Resource
import com.marcoscg.movies.data.sources.remote.api.ApiClient
import com.marcoscg.movies.model.Genres
import com.marcoscg.movies.model.MovieExtended
import com.marcoscg.movies.ui.details.viewmodel.MovieDetailsViewModel
import kotlinx.android.synthetic.main.activity_movie_details.*

class MovieDetailsActivity : BaseActivity() {

    private val movieDetailsViewModel: MovieDetailsViewModel by lazy {
        ViewModelProvider(this).get(MovieDetailsViewModel::class.java)
    }

    private val args: MovieDetailsActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        setupToolbar()
        clearStatusBar()

        iv_activity_movie_details.transitionName = args.id.toString()

        if (movieDetailsViewModel.getSingleMovie().value == null)
            movieDetailsViewModel.fetchSingleMovie(args.id.toString())

        movieDetailsViewModel.getSingleMovie().observe(this, Observer {
            handleSingleMovieDataState(it)
        })

        postponeEnterTransition()
    }

    override fun finish() {
        super.finish()
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }

    private fun handleSingleMovieDataState(state: Resource<MovieExtended>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                progress_bar.visible()
            }
            Resource.Status.SUCCESS -> {
                progress_bar.gone()
                loadMovieData(state.data)
            }
            Resource.Status.ERROR -> {
                progress_bar.gone()
                Toast.makeText(this, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun loadMovieData(data: MovieExtended?) {
        data?.let {
            title = data.title
            detail_description.text = data.overview
            fillGenres(data.genres)
        }

        iv_activity_movie_details.load(ApiClient.POSTER_BASE_URL + args.posterPath) { color ->
            window?.statusBarColor = color.darken
            collapsing_toolbar.setBackgroundColor(color)
            collapsing_toolbar.setContentScrimColor(color)
            startPostponedEnterTransition()
        }
    }

    private fun fillGenres(genres: List<Genres>) {
        for (g in genres) {
            val chip = Chip(this)
            chip.text = g.name
            genres_chip_group.addView(chip)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}