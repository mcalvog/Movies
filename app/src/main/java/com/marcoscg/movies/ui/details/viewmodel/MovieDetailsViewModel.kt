package com.marcoscg.movies.ui.details.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.marcoscg.movies.data.Resource
import com.marcoscg.movies.data.repository.MoviesLocalRepositoryImpl
import com.marcoscg.movies.data.repository.MoviesRemoteRepositoryImpl
import com.marcoscg.movies.domain.interactor.*
import com.marcoscg.movies.model.MovieDetail
import com.marcoscg.movies.model.toSimple
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovieDetailsViewModel : ViewModel() {

    private val singleMovieStateFlow = MutableStateFlow<Resource<MovieDetail>>(Resource.loading())
    private val favoritesStateFlow = MutableStateFlow<Resource<Boolean>>(Resource.loading())
    var disposable: Disposable? = null

    val singleMovieState: StateFlow<Resource<MovieDetail>>
        get() = singleMovieStateFlow

    val favoritesState: StateFlow<Resource<Boolean>>
        get() = favoritesStateFlow

    fun fetchSingleMovie(id: String) {
        singleMovieStateFlow.value = Resource.loading()

        disposable = GetSingleMovieUseCase(MoviesRemoteRepositoryImpl()).execute(id)
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

    fun addFavoriteMovie(context: Context, movie: MovieDetail) {
        favoritesStateFlow.value = Resource.loading()

        disposable = AddFavoriteMovieUseCase(MoviesLocalRepositoryImpl(context)).execute(movie.toSimple())
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

    fun deleteFavoriteMovie(context: Context, movie: MovieDetail) {
        favoritesStateFlow.value = Resource.loading()

        disposable = DeleteFavoriteMovieUseCase(MoviesLocalRepositoryImpl(context)).execute(movie.toSimple())
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

    fun updateFavoriteMovie(context: Context, movie: MovieDetail) {
        favoritesStateFlow.value = Resource.loading()

        disposable = UpdateFavoriteMovieUseCase(MoviesLocalRepositoryImpl(context)).execute(movie.toSimple())
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

    fun toggleFavorite(context: Context, movie: MovieDetail) {
        favoritesStateFlow.value = Resource.loading()
        
        disposable = GetFavoriteMovieUseCase(MoviesLocalRepositoryImpl(context)).execute(movie.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // favorite movie exists, remove from favorites
                deleteFavoriteMovie(context, movie)
            }, {
                // favorite movie does not exist, add to favorites
                addFavoriteMovie(context, movie)
            })
    }

    fun fetchFavoriteMovieState(context: Context, movie: MovieDetail) {
        favoritesStateFlow.value = Resource.loading()

        disposable = GetFavoriteMovieUseCase(MoviesLocalRepositoryImpl(context)).execute(movie.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // favorite movie exists, update data first
                updateFavoriteMovie(context, movie)
                favoritesStateFlow.value = Resource.success(true)
            }, {
                // favorite movie does not exist
                favoritesStateFlow.value = Resource.success(false)
            })
    }

}