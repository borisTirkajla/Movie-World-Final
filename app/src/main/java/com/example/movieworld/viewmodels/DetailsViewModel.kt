package com.example.movieworld.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.movieworld.BuildConfig
import com.example.movieworld.data.Repository
import com.example.movieworld.models.moviebyid.MovieById
import com.example.movieworld.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.set

private const val TAG = "DetailsViewModel"

@HiltViewModel
class DetailsViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    val movieResponse: MutableLiveData<NetworkResult<MovieById>> =
        MutableLiveData()

    var movieId: String? = null
//    val trailerUrl: MutableLiveData<UrlResult<String>> = MutableLiveData()
var trailerUrl: UrlResult<String> = UrlResult.Loading()

    fun findMovieById(id: String) {
        viewModelScope.launch {
            searchMovieSafeCall(id)
        }
    }

    private suspend fun searchMovieSafeCall(id: String) {
        movieResponse.value = NetworkResult.Loading()
        if (NetworkListener.hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.getMovieById(id)
                Log.d(TAG, response.toString())
                movieResponse.value = NetworkResponse.handleMovieByIdResponse(response)
//                movieId = movieResponse.value?.data?.id
            } catch (e: Exception) {
                movieResponse.value = NetworkResult.Error(e.localizedMessage)
                e.localizedMessage?.let { Log.d(TAG, it) }
            }
        } else {
            movieResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }



    fun readMovieTrailerUrl() {
        movieResponse.value?.data?.id?.let {
            viewModelScope.launch {
                val url = repository.local.readMovieTrailerUrl(it)
                trailerUrl = if (url.isNullOrEmpty()) {
                    UrlResult.NoUrl("There is no url saved for this movie")
                } else {
                    UrlResult.HasUrl(url)
                }
            }
        }
    }

    fun applySearchQuery(movieId: String): HashMap<String, Any> {
        val queries: HashMap<String, Any> = HashMap()

        queries[Constants.QUERY_API_KEY] = BuildConfig.IMDB_API_KEY
        queries[Constants.QUERY_ID] = movieId

        return queries
    }


    fun setMovieResponseFromLocalDatabase(movie: MovieById) {
        movieResponse.value = NetworkResult.Success(movie)
    }

}

