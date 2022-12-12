package com.example.movieworld.models.trailer


import com.google.gson.annotations.SerializedName

data class Trailer(
    @SerializedName("errorMessage")
    val errorMessage: String,
//    @SerializedName("fullTitle")
//    val fullTitle: String,
//    @SerializedName("imDbId")
//    val imDbId: String,
//    @SerializedName("title")
//    val title: String,
//    @SerializedName("type")
//    val type: String,
    @SerializedName("videoId")
    val videoId: String,
    @SerializedName("videoUrl")
    val videoUrl: String
)