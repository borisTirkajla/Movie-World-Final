package com.example.movieworld.bindingadapters

import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.italic
import androidx.databinding.BindingAdapter

class ActorBinding {
    companion object {
        @BindingAdapter("actorNameInMovie")
        @JvmStatic
        fun actorNameInMovie(
            textView: TextView,
            name: String?
        ) {
            name?.let { it ->
                val s = SpannableStringBuilder()
                    .bold { append("In movie as: ") }
                    .italic { append(it) }
                textView.text = s
            }
        }
    }
}