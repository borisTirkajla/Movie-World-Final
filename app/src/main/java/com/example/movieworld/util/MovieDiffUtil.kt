package com.example.movieworld.util

import androidx.recyclerview.widget.DiffUtil
import com.example.movieworld.models.moviebygenre.Movie

class MovieDiffUtil<T>(
    private val oldList: List<T>,
    private val newList: List<T>
) :DiffUtil.Callback(){
    override fun getOldListSize() =
        oldList.size

    override fun getNewListSize() =
        newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] === newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}