package com.example.movieworld.models.moviesearch


import com.google.gson.annotations.SerializedName

data class MovieBySearch(
    @SerializedName("errorMessage")
    val errorMessage: String,
    @SerializedName("expression")
    val expression: String,
    @SerializedName("results")
    val results: List<Result>
)