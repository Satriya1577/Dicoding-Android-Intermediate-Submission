package com.example.mystoryapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.data.pref.UserModel
import com.example.mystoryapp.data.remote.Result
import com.example.mystoryapp.data.remote.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: StoryRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = repository.login(email, password)
}