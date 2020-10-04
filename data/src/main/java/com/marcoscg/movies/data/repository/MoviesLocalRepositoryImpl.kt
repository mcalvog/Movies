package com.marcoscg.movies.data.repository

import android.content.Context
import com.marcoscg.movies.data.sources.local.db.MoviesDatabase
import com.marcoscg.movies.data.sources.local.mapper.MoviesLocalMapper
import com.marcoscg.movies.domain.repository.MoviesLocalRepository
import com.marcoscg.movies.model.Movie
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

class MoviesLocalRepositoryImpl(val context: Context) : MoviesLocalRepository {

    private val moviesLocalMapper = MoviesLocalMapper()

    override fun getFavoriteMovies(): Observable<List<Movie>> {
        return MoviesDatabase.invoke(context).movieDao().getFavoriteMovies().map {
            it.map { movie ->
                moviesLocalMapper.mapFromLocal(movie)
            }
        }
    }

    override fun getFavoriteMovie(id: Int): Single<Movie> {
        return MoviesDatabase.invoke(context).movieDao().getFavoriteMovie(id).map {
            moviesLocalMapper.mapFromLocal(it)
        }
    }

    override fun addFavoriteMovie(movie: Movie): Completable {
        return MoviesDatabase.invoke(context).movieDao().addFavoriteMovie(moviesLocalMapper.mapToLocal(movie))
    }

    override fun deleteFavoriteMovie(movie: Movie): Completable {
        return MoviesDatabase.invoke(context).movieDao().deleteFavoriteMovie(moviesLocalMapper.mapToLocal(movie))
    }

}