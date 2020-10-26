package ru.rakhman.moviefinder.network

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.rakhman.moviefinder.BuildConfig
import ru.rakhman.moviefinder.data.MoviesResponse


interface MovieApiInterface {


    @GET("movie/now_playing")
    fun getNowPlayedMovies(
        @Query("api_key") apiKey: String = apiKeyMovie,
        @Query("language") language: String = LANG
    ): Single<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String = apiKeyMovie,
        @Query("language") language: String = LANG
    ): Single<MoviesResponse>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = apiKeyMovie,
        @Query("language") language: String = LANG
    ): Single<MoviesResponse>

    @GET("tv/popular")
    fun getPopularTvShows(
        @Query("api_key") apiKey: String = apiKeyMovie,
        @Query("language") language: String = LANG
    ): Single<MoviesResponse>

    @GET("tv/on_the_air")
    fun getOnTheAirTvShows(
        @Query("api_key") apiKey: String = apiKeyMovie,
        @Query("language") language: String = LANG
    ): Single<MoviesResponse>

    @GET("search/movie")
    fun searchByQuery(
        @Query("api_key") apiKey: String = apiKeyMovie,
        @Query("language") language: String = LANG,
        @Query("query") query: String
    ): Single<MoviesResponse>

    companion object {

        const val apiKeyMovie = BuildConfig.THE_MOVIE_DATABASE_API
        const val LANG = "ru"

    }
}
