package com.example.mystoryapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.mystoryapp.data.pref.UserModel
import com.example.mystoryapp.data.pref.UserPreference
import com.example.mystoryapp.data.remote.Result
import com.example.mystoryapp.data.remote.response.ErrorResponse
import com.example.mystoryapp.data.remote.response.FileUploadResponse
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.data.remote.response.LoginResponse
import com.example.mystoryapp.data.remote.response.RegisterResponse
import com.example.mystoryapp.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File


class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.register(name, email, password)
            emit(Result.Success(client))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage ?: ""))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.login(email, password)
            emit(Result.Success(client))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage ?: ""))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun getStories(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val listStory = ArrayList<ListStoryItem>()
            val client = apiService.getStories().listStory
            client?.forEach {
                if (it != null) {
                    listStory.add(it)
                }
            }
            emit(Result.Success(listStory))
        } catch (e: Exception) {
            Log.e("StoryRepository", "Error fetching stories: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }


    fun uploadStory(imageFile: File, description: String, lat: Float? = null, lon: Float? = null): LiveData<Result<FileUploadResponse>> = liveData {
        emit(Result.Loading)
        try {
            val descriptionRequesBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            val client = when {
                lat == null && lon == null -> {
                    apiService.uploadStory(multipartBody, descriptionRequesBody)
                }
                else -> {
                    val latDescriptionBody = lat.toString().toRequestBody("text/plain".toMediaType())
                    val lonDescriptionBody = lon.toString().toRequestBody("text/plain".toMediaType())
                    apiService.uploadStory(multipartBody, descriptionRequesBody, latDescriptionBody, lonDescriptionBody)
                }
            }

            emit(Result.Success(client))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage ?: ""))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun getStoriesWithLocation(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val listStory = ArrayList<ListStoryItem>()
            val client = apiService.getStoriesWithLocation().listStory
            client?.forEach {
                if (it != null) {
                    listStory.add(it)
                }
            }
            emit(Result.Success(listStory))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage ?: ""))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(
                    userPreference,
                    apiService
                )
            }.also { instance = it }
    }
}
