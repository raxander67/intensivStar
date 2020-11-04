package ru.rakhman.moviefinder.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovieFavorite(movie:List<MovieFavorite>): Completable

    @Delete
    fun deleteMovieFavorite(movie: MovieFavorite): Completable

    @Query("SELECT * FROM movie_favorite ORDER BY title ASC")
    fun getMovieFavorite(): Observable<List<MovieFavorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovieFeedFragment(movie:List<MovieFeedFragment>): Completable
}