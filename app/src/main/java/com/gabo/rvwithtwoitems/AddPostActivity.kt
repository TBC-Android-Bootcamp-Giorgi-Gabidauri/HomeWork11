package com.gabo.rvwithtwoitems

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.room.Room
import com.bumptech.glide.Glide
import com.gabo.rvwithtwoitems.database.Database
import com.gabo.rvwithtwoitems.database.entity.Post
import com.gabo.rvwithtwoitems.databinding.ActivityAddPostBinding

class AddPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPostBinding
    private lateinit var db: Database
    private var postToEdit: Post? = null
    private var image: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupListeners()
    }

    private fun setupView() {
        db = Room.databaseBuilder(this, Database::class.java, "Post Database")
            .allowMainThreadQueries().build()

        postToEdit = intent.extras?.getParcelable<Post>("PostToEdit")
        with(binding) {
            postToEdit?.let {
                etFullName.setText(it.fullName)
                etMainText.setText(it.mainText)
                swWthImage.isChecked = it.hasImage
                btnUploadImage.isVisible = it.hasImage
                image = it.image
                Glide.with(ivImageFromGallery.context).load(this@AddPostActivity.image.toUri())
                    .into(ivImageFromGallery)
//              ivImageFromGallery.setImageURI(it.image.toUri())
                ivImageFromGallery.isVisible = it.hasImage
            }
            intent.removeExtra("PostToEdit")
        }
    }

    private fun setupListeners() {
        with(binding) {
            swWthImage.setOnCheckedChangeListener { _, _ ->
                btnUploadImage.isVisible = swWthImage.isChecked
                ivImageFromGallery.isVisible = swWthImage.isChecked
                postToEdit?.hasImage = swWthImage.isChecked
            }
            btnUploadImage.setOnClickListener {
                openGalleryForImage()
            }
            btnSave.setOnClickListener {
                if (postToEdit != null) {
                    if (swWthImage.isChecked) {
                        updatePostWithPhoto(
                            postToEdit!!.id,
                            etFullName.text.toString(),
                            etMainText.text.toString(),
                            image,
                            swWthImage.isChecked
                        )
                    } else {
                        updatePost(
                            postToEdit!!.id,
                            etFullName.text.toString(),
                            etMainText.text.toString(),
                        )
                    }
                } else {
                    if (swWthImage.isChecked) {
                        if (image != "") {
                            addPostWithPhoto(
                                etFullName.text.toString(),
                                etMainText.text.toString(),
                                image,
                                swWthImage.isChecked
                            )
                        }
                    } else {
                        addPost(etFullName.text.toString(), etMainText.text.toString())
                    }
                }
            }
        }
    }


    private fun addPost(fullName: String, mainText: String) {
        db.userDao().addPost(Post(fullName = fullName, mainText = mainText))
        val msg = "Post added successfully"
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun addPostWithPhoto(
        fullName: String,
        mainText: String,
        image: String,
        hasImage: Boolean
    ) {
        if (image != "") {
            db.userDao().addPost(
                Post(
                    fullName = fullName,
                    mainText = mainText,
                    image = image,
                    hasImage = hasImage
                )
            )
            val msg = "Post added successfully"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this@AddPostActivity, "Upload Image", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun updatePost(id: Int, fullName: String, mainText: String) {
        val msg: String
        if (db.userDao().postAlreadyExist(id)) {
            db.userDao()
                .updatePost(Post(id = id, fullName = fullName, mainText = mainText))
            msg = "Post updated successfully"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        } else {
            msg = "Post does not exist"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun updatePostWithPhoto(
        id: Int,
        fullName: String,
        mainText: String,
        image: String,
        hasImage: Boolean,
    ) {
        val msg: String
        if (image != "") {
            if (db.userDao().postAlreadyExist(id)) {
                db.userDao().updatePost(
                    Post(
                        id = id,
                        fullName = fullName,
                        mainText = mainText,
                        image = image,
                        hasImage = hasImage
                    )
                )
                msg = "Post updated successfully"
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            } else {
                msg = "Post does not exist"
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
            finish()
        } else {
            Toast.makeText(this@AddPostActivity, "Upload Image", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            image = data?.data.toString()
            binding.ivImageFromGallery.setImageURI(image.toUri())
            Toast.makeText(this@AddPostActivity, "image uploaded successfully", Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object {
        const val REQUEST_CODE = 100
    }
}