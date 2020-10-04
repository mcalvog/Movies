package com.marcoscg.movies.domain.interactor

import com.marcoscg.movies.domain.repository.MoviesLocalRepository
import com.marcoscg.movies.model.Movie
import io.reactivex.Single

class GetFavoriteMovieUseCase(private val moviesLocalRepository: MoviesLocalRepository) {

    fun execute(id: Int): Single<Movie> {
        return moviesLocalRepository.getFavoriteMovie(id)
    }

}