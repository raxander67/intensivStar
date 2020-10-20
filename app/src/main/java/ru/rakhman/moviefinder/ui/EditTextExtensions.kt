package ru.rakhman.moviefinder.ui

import android.text.Editable
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.search_toolbar.view.*



fun EditText.afterTextChanged(action: (s: Editable?) -> Unit) =
    addTextChangedListener(afterTextChanged = action)

fun EditText.onTextChangedObservable(): Observable<String>{
    return Observable.create(ObservableOnSubscribe<String> { subscriber ->
        doAfterTextChanged { text ->
            subscriber.onNext(text.toString())
        }
    })
}