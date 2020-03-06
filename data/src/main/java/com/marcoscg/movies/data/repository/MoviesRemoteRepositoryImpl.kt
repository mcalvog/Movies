package com.marcoscg.movies.data.repository

import com.marcoscg.movies.data.sources.remote.api.ApiClient
import com.marcoscg.movies.domain.repository.MoviesRemoteRepository
import com.marcoscg.movies.model.MoviesResponse
import io.reactivex.Single

class MoviesRemoteRepositoryImpl : MoviesRemoteRepository  {

    private val apiKey = "THE_MOVIE_DB_API_KEY"

    override fun getPopularMovies(): Single<MoviesResponse> {
        return ApiClient.movieService().getPopularMovies(apiKey, 1)
    }

    override fun getUpcomingMovies(): Single<MoviesResponse> {
        return ApiClient.movieService().getUpcomingMovies(apiKey, 1)
    }

}