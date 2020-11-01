package ru.rakhman.moviefinder.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.rakhman.moviefinder.BuildConfig
import ru.rakhman.moviefinder.R
import ru.rakhman.moviefinder.network.MovieApiClient
import ru.rakhman.moviefinder.ui.extension.ObservableExtension
import ru.rakhman.moviefinder.ui.extension.SingleExtension
import ru.rakhman.moviefinder.ui.feed.FeedFragment
import ru.rakhman.moviefinder.ui.feed.MainCardContainer
import ru.rakhman.moviefinder.ui.feed.MovieItem
import ru.rakhman.moviefinder.ui.onTextChangedObservable
import timber.log.Timber
import java.util.concurrent.TimeUnit


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {
    private val language by lazy { resources.getString(R.string.language) }
    private var param1: String? = null
    private var param2: String? = null

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }
    private var compositeDisposable = CompositeDisposable()

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
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchTerm = requireArguments().getString("search")
        search_toolbar.setText(searchTerm)
        // Запрос по фильмам
        if (searchTerm != null) {
            getSearchMovies(searchTerm)
        }
        executeSearch()
    }

    private fun getSearchMovies(searchText: String) {
        // search function
        compositeDisposable.add(
            MovieApiClient.apiClient.searchByQuery(query = searchText)
                .map{moviesList->moviesList.results}
                .doOnError { Timber.e("getSearchMovies was started Error: $it.toString()") }
                .compose(SingleExtension())
                .doOnSubscribe { progress_bar.visibility = View.VISIBLE }
                .doOnTerminate { progress_bar.visibility = View.GONE }
                .subscribe({moviesList->
                    moviesList.forEach { m -> Timber.d(m.title.orEmpty()) }
                    movies_recycler_view.adapter = MoviesAdapter(moviesList, R.layout.list_item_movie)
                }, {
                    Timber.e(it.toString())
                })
        )
    }

    private fun executeSearch() {
        // response function for text input
        compositeDisposable.add(search_toolbar.search_edit_text
            .onTextChangedObservable()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .filter { it.length > 3 }
            .debounce(500, TimeUnit.MILLISECONDS)
            .compose(ObservableExtension())
            .doOnNext { Timber.d("request has started") }
            .subscribe({
                getSearchMovies(it)
            }, {
                Timber.e(it.toString())
            })
        )
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}