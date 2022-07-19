package com.gabo.rvwithtwoitems.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gabo.rvwithtwoitems.database.entity.Post
import com.gabo.rvwithtwoitems.databinding.PostItemBinding
import com.gabo.rvwithtwoitems.databinding.PostWithPhotoItemBinding

class PostsAdapter(
    private val clickEdit: (Post) -> Unit,
    private val clickDelete: (Post) -> Unit
) : ListAdapter<Post, RecyclerView.ViewHolder>(PostItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return if(viewType == POST_ITEM){
           PostVH(PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
       }else {
           PostWithPhotoVH(PostWithPhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
       }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == POST_ITEM){
            (holder as PostVH).bind(getItem(position),clickEdit,clickDelete)
        }else{
            (holder as PostWithPhotoVH).bind(getItem(position),clickEdit,clickDelete)
        }
    }

    override fun getItemViewType(position: Int) =
        if (getItem(position).hasImage) POST_WITH_PHOTO_ITEM else POST_ITEM

    inner class PostWithPhotoVH(binding: PostWithPhotoItemBinding): RecyclerView.ViewHolder(binding.root){
        private val fullName = binding.tvFullName
        private val mainText = binding.tvMainText
        private val photo = binding.ivPhoto
        private val edit = binding.btnEdit
        private val delete = binding.btnDelete
        fun bind(model: Post, clickEdit: (Post) -> Unit, clickDelete: (Post) -> Unit) {
            fullName.text = model.fullName
            mainText.text = model.mainText
            Glide.with(photo.context).load(Uri.parse(model.image)).into(photo)
            edit.setOnClickListener { clickEdit(model) }
            delete.setOnClickListener { clickDelete(model) }
        }
    }

    inner class PostVH(binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val fullName = binding.tvFullName
        private val mainText = binding.tvMainText
        private val edit = binding.btnEdit
        private val delete = binding.btnDelete
        fun bind(model: Post,clickEdit: (Post) -> Unit, clickDelete: (Post) -> Unit) {
            fullName.text = model.fullName
            mainText.text = model.mainText
            edit.setOnClickListener { clickEdit(model) }
            delete.setOnClickListener { clickDelete(model) }
        }
    }

    companion object {
        const val POST_ITEM = 1
        const val POST_WITH_PHOTO_ITEM = 2
    }
}