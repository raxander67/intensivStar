package ru.rakhman.moviefinder.ui.feed

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.feed_fragment.movies_recycler_view
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.rakhman.moviefinder.R
import ru.rakhman.moviefinder.data.Movie
import ru.rakhman.moviefinder.data.MoviesResponse
import ru.rakhman.moviefinder.network.MovieApiClient
import ru.rakhman.moviefinder.ui.onTextChangedObservable
import ru.rakhman.moviefinder.ui.extension.ObservableExtension
import ru.rakhman.moviefinder.ui.extension.SingleExtension
import timber.log.Timber
import java.util.concurrent.TimeUnit

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
        // реагируем на ввод поискового запроса
        search_toolbar.search_edit_text
            .onTextChangedObservable()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .filter { it.length > 3 }
            .debounce(500, TimeUnit.MILLISECONDS)
            .compose (ObservableExtension())
            .subscribe({
                openSearch(it.toString())
            }, {
                Timber.e(it.toString())
            })


        // Запросы по фильмам
        val getNowPlayedMovies = MovieApiClient.apiClient.getNowPlayedMovies()
        val getUpcomingMovies = MovieApiClient.apiClient.getUpcomingMovies()
        val getPopularMovies = MovieApiClient.apiClient.getPopularMovies()


        // Получаем список текущих фильмов
        getNowPlayedMovies
            .compose (SingleExtension())
            .subscribe(
                // Получаем результат
                getQueryToView(R.string.now_played)
                ,
                //error
                errorLog()
            )

        //Получение новинок
        getUpcomingMovies
            .compose (SingleExtension())
            .subscribe(
                // Получаем результат
                getQueryToView(R.string.upcoming),
                //error
                errorLog()
            )

        //Получение популярных фильмов
        getPopularMovies
            .compose (SingleExtension())
            .subscribe(
                // Получаем результат
                getQueryToView(R.string.popular)
                ,
                //error
                errorLog()
            )
    }

    private fun errorLog(): (t: Throwable) -> Unit {
        return {
                error ->
            Timber.d(error.toString())
        }
    }

    private fun getQueryToView(rString:Int): (t: MoviesResponse) -> Unit {
        return {
                it ->
            val moviesList = it.results
            moviesList.forEach { m -> Timber.d(m.title.orEmpty()) }
            val movies = listOf(
                MainCardContainer(
                    rString,
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

        const val TILE = "title"
        const val SEARCH = "search"
    }
}