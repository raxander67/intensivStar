package ru.rakhman.moviefinder.ui.feed

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_fragment.movies_recycler_view
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.rakhman.moviefinder.BuildConfig
import ru.rakhman.moviefinder.R
import ru.rakhman.moviefinder.data.Movie
import ru.rakhman.moviefinder.network.MovieApiClient
import ru.rakhman.moviefinder.ui.onTextChangedObservable
import ru.rakhman.moviefinder.ui.search.MoviesAdapter
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
            .map{it.trim()}
            .filter { it.isNotEmpty() }
            .filter{ it.length > 3}
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                openSearch(it.toString())
            }, {
                Timber.e(it.toString() )
            })


        // Запросы по фильмам
        val getNowPlayedMovies = MovieApiClient.apiClient.getNowPlayedMovies(BuildConfig.THE_MOVIE_DATABASE_API, LANG)
        val getUpcomingMovies = MovieApiClient.apiClient.getUpcomingMovies(BuildConfig.THE_MOVIE_DATABASE_API, LANG)
        val getPopularMovies = MovieApiClient.apiClient.getPopularMovies(BuildConfig.THE_MOVIE_DATABASE_API, LANG)


        // Получаем список текущих фильмов
        getNowPlayedMovies
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { // Получаем результат
                        it-> val moviesList=it.results
                    moviesList.forEach { m -> Timber.d(m.title.orEmpty()) }
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
                ,
                {
                    //error
                    error->Timber.d(error.toString())
                }
            )

        //Получение новинок
        getUpcomingMovies
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { // Получаем результат
                        it-> val moviesList=it.results
                    moviesList.forEach { m -> Timber.d(m.title.orEmpty()) }
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
                ,
                {
                    //error
                        error->Timber.d(error.toString())
                }
            )

        //Получение популярных фильмов
        getPopularMovies
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { // Получаем результат
                        it-> val moviesList=it.results
                    moviesList.forEach { m -> Timber.d(m.title.orEmpty()) }
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