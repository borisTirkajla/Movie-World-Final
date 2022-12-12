package com.example.movieworld.data.network

import com.example.movieworld.models.moviebygenre.Movie
import com.example.movieworld.models.moviesearch.MovieBySearch
import com.example.movieworld.models.moviebygenre.MovieListResponse
import com.example.movieworld.models.moviebyid.MovieById
import com.example.movieworld.models.trailer.Trailer
import com.example.movieworld.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ImdbApi {

//    @GET("AdvancedSearch/k_gs2oc4v0?genres=horror")
    @GET("AdvancedSearch/")
    @JvmSuppressWildcards
    suspend fun getMovieByGenre(
        @QueryMap queries : Map<String,Any>
    ): Response<MovieListResponse>

    @GET("SearchMovie/")
    @JvmSuppressWildcards
    suspend fun searchMovies(@QueryMap searchQuery : Map<String,Any>
    ): Response<MovieBySearch>

//    @GET("Title/")
//    @JvmSuppressWildcards
//    suspend fun getMovieById(
//    ): Response<MovieById>

    @GET("Title/")
    @JvmSuppressWildcards
    suspend fun getMovieDetails(
        @Query("id") id: String,
        @Query("apiKey") apiKey: String
    ): Response<MovieById>

    @GET("YouTubeTrailer/")
    suspend fun getTrailerById(
        @Query("apiKey") apiKey: String,
        @Query("id") id: String
    ): Response<Trailer>
}