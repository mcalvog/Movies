package com.marcoscg.movies.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcoscg.movies.data.Resource
import com.marcoscg.movies.data.repository.MoviesRemoteRepositoryImpl
import com.marcoscg.movies.domain.interactor.GetUpcomingMoviesUseCase
import com.marcoscg.movies.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UpcomingViewModel : ViewModel() {

    private val liveData: MutableLiveData<Resource<List<Movie>>> = MutableLiveData()
    var disposable: Disposable? = null

    fun getUpcomingMovies(): LiveData<Resource<List<Movie>>> {
        return liveData
    }

    fun fetchUpcomingMovies() {
        liveData.postValue(Resource.loading())

        disposable = GetUpcomingMoviesUseCase(MoviesRemoteRepositoryImpl()).execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                liveData.postValue(Resource.success(res.results))
            }, { throwable ->
                throwable.localizedMessage?.let {
                    liveData.postValue(Resource.error(it))
                }
            })
    }

}