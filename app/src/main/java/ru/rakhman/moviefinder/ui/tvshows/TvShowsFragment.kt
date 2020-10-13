package ru.rakhman.moviefinder.ui.tvshows

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.rakhman.moviefinder.BuildConfig
import ru.rakhman.moviefinder.R
import ru.rakhman.moviefinder.data.Movie
import ru.rakhman.moviefinder.data.MoviesResponse
import ru.rakhman.moviefinder.network.MovieApiClient
import ru.rakhman.moviefinder.ui.afterTextChanged
import ru.rakhman.moviefinder.ui.feed.FeedFragment
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

        // Добавляем recyclerView
        movies_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        // Запросы по фильмам
        var moviesList:List<Movie> //= listOf("")
        val getPopularTvShows= MovieApiClient.apiClient.getPopularTvShows(BuildConfig.THE_MOVIE_DATABASE_API,LANG)
        val getOnTheAirTvShows= MovieApiClient.apiClient.getOnTheAirTvShows(BuildConfig.THE_MOVIE_DATABASE_API,LANG)


        // Получаем список телесериалов
        getPopularTvShows.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                // Получаем результат
                moviesList=response.body()!!.results
                moviesList.forEach { moviesList->Timber.d(moviesList.title.orEmpty()) }
                val movies= listOf(
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
                movies_recycler_view.adapter =adapter.apply { addAll(movies) }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                //Логируем ошибки
                Timber.e(t.toString())
            }
        })

        // Получаем список телесериалов, которые сейчас в эфире
        getOnTheAirTvShows.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                // Получаем результат
                moviesList=response.body()!!.results
                moviesList.forEach { moviesList->Timber.d(moviesList.title.orEmpty()) }
                val movies= listOf(
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
                movies_recycler_view.adapter =adapter.apply { addAll(movies) }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                //Логируем ошибки
                Timber.e(t.toString())
            }
        })
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
        const val SEARCH="search"
        const val LANG = "ru"
    }
}