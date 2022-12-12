package com.example.movieworld.bindingadapters

import android.text.SpannableStringBuilder
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.italic
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.example.movieworld.models.moviebyid.BoxOffice
import com.example.movieworld.models.moviebyid.Director
import com.example.movieworld.models.moviebyid.MovieById
import com.example.movieworld.models.moviebyid.Writer
import com.example.movieworld.util.NetworkResult
import java.util.*


class MovieDetailsBinding {
    companion object {
        @BindingAdapter("progressBarVisibility")
        @JvmStatic
        fun progressBarVisibility(
            progressBar: ProgressBar,
            networkResult: NetworkResult<MovieById>?
        ) {
            when (networkResult) {
                is NetworkResult.Error -> progressBar.visibility = View.INVISIBLE
                is NetworkResult.Loading -> progressBar.visibility = View.VISIBLE
                is NetworkResult.Success -> progressBar.visibility = View.INVISIBLE
                null -> progressBar.visibility = View.VISIBLE
            }
        }

        @BindingAdapter("errorImageViewVisibility")
        @JvmStatic
        fun errorImageViewVisibility(
            imageView: ImageView,
            networkResult: NetworkResult<MovieById>?
        ) {
            when (networkResult) {
                is NetworkResult.Error -> imageView.visibility = View.VISIBLE
                is NetworkResult.Loading -> imageView.visibility = View.INVISIBLE
                is NetworkResult.Success -> imageView.visibility = View.INVISIBLE
                null -> imageView.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("setTextDirectors")
        @JvmStatic
        fun setTextDirectors(
            textView: TextView,
            directorList: List<Director>?
        ) {
            if (directorList != null) {
                if (directorList.isNotEmpty()) {
                    val list = arrayListOf<String>()
                    directorList.forEach { director ->
                        list.add(director.name)
                    }
                    val s = SpannableStringBuilder()
                        .bold { append("Directors: ") }
                        .italic { append(list.joinToString()) }
                    textView.text = s
                }

            }
        }

        @BindingAdapter("setTextWriters")
        @JvmStatic
        fun setTextWriters(
            textView: TextView,
            writerList: List<Writer>?
        ) {
            if (writerList != null) {
                if (writerList.isNotEmpty()) {
                    val list = arrayListOf<String>()
                    writerList.forEach { writer ->
                        list.add(writer.name)
                    }
                    val s = SpannableStringBuilder()
                        .bold { append("Writers: ") }
                        .italic { append(list.joinToString()) }
                    textView.text = s
                }
            }
        }

        @BindingAdapter("setTextAwards")
        @JvmStatic
        fun setTextAwards(
            textView: TextView,
            awards: String?
        ) {
            if (!awards.isNullOrEmpty()) {
                val s = SpannableStringBuilder()
                    .bold { append("Awards: ") }
                    .italic { append(awards) }
                textView.text = s
            }
        }

        @BindingAdapter("setTextBudget")
        @JvmStatic
        fun setTextBudget(
            textView: TextView,
            boxOffice: BoxOffice?
        ) {
            if (boxOffice != null && !boxOffice.budget.isNullOrEmpty()) {
                val s = SpannableStringBuilder()
                    .bold { append("Movie Budget: ") }
                    .italic { append(boxOffice.budget) }
                textView.text = s
            }

        }

        @BindingAdapter("setTextRuntime")
        @JvmStatic
        fun setTextRuntime(
            textView: TextView,
            runtime: String?
        ) {
            if (runtime != null) {
                val hours = runtime.toInt() / 60
                val minutes = runtime.toInt() % 60
                val timeText = "%d h %02d min".format(hours, minutes)
                val s = SpannableStringBuilder()
                    .bold { append("Runtime: ") }
                    .italic { append(timeText) }
                textView.text = s
            }
        }

        @BindingAdapter("setTextKeywords")
        @JvmStatic
        fun setTextKeywords(
            textView: TextView,
            keywordsList: List<String>?
        ) {
            if (keywordsList != null) {
                val s = SpannableStringBuilder()
                    .bold { append("Keywords: ") }
                    .italic { append(keywordsList.joinToString()) }

                textView.text = s
            }
        }

        @BindingAdapter("setTextReleaseDate")
        @JvmStatic
        fun setTextReleaseDate(
            textView: TextView,
            releaseDate: String?
        ) {
            if (releaseDate != null) {
                val s = SpannableStringBuilder()
                    .bold { append("Release Date: ") }
                    .italic { append(releaseDate) }

                textView.text = s
            }
        }


        @BindingAdapter("isImdbRatingValid")
        @JvmStatic
        fun isImdbRatingValid(
            imageView: ImageView,
            imdbRating: String?
        ) {
            imageView.isVisible = !imdbRating.isNullOrEmpty()
        }

        @BindingAdapter("errorTextViewVisibility")
        @JvmStatic
        fun errorTextViewVisibility(
            textView: TextView,
            networkResult: NetworkResult<MovieById>?
        ) {
            when (networkResult) {
                is NetworkResult.Error -> textView.visibility = View.VISIBLE
                is NetworkResult.Loading -> textView.visibility = View.INVISIBLE
                is NetworkResult.Success -> textView.visibility = View.INVISIBLE
                null -> textView.visibility = View.INVISIBLE
            }

        }

        @BindingAdapter("app:viewPagerVisibility")
        @JvmStatic
        fun viewPagerVisibility(
            viewPager: ViewPager,
            networkResult: NetworkResult<MovieById>?
        ) {
            when (networkResult) {
                is NetworkResult.Error -> viewPager.visibility = View.INVISIBLE
                is NetworkResult.Loading -> viewPager.visibility = View.INVISIBLE
                is NetworkResult.Success -> viewPager.visibility = View.VISIBLE
                null -> viewPager.visibility = View.INVISIBLE
            }

        }
    }
}