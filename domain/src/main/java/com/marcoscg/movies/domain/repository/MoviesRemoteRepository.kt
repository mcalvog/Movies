package com.marcoscg.movies.domain.repository

import com.marcoscg.movies.model.MovieDetail
import com.marcoscg.movies.model.MoviesResponse
import io.reactivex.Single

interface MoviesRemoteRepository {

    fun getPopularMovies(page: Int): Single<MoviesResponse>

    fun getUpcomingMovies(page: Int): Single<MoviesResponse>

    fun getSingleMovie(id: String): Single<MovieDetail>

}