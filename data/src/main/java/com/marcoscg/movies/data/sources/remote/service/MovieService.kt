package com.marcoscg.movies.data.sources.remote.service

import com.marcoscg.movies.model.MovieDetail
import com.marcoscg.movies.model.MoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Single<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("page") page: Int): Single<MoviesResponse>

    @GET("movie/{id}")
    fun getSingleMovie(@Path("id") id: String): Single<MovieDetail>

}