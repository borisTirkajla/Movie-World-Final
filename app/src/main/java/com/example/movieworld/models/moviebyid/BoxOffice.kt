package com.example.movieworld.models.moviebyid


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BoxOffice(
    @SerializedName("budget")
    val budget: String?
) : Parcelable