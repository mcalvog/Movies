package com.marcoscg.movies.ui.details.viewmodel

import androidx.lifecycle.ViewModel
import com.marcoscg.movies.data.Resource
import com.marcoscg.movies.domain.interactor.*
import com.marcoscg.movies.model.MovieDetail
import com.marcoscg.movies.model.toSimple
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovieDetailsViewModel(private val getSingleMovieUseCase: GetSingleMovieUseCase,
                            private val addFavoriteMovieUseCase: AddFavoriteMovieUseCase,
                            private val deleteFavoriteMovieUseCase: DeleteFavoriteMovieUseCase,
                            private val updateFavoriteMovieUseCase: UpdateFavoriteMovieUseCase,
                            private val getFavoriteMovieUseCase: GetFavoriteMovieUseCase
) : ViewModel() {

    private val singleMovieStateFlow = MutableStateFlow<Resource<MovieDetail>>(Resource.empty())
    private val favoritesStateFlow = MutableStateFlow<Resource<Boolean>>(Resource.empty())
    var disposable: Disposable? = null

    val singleMovieState: StateFlow<Resource<MovieDetail>>
        get() = singleMovieStateFlow

    val favoritesState: StateFlow<Resource<Boolean>>
        get() = favoritesStateFlow

    fun fetchSingleMovie(id: String) {
        singleMovieStateFlow.value = Resource.loading()

        disposable = getSingleMovieUseCase.execute(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                singleMovieStateFlow.value = Resource.success(res)
            }, { throwable ->
                throwable.localizedMessage?.let {
                    singleMovieStateFlow.value = Resource.error(it)
                }
            })
    }

    fun addFavoriteMovie(movie: MovieDetail) {
        favoritesStateFlow.value = Resource.loading()

        disposable = addFavoriteMovieUseCase.execute(movie.toSimple())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                favoritesStateFlow.value = Resource.success(true)
            }, { throwable ->
                throwable.localizedMessage?.let {
                    favoritesStateFlow.value = Resource.error(it)
                }
            })
    }

    fun deleteFavoriteMovie(movie: MovieDetail) {
        favoritesStateFlow.value = Resource.loading()

        disposable = deleteFavoriteMovieUseCase.execute(movie.toSimple())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                favoritesStateFlow.value = Resource.success(false)
            }, { throwable ->
                throwable.localizedMessage?.let {
                    favoritesStateFlow.value = Resource.error(it)
                }
            })
    }

    fun updateFavoriteMovie(movie: MovieDetail) {
        favoritesStateFlow.value = Resource.loading()

        disposable = updateFavoriteMovieUseCase.execute(movie.toSimple())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                favoritesStateFlow.value = Resource.success(true)
            }, { throwable ->
                throwable.localizedMessage?.let {
                    favoritesStateFlow.value = Resource.error(it)
                }
            })
    }

    fun toggleFavorite(movie: MovieDetail) {
        favoritesStateFlow.value = Resource.loading()
        
        disposable = getFavoriteMovieUseCase.execute(movie.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // favorite movie exists, remove from favorites
                deleteFavoriteMovie(movie)
            }, {
                // favorite movie does not exist, add to favorites
                addFavoriteMovie(movie)
            })
    }

    fun fetchFavoriteMovieState(movie: MovieDetail) {
        favoritesStateFlow.value = Resource.loading()

        disposable = getFavoriteMovieUseCase.execute(movie.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // favorite movie exists, update data first
                updateFavoriteMovie(movie)
                favoritesStateFlow.value = Resource.success(true)
            }, {
                // favorite movie does not exist
                favoritesStateFlow.value = Resource.success(false)
            })
    }

}