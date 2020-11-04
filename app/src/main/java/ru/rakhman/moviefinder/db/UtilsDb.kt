package ru.rakhman.moviefinder.db


import ru.rakhman.moviefinder.data.Movie


fun convertMovie(movie: Movie) : MovieEntity {
    return MovieEntity(
        movie.id,
        /*movie.genreIds,*/
        movie.title,
        movie.posterPath,
        movie.isAdult,
        movie.overview,
        movie.releaseDate,
        movie.originalTitle,
        movie.originalLanguage,
        movie.backdropPath,
        movie.popularity,
        movie.voteCount,
        movie.video,
        movie.voteAverage
    )
}
