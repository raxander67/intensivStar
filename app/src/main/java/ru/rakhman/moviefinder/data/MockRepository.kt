package ru.rakhman.moviefinder.data

object MockRepository {

    fun getMovies(): List<Movie_tmp> {

        val moviesList_tmp = mutableListOf<Movie_tmp>()
        for (x in 0..10) {
            val movieMovie_tmp = Movie_tmp(
                title = "Spider-Man $x",
                voteAverage = 10.0 - x
            )
            moviesList_tmp.add(movieMovie_tmp)
        }

        return moviesList_tmp
    }

}