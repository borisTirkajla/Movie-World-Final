package com.example.movieworld.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieworld.models.moviebyid.MovieById
import com.example.movieworld.util.Constants.FAVORITE_MOVIES_TABLE

@Entity(tableName = FAVORITE_MOVIES_TABLE)
class FavoriteMovieEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var trailerUrl: String? = null,
    var movie: MovieById
)