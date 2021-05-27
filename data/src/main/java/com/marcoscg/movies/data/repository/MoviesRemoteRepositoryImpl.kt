package com.marcoscg.movies.data.repository

import com.marcoscg.movies.data.sources.remote.api.ApiClient
import com.marcoscg.movies.data.sources.remote.mapper.MoviesRemoteMapper
import com.marcoscg.movies.domain.repository.MoviesRemoteRepository
import com.marcoscg.movies.model.MovieDetail
import com.marcoscg.movies.model.MoviesResponse
import io.reactivex.Single

class MoviesRemoteRepositoryImpl : MoviesRemoteRepository  {

    private val moviesRemoteMapper = MoviesRemoteMapper()

    override fun getPopularMovies(page: Int): Single<MoviesResponse> {
        return ApiClient.movieService().getPopularMovies(page).map {
            moviesRemoteMapper.mapFromRemote(it)
        }
    }

    override fun getUpcomingMovies(page: Int): Single<MoviesResponse> {
        return ApiClient.movieService().getUpcomingMovies(page).map {
            moviesRemoteMapper.mapFromRemote(it)
        }
    }

    override fun getSingleMovie(id: String): Single<MovieDetail> {
        return ApiClient.movieService().getSingleMovie(id).map {
            moviesRemoteMapper.mapDetailFromRemote(it)
        }
    }

}