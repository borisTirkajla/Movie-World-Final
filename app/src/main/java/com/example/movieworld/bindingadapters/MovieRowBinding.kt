package com.example.movieworld.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.example.movieworld.R

private const val TAG = "MovieRowBinding"

class MovieRowBinding {
    companion object {
        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {
            imageView.load(imageUrl)
            {
                crossfade(600)
                error(R.drawable.ic_broken_image)
                placeholder(R.drawable.loading_animation)
            }
        }
    }
}