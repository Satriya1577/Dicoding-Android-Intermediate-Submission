package com.example.mystoryapp.ui.upload

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.R
import com.example.mystoryapp.data.remote.Result
import com.example.mystoryapp.databinding.ActivityUploadStoryBinding
import com.example.mystoryapp.ui.ViewModelFactory
import com.example.mystoryapp.ui.main.MainActivity

class UploadStoryActivity : AppCompatActivity(){

    private val viewModel by viewModels<UploadStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityUploadStoryBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        setupAction()
    }

    private fun setupAction() {
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(this, "Photo Picker No media selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }


    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this)
            val description = binding.etDescription.text.toString()
            viewModel.uploadStory(imageFile, description).observe(this) { result ->
                if (result != null) {
                    when(result) {
                        is Result.Success -> {
                            AlertDialog.Builder(this).apply {
                                setTitle(R.string.success_title)
                                setMessage(R.string.success_upload_message)
                                setPositiveButton(R.string.positive_reply) { _, _ ->
                                    val intent = Intent(this@UploadStoryActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                            binding.progressIndicator.visibility = View.GONE
                        }
                        is Result.Error -> {
                            AlertDialog.Builder(this).apply {
                                setTitle(R.string.failed_title)
                                setMessage(result.error)
                                setPositiveButton(R.string.positive_reply) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                create()
                                show()
                            }
                            binding.progressIndicator.visibility = View.GONE

                        }
                        is Result.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}