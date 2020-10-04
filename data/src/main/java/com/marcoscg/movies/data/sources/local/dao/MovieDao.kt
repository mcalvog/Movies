package com.marcoscg.movies.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.marcoscg.movies.data.sources.local.model.MovieDbModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface MovieDao {

    @Query("SELECT * FROM favorite_movies")
    fun getFavoriteMovies(): Observable<List<MovieDbModel>>

    @Query("SELECT * FROM favorite_movies WHERE id == :id")
    fun getFavoriteMovie(id: Int): Single<MovieDbModel>

    @Insert
    fun addFavoriteMovie(movie: MovieDbModel): Completable

    @Delete
    fun deleteFavoriteMovie(movie: MovieDbModel): Completable

}