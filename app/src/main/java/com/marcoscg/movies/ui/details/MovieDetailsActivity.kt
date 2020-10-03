package com.marcoscg.movies.ui.details

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ActivityNavigator
import androidx.navigation.navArgs
import com.google.android.material.chip.Chip
import com.marcoscg.movies.R
import com.marcoscg.movies.base.BaseActivity
import com.marcoscg.movies.common.glide.load
import com.marcoscg.movies.common.utils.ColorUtils.darken
import com.marcoscg.movies.common.utils.TimeUtils
import com.marcoscg.movies.common.utils.gone
import com.marcoscg.movies.common.utils.orNa
import com.marcoscg.movies.common.utils.visible
import com.marcoscg.movies.data.Resource
import com.marcoscg.movies.data.sources.remote.api.ApiClient
import com.marcoscg.movies.model.Genres
import com.marcoscg.movies.model.MovieExtended
import com.marcoscg.movies.ui.details.viewmodel.MovieDetailsViewModel
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.layout_movie_details_rating.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.text.DecimalFormat
import java.util.*

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

    override fun onBackPressed() {
        super.onBackPressed()
        hideFab()
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
            collapsing_toolbar.title = data.title
            detail_description.text = data.overview
            company_name.text = data.production_companies.firstOrNull()?.name.orNa()
            runtime.text = TimeUtils.formatMinutes(this, data.runtime)
            year.text = LocalDate.parse(data.release_date).format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    .withLocale(Locale.getDefault())
            )
            website.text = HtmlCompat.fromHtml(
                getString(
                    R.string.visit_website_url_pattern,
                    data.homepage,
                    getString(R.string.visit_website)
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            website.movementMethod = LinkMovementMethod.getInstance()
            fillGenres(data.genres)

            // Rating
            detail_rating.text = data.vote_average.toString()
            detail_votes.text = data.vote_count.toString()
            detail_revenue.text = getString(R.string.revenue_pattern, DecimalFormat("##.##").format(data.revenue / 1000000.0))

            favorite_fab.setOnClickListener {
                movieDetailsViewModel.toggleFavorite(data)
            }
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

    // Source: https://stackoverflow.com/a/49824144
    private fun hideFab() {
        (favorite_fab.layoutParams as CoordinatorLayout.LayoutParams).behavior = null
        favorite_fab.requestLayout()
        favorite_fab.gone()
    }

}