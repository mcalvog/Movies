package com.marcoscg.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.marcoscg.movies.R
import com.marcoscg.movies.common.utils.gone
import com.marcoscg.movies.common.utils.visible
import com.marcoscg.movies.data.Resource
import com.marcoscg.movies.model.Movie
import com.marcoscg.movies.ui.home.master.PopularMoviesAdapter
import com.marcoscg.movies.ui.home.viewmodel.UpcomingViewModel
import kotlinx.android.synthetic.main.fragment_movie_list.*

class UpcomingFragment : Fragment() {

    private val upcomingViewModel: UpcomingViewModel by lazy {
        ViewModelProvider(this).get(UpcomingViewModel::class.java)
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

        if (upcomingViewModel.getUpcomingMovies().value == null)
            upcomingViewModel.fetchUpcomingMovies()

        upcomingViewModel.getUpcomingMovies().observe(viewLifecycleOwner, Observer {
            handleMoviesDataState(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        upcomingViewModel.disposable?.dispose()
    }

    private fun handleMoviesDataState(state: Resource<List<Movie>>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                fragment_home_loading_view.visible()
            }
            Resource.Status.SUCCESS -> {
                fragment_home_loading_view.gone()
                loadMovies(state.data)
            }
            Resource.Status.ERROR -> {
                fragment_home_loading_view.gone()
                Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadMovies(movies: List<Movie>?) {
        movies?.let { popularMoviesAdapter?.fillList(it) }
    }

    private fun setupRecyclerView() {
        popularMoviesAdapter = PopularMoviesAdapter(context)

        rv_fragment_home.adapter = popularMoviesAdapter
    }
}