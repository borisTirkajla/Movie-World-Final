package com.example.movieworld.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieworld.models.moviebygenre.MovieListResponse
import com.example.movieworld.util.Constants.MOVIES_TABLE

@Entity(tableName = MOVIES_TABLE)
class MoviesEntity(var movieList: MovieListResponse) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}