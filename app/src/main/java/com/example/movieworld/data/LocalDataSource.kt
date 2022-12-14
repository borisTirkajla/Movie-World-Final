package com.example.movieworld.data

import com.example.movieworld.data.database.MoviesDao
import com.example.movieworld.data.database.entities.FavoriteMovieEntity
import com.example.movieworld.data.database.entities.MoviesEntity
import com.example.movieworld.models.moviebyid.MovieById
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val moviesDao: MoviesDao
) {

    fun readMovies(): Flow<List<MoviesEntity>> {
        return moviesDao.readMovies()
    }

    fun readFavoritesMovies(): Flow<List<FavoriteMovieEntity>> {
        return moviesDao.readFavoriteMovies()
    }


    suspend fun insertMovies(moviesEntity: MoviesEntity) {
        moviesDao.insertMovies(moviesEntity)
    }

    fun updateMovieWithTrailerUrl(movieId: String, trailerUrl: String) {
        moviesDao.updateMovieWithTrailerUrl(movieId, trailerUrl)
    }

    suspend fun readMovieTrailerUrl(id: String?): String? {
        return moviesDao.readMovieTrailerUrl(id)
    }

    suspend fun insertFavoriteMovies(movie: MovieById) {
        moviesDao.insertFavoriteMovies(
            FavoriteMovieEntity(
                id = movie.id,
                movie = movie
            )
        )
    }

    suspend fun deleteFavoriteMovie(movie: MovieById) {
        moviesDao.deleteFavoriteMovie(
            FavoriteMovieEntity(
                id = movie.id,
                movie = movie
            )
        )
    }

    suspend fun deleteAllFavoriteMovies() {
        moviesDao.deleteAllFavoriteMovies()
    }
}