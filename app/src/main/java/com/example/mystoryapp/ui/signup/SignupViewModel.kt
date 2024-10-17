package com.example.mystoryapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.data.remote.Result
import com.example.mystoryapp.data.remote.response.RegisterResponse

class SignupViewModel(private val repository: StoryRepository) : ViewModel() {

    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = repository.register(name, email, password)
}