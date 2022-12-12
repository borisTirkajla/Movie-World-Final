package com.example.movieworld.data

import com.example.movieworld.BuildConfig
import com.example.movieworld.data.network.ImdbApi
import com.example.movieworld.models.moviebygenre.Movie
import com.example.movieworld.models.moviebygenre.MovieListResponse
import com.example.movieworld.models.moviebyid.MovieById
import com.example.movieworld.models.moviesearch.MovieBySearch
import com.example.movieworld.models.trailer.Trailer
import com.example.movieworld.util.Constants
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val imdbApi: ImdbApi
) {

    suspend fun getMovieByGenre(queries: Map<String, Any>): Response<MovieListResponse> {
        return imdbApi.getMovieByGenre(queries)
    }

    suspend fun searchMovie(searchQuery: Map<String, Any>): Response<MovieBySearch> {
        return imdbApi.searchMovies(searchQuery)
    }

    suspend fun getMovieById(id:String): Response<MovieById> {
        return imdbApi.getMovieDetails(BuildConfig.IMDB_API_KEY,id)
    }

    suspend fun getTrailerById(id: String): Response<Trailer> {
        return imdbApi.getTrailerById(BuildConfig.IMDB_API_KEY,id)
    }
}