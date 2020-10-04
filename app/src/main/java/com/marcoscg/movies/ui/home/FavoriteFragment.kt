package com.marcoscg.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.marcoscg.movies.R
import com.marcoscg.movies.common.utils.setAnchorId
import com.marcoscg.movies.data.Resource
import com.marcoscg.movies.model.Movie
import com.marcoscg.movies.ui.home.master.PopularMoviesAdapter
import com.marcoscg.movies.ui.home.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.fragment_movie_list.*

class FavoriteFragment : Fragment(), PopularMoviesAdapter.OnItemClickListener {

    private val favoriteViewModel: FavoriteViewModel by lazy {
        ViewModelProvider(this).get(FavoriteViewModel::class.java)
    }

    private var popularMoviesAdapter: PopularMoviesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()
        setupSwipeRefresh()

        favoriteViewModel.fetchFavoriteMovies(requireContext())

        favoriteViewModel.getFavoriteMovies().observe(viewLifecycleOwner, Observer {
            handleFavoriteMoviesDataState(it)
        })
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

        favoriteViewModel.disposable?.dispose()
    }

    private fun handleFavoriteMoviesDataState(state: Resource<List<Movie>>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                srl_fragment_movie_list.isRefreshing = true
            }
            Resource.Status.SUCCESS -> {
                srl_fragment_movie_list.isRefreshing = false
                loadMovies(state.data)
            }
            Resource.Status.ERROR -> {
                srl_fragment_movie_list.isRefreshing = false
                Snackbar.make(srl_fragment_movie_list, "Error: ${state.message}", Snackbar.LENGTH_LONG)
                    .setAnchorId(R.id.bottom_navigation).show()
            }
        }
    }

    private fun loadMovies(movies: List<Movie>?) {
        movies?.let { popularMoviesAdapter?.fillList(it) }
    }

    private fun setupRecyclerView() {
        popularMoviesAdapter = PopularMoviesAdapter(context)
        popularMoviesAdapter?.setOnMovieClickListener(this)

        rv_fragment_movie_list.adapter = popularMoviesAdapter
    }

    private fun setupSwipeRefresh() {
        srl_fragment_movie_list.setOnRefreshListener {
            favoriteViewModel.fetchFavoriteMovies(requireContext())
        }
    }
}