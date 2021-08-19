package com.marcoscg.movies.ui.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.marcoscg.movies.data.Resource
import com.marcoscg.movies.data.repository.MoviesLocalRepositoryImpl
import com.marcoscg.movies.domain.interactor.GetFavoriteMoviesUseCase
import com.marcoscg.movies.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoriteViewModel : ViewModel() {

    private val stateFlow = MutableStateFlow<Resource<List<Movie>>>(Resource.loading())
    var disposable: Disposable? = null

    val favoriteMoviesState: StateFlow<Resource<List<Movie>>>
        get() = stateFlow

    fun fetchFavoriteMovies(context: Context) {
        stateFlow.value = Resource.loading()

        disposable = GetFavoriteMoviesUseCase(MoviesLocalRepositoryImpl(context)).execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                stateFlow.value = Resource.success(res)
            }, { throwable ->
                throwable.localizedMessage?.let {
                    stateFlow.value = Resource.error(it)
                }
            })
    }

}