package com.example.movieworld.models.moviebyid


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Similar(
    @SerializedName("id")
    val id: String,
    @SerializedName("imDbRating")
    val imDbRating: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("title")
    val title: String
) : Parcelable