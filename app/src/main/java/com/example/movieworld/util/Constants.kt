package com.example.movieworld.util

import com.example.movieworld.ui.fragments.movielist.GenresEnum

object Constants {

    const val BUNDLE_ID = "id"
    const val BUNDLE_MOVIE_BY_ID = "movieById"

    const val BASE_URL = "https://imdb-api.com/API/"

    // API Query Keys
    const val QUERY_API_KEY = "apiKey"
    const val QUERY_GENRES = "genres"
    const val QUERY_SEARCH = "expression"
    const val QUERY_ID = "id"

    // Room Database
    const val DATABASE_NAME = "movies_database"
    const val MOVIES_TABLE = "movies_table"
    const val FAVORITE_MOVIES_TABLE = "favorite_movies_table"

    // GenreDialog and Preferences
    val DEFAULT_GENRE = GenresEnum.Action
    val DEFAULT_GENRE_ID = GenresEnum.Action.ordinal
    const val SELECTED_GENRE_ID = "genreId"
    const val PREFERENCES_NAME = "movie_preferences"
    const val PREFERENCES_BACK_ONLINE = "back_online"
}