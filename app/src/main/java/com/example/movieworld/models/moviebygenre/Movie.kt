package com.example.movieworld.models.moviebygenre


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    @SerializedName("description")
    val description: String? = null,
//    @SerializedName("genres")
//    val genres: String,
    @SerializedName("id")
    val id: String,
//    @SerializedName("imDbRating")
//    val imDbRating: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("plot")
    val plot: String? = null,
//    @SerializedName("runtimeStr")
//    val runtimeStr: String,
//    @SerializedName("starList")
//    val starList: List<Star>,
    @SerializedName("title")
    val title: String
) : Parcelable