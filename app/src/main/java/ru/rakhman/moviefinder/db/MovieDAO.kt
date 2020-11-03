package ru.rakhman.moviefinder.db

import androidx.room.*

@Dao
interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(movie:List<MovieEntity>)

    @Delete
    fun delete(movie: MovieEntity)

    @Query("SELECT * FROM Movies ORDER BY title ASC")
    fun getMovies(): List<MovieEntity>
}