package com.example.mystoryapp.data.local

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.google.gson.annotations.SerializedName


@Entity(tableName = "story")
data class Story(

    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("lon")
    val lon: Double? = null,

    @PrimaryKey
    @NonNull
    @field:SerializedName("id")
    val id: String = "",

    @field:SerializedName("lat")
    val lat: Double? = null
)