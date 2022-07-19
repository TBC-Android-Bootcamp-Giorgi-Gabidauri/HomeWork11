package com.gabo.rvwithtwoitems.database.dao

import androidx.room.*
import com.gabo.rvwithtwoitems.database.entity.Post

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addPost(post: Post)

    @Query("DELETE FROM posts WHERE id=:id")
    fun deletePost(id: Int)

    @Update
    fun updatePost(post: Post)

    @Query("SELECT EXISTS(SELECT*FROM posts WHERE id=:id)")
    fun postAlreadyExist(id: Int): Boolean

    @Query("SELECT * FROM posts")
    fun getPosts(): List<Post>

}