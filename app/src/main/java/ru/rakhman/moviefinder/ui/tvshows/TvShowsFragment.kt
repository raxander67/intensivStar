package ru.rakhman.moviefinder.ui.tvshows

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_fragment.*
import ru.rakhman.moviefinder.BuildConfig
import ru.rakhman.moviefinder.R
import ru.rakhman.moviefinder.data.Movie
import ru.rakhman.moviefinder.network.MovieApiClient
import ru.rakhman.moviefinder.ui.feed.MainCardContainer
import ru.rakhman.moviefinder.ui.feed.MovieItem
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TvShowsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private val adapter by lazy {
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
        return inflater.inflate(R.layout.tv_shows_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Запросы по фильмам
        val getPopularTvShows= MovieApiClient.apiClient.getPopularTvShows(BuildConfig.THE_MOVIE_DATABASE_API,LANG)
        val getOnTheAirTvShows= MovieApiClient.apiClient.getOnTheAirTvShows(BuildConfig.THE_MOVIE_DATABASE_API,LANG)


        // Получаем список телесериалов
        getPopularTvShows
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { // Получаем результат
                        it-> val moviesList=it.results
                    moviesList.forEach { m -> Timber.d(m.title.orEmpty()) }
                    val movies = listOf(
                        MainCardContainer(
                            R.string.tv_show_popular,
                            moviesList.map {
                                MovieItem(it) { movie ->
                                    openMovieDetails(
                                        movie
                                    )
                                }
                            }.toList()
                        )
                    )
                    movies_recycler_view.adapter = adapter.apply { addAll(movies) }
                }
                ,
                {
                    //error
                        error->Timber.d(error.toString())
                }
            )

        // Получаем список телесериалов, которые сейчас в эфире
        getOnTheAirTvShows
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { // Получаем результат
                        it-> val moviesList=it.results
                    moviesList.forEach { m -> Timber.d(m.title.orEmpty()) }
                    val movies = listOf(
                        MainCardContainer(
                            R.string.tv_show_on_air,
                            moviesList.map {
                                MovieItem(it) { movie ->
                                    openMovieDetails(
                                        movie
                                    )
                                }
                            }.toList()
                        )
                    )
                    movies_recycler_view.adapter = adapter.apply { addAll(movies) }
                }
                ,
                {
                    //error
                        error->Timber.d(error.toString())
                }
            )
    }

    private fun openMovieDetails(movie: Movie) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val bundle = Bundle()
        bundle.putString(TILE, movie.title)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TvShowsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        const val TILE="title"
        const val LANG = "ru"
    }
}