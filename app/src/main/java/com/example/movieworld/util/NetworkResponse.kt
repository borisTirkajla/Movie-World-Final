package com.example.movieworld.util

import com.example.movieworld.models.moviebygenre.MovieListResponse
import com.example.movieworld.models.moviebyid.MovieById
import com.example.movieworld.models.moviesearch.MovieBySearch
import retrofit2.Response

class NetworkResponse {
    companion object {

        fun handleMovieByIdResponse(response: Response<MovieById>): NetworkResult<MovieById> {
            return when {
                response.isSuccessful -> {
                    NetworkResult.Success(response.body()!!)
                }
                else -> NetworkResult.Error(response.message())
            }
        }

        fun handleMovieListResponse(response: Response<MovieListResponse>): NetworkResult<MovieListResponse> {
            return when {
                response.body()?.movies.isNullOrEmpty() -> {
                    NetworkResult.Error("Movies not found.")
                }
                response.isSuccessful -> {
                    NetworkResult.Success(response.body()!!)
                }
                else -> NetworkResult.Error(response.message())
            }
        }

        fun handleMovieBySearchResponse(response: Response<MovieBySearch>): NetworkResult<MovieBySearch> {
            return when {
                response.body()?.results.isNullOrEmpty() -> {
                    NetworkResult.Error("Movie not found.")
                }
                response.isSuccessful -> {

                    NetworkResult.Success(response.body()!!)
                }
                else -> NetworkResult.Error(response.message())
            }
        }
    }
}
