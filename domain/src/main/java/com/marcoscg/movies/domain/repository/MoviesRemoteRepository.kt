package com.marcoscg.movies.domain.repository

import com.marcoscg.movies.model.Movie
import io.reactivex.Observable

interface MoviesRemoteRepository {

    fun getPopularMovies(): Observable<List<Movie>>

}