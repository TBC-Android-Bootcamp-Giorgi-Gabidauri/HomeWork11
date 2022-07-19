package com.gabo.rvwithtwoitems.adapter

import androidx.recyclerview.widget.DiffUtil
import com.gabo.rvwithtwoitems.database.entity.Post

class PostItemDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem == newItem
}
