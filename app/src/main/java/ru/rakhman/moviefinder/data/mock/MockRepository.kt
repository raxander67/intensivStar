package ru.rakhman.moviefinder.data.mock

object MockRepository {

    fun getMovies(): List<MockMovie> {

        val moviesList = mutableListOf<MockMovie>()
        for (x in 0..10) {
            val movieMovie = MockMovie(
                title = "Spider-Man $x",
                voteAverage = 10.0 - x
            )
            moviesList.add(movieMovie)
        }

        return moviesList
    }

}