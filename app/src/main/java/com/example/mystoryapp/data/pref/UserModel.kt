package com.example.mystoryapp.data.pref


data class UserModel(
    val userId: String,
    val token: String,
    val name: String,
    val isLogin: Boolean = false
)