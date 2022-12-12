package com.example.movieworld.data.database

import androidx.room.TypeConverter
import com.example.movieworld.models.moviebygenre.MovieListResponse
import com.example.movieworld.models.moviebyid.MovieById
import com.google.gson.Gson

class MoviesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun movieListResponseToString(movie: MovieListResponse): String {
        return gson.toJson(movie)
    }

    @TypeConverter
    fun stringToMovieListResponse(data: String): MovieListResponse {
        return gson.fromJson(data, MovieListResponse::class.java)
    }

    @TypeConverter
    fun movieByIdToString(movie: MovieById): String {
        return gson.toJson(movie)
    }

    @TypeConverter
    fun stringToMovieById(data: String): MovieById{
        return gson.fromJson(data,MovieById::class.java)
    }
}