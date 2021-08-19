package com.marcoscg.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.marcoscg.movies.R
import com.marcoscg.movies.common.utils.setAnchorId
import com.marcoscg.movies.data.Resource
import com.marcoscg.movies.databinding.FragmentMovieListBinding
import com.marcoscg.movies.model.Movie
import com.marcoscg.movies.ui.home.master.MovieListAdapter
import com.marcoscg.movies.ui.home.viewmodel.FavoriteViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment(R.layout.fragment_movie_list), MovieListAdapter.OnItemClickListener {

    private val favoriteViewModel: FavoriteViewModel by lazy {
        ViewModelProvider(this).get(FavoriteViewModel::class.java)
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

        favoriteViewModel.fetchFavoriteMovies(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            favoriteViewModel.favoriteMoviesState.collect {
                handleFavoriteMoviesDataState(it)
            }
        }
    }

    override fun onItemClick(movie: Movie, container: View) {
        val action = FavoriteFragmentDirections.navigateToMovieDetails(id = movie.id, posterPath = movie.poster_path)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(),
            Pair.create(container, container.transitionName)
        )

        findNavController().navigate(action, ActivityNavigatorExtras(options))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        favoriteViewModel.disposable?.dispose()
    }

    private fun handleFavoriteMoviesDataState(state: Resource<List<Movie>>) {
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
                Snackbar.make(binding.srlFragmentMovieList, getString(R.string.error_message_pattern, state.message), Snackbar.LENGTH_LONG)
                    .setAnchorId(R.id.bottom_navigation).show()
            }
        }
    }

    private fun loadMovies(movies: List<Movie>?) {
        movies?.let {
            movieListAdapter?.clear()
            movieListAdapter?.fillList(it)
        }
    }

    private fun setupRecyclerView() {
        movieListAdapter = MovieListAdapter(context)
        movieListAdapter?.setOnMovieClickListener(this)

        binding.rvFragmentMovieList.adapter = movieListAdapter
    }

    private fun setupSwipeRefresh() {
        binding.srlFragmentMovieList.setOnRefreshListener {
            favoriteViewModel.fetchFavoriteMovies(requireContext())
        }
    }
}