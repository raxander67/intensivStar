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
import ru.rakhman.moviefinder.db.convertToDbMovieFavorite
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TILE = "title"
private const val OVERVIEW = "overview"
private const val MOVIE_ID = "movie_id"
private const val VOTE_AVERAGE = "voteAverage"
private const val POSTER_PATH="posterPath"
private const val RELEASE_DATE="releaseDate"
private const val ORIGINAL_TITLE="originalTitle"
private const val ORIGINAL_LANGUAGE="originalLanguage"

class MovieDetailsFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var title: String? = null
    private var overview: String? = null
    private var movie_id: Int? = null
    private var voteAverage:Double?=null
    private var posterPath: String? = null
    private var releaseDate: String? = null
    private var originalTitle: String? = null
    private var originalLanguage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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

        title = requireArguments().getString(TILE)
        overview = requireArguments().getString(OVERVIEW)
        movie_id = requireArguments().getInt(MOVIE_ID)
        voteAverage= requireArguments().getDouble(VOTE_AVERAGE)
        posterPath= requireArguments().getString(POSTER_PATH)
        releaseDate= requireArguments().getString(RELEASE_DATE)
        originalTitle= requireArguments().getString(ORIGINAL_TITLE)
        originalLanguage= requireArguments().getString(ORIGINAL_LANGUAGE)



        title_movie.text = title
        overview_text_view.text = overview
        original_titel.text=originalTitle
        original_language.text=originalLanguage
        movie_rating.rating=voteAverage?.div(2)?.toFloat() ?: 0.0F
        image_preview
       // val image_movie: ImageView = findViewById<ImageView>(R.id.image_preview)
        Picasso.get().load(posterPath).into(image_preview)

        favorite_checkBox.setOnClickListener { onCheckboxClicked() }
    }

    fun onCheckboxClicked() {
        Timber.d("writed")
        val checked: Boolean = favorite_checkBox.isChecked
//        val context: Context? = getContext()
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
        val db = MovieDatabase.get(requireContext())
            .movieDao()/*context?.let { MovieDatabase.get(it).movieDao() }*/
        if (checked) {
//            Toast.makeText( context,"Test", Toast.LENGTH_LONG ).show()

            val convMovie: MovieFavorite = convertToDbMovieFavorite(movie, posterPath!!)
            val listConvMovie = listOf(convMovie)
            db.saveMovieFavorite(listConvMovie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}