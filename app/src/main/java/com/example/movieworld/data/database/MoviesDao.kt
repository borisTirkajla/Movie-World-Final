package com.example.movieworld.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieworld.data.database.entities.FavoriteMovieEntity
import com.example.movieworld.data.database.entities.MoviesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(moviesEntity: MoviesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovies(favoriteMovie: FavoriteMovieEntity)

    @Query("SELECT * FROM movies_table ORDER BY id ASC")
    fun readMovies(): Flow<List<MoviesEntity>>

    @Query("SELECT * FROM favorite_movies_table ORDER BY id ASC")
    fun readFavoriteMovies(): Flow<List<FavoriteMovieEntity>>

    @Delete
    suspend fun deleteFavoriteMovie(favoriteMovie: FavoriteMovieEntity)

    @Query("DELETE FROM favorite_movies_table")
    suspend fun deleteAllFavoriteMovies()

    @Query("UPDATE favorite_movies_table SET trailerUrl=:trailerUrl WHERE id = :movieId")
    abstract fun updateMovieWithTrailerUrl(movieId: String, trailerUrl: String)

    @Query("SELECT trailerUrl FROM favorite_movies_table WHERE id=:id")
    suspend fun readMovieTrailerUrl(id: String?): String?
}