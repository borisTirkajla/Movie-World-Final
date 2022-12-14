package com.example.movieworld.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.movieworld.data.Repository
import com.example.movieworld.data.database.entities.FavoriteMovieEntity
import com.example.movieworld.data.database.entities.MoviesEntity
import com.example.movieworld.models.moviebygenre.MovieListResponse
import com.example.movieworld.util.NetworkListener
import com.example.movieworld.util.NetworkResponse
import com.example.movieworld.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    /**ROOM DATABASE*/
    val readMovies: LiveData<List<MoviesEntity>> = repository.local.readMovies().asLiveData()

    private fun insertMovies(moviesEntity: MoviesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertMovies(moviesEntity)
        }
    }


    /**RETROFIT*/
    var movieResponse: MutableLiveData<NetworkResult<MovieListResponse>> = MutableLiveData()

    fun getMovieByGenre(queries: Map<String, Any>) {
        viewModelScope.launch {
            getMovieByGenreSafeCall(queries)
        }
    }


    private suspend fun getMovieByGenreSafeCall(queries: Map<String, Any>) {
        movieResponse.value = NetworkResult.Loading()
        if (NetworkListener.hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.getMovieByGenre(queries = queries)
                movieResponse.value = NetworkResponse.handleMovieListResponse(response)
                val movieListResponse = movieResponse.value?.data
                if (movieListResponse != null) {
                    offlineCacheMovies(movieListResponse)
                }

            } catch (e: Exception) {
                movieResponse.value = NetworkResult.Error(e.localizedMessage)
            }
        } else {
            movieResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheMovies(movieListResponse: MovieListResponse) {
        val moviesEntity = MoviesEntity(movieListResponse)
        insertMovies(moviesEntity)
    }
}