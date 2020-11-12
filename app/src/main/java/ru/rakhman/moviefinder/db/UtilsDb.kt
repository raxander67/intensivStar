package ru.rakhman.moviefinder.db


import android.util.Log
import ru.rakhman.moviefinder.data.Movie


fun convertToDbMovieFavorite(movie: Movie, posterPath:String) : MovieFavorite {
    return MovieFavorite(
        movie.id,
        /*movie.genreIds,*/
        movie.title,
        posterPath,
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
fun convertToListDbMovieFeedFragment(movies: List<Movie>): List<MovieFeedFragment> {
    var convMovies= mutableListOf<MovieFeedFragment>()

    for(movie in movies)
        convMovies.add(convertToDbMovieFeedFragment(movie))
    return convMovies
}
fun convertToDbMovieFeedFragment(movie: Movie) : MovieFeedFragment{
    return MovieFeedFragment(
        movie.id,
        movie.title,
        movie.posterPath,
        movie.voteAverage
    )
}
fun convertDbMoviesFavoriteToMovies(movieFavorite:List<MovieFavorite>):List<Movie>{
    var convMovies= mutableListOf<Movie>()
    for(movie in movieFavorite)
        convMovies.add(convDbMovFavToMovie(movie))
    return convMovies
}

fun convDbMovFavToMovie(movieFav: MovieFavorite): Movie {
    var posterPath:String?=movieFav.posterPath
    // TODO Сохранять сам рисунок в базе, пока временно используется URL рисунка
    val tmp  ="https://image.tmdb.org/t/p/w500"
    val m = Movie(isAdult = false,
        overview = movieFav.overview,
        releaseDate = movieFav.releaseDate,
        genreIds = listOf(1, 2, 3),
        id = movieFav.id,
        originalTitle = movieFav.originalTitle,
        originalLanguage = movieFav.originalLanguage,
        title = movieFav.title,
        backdropPath = "",
        popularity = 0.0,
        voteCount = null,
        video = true,
        voteAverage = movieFav.voteAverage)
    m.posterPath= posterPath!!.subSequence(tmp.length, posterPath!!.length).toString()

    return m

}

