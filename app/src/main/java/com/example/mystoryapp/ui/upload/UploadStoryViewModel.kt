package com.example.mystoryapp.ui.upload

import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.StoryRepository
import java.io.File

class UploadStoryViewModel (private val repository: StoryRepository) : ViewModel() {
    fun uploadStory(file: File, description: String) = repository.uploadStory(file,description)
}