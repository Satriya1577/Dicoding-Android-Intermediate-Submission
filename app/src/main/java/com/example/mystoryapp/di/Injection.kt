package com.example.mystoryapp.di

import android.content.Context
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.data.pref.UserPreference
import com.example.mystoryapp.data.pref.dataStore
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUser().first()}
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(pref, apiService)
    }


}