package com.example.movieworld.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movieworld.adapters.MovieFavoriteListAdapter
import com.example.movieworld.data.database.entities.FavoriteMovieEntity
import com.example.movieworld.models.moviebyid.MovieById
import com.example.movieworld.util.NetworkResponse
import com.example.movieworld.util.NetworkResult
import com.facebook.shimmer.ShimmerFrameLayout

class FavoriteMovieBinding {
    companion object{

        @BindingAdapter("viewVisibility","setData", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favoritesMovieList: List<FavoriteMovieEntity>?,
            mAdapter: MovieFavoriteListAdapter?
        ) {
            if (favoritesMovieList.isNullOrEmpty()) {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.VISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            }else{
                when (view) {
                    is ImageView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.setData(favoritesMovieList)
                    }
                }

            }
        }
    }
}