package com.raxdenstudios.app.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("UnnecessaryAbstractClass")
abstract class BaseListAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    areContentsTheSame: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem -> oldItem == newItem },
) : ListAdapter<T, VH>(
    object : DiffUtil.ItemCallback<T>() {

        override fun areItemsTheSame(
            oldItem: T,
            newItem: T,
        ) = areItemsTheSame(oldItem, newItem)

        override fun areContentsTheSame(
            oldItem: T,
            newItem: T,
        ): Boolean = areContentsTheSame(oldItem, newItem)
    }
)
