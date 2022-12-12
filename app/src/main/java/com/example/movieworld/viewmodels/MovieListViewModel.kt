package com.example.movieworld.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.movieworld.BuildConfig
import com.example.movieworld.data.DataStoreRepository
import com.example.movieworld.ui.fragments.movielist.GenresEnum
import com.example.movieworld.util.Constants
import com.example.movieworld.util.Constants.DEFAULT_GENRE
import com.example.movieworld.util.Constants.QUERY_API_KEY
import com.example.movieworld.util.Constants.QUERY_GENRES
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    var genreType = DEFAULT_GENRE
    val readGenreType = dataStoreRepository.readGenreType()
    private var query: String? = null
    var networkStatus = false
    var backOnline = false

    val readBackOnline = dataStoreRepository.readBackOnline().asLiveData()

    fun saveGenreType(selectedGenreId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveGenreType(
                selectedGenreId = selectedGenreId
            )
        }
    }

    fun saveBackOnline(backOnline: Boolean) {
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveBackOnline(backOnline)
        }
    }

    fun applyQueries(query: String? = null): HashMap<String, Any> {
        val queries: HashMap<String, Any> = HashMap()

        viewModelScope.launch {
            readGenreType.collect { value ->
                genreType = GenresEnum.values()[value.selectedGenreId]
            }
        }

        if (query != null) {
            this.query = query
        }

        queries[QUERY_API_KEY] = BuildConfig.IMDB_API_KEY
        queries[QUERY_GENRES] = this.query ?: genreType.genre

        return queries
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(),"No Internet Connection.",Toast.LENGTH_LONG).show()
            saveBackOnline(true)
        }else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(),"We are online",Toast.LENGTH_LONG).show()
                saveBackOnline(false)
            }
        }
    }
}