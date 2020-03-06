package com.marcoscg.movies.domain.repository

import com.marcoscg.movies.model.Movie
import com.marcoscg.movies.model.MoviesResponse
import io.reactivex.Observable
import io.reactivex.Single

interface MoviesRemoteRepository {

    fun getPopularMovies(): Single<MoviesResponse>

    fun getUpcomingMovies(): Single<MoviesResponse>

}