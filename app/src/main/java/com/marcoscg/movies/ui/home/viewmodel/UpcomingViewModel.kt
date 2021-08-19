package com.marcoscg.movies.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import com.marcoscg.movies.data.Resource
import com.marcoscg.movies.data.repository.MoviesRemoteRepositoryImpl
import com.marcoscg.movies.domain.interactor.GetUpcomingMoviesUseCase
import com.marcoscg.movies.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UpcomingViewModel : ViewModel() {

    private val stateFlow = MutableStateFlow<Resource<List<Movie>>>(Resource.loading())
    private var currentPage = 1
    private var lastPage = 1

    var disposable: Disposable? = null

    val upcomingMoviesState: StateFlow<Resource<List<Movie>>>
        get() = stateFlow

    fun fetchUpcomingMovies() {
        stateFlow.value = Resource.loading()

        disposable = GetUpcomingMoviesUseCase(MoviesRemoteRepositoryImpl()).execute(currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                lastPage = res.total_pages
                stateFlow.value = Resource.success(res.results)
            }, { throwable ->
                lastPage = currentPage // prevent loading more pages
                throwable.localizedMessage?.let {
                    stateFlow.value = Resource.error(it)
                }
            })
    }

    fun fetchNextUpcomingMovies() {
        currentPage++
        fetchUpcomingMovies()
    }

    fun refreshUpcomingMovies() {
        currentPage = 1
        fetchUpcomingMovies()
    }

    fun isFirstPage(): Boolean {
        return currentPage == 1
    }

    fun isLastPage(): Boolean {
        return currentPage == lastPage
    }

}