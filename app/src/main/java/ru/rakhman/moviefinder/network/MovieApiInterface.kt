package ru.rakhman.moviefinder.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.rakhman.moviefinder.data.MoviesResponse


interface MovieApiInterface {
    @GET("movie/now_playing")
    fun getNowPlayedMovies(
    @Query("api_key") apiKey: String,
    @Query("language") language: String
    ): Call<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
    @Query("api_key") apiKey: String,
    @Query("language") language: String
    ): Call<MoviesResponse>

    @GET("movie/popular")
    fun getPopularMovies(
    @Query("api_key") apiKey: String,
    @Query("language") language: String
    ): Call<MoviesResponse>

    @GET("tv/popular")
    fun getPopularTvShows(
    @Query("api_key") apiKey: String,
    @Query("language") language: String
    ): Call<MoviesResponse>

    @GET("tv/on_the_air")
    fun getOnTheAirTvShows(
    @Query("api_key") apiKey: String,
    @Query("language") language: String
    ): Call<MoviesResponse>

}
