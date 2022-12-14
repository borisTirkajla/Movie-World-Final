package com.example.movieworld.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieworld.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrailerViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val trailerUrl: MutableLiveData<String?> =
        MutableLiveData()

    var shouldPlayTrailer = false

    fun getTrailerById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.remote.getTrailerById(id)
            if (response.isSuccessful) {
                response.body()?.let { trailer ->
                    if (trailer.videoUrl.isNotEmpty()) {
                        trailerUrl.postValue(trailer.videoUrl)
                        saveTrailerUrlForMovieById(
                            movieId = id,
                            trailerUrl = trailer.videoUrl
                        )
                    } else {
                        trailerUrl.postValue(null)
                    }
                }
            }
        }
    }

    fun setTrailerById(id: String) {
        trailerUrl.postValue(id)
    }

    private fun saveTrailerUrlForMovieById(movieId: String, trailerUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.updateMovieWithTrailerUrl(
                movieId = movieId,
                trailerUrl = trailerUrl
            )
        }
    }

}