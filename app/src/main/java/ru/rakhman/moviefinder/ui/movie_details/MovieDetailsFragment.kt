package ru.rakhman.moviefinder.ui.movie_details

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import kotlinx.android.synthetic.main.movie_details_fragment.*
import ru.rakhman.moviefinder.R
import ru.rakhman.moviefinder.data.Movie
import ru.rakhman.moviefinder.db.MovieDatabase
import ru.rakhman.moviefinder.db.MovieEntity
import ru.rakhman.moviefinder.db.convertMovie

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TILE = "title"
private const val OVERVIEW = "overview"

class MovieDetailsFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var title: String? = null
    private var overview: String? = null

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

        title_movie.text = title
        overview_text_view.text = overview

        favorite_checkBox.setOnClickListener { onCheckboxClicked() }
    }

    fun onCheckboxClicked() {

        val checked: Boolean = favorite_checkBox.isChecked
        val context: Context? = getContext()
        val movie = Movie(
            isAdult = false,
            overview = overview,
            releaseDate = "",
            genreIds = listOf<Int>(1, 2, 3),
            id = 123,
            originalTitle = "",
            originalLanguage = "",
            title = title,
            backdropPath = "",
            popularity = 5.7,
            voteCount = null,
            video = true,
            voteAverage = 5.9
        )
        val db = context?.let { MovieDatabase.get(it).movieDao() }
        /*if (checked) {
            Toast.makeText(
                context,
                "Test",
                Toast.LENGTH_LONG
            ).show()*/
        val convMovie: MovieEntity = convertMovie(movie)
        val listConvMovie = listOf<MovieEntity>(convMovie)
        if (db != null) {
            db.save(listConvMovie)
        } else {
            // delete this

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