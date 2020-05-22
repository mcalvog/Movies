package com.marcoscg.movies.data.repository

import com.marcoscg.movies.data.sources.remote.api.ApiClient
import com.marcoscg.movies.domain.repository.MoviesRemoteRepository
import com.marcoscg.movies.model.MovieExtended
import com.marcoscg.movies.model.MoviesResponse
import io.reactivex.Single

class MoviesRemoteRepositoryImpl : MoviesRemoteRepository  {

    override fun getPopularMovies(): Single<MoviesResponse> {
        return ApiClient.movieService().getPopularMovies(1)
    }

    override fun getUpcomingMovies(): Single<MoviesResponse> {
        return ApiClient.movieService().getUpcomingMovies(1)
    }

    override fun getSingleMovie(id: String): Single<MovieExtended> {
        return ApiClient.movieService().getSingleMovie(id)
    }

}