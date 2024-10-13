package com.example.mystoryapp.di

import android.content.Context
import com.example.mystoryapp.data.UserRepository
import com.example.mystoryapp.data.pref.UserPreference
import com.example.mystoryapp.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}