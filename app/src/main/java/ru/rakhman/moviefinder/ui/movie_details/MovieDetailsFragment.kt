package ru.rakhman.moviefinder.ui.movie_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.movie_details_fragment.*
import kotlinx.android.synthetic.main.movie_details_fragment.movie_rating
import ru.rakhman.moviefinder.R
import ru.rakhman.moviefinder.data.Movie
import ru.rakhman.moviefinder.db.MovieDatabase
import ru.rakhman.moviefinder.db.MovieFavorite
import ru.rakhman.moviefinder.db.convertDbMoviesFavoriteToMovies
import ru.rakhman.moviefinder.db.convertToDbMovieFavorite
import ru.rakhman.moviefinder.ui.extension.CompletableExtension
import ru.rakhman.moviefinder.ui.extension.SingleExtension
import ru.rakhman.moviefinder.ui.feed.FeedFragment
import timber.log.Timber

/*private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
*/

class MovieDetailsFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var title: String? = null
    private var overview: String? = null
    private var movie_id: Int? = null
    private var voteAverage: Double? = null
    private var posterPath: String? = null
    private var releaseDate: String? = null
    private var originalTitle: String? = null
    private var originalLanguage: String? = null
    private val db by lazy { MovieDatabase.get(requireContext()).movieDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.movie_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = requireArguments().getString(FeedFragment.TILE)
        overview = requireArguments().getString(FeedFragment.OVERVIEW)
        movie_id = requireArguments().getInt(FeedFragment.MOVIE_ID)
        voteAverage = requireArguments().getDouble(FeedFragment.VOTE_AVERAGE)
        posterPath = requireArguments().getString(FeedFragment.POSTER_PATH)
        releaseDate = requireArguments().getString(FeedFragment.RELEASE_DATE)
        originalTitle = requireArguments().getString(FeedFragment.ORIGINAL_TITLE)
        originalLanguage = requireArguments().getString(FeedFragment.ORIGINAL_LANGUAGE)

        title_movie.text = title
        overview_text_view.text = overview
        original_titel.text = originalTitle
        original_language.text = originalLanguage
        movie_rating.rating = voteAverage?.div(2)?.toFloat() ?: 0.0F
        Picasso.get().load(posterPath).into(image_preview)

        // favorite_checkBox.setOnClickListener { insertIntoDb(favorite_checkBox.isChecked) }
        checkMovieFavorite()
        favorite_checkBox.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                saveMovieIntoDb()
            } else {
                deleteMovieFromFavorite()
            }
        }
    }

    private fun deleteMovieFromFavorite() {
//deleteMovieFavorite
        val movieFavorite: MovieFavorite= MovieFavorite(
        movie_id,title,posterPath,false,overview,releaseDate,
            originalTitle,originalLanguage,"",0.0,true,voteAverage
        )
        db.deleteMovieFavorite(movieFavorite)
            .compose(CompletableExtension())
            .subscribe ({ favorite_checkBox.setChecked(false) })
    }

    private fun checkMovieFavorite() {

        db.loadById(movie_id!!)
            .compose (SingleExtension())
            .subscribe({
                // The movie is not in the table 'movie_favorite'
                Timber.d("Good")
                favorite_checkBox.setChecked(true)
            },{
                //The movie exists in the table 'movie_favorite'
                favorite_checkBox.setChecked(false)
                Timber.e("Bad")})
    }

    private fun saveMovieIntoDb() {
        Timber.d("saveMovieIntoDb")

        val movie = Movie(
            isAdult = false,
            overview = overview,
            releaseDate = releaseDate,
            genreIds = listOf(1, 2, 3),
            id = movie_id,
            originalTitle = originalTitle,
            originalLanguage = originalLanguage,
            title = title,
            backdropPath = "",
            popularity = 0.0,
            voteCount = null,
            video = true,
            voteAverage = voteAverage
        )

        val convMovie: MovieFavorite = convertToDbMovieFavorite(movie, posterPath!!)
        val listConvMovie = listOf(convMovie)
        db.saveMovieFavorite(listConvMovie)
            .compose(CompletableExtension())
            .subscribe()
    }
}
/*companion object {

    @JStatic
    fun newInstance(param1: String, param2: String) =
        MovieDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
}*/
