package com.marcoscg.movies.ui.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcoscg.movies.data.Resource
import com.marcoscg.movies.data.repository.MoviesRemoteRepositoryImpl
import com.marcoscg.movies.domain.interactor.GetSingleMovieUseCase
import com.marcoscg.movies.model.MovieExtended
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsViewModel : ViewModel() {

    private val liveData: MutableLiveData<Resource<MovieExtended>> = MutableLiveData()
    var disposable: Disposable? = null

    fun getSingleMovie(): LiveData<Resource<MovieExtended>> {
        return liveData
    }

    fun fetchSingleMovie(id: String) {
        liveData.postValue(Resource.loading())

        disposable = GetSingleMovieUseCase(MoviesRemoteRepositoryImpl()).execute(id)
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