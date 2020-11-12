package ru.rakhman.moviefinder.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovieFavorite(movie:List<MovieFavorite>): Completable

    @Delete
    fun deleteMovieFavorite(movie: MovieFavorite): Completable

    @Query("SELECT * FROM movie_favorite ORDER BY title ASC")
    fun getMovieFavorite(): Observable<List<MovieFavorite>>

    @Transaction
    @Query("SELECT * FROM movie_favorite WHERE id = :id")
    fun loadById(id: Int): Single<MovieFavorite?>

}
@Dao
interface MovieFF {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovieFeedFragment(movie: List<MovieFeedFragment>): Completable

    @Delete
    fun deleteMovieFeedFragment(movie: MovieFeedFragment): Completable
}