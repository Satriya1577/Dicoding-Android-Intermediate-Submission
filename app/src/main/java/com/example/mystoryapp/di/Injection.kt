package com.example.mystoryapp.di

import android.content.Context
import com.example.mystoryapp.data.UserRepository
import com.example.mystoryapp.data.pref.UserPreference
import com.example.mystoryapp.data.pref.dataStore
import com.example.mystoryapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }


}