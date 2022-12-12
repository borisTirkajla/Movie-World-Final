package com.example.movieworld.models.moviebyid


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Writer(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
) : Parcelable