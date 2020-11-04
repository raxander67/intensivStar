package ru.rakhman.moviefinder.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "Movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int?,
    /*val movieGenreId: List<Int>,*/
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name="poster_path")
    val posterPath: String? = null,
    @ColumnInfo(name = "adult")
    val isAdult: Boolean,
    @ColumnInfo(name = "overview")
    val overview: String?,
    @ColumnInfo(name = "release_date")
    val releaseDate: String?,
    @ColumnInfo(name = "original_title")
    val originalTitle: String?,
    @ColumnInfo(name = "original_language")
    val originalLanguage: String?,
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String?,
    @ColumnInfo(name = "popularity")
    val popularity: Double?,
    @ColumnInfo(name = "vote_count")
    val voteCount: Int?,
    @ColumnInfo(name = "video")
    val video: Boolean?,
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double?
)
@Entity(tableName = "genre")
data class Genre(
    @PrimaryKey
    val genreId: Long,
    val genreName: String
)
