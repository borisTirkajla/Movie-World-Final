package com.example.movieworld.models.moviebygenre


import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    @SerializedName("errorMessage")
    val errorMessage: String?,
    @SerializedName("results")
    val movies: List<Movie>
)