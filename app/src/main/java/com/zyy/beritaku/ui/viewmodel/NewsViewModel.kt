package com.zyy.beritaku.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zyy.beritaku.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(
    private val repository: NewsRepository = NewsRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private var currentCategory: String = "general"

    init {
        loadNews()
    }

    fun loadNews(category: String = currentCategory) {
        currentCategory = category
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            val result = repository.getTopHeadlines(if (category == "general") null else category)
            result.onSuccess { articles ->
                _uiState.value = NewsUiState.Success(articles = articles, selectedCategory = category)
            }.onFailure { error ->
                _uiState.value = NewsUiState.Error(
                    error.localizedMessage ?: "Terjadi kesalahan saat memuat berita."
                )
            }
        }
    }

    fun searchNews(query: String) {
        if (query.isBlank()) {
            loadNews(currentCategory)
            return
        }
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            val result = repository.searchNews(query)
            result.onSuccess { articles ->
                _uiState.value = NewsUiState.Success(articles = articles, selectedCategory = currentCategory)
            }.onFailure { error ->
                _uiState.value = NewsUiState.Error(
                    error.localizedMessage ?: "Pencarian berita gagal."
                )
            }
        }
    }
}
