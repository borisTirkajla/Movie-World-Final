package com.example.movieworld.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.movieworld.BuildConfig
import com.example.movieworld.data.Repository
import com.example.movieworld.models.moviesearch.MovieBySearch
import com.example.movieworld.util.Constants
import com.example.movieworld.util.NetworkListener
import com.example.movieworld.util.NetworkResponse
import com.example.movieworld.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val TAG = "SearchMovieViewModel"
@HiltViewModel
class SearchMovieViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    var searchedMovieResponse: MutableLiveData<NetworkResult<MovieBySearch>> =
        MutableLiveData()

    fun searchMovie(searchQuery: Map<String, Any>) {
        viewModelScope.launch {
            searchMovieSafeCall(searchQuery)
        }
    }

    private suspend fun searchMovieSafeCall(searchQuery: Map<String, Any>) {
        searchedMovieResponse.value = NetworkResult.Loading()
        if (NetworkListener.hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.searchMovie(searchQuery = searchQuery)
                Log.d(TAG,response.toString())
                searchedMovieResponse.value = NetworkResponse.handleMovieBySearchResponse(response)
            } catch (e: Exception) {
                searchedMovieResponse.value = NetworkResult.Error(e.localizedMessage)
            }
        } else {
            searchedMovieResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, Any> {
        val queries: HashMap<String, Any> = HashMap()

        queries[Constants.QUERY_API_KEY] = BuildConfig.IMDB_API_KEY
        queries[Constants.QUERY_SEARCH] = searchQuery

        return queries
    }
}