package ru.rakhman.moviefinder.ui.watchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_watchlist.movies_recycler_view
import ru.rakhman.moviefinder.R
import ru.rakhman.moviefinder.db.MovieDAO
import ru.rakhman.moviefinder.db.MovieDatabase
import ru.rakhman.moviefinder.db.convDbMovFavToMovie
import ru.rakhman.moviefinder.db.convertDbMoviesFavoriteToMovies
import ru.rakhman.moviefinder.ui.extension.CompletableExtension
import ru.rakhman.moviefinder.ui.extension.ObservableExtension
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class WatchlistFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

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
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movies_recycler_view.layoutManager = GridLayoutManager(context, 4)
        movies_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        val db: MovieDatabase = MovieDatabase.get(requireContext())
        db.movieDao()
            .getMovieFavorite()
            .map { convertDbMoviesFavoriteToMovies(it) }
            .compose(ObservableExtension())
            .subscribe({
                val moviesList =
                    it.map {
                        MoviePreviewItem(
                            it
                        ) { movie -> }
                    }.toList()
                adapter.clear()
                movies_recycler_view.adapter = adapter.apply { addAll(moviesList) }
                Timber.d("Success")
            },
                {
                    Timber.e("Error $it")
                })

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            WatchlistFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}