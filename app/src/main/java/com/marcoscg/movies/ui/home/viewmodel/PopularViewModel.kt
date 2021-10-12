package com.marcoscg.movies.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import com.marcoscg.movies.data.Resource
import com.marcoscg.movies.domain.interactor.GetPopularMoviesUseCase
import com.marcoscg.movies.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PopularViewModel(private val getPopularMoviesUseCase: GetPopularMoviesUseCase) : ViewModel() {

    private val stateFlow = MutableStateFlow<Resource<List<Movie>>>(Resource.empty())
    private var currentPage = 1
    private var lastPage = 1

    var disposable: Disposable? = null

    val popularMoviesState: StateFlow<Resource<List<Movie>>>
        get() = stateFlow

    fun fetchPopularMovies() {
        stateFlow.value = Resource.loading()

        disposable = getPopularMoviesUseCase.execute(currentPage)
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

    fun fetchNextPopularMovies() {
        currentPage++
        fetchPopularMovies()
    }

    fun refreshPopularMovies() {
        currentPage = 1
        fetchPopularMovies()
    }

    fun isFirstPage(): Boolean {
        return currentPage == 1
    }

    fun isLastPage(): Boolean {
        return currentPage == lastPage
    }

}