package com.zyy.beritaku.data.repository

import com.zyy.beritaku.data.model.Article
import com.zyy.beritaku.data.remote.NewsApiService
import com.zyy.beritaku.data.remote.RetrofitClient

class ApiKeyMissingException(message: String = "API Key belum disetel") : Exception(message)

class NewsRepository(
    private val apiService: NewsApiService = RetrofitClient.apiService
) {
    suspend fun getTopHeadlines(category: String? = null): Result<List<Article>> {
        return try {
            // Validasi apakah user sudah memasukkan API key resmi dari newsapi.org
            if (RetrofitClient.API_KEY == "YOUR_API_KEY_HERE" || RetrofitClient.API_KEY.isBlank()) {
                return Result.failure(ApiKeyMissingException("API Key belum dikonfigurasi pada RetrofitClient.kt."))
            }

            val response = apiService.getTopHeadlines(
                country = "us",
                category = category,
                apiKey = RetrofitClient.API_KEY
            )

            if (response.status == "ok" && response.articles != null) {
                val validArticles = response.articles.filter {
                    !it.title.isNullOrBlank() && it.title != "[Removed]"
                }
                Result.success(validArticles)
            } else {
                Result.failure(Exception(response.message ?: "Gagal memuat berita dari server"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchNews(query: String): Result<List<Article>> {
        return try {
            if (RetrofitClient.API_KEY == "YOUR_API_KEY_HERE" || RetrofitClient.API_KEY.isBlank()) {
                return Result.failure(ApiKeyMissingException("API Key belum dikonfigurasi pada RetrofitClient.kt."))
            }

            val response = apiService.searchNews(
                query = query,
                apiKey = RetrofitClient.API_KEY
            )

            if (response.status == "ok" && response.articles != null) {
                val validArticles = response.articles.filter {
                    !it.title.isNullOrBlank() && it.title != "[Removed]"
                }
                Result.success(validArticles)
            } else {
                Result.failure(Exception(response.message ?: "Gagal mencari berita"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
