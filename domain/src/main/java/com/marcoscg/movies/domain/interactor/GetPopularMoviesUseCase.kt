package com.marcoscg.movies.domain.interactor

import com.marcoscg.movies.domain.repository.MoviesRemoteRepository
import com.marcoscg.movies.model.Movie
import io.reactivex.Observable

class GetPopularMoviesUseCase(private val moviesRemoteRepository: MoviesRemoteRepository) {

    fun perform(): Observable<List<Movie>> {
        return moviesRemoteRepository.getPopularMovies()
    }

}