package com.marcoscg.movies.ui.details.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class MovieDetailsViewModel : ViewModel() {

    private val singleMovieLiveData: MutableLiveData<Resource<MovieDetail>> = MutableLiveData()
    private val favoritesLiveData: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    var disposable: Disposable? = null

    fun getSingleMovie(): LiveData<Resource<MovieDetail>> {
        return singleMovieLiveData
    }

    fun fetchSingleMovie(id: String) {
        singleMovieLiveData.postValue(Resource.loading())

        disposable = GetSingleMovieUseCase(MoviesRemoteRepositoryImpl()).execute(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                singleMovieLiveData.postValue(Resource.success(res))
            }, { throwable ->
                throwable.localizedMessage?.let {
                    singleMovieLiveData.postValue(Resource.error(it))
                }
            })
    }

    fun addFavoriteMovie(context: Context, movie: MovieDetail) {
        favoritesLiveData.postValue(Resource.loading())

        disposable = AddFavoriteMovieUseCase(MoviesLocalRepositoryImpl(context)).execute(movie.toSimple())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                favoritesLiveData.postValue(Resource.success(true))
            }, { throwable ->
                throwable.localizedMessage?.let {
                    favoritesLiveData.postValue(Resource.error(it))
                }
            })
    }

    fun deleteFavoriteMovie(context: Context, movie: MovieDetail) {
        favoritesLiveData.postValue(Resource.loading())

        disposable = DeleteFavoriteMovieUseCase(MoviesLocalRepositoryImpl(context)).execute(movie.toSimple())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                favoritesLiveData.postValue(Resource.success(false))
            }, { throwable ->
                throwable.localizedMessage?.let {
                    favoritesLiveData.postValue(Resource.error(it))
                }
            })
    }

    fun toggleFavorite(context: Context, movie: MovieDetail) {
        favoritesLiveData.postValue(Resource.loading())

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

    fun getFavoriteMovieState(): LiveData<Resource<Boolean>> {
        return favoritesLiveData
    }

    fun fetchFavoriteMovieState(context: Context, movie: MovieDetail) {
        favoritesLiveData.postValue(Resource.loading())

        disposable = GetFavoriteMovieUseCase(MoviesLocalRepositoryImpl(context)).execute(movie.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // favorite movie exists
                favoritesLiveData.postValue(Resource.success(true))
            }, {
                // favorite movie does not exist
                favoritesLiveData.postValue(Resource.success(false))
            })
    }

}