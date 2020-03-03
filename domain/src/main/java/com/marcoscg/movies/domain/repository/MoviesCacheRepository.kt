package com.marcoscg.movies.domain.repository

import com.marcoscg.movies.model.Movie
import io.reactivex.Completable
import io.reactivex.Observable

interface MoviesCacheRepository {

    fun getFavoriteMovies(): Observable<List<Movie>>

    fun saveFavoriteMovie(movie: Movie): Completable

}