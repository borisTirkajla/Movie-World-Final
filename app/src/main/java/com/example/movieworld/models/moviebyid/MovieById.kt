package com.example.movieworld.models.moviebyid


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class MovieById(
    @SerializedName("actorList")
    val actorList: List<Actor>?,
    @SerializedName("awards")
    val awards: String?,
    @SerializedName("boxOffice")
    val boxOffice: BoxOffice?,
    @SerializedName("directorList")
    val directorList: List<Director>?,
    @SerializedName("errorMessage")
    val errorMessage: @RawValue Any?,
    @SerializedName("fullTitle")
    val fullTitle: String?,
    @SerializedName("genreList")
    val genreList: List<Genre>?,
    @SerializedName("id")
    val id: String,
    @SerializedName("imDbRating")
    val imDbRating: String?,
    @SerializedName("image")
    val image: String,
    @SerializedName("keywordList")
    val keywordList: List<String>?,
    @SerializedName("plot")
    val plot: String?,
    @SerializedName("releaseDate")
    val releaseDate: String?,
    @SerializedName("runtimeMins")
    val runtimeMins: String?,
    @SerializedName("similars")
    val similars: List<Similar>,
    @SerializedName("starList")
    val starList: List<Star>,
    @SerializedName("title")
    val title: String?,
    @SerializedName("writerList")
    val writerList: List<Writer>?
) : Parcelable