package com.marcoscg.movies.domain.interactor

import com.marcoscg.movies.domain.repository.MoviesRemoteRepository
import com.marcoscg.movies.model.MovieExtended
import io.reactivex.Single

class GetSingleMovieUseCase(private val moviesRemoteRepository: MoviesRemoteRepository) {

    fun execute(id: String): Single<MovieExtended> {
        return moviesRemoteRepository.getSingleMovie(id)
    }

}