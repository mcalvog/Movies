package com.marcoscg.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.core.util.Pair as UtilPair
import com.google.android.material.snackbar.Snackbar
import com.marcoscg.movies.R
import com.marcoscg.movies.common.recyclerview.PaginationScrollListener
import com.marcoscg.movies.common.utils.gone
import com.marcoscg.movies.common.utils.setAnchorId
import com.marcoscg.movies.common.utils.visible
import com.marcoscg.movies.data.Resource
import com.marcoscg.movies.databinding.FragmentMovieListBinding
import com.marcoscg.movies.model.Movie
import com.marcoscg.movies.ui.home.master.MovieListAdapter
import com.marcoscg.movies.ui.home.viewmodel.UpcomingViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UpcomingFragment : Fragment(R.layout.fragment_movie_list), MovieListAdapter.OnItemClickListener {

    private val upcomingViewModel: UpcomingViewModel by lazy {
        ViewModelProvider(this).get(UpcomingViewModel::class.java)
    }

    private var movieListAdapter: MovieListAdapter? = null

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()
        setupSwipeRefresh()

        upcomingViewModel.refreshUpcomingMovies()

        viewLifecycleOwner.lifecycleScope.launch {
            upcomingViewModel.upcomingMoviesState.collect {
                handleMoviesDataState(it)
            }
        }
    }

    override fun onItemClick(movie: Movie, container: View) {
        val action = UpcomingFragmentDirections.navigateToMovieDetails(id = movie.id, posterPath = movie.poster_path)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(),
            UtilPair.create(container, container.transitionName)
        )

        findNavController().navigate(action, ActivityNavigatorExtras(options))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        upcomingViewModel.disposable?.dispose()
    }

    private fun handleMoviesDataState(state: Resource<List<Movie>>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding.srlFragmentMovieList.isRefreshing = true
            }
            Resource.Status.SUCCESS -> {
                binding.srlFragmentMovieList.isRefreshing = false
                loadMovies(state.data)
            }
            Resource.Status.ERROR -> {
                binding.srlFragmentMovieList.isRefreshing = false
                binding.pbFragmentMovieList.gone()
                Snackbar.make(binding.srlFragmentMovieList, getString(R.string.error_message_pattern, state.message), Snackbar.LENGTH_LONG)
                    .setAnchorId(R.id.bottom_navigation).show()
            }
        }
    }

    private fun loadMovies(movies: List<Movie>?) {
        movies?.let {
            if (upcomingViewModel.isFirstPage()) {
                // Remove previous movies
                movieListAdapter?.clear()
            }

            movieListAdapter?.fillList(it)
        }
    }

    private fun setupRecyclerView() {
        movieListAdapter = MovieListAdapter(context)
        movieListAdapter?.setOnMovieClickListener(this)

        binding.rvFragmentMovieList.adapter = movieListAdapter
        binding.rvFragmentMovieList.addOnScrollListener(object : PaginationScrollListener(binding.rvFragmentMovieList.linearLayoutManager) {
            override fun isLoading(): Boolean {
                val isLoading = binding.srlFragmentMovieList.isRefreshing

                if (isLoading) {
                    binding.pbFragmentMovieList.visible()
                } else {
                    binding.pbFragmentMovieList.gone()
                }

                return isLoading
            }

            override fun isLastPage(): Boolean {
                return upcomingViewModel.isLastPage()
            }

            override fun loadMoreItems() {
                upcomingViewModel.fetchNextUpcomingMovies()
            }
        })
    }

    private fun setupSwipeRefresh() {
        binding.srlFragmentMovieList.setOnRefreshListener {
            upcomingViewModel.refreshUpcomingMovies()
        }
    }
}