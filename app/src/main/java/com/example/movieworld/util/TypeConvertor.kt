package com.example.movieworld.util

import androidx.core.content.ContextCompat
import com.example.movieworld.data.database.entities.FavoriteMovieEntity
import com.example.movieworld.models.moviebygenre.Movie
import com.example.movieworld.models.moviebyid.MovieById

class TypeConvertor {
    companion object{
        fun favoriteMovieEntityToMovieById(favoriteMovieEntity: FavoriteMovieEntity): MovieById {
            return MovieById(
                actorList = favoriteMovieEntity.movie.actorList,
                awards = favoriteMovieEntity.movie.awards,
                boxOffice = favoriteMovieEntity.movie.boxOffice,
                directorList = favoriteMovieEntity.movie.directorList,
                errorMessage = favoriteMovieEntity.movie.errorMessage,
                fullTitle = favoriteMovieEntity.movie.fullTitle,
                genreList = favoriteMovieEntity.movie.genreList,
                id = favoriteMovieEntity.movie.id,
                imDbRating = favoriteMovieEntity.movie.imDbRating,
                image = favoriteMovieEntity.movie.image,
                keywordList = favoriteMovieEntity.movie.keywordList,
                plot = favoriteMovieEntity.movie.plot,
                releaseDate = favoriteMovieEntity.movie.releaseDate,
                runtimeMins = favoriteMovieEntity.movie.runtimeMins,
                similars = favoriteMovieEntity.movie.similars,
                starList = favoriteMovieEntity.movie.starList,
                title = favoriteMovieEntity.movie.title,
                writerList = favoriteMovieEntity.movie.writerList,

            )
        }

        fun favoriteMovieEntityToMovie(favoriteMovieEntity: FavoriteMovieEntity) : Movie {
            return Movie(
                id = favoriteMovieEntity.id,
                image = favoriteMovieEntity.movie.image,
                plot = favoriteMovieEntity.movie.plot,
                title = favoriteMovieEntity.movie.title ?: "Error: No Title Found."
            )
        }
    }
}