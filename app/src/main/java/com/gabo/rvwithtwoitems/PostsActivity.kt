package com.gabo.rvwithtwoitems

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.room.Room
import com.gabo.rvwithtwoitems.adapter.PostsAdapter
import com.gabo.rvwithtwoitems.database.Database
import com.gabo.rvwithtwoitems.database.entity.Post
import com.gabo.rvwithtwoitems.databinding.ActivityPostsBinding

class PostsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostsBinding
    private lateinit var db: Database
    private lateinit var adapter: PostsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(this, Database::class.java, "Post Database")
            .allowMainThreadQueries().build()

        setupAdapter()
        setupListeners()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupListeners() {
        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, AddPostActivity::class.java))
        }
        binding.btnRefresh.setOnClickListener {
            adapter.submitList(db.userDao().getPosts())
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupAdapter() {
        adapter = PostsAdapter(
            clickEdit = { post -> editPost(post) },
            clickDelete = { post ->
                removePost(post)
                adapter.submitList(db.userDao().getPosts())
            }
        )
        binding.rvPosts.adapter = adapter
        binding.rvPosts.layoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
        adapter.submitList(db.userDao().getPosts())
    }

    private fun removePost(post: Post) {
        val msg: String
        if (db.userDao().postAlreadyExist(post.id)) {
            db.userDao().deletePost(post.id)
            msg = "Post removed Successfully"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        } else {
            msg = "Something went wrong"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun editPost(post: Post) {
        val intent = Intent(this, AddPostActivity::class.java)
        intent.putExtra("PostToEdit", post)
        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()
        adapter.submitList(db.userDao().getPosts())
    }
}
