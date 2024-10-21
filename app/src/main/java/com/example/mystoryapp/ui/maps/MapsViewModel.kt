package com.example.mystoryapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.data.remote.Result
import com.example.mystoryapp.data.remote.response.ListStoryItem

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getStoriesWithLocation(): LiveData<Result<List<ListStoryItem>>> {
        return repository.getStoriesWithLocation()
    }
}