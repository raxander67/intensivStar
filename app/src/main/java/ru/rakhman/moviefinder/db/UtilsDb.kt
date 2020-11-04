package ru.rakhman.moviefinder.db


import ru.rakhman.moviefinder.data.Movie


fun convertToMovieFavorite(movie: Movie) : MovieFavorite {
    return MovieFavorite(
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
        movie.video,
        movie.voteAverage
    )
}
fun convertToMovieFeedFragment(movie: Movie) : MovieFeedFragment {
    return MovieFeedFragment(
        movie.id,
        movie.title,
        movie.posterPath,
        movie.voteAverage
    )
}

