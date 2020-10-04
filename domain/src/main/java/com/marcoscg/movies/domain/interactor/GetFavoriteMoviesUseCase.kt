package com.marcoscg.movies.domain.interactor

import com.marcoscg.movies.domain.repository.MoviesLocalRepository
import com.marcoscg.movies.model.Movie
import io.reactivex.Observable
import io.reactivex.Single

class GetFavoriteMoviesUseCase(private val moviesLocalRepository: MoviesLocalRepository) {

    fun execute(): Observable<List<Movie>> {
        return moviesLocalRepository.getFavoriteMovies()
    }

}