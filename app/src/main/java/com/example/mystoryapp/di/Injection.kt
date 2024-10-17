package com.example.mystoryapp.di

import android.content.Context
import android.util.Log
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.data.pref.UserPreference
import com.example.mystoryapp.data.pref.dataStore
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService {
            runBlocking { pref.getSession().first().token }
        }
        return StoryRepository.getInstance(pref, apiService)
    }
}