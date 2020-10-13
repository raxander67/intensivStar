package ru.rakhman.moviefinder.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
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
import ru.rakhman.moviefinder.data.MockRepository
import ru.rakhman.moviefinder.data.Movie
import ru.rakhman.moviefinder.data.MoviesResponse
import ru.rakhman.moviefinder.network.MovieApiClient
import ru.rakhman.moviefinder.ui.afterTextChanged
import timber.log.Timber

class FeedFragment : Fragment() {
    private val TAG = "FeedFragment"
    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Добавляем recyclerView
        movies_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        search_toolbar.search_edit_text.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > 3) {
                openSearch(it.toString())
            }
        }

        // Запросы по фильмам
        var moviesList: List<Movie> //= listOf("")
        val getNowPlayedMovies = MovieApiClient.apiClient.getNowPlayedMovies(BuildConfig.THE_MOVIE_DATABASE_API, LANG)
        val getUpcomingMovies = MovieApiClient.apiClient.getUpcomingMovies(BuildConfig.THE_MOVIE_DATABASE_API, LANG)
        val getPopularMovies = MovieApiClient.apiClient.getPopularMovies(BuildConfig.THE_MOVIE_DATABASE_API, LANG)

        // Получаем список текущих фильмов
        getNowPlayedMovies.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                // Получаем результат
                moviesList = response.body()!!.results
                moviesList.forEach { moviesList -> Timber.d(moviesList.title.orEmpty()) }
                val movies = listOf(
                    MainCardContainer(
                        R.string.now_played,
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

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                //Логируем ошибки
                Log.e(TAG, t.toString())
            }
        })

        //Получение новинок
        getUpcomingMovies.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                // Получаем результат
                moviesList = response.body()!!.results
                moviesList.forEach { moviesList -> Timber.d(moviesList.title.orEmpty()) }
                val movies = listOf(
                    MainCardContainer(
                        R.string.upcoming,
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

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                //Логируем ошибки
                Log.e(TAG, t.toString())
            }
        })

        //Получение популярных фильмов
        getPopularMovies.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                // Получаем результат
                moviesList = response.body()!!.results
                moviesList.forEach { moviesList -> Timber.d(moviesList.title.orEmpty()) }
                val movies = listOf(
                    MainCardContainer(
                        R.string.popular,
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

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                //Логируем ошибки
                Log.e(TAG, t.toString())
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

    private fun openSearch(searchText: String) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val bundle = Bundle()
        bundle.putString(SEARCH, searchText)
        findNavController().navigate(R.id.search_dest, bundle, options)
    }

    override fun onStop() {
        super.onStop()
        search_toolbar.clear()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    companion object {

        const val TILE="title"
        const val SEARCH="search"
        const val LANG = "ru"
    }
}