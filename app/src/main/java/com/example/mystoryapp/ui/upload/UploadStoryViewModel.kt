package com.example.mystoryapp.ui.upload

import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.StoryRepository
import java.io.File

class UploadStoryViewModel (private val repository: StoryRepository) : ViewModel() {
    fun uploadStory(file: File, description: String, lat: Float? = null, lon: Float? = null) = repository.uploadStory(file,description, lat, lon)
}