package com.marcoscg.movies.data.sources.local.mapper

import com.marcoscg.movies.data.sources.local.model.MovieDbModel
import com.marcoscg.movies.model.Movie
import com.marcoscg.movies.model.utils.orDefault
import com.marcoscg.movies.model.utils.orFalse

class MoviesLocalMapper {

    fun mapFromLocal(movieDbModel: MovieDbModel): Movie {
        return Movie(movieDbModel.popularity.orDefault(), movieDbModel.vote_count.orDefault(), movieDbModel.video.orFalse(),
            movieDbModel.poster_path.orEmpty(), movieDbModel.id, movieDbModel.adult.orFalse(), movieDbModel.backdrop_path.orEmpty(),
            movieDbModel.original_language.orEmpty(), movieDbModel.original_title.orEmpty(), movieDbModel.title,
            movieDbModel.vote_average.orDefault(), movieDbModel.overview.orEmpty(), movieDbModel.release_date.orEmpty())
    }

    fun mapToLocal(movie: Movie): MovieDbModel {
        return MovieDbModel(movie.popularity, movie.vote_count, movie.video, movie.poster_path, movie.id, movie.adult,
            movie.backdrop_path, movie.original_language, movie.original_title, movie.title, movie.vote_average,
            movie.overview, movie.release_date)
    }

}