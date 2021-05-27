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
import com.marcoscg.movies.common.utils.*
import com.marcoscg.movies.common.utils.ColorUtils.darken
import com.marcoscg.movies.data.Resource
import com.marcoscg.movies.data.sources.remote.api.ApiClient
import com.marcoscg.movies.databinding.ActivityMovieDetailsBinding
import com.marcoscg.movies.model.Genres
import com.marcoscg.movies.model.MovieDetail
import com.marcoscg.movies.ui.details.viewmodel.MovieDetailsViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.text.DecimalFormat
import java.util.*

class MovieDetailsActivity : BaseActivity() {

    private val movieDetailsViewModel: MovieDetailsViewModel by lazy {
        ViewModelProvider(this).get(MovieDetailsViewModel::class.java)
    }

    private lateinit var binding: ActivityMovieDetailsBinding
    private val args: MovieDetailsActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        clearStatusBar()

        setupPosterImage()

        if (movieDetailsViewModel.getSingleMovie().value == null)
            movieDetailsViewModel.fetchSingleMovie(args.id.toString())

        movieDetailsViewModel.getSingleMovie().observe(this, Observer {
            handleSingleMovieDataState(it)
        })

        movieDetailsViewModel.getFavoriteMovieState().observe(this, Observer {
            handleFavoriteMovieDataState(it)
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        hideFab()
    }

    override fun finish() {
        super.finish()
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }

    private fun handleSingleMovieDataState(state: Resource<MovieDetail>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visible()
            }
            Resource.Status.SUCCESS -> {
                binding.progressBar.gone()
                loadMovieData(state.data)
            }
            Resource.Status.ERROR -> {
                binding.progressBar.gone()
                Toast.makeText(this, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun handleFavoriteMovieDataState(state: Resource<Boolean>) {
        when (state.status) {
            Resource.Status.LOADING -> { }
            Resource.Status.SUCCESS -> {
                updateFavoriteButton(state.data)
            }
            Resource.Status.ERROR -> {
                Toast.makeText(this, "Error: ${state.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadMovieData(data: MovieDetail?) {
        data?.let {
            binding.collapsingToolbar.title = data.title
            binding.detailDescription.text = data.overview
            binding.companyName.text = data.production_companies.firstOrNull()?.name.orNa()

            binding.runtime.text = if (data.runtime > 0)
                TimeUtils.formatMinutes(this, data.runtime) else getString(R.string.no_data_na)

            binding.year.text = if (data.release_date.isNotEmpty())
                LocalDate.parse(data.release_date).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    .withLocale(Locale.getDefault())) else getString(R.string.no_release_date)

            binding.website.text = HtmlCompat.fromHtml(
                getString(
                    R.string.visit_website_url_pattern,
                    data.homepage,
                    getString(R.string.visit_website)
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            binding.website.movementMethod = LinkMovementMethod.getInstance()
            fillGenres(data.genres)

            // Rating
            binding.detailExtraInfo.detailRating.text = if (data.vote_average > 0) data.vote_average.toString() else getString(R.string.no_ratings)
            binding.detailExtraInfo.detailVotes.text = if (data.vote_count > 0) data.vote_count.toString() else getString(R.string.no_data_na)
            binding.detailExtraInfo.detailRevenue.text = getString(R.string.revenue_pattern, DecimalFormat("##.##").format(data.revenue / 1000000.0))

            binding.favoriteFab.setOnClickListener {
                movieDetailsViewModel.toggleFavorite(this, data)
            }

            movieDetailsViewModel.fetchFavoriteMovieState(this, data)
        }
    }

    private fun updateFavoriteButton(data: Boolean?) {
        data?.let { favorite ->
            binding.favoriteFab.setImageResource(
                if (favorite)
                    R.drawable.ic_baseline_star_24
                else R.drawable.ic_star_border_black_24dp
            )
        }
    }

    private fun fillGenres(genres: List<Genres>) {
        for (g in genres) {
            val chip = Chip(this)
            chip.text = g.name
            binding.genresChipGroup.addView(chip)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupPosterImage() {
        postponeEnterTransition()

        binding.ivActivityMovieDetails.transitionName = args.id.toString()
        binding.ivActivityMovieDetails.load(url = ApiClient.POSTER_BASE_URL + args.posterPath, width = 160.dp, height = 160.dp) { color ->
            window?.statusBarColor = color.darken
            binding.collapsingToolbar.setBackgroundColor(color)
            binding.collapsingToolbar.setContentScrimColor(color)
            startPostponedEnterTransition()
        }
    }

    // Source: https://stackoverflow.com/a/49824144
    private fun hideFab() {
        (binding.favoriteFab.layoutParams as CoordinatorLayout.LayoutParams).behavior = null
        binding.favoriteFab.requestLayout()
        binding.favoriteFab.gone()
    }

}