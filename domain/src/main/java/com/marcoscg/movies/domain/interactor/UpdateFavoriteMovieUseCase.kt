package com.marcoscg.movies.domain.interactor

import com.marcoscg.movies.domain.repository.MoviesLocalRepository
import com.marcoscg.movies.model.Movie
import io.reactivex.Completable
import io.reactivex.Single

class UpdateFavoriteMovieUseCase(private val moviesLocalRepository: MoviesLocalRepository) {

    fun execute(movie: Movie): Completable {
        return moviesLocalRepository.updateFavoriteMovie(movie)
    }

}