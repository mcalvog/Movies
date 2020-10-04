package com.marcoscg.movies.ui.home.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcoscg.movies.data.Resource
import com.marcoscg.movies.data.repository.MoviesLocalRepositoryImpl
import com.marcoscg.movies.domain.interactor.GetFavoriteMoviesUseCase
import com.marcoscg.movies.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FavoriteViewModel : ViewModel() {

    private val liveData: MutableLiveData<Resource<List<Movie>>> = MutableLiveData()
    var disposable: Disposable? = null

    fun getFavoriteMovies(): LiveData<Resource<List<Movie>>> {
        return liveData
    }

    fun fetchFavoriteMovies(context: Context) {
        liveData.postValue(Resource.loading())

        disposable = GetFavoriteMoviesUseCase(MoviesLocalRepositoryImpl(context)).execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                liveData.postValue(Resource.success(res))
            }, { throwable ->
                throwable.localizedMessage?.let {
                    liveData.postValue(Resource.error(it))
                }
            })
    }

}