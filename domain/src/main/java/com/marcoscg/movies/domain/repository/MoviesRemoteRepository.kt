package com.marcoscg.movies.domain.repository

import com.marcoscg.movies.model.MovieExtended
import com.marcoscg.movies.model.MoviesResponse
import io.reactivex.Single

interface MoviesRemoteRepository {

    fun getPopularMovies(): Single<MoviesResponse>

    fun getUpcomingMovies(): Single<MoviesResponse>

    fun getSingleMovie(id: String): Single<MovieExtended>

}