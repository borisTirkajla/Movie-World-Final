package com.example.movieworld.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movieworld.data.database.entities.FavoriteMovieEntity
import com.example.movieworld.data.database.entities.MoviesEntity

@Database(
    entities = [MoviesEntity::class, FavoriteMovieEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(MoviesTypeConverter::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}