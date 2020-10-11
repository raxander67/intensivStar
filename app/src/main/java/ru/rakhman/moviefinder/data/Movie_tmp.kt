package ru.rakhman.moviefinder.data

import com.google.gson.annotations.SerializedName

class Movie_tmp(
    var title: String? = "",
    var voteAverage: Double = 0.0
) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}
