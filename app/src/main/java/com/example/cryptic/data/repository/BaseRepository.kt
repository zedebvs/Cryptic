package com.example.cryptic.data.repository

import com.example.cryptic.data.api.ApiService
import com.example.cryptic.data.api.models.NewStatus
import com.example.cryptic.data.api.models.PrivateProfile
import com.example.cryptic.data.api.models.PublicProfile
import com.example.cryptic.data.api.models.UpdateStatus
import okhttp3.MultipartBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepository {

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
        return try {
            val response = apiCall()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Пустой ответ от сервера"))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Неизвестная ошибка"
                Result.failure(Exception(errorMessage))
            }

        } catch (e: HttpException) {
            Result.failure(Exception("Ошибка сервера: ${e.message()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Проблема с подключением"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class ProfileRepository(
    private val apiService: ApiService
) : BaseRepository() {

    suspend fun getPublicProfile(): Result<PublicProfile> {
        return safeApiCall {
            apiService.getPublicProfile()
        }
    }
    suspend fun getPrivateProfile(): Result<PrivateProfile> {
        return safeApiCall {
            apiService.getPrivateProfile()
        }
    }
    suspend fun updateStatus(status: String): Result<NewStatus> {
        return safeApiCall {
            apiService.updateStatus(UpdateStatus(status))
        }
    }
    suspend fun uploadAvatar(file: MultipartBody.Part): Boolean {
        val response = apiService.uploadAvatar(file)
        return response.isSuccessful
    }
    suspend fun searchUsers(query: String): Result<List<PublicProfile>> {
        return safeApiCall {
            apiService.searchUsers(query)
        }
    }
}