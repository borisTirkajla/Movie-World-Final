package com.example.movieworld.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.movieworld.data.Repository
import com.example.movieworld.data.database.entities.FavoriteMovieEntity
import com.example.movieworld.models.moviebyid.MovieById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FavoriteMoviesViewModel"

@HiltViewModel
class FavoriteMoviesViewModel @Inject constructor(
    application: Application, private val repository: Repository
) : AndroidViewModel(application) {
    var readFavoriteMovies: LiveData<List<FavoriteMovieEntity>> =
        repository.local.readFavoritesMovies().asLiveData()

    fun insertFavoriteMovie(movie: MovieById) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteMovies(movie)
        }

    fun deleteFavoriteMovie(movie: MovieById) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteMovie(movie)
        }

    fun deleteAllFavoriteMovies() = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteAllFavoriteMovies()
    }
}