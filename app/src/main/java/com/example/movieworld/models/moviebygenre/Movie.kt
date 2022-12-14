package com.example.movieworld.models.moviebygenre


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("plot")
    val plot: String? = null,
    @SerializedName("title")
    val title: String
) : Parcelable